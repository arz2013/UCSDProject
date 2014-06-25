package edu.ucsd.dao;

import edu.ucsd.model.Sentence;
import edu.ucsd.model.SentenceToWord;
import edu.ucsd.model.Word;
import edu.ucsd.model.WordToWordDependency;

public interface SentenceDao {
	public void save(Sentence newSentence);
	public void save(Word newWord);
	public void save(SentenceToWord sentenceToWord);
	public void save(WordToWordDependency dependency);
}
