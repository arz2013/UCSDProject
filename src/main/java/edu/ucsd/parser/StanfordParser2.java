package edu.ucsd.parser;

import static com.google.inject.spring.SpringIntegration.fromSpring;
import edu.ucsd.dao.SentenceDao;
import edu.ucsd.model.Document;
import edu.ucsd.system.SystemApplicationContext;
import edu.ucsd.utils.FileUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.StopWatch;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class StanfordParser2 {
	private static Logger logger = LoggerFactory.getLogger(StanfordParser2.class);
		
	private static class SpringModule extends AbstractModule {
		@Override
		protected void configure() {
			bind(BeanFactory.class).toInstance(SystemApplicationContext.getApplicationContext());	
			bind(SentenceDao.class).toProvider(fromSpring(SentenceDao.class, "sentenceDao"));
			bind(DisneyParser.class).toProvider(fromSpring(DisneyParser.class, "parser"));
		}
	}
	
	public static void main(String[] args) throws IOException {
		StopWatch stopWatch = new StopWatch("Parsing and Insertion");		
		stopWatch.start();
		Injector injector = Guice.createInjector(new SpringModule());
		
		List<String> disneyFinancialStatement = FileUtils.readDisneyFinancialStatement("DFS.txt");	
		DisneyParser parser = injector.getInstance(DisneyParser.class);
		parser.parseAndLoad(disneyFinancialStatement, new Document("Disney Financial Statement", 2013, 0));
		
		stopWatch.stop();
		logger.info(stopWatch.prettyPrint());
	}
}
