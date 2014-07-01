package edu.ucsd.dao;

import java.util.List;

import edu.ucsd.model.NonLeafParseNode;
import edu.ucsd.model.NonLeafToLeaf;
import edu.ucsd.model.ParseChild;
import edu.ucsd.model.Sentence;
import edu.ucsd.model.SentenceToRoot;
import edu.ucsd.model.SentenceToWord;
import edu.ucsd.model.Word;
import edu.ucsd.model.WordToWordDependency;

public interface SentenceDao {
	public void save(Sentence newSentence);
	public void save(Word newWord);
	public void save(SentenceToWord sentenceToWord);
	public void save(WordToWordDependency dependency);
	public void save(NonLeafParseNode nonLeaf);
	public void save(ParseChild parseChild);
	public void save(SentenceToRoot sentenceToRoot);
	public void save(NonLeafToLeaf nonLeafToLeaf);
	public Sentence getSentenceByText(String text);
	public List<Word> getWordsBySentenceText(String text);
	public String getRelationShip(Long startWordId, Long endWordId);
}
