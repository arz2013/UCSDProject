package edu.ucsd.patternmatching.filter;

import edu.ucsd.model.NeTags;
import edu.ucsd.model.Word;

public class NETagFilter extends WordFilter {
	@Override
	public boolean match(Word toFilter) {
		return NeTags.MONEY.name().equals(toFilter.getNeTag());
	}
}
