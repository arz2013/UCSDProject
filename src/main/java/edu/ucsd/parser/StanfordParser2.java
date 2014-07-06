package edu.ucsd.parser;

import edu.ucsd.model.Document;
import edu.ucsd.system.SystemApplicationContext;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.util.StopWatch;

public class StanfordParser2 {
	
	private static List<String> readDisneyFinancialStatement() throws IOException {
		InputStream is = SentenceBreaker.class.getClassLoader().getResourceAsStream("DFS.txt");
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
	
	public static void main(String[] args) throws IOException {
		StopWatch stopWatch = new StopWatch("Parsing and Insertion");
		
		stopWatch.start();
		ApplicationContext appContext = SystemApplicationContext.getApplicationContext();

		List<String> disneyFinancialStatement = readDisneyFinancialStatement();	
		DisneyParser parser = DisneyParser.class.cast(appContext.getBean("parser"));
		parser.parseAndLoad(disneyFinancialStatement, new Document("Disney Financial Statement", 2013, 0));
		
		stopWatch.stop();
		System.out.println(stopWatch.prettyPrint());
	}
}
