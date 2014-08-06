package edu.ucsd.parser;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;

import org.neo4j.graphdb.Node;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;

import edu.stanford.nlp.ling.CoreAnnotations.IndexAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import edu.ucsd.dao.SentenceDao;
import edu.ucsd.model.Document;
import edu.ucsd.model.DocumentToSentence;
import edu.ucsd.model.Rel;
import edu.ucsd.model.Sentence;
import edu.ucsd.model.Word;
import edu.ucsd.model.WordToWordDependency;

public class DisneyParser {
	private SentenceDao sentenceDao;
	private Neo4jTemplate template;
	
	@Inject
	public void setSentenceDao(SentenceDao sentenceDao) {
		this.sentenceDao = sentenceDao;
	}
	
	@Inject
	public void setTemplate(Neo4jTemplate template) {
		this.template = template;
	}

	@Transactional
	public void parseAndLoad(List<String> disneyFinancialStatement, Document doc) {
		if(disneyFinancialStatement == null || doc == null) {
			throw new IllegalArgumentException("Neither parameters can be null");
		}
		
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		
		sentenceDao.save(doc);
		
		int noSentence = 0;

		for(String text : disneyFinancialStatement) {
			// create an empty Annotation just with the given text
			Annotation document = new Annotation(text);

			// run all Annotators on this text
			pipeline.annotate(document);

			// these are all the sentences in this document
			// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
			List<CoreMap> sentences = document.get(SentencesAnnotation.class);
				
			if(sentences.size() > 1) {
				// An example: Walt Disney famously said, “Disneyland will never be completed. It will continue to grow as long as there is imagination left in the world.”
				System.out.println("Sentence with 2 coremaps: " + text);
			}
		
			for(CoreMap sentence: sentences) {
				int wordIndex = 0; 
				
				// Maintain a list of words that has already been seen
				Map<Word.TextAndPosition, Word> seenWords = new HashMap<Word.TextAndPosition, Word>();
				Word root = Word.newWord("ROOT", wordIndex);
				wordIndex++;
				
				sentenceDao.save(root);
				
				Node prevNode = template.getNode(root.getId());
				
				seenWords.put(root.getTextAndPosition(), root);
				
				Sentence newSentence = Sentence.newSentence(sentence.get(TextAnnotation.class), noSentence);
				
				newSentence.addWord(root);
				
				for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
					// this is the text of the token
					String word = token.get(TextAnnotation.class);
					// this is the POS tag of the token
					String pos = token.get(PartOfSpeechAnnotation.class);
					// this is the NER label of the token
					String ne = token.get(NamedEntityTagAnnotation.class);
					
					Word newWord = Word.newWord(word, wordIndex);
					newWord.setNameEntityTag(ne);
					newWord.setPosTag(pos);
					sentenceDao.save(newWord);
					
					Node currNode = template.getNode(newWord.getId());
					//template.createRelationshipBetween(prevNode, currNode, Rel.NEXT.name(), new HashMap<String, Object>());
					
					prevNode = currNode;
					
					seenWords.put(newWord.getTextAndPosition(), newWord);
					
					newSentence.addWord(newWord);
					
					wordIndex++;
				}
				
				sentenceDao.save(newSentence);
				
				Node sentenceNode = template.getNode(newSentence.getId());
				//template.createRelationshipBetween(sentenceNode, template.getNode(root.getId()), Rel.FIRST_CHILD.name(), new HashMap<String, Object>());
				
				// traversing the words in the current sentence
				// a CoreLabel is a CoreMap with additional token-specific methods
		
				sentenceDao.save(new DocumentToSentence(doc, newSentence));

				// this is the parse tree of the current sentence
				Tree tree = sentence.get(TreeAnnotation.class);
				// Get dependency tree
				TreebankLanguagePack tlp = new PennTreebankLanguagePack();
				GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
				GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
				Collection<TypedDependency> tds = gs.typedDependencies();
				for(TypedDependency td : tds) {
					// Check for existence of words
					Word startWord = getWord(td.gov(), seenWords);
					Word endWord = getWord(td.dep(), seenWords);
					
					WordToWordDependency dependency = new WordToWordDependency(startWord, endWord, td.reln().toString());
					sentenceDao.save(dependency);
				}
				
				// Now save the parse trees as well
				DFS dfs = new DFS(sentenceDao, template, newSentence, seenWords);
				dfs.performDepthFirstTraversal(tree);
			
				noSentence++;
				seenWords.clear();
			}
		}
	}
	
	private Word getWord(TreeGraphNode node, Map<Word.TextAndPosition, Word> seenWords) {
		Word word = Word.newWord(node.nodeString(), node.label().get(IndexAnnotation.class));
		return seenWords.get(word.getTextAndPosition());
	}
	
}
