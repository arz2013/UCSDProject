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
import edu.ucsd.model.SentenceToRoot;
import edu.ucsd.model.Word;

public class DFS {
	private SentenceDao sentenceDao;
	private Sentence sentence;
	private Map<Word.TextAndPosition, Word> seenWords;
	
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
			//System.out.println("Word : " + tree.value() + " Score: " + tree.score());
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
