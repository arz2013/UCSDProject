package edu.ucsd.patternmatching.filter;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.model.Word;

public class WordTextFilter extends WordFilter {
	private static List<String> textsToFilter = new ArrayList<String>();
	
	static {
		textsToFilter.add("sales");
		textsToFilter.add("performance");
		textsToFilter.add("records");
	}

	@Override
	public boolean match(Word toFilter) {
		for(String text : textsToFilter) {
			if(text.equalsIgnoreCase(toFilter.getText())) {
				return true;
			}
		}
		return false;
	}

}
