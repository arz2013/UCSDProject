package edu.ucsd.processing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.neo4j.graphalgo.impl.ancestor.AncestorsUtil;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.kernel.Traversal;

import static edu.ucsd.dao.SentenceDao.SentenceNumberAndWords;
import edu.ucsd.dao.SentenceDao;
import edu.ucsd.model.NeTags;
import edu.ucsd.model.NonLeafParseNode;
import edu.ucsd.model.ParseTreeTags;
import edu.ucsd.model.Rel;
import edu.ucsd.utils.Neo4JUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is not intended to be some sort of common utility to find common ancestors of Nodes, etc, 
 * rather, it's just another processing logic where find the common ancestor in the parse tree of certain
 * phrases in a sentence. For example: The Walt Disney Company (occuring in a sentence) as parsed by the Stanford Parser 
 * will produce the following: (NP (DT The) (NNP Walt) (NNP Disney) (NNP Company)). From the example it's clear
 * that the common ancestor is the NP tag which is represented in our data model as a NonLeafParseNode
 * What this class will do then is find such NP nodes and add populate the type property of those nodes 
 * with the value "NP" so custom indexing can be done later. 
 * 
 * @author rogertan
 *
 */
public class CommonAncestor {
	private static Logger logger = LoggerFactory.getLogger(CommonAncestor.class);
	
	@Inject
	private SentenceDao sentenceDao;
	
	public void findNPAncestorNodeAndMark() {
		Iterable<SentenceNumberAndWords> sentenceNumbersAndWords = sentenceDao.getWordsKeyedBySentenceNumberWithSpecificNeTag(NeTags.ORGANIZATION);
		int countWords = 0;
		for(SentenceNumberAndWords sentenceNumberAndWords : sentenceNumbersAndWords) {
			PhraseTokenizer tokenizer = new PhraseTokenizer(sentenceNumberAndWords.getWords());
			if(logger.isDebugEnabled()) {
				logger.debug("Sentence Number : " + sentenceNumberAndWords.getSentenceNumber());
				logger.debug("Original Words  : " + sentenceNumberAndWords.getWords());
			}
			while(tokenizer.hasNext()) {
				List<Node> nextPhrase = tokenizer.nextPhrase();
				countWords += nextPhrase.size();
				if(logger.isDebugEnabled()) {
					logger.debug("Next Phrase: " + nextPhrase);
				}
				Node lca = AncestorsUtil.lowestCommonAncestor(nextPhrase, Traversal.expanderForTypes(Rel.HAS_PARSE_CHILD, Direction.INCOMING));
				if (lca != null) {
					if(logger.isDebugEnabled()) {
						logger.debug("Common Ancestor: " + lca.getId() + ", " + lca.getProperty("value"));
					}
					lca.setProperty(NonLeafParseNode.TYPE_FIELD, ParseTreeTags.NP.name());
				} else { // There must only be one node in the list so lowestCommonAncestor does not work
					Node onlyOne = nextPhrase.get(0);
					Node parent = Neo4JUtils.getAncestor(onlyOne, Traversal.expanderForTypes(Rel.HAS_PARSE_CHILD, Direction.INCOMING));
					Node grandParent = Neo4JUtils.getAncestor(parent, Traversal.expanderForTypes(Rel.HAS_PARSE_CHILD, Direction.INCOMING));
					if(logger.isDebugEnabled()) {
						logger.debug("Common Ancestor: " + grandParent.getId() + ", " + grandParent.getProperty("value"));
					}
					grandParent.setProperty(NonLeafParseNode.TYPE_FIELD, ParseTreeTags.NP.name());
				}
			}
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Number of words: " + countWords);
		}
	}
	
	private static class PhraseTokenizer {
		private List<Node> toBeProcessed = new ArrayList<Node>();
		
		public PhraseTokenizer(List<Node> input) {
			this.toBeProcessed.addAll(input);
			Collections.sort(this.toBeProcessed, new Comparator<Node>() {

				@Override
				public int compare(Node word1, Node word2) {
					Integer word1Position = (Integer)word1.getProperty("position");
					Integer word2Position = (Integer)word2.getProperty("position"); 
					return word1Position.compareTo(word2Position);
				}
				
			});
		}
		
		public boolean hasNext() {
			return toBeProcessed.size() > 0;
		}
		
		public List<Node> nextPhrase() {
			List<Node> nextPhrase = new ArrayList<Node>();
			
			if(hasNext()) {
				Node previousWord = this.toBeProcessed.remove(0);
				nextPhrase.add(previousWord);
				
				while(hasNext()) {
					Node currentWord = this.toBeProcessed.get(0); // Take off one Node
					if(consecutiveWord(previousWord, currentWord)) {
						currentWord = this.toBeProcessed.remove(0);
						nextPhrase.add(currentWord);
						previousWord = currentWord;
					} else {
						break;
					}
				} 
			}
			
			return nextPhrase;
		}
		
		private boolean consecutiveWord(Node word1, Node word2) {
			Integer word1Position = (Integer)word1.getProperty("position");
			Integer word2Position = (Integer)word2.getProperty("position"); 
			return word1Position + 1 == word2Position;
		}
	}
}
