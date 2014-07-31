package edu.ucsd.patternmatching;

import org.springframework.context.ApplicationContext;

import edu.ucsd.dao.SentenceDao;
import edu.ucsd.model.Document;
import edu.ucsd.system.SystemApplicationContext;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class PenultimateParseTreeNodeMatcherMain {
	private static Logger logger = LoggerFactory.getLogger(PenultimateParseTreeNodeMatcherMain.class);

	public static void main(String[] args) {
		ApplicationContext context = SystemApplicationContext.getApplicationContext();
		SentenceDao sentenceDao = SentenceDao.class.cast(context.getBean("sentenceDao"));
		Document doc = sentenceDao.getDocumentByTitleYearAndNumber("Disney Financial Statement", 2013, 0);
		PenultimateParseTreeNodeMatcher matcher = PenultimateParseTreeNodeMatcher.class.cast(context.getBean("penultimateMatcher"));
		matcher.performMatching(doc);
	}

}
