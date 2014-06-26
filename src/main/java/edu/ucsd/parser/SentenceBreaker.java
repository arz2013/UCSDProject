package edu.ucsd.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class SentenceBreaker {

	/**
	 * Utilizes a hack to work around problems with the BreakIterator i.e. 
	 * 1. In the sentence Already in the first quarter of fiscal 2014, we’ve had tremendous success with Thor: The Dark World from Marvel, Frozen from Walt Disney Animation, and Saving Mr. Banks from Walt Disney Studios. 
     *    the BreakIterator would detect a new sentence after Saving Mr. (dot notation)
	 * 
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		InputStream is = SentenceBreaker.class.getClassLoader().getResourceAsStream("DFS.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		StringBuilder sb = new StringBuilder();
		String inputLine; 
		while((inputLine = br.readLine()) != null) {
			sb.append(inputLine);
		}
		
		int count = 0;
		
		// String paragraph = "I'm going to eat something. But I don't know what yet.";
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
	    		  previous = previous.concat(text);
	    		  sentences.set(count - 1, previous);
	    	  } else {
	    		  sentences.add(text);
	    		  count++;
	    	  }
	    	  
	          previous = text;
	    }
	    
	    for (String sentence : sentences) {
	    	System.out.println(sentence);
	    }
	    
	    System.out.println("Total number of sentences: " + count);
	}

}
