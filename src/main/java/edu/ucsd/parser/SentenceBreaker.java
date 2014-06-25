package edu.ucsd.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.BreakIterator;

public class SentenceBreaker {

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
		BreakIterator boundary = BreakIterator.getSentenceInstance();
		boundary.setText(paragraph);
		int start = boundary.first();
	    for (int end = boundary.next();
	          end != BreakIterator.DONE;
	          start = end, end = boundary.next()) {
	    	  count++;
	          System.out.println(paragraph.substring(start,end));
	    }
	    
	    System.out.println("Total number of sentences: " + count);
	}

}
