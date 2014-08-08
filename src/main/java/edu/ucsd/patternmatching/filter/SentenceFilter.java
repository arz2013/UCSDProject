package edu.ucsd.patternmatching.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.ucsd.dao.SentenceDao;
import edu.ucsd.model.Sentence;
import edu.ucsd.model.Word;


public class SentenceFilter implements Filter<Sentence> {
	private List<WordFilter> wordFilters;
	private SentenceDao sentenceDao;
	
	public SentenceFilter(SentenceDao sentenceDao) {
		wordFilters = new ArrayList<WordFilter>();
		wordFilters.add(new NETagFilter());
		wordFilters.add(new WordTextFilter());
		
		this.sentenceDao = sentenceDao;
	}

	@Override
	public boolean match(Sentence toFilter) {
		for(Word word : this.getWordsInOrder(toFilter)) {
			for(WordFilter wordFilter : wordFilters) {
				if(wordFilter.match(word)) {
					return true;
				}
			}
		}
		
		return false;
	}
	

	private List<Word> getWordsInOrder(Sentence toFilter) {
		List<Word> words = sentenceDao.getWordsBySentenceText(toFilter.getText());
		Collections.sort(words, new Comparator<Word>() {

			@Override
			public int compare(Word w1, Word w2) {
				return new Integer(w1.getPosition()).compareTo(w2.getPosition());
			}
			
		});
		return words;
	}
	

}
