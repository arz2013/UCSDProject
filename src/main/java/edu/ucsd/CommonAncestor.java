package edu.ucsd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.Node;
import org.springframework.context.ApplicationContext;

import static edu.ucsd.dao.SentenceDao.SentenceNumberAndWords;

import edu.ucsd.dao.SentenceDao;
import edu.ucsd.model.NeTags;
import edu.ucsd.system.SystemApplicationContext;

public class CommonAncestor {
	public static void main(String[] args) {
		ApplicationContext context = SystemApplicationContext.getApplicationContext();
		SentenceDao sentenceDao = SentenceDao.class.cast(context.getBean("sentenceDao"));
		Iterable<SentenceNumberAndWords> sentenceNumbersAndWords = sentenceDao.getWordsKeyedBySentenceNumberWithSpecificNeTag(NeTags.ORGANIZATION);
		for(SentenceNumberAndWords sentenceNumberAndWords : sentenceNumbersAndWords) {
			PhraseTokenizer tokenizer = new PhraseTokenizer(sentenceNumberAndWords.getWords());
			System.out.println("Sentence Number : " + sentenceNumberAndWords.getSentenceNumber());
			System.out.println("Original Words  : " + sentenceNumberAndWords.getWords());
			while(tokenizer.hasNext()) {
				System.out.println("Next Phrase: " + tokenizer.nextPhrase());
			}
		}
	}
	
	private static class PhraseTokenizer {
		private List<Node> toBeProcessed = new ArrayList<Node>();
		
		public PhraseTokenizer(List<Node> input) {
			this.toBeProcessed.addAll(input);
			Collections.sort(this.toBeProcessed, new Comparator<Node>() {

				@Override
				public int compare(Node word1, Node word2) {
					Integer word1Position = (Integer)word1.getProperty("position");
					Integer word2Position = (Integer)word2.getProperty("position"); 
					return word1Position.compareTo(word2Position);
				}
				
			});
		}
		
		public boolean hasNext() {
			return toBeProcessed.size() > 0;
		}
		
		public List<Node> nextPhrase() {
			List<Node> nextPhrase = new ArrayList<Node>();
			
			if(hasNext()) {
				Node previousWord = this.toBeProcessed.remove(0);
				nextPhrase.add(previousWord);
				
				while(hasNext()) {
					Node currentWord = this.toBeProcessed.get(0); // Take off one Node
					if(consecutiveWord(previousWord, currentWord)) {
						currentWord = this.toBeProcessed.remove(0);
						nextPhrase.add(currentWord);
						previousWord = currentWord;
					} else {
						break;
					}
				} 
			}
			
			return nextPhrase;
		}
		
		private boolean consecutiveWord(Node word1, Node word2) {
			Integer word1Position = (Integer)word1.getProperty("position");
			Integer word2Position = (Integer)word2.getProperty("position"); 
			return word1Position + 1 == word2Position;
		}
	}
}
