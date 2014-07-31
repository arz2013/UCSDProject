package edu.ucsd.patternmatching;

import org.springframework.context.ApplicationContext;

import edu.ucsd.dao.SentenceDao;
import edu.ucsd.model.Document;
import edu.ucsd.system.SystemApplicationContext;

public class ActiveVerbSearchMain {

	public static void main(String[] args) {
		ApplicationContext context = SystemApplicationContext.getApplicationContext();
		SentenceDao sentenceDao = SentenceDao.class.cast(context.getBean("sentenceDao"));
		Document doc = sentenceDao.getDocumentByTitleYearAndNumber("Disney Financial Statement", 2013, 0);
		ActiveVerbSearch verbSearch = ActiveVerbSearch.class.cast(context.getBean("activeVerbSearch"));
		verbSearch.searchVerb(doc);
	}

}
