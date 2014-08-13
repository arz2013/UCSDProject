package edu.ucsd.patternmatching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import edu.ucsd.dao.SentenceDao;
import edu.ucsd.model.Document;
import edu.ucsd.model.Sentence;
import edu.ucsd.patternmatching.filter.SentenceFilter;
import edu.ucsd.system.SystemApplicationContext;

public class DocumentSentenceFiltering {
	private static Logger logger = LoggerFactory.getLogger(DocumentSentenceFiltering.class);

	public static void main(String[] args) {
		ApplicationContext context = SystemApplicationContext.getApplicationContext();
		SentenceDao sentenceDao = SentenceDao.class.cast(context.getBean("sentenceDao"));
		Document doc = sentenceDao.getDocumentByTitleYearAndNumber("Disney Financial Statement", 2013, 0);
		List<Sentence> sentences = sentenceDao.getSentencesBasedOnDocument(doc.getId());
		SentenceFilter sentenceFilter = new SentenceFilter(sentenceDao);
		List<Sentence> matchingSentences = new ArrayList<Sentence>();
		for(Sentence sentence : sentences) {
			if(sentenceFilter.match(sentence)) {
				matchingSentences.add(sentence);
			}
		}
		
		Collections.sort(matchingSentences, new Comparator<Sentence>() {

			@Override
			public int compare(Sentence s1, Sentence s2) {
				return new Integer(s1.getSentenceNumber()).compareTo(s2.getSentenceNumber());
			}
			
		});
		
		for(Sentence sentence : matchingSentences) {
			logger.info(sentence.getId() + ", " + sentence.getSentenceNumber() + ", " + sentence.getText());
		}
	}

}
