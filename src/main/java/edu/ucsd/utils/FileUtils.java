package edu.ucsd.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.parser.SentenceBreaker;

public abstract class FileUtils {
	public static List<String> readFromFile(String fileName) throws IOException {
		InputStream is = SentenceBreaker.class.getClassLoader().getResourceAsStream(fileName);
		List<String> result = new ArrayList<String>();
			
		try(BufferedReader br =  new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
			String inputLine;
			while((inputLine = br.readLine()) != null) {
				result.add(inputLine);
			}
		} 
		
		return result;
	}
	
	/**
	 * There's some hacks here that's related to how we want the Stanford Parser to see the input sentence 
	 * i.e. the delimiter for sentences in the Parser is a ".", so even if the "." exists in a sentence within
	 * a multi sentence quotation, the Parser will break off a given sentence at a "."
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static List<String> readDisneyFinancialStatement(String fileName) throws IOException {
		InputStream is = SentenceBreaker.class.getClassLoader().getResourceAsStream(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		StringBuilder sb = new StringBuilder();
		String inputLine; 
		while((inputLine = br.readLine()) != null) {
			sb.append(inputLine);
		}
		
		int count = 0;		
		String paragraph = sb.toString();
		List<String> sentences = new ArrayList<String>();
		BreakIterator boundary = BreakIterator.getSentenceInstance();
		boundary.setText(paragraph);
		String previous = "";
		int start = boundary.first();
	    for (int end = boundary.next();
	          end != BreakIterator.DONE;
	          start = end, end = boundary.next()) {
	    	  String text = paragraph.substring(start, end);
	    	  if (text.startsWith("Banks from Walt Disney Studios.") || text.startsWith("It will continue to grow as long as there is imagination left")) {
	    		  previous = previous.concat(text.trim());
	    		  sentences.set(count - 1, previous);
	    	  } else {
	    		  sentences.add(text.trim());
	    		  count++;
	    	  }
	    	  
	          previous = text;
	    }

	    return sentences;
	}
}
