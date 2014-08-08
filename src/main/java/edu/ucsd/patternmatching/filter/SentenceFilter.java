package edu.ucsd.patternmatching.filter;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.model.Sentence;
import edu.ucsd.model.Word;


public class SentenceFilter implements Filter<Sentence> {
	private List<WordFilter> wordFilters;
	
	public SentenceFilter() {
		wordFilters = new ArrayList<WordFilter>();
		wordFilters.add(new NETagFilter());
		wordFilters.add(new WordTextFilter());
	}

	@Override
	public boolean match(Sentence toFilter) {
		for(Word word : toFilter.getWordsInOrder()) {
			for(WordFilter wordFilter : wordFilters) {
				if(wordFilter.match(word)) {
					return true;
				}
			}
		}
		
		return false;
	}

}
