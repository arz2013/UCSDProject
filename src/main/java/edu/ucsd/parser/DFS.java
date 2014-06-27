package edu.ucsd.parser;


import java.util.List;
import java.util.Set;

import edu.stanford.nlp.trees.Tree;
import edu.ucsd.dao.SentenceDao;
import edu.ucsd.model.NonLeafParseNode;
import edu.ucsd.model.NonLeafToLeaf;
import edu.ucsd.model.ParseChild;
import edu.ucsd.model.Sentence;
import edu.ucsd.model.SentenceToRoot;
import edu.ucsd.model.Word;

public class DFS {
	private SentenceDao sentenceDao;
	private Sentence sentence;
	private Set<Word> seenWords;
	
	public DFS(SentenceDao sentenceDao, Sentence sentence, Set<Word> seenWords) {
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
			Word word = Word.newWord(tree.value());
			sentenceDao.save(word);
			sentenceDao.save(new NonLeafToLeaf(parent, word));
			//System.out.println("Leaf: " + tree.value());
			return;
		} else {
			sentenceDao.save(currentNode);
			if(currentNode.isRoot()) {
				sentenceDao.save(new SentenceToRoot(sentence, currentNode));
			} else {
				sentenceDao.save(new ParseChild(parent, currentNode));
			}
			// System.out.println("Non Leaf: " + tree.value());
		}
		
		for(Tree child : children) {
			this.innerDepthFirstTraversal(child, currentNode);
		}
	}
}
