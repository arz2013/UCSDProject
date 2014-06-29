package edu.ucsd;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import edu.ucsd.dao.SentenceDao;
import edu.ucsd.parser.DisneyParser;
import edu.ucsd.system.SystemApplicationContext;

public class SingleSentenceParseAndLoadTest {
	private SentenceDao sentenceDao;
	
	@Before
	public void setUp() {
		ApplicationContext appContext = SystemApplicationContext.getApplicationContext();
		sentenceDao = SentenceDao.class.cast(appContext.getBean("sentenceDao")); 
		String text = "Looking back at everything we’ve accomplished this year, I am once again awed by the tremendous creativity and commitment of the men and women who make up The Walt Disney Company.";
		List<String> disneyFinancialStatement = new ArrayList<String>();
		disneyFinancialStatement.add(text);
		DisneyParser parser = new DisneyParser(sentenceDao, disneyFinancialStatement);
		parser.parseAndLoad();
	}
	
	@Test
	public void testParseAndLoad() {
		
	}

}
