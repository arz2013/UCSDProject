package edu.ucsd.patternmatching.filter;

import edu.ucsd.model.Word;

public abstract class WordFilter implements Filter<Word> {

	@Override
	public abstract boolean match(Word toFilter);
}
