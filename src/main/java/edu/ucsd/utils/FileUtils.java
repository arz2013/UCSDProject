package edu.ucsd.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.parser.SentenceBreaker;

public abstract class FileUtils {
	public static List<String> readFromFile(String fileName) throws IOException {
		InputStream is = SentenceBreaker.class.getClassLoader().getResourceAsStream(fileName);
		BufferedReader br = null;
		List<String> result = new ArrayList<String>();
			
		try {
			br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	
			String inputLine;
			while((inputLine = br.readLine()) != null) {
				result.add(inputLine);
			}
		} finally {
			br.close();
		}
		
		return result;
	}
}
