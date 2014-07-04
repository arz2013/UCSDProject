package edu.ucsd.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.stanford.nlp.trees.Tree;
import edu.ucsd.dao.SentenceDao;
import edu.ucsd.model.NonLeafParseNode;
import edu.ucsd.model.NonLeafToLeaf;
import edu.ucsd.model.ParseChild;
import edu.ucsd.model.Sentence;
import edu.ucsd.model.SentenceToNonLeafParseNode;
import edu.ucsd.model.Word;

public class DFS {
	private SentenceDao sentenceDao;
	private Sentence sentence;
	private Map<Word.TextAndPosition, Word> seenWords;
	
	private boolean isExcludeRoot = true;
	
	private List<String> inOrder = new ArrayList<String>();
	
	public DFS(SentenceDao sentenceDao, Sentence sentence, Map<Word.TextAndPosition, Word> seenWords) {
		if(sentenceDao == null) {
			throw new IllegalArgumentException("DAO can not be null.");
		} 
		
		if(sentence == null) {
			throw new IllegalArgumentException("Sentence cannot be null.");
		}
		
		if(seenWords == null) {
			throw new IllegalArgumentException("Seen Words can't be null");
		}
		
		this.sentenceDao = sentenceDao;
		this.sentence = sentence;
		this.seenWords = seenWords;
	}
	
	public void performDepthFirstTraversal(Tree tree) {
		if(tree == null) {
			throw new IllegalArgumentException("Argument Tree can not be null");
		}
		
		innerDepthFirstTraversal(tree, null);
	}
	
	private void innerDepthFirstTraversal(Tree tree, NonLeafParseNode parent) {
		List<Tree> children = tree.getChildrenAsList();
		NonLeafParseNode currentNode = NonLeafParseNode.newNonLeafParseNode(tree.value());
		
		if(tree.isLeaf()) {
			inOrder.add(tree.value());

			Word word = Word.newWord(tree.value(), inOrder.size());
			word = seenWords.get(word.getTextAndPosition());
			sentenceDao.save(new NonLeafToLeaf(parent, word));
			return;
		} else {
			if(currentNode.isRoot() && !isExcludeRoot) { // Check if the current node is root and whether root needs to be excluded
				sentenceDao.save(currentNode);			
				sentenceDao.save(new SentenceToNonLeafParseNode(sentence, currentNode));
			} else if(parent != null) { 
				if(isExcludeRoot && parent.isRoot()) { // Check if root needs to be excluded, in which case we link the children of root directly to the sentence
					sentenceDao.save(currentNode);
					sentenceDao.save(new SentenceToNonLeafParseNode(sentence, currentNode));
				} else {
					sentenceDao.save(currentNode);
					sentenceDao.save(new ParseChild(parent, currentNode));
				}
			}
		}
		
		for(Tree child : children) {
			this.innerDepthFirstTraversal(child, currentNode);
		}
	}
}
