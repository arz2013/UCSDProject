package edu.ucsd;

import static com.google.inject.spring.SpringIntegration.fromSpring;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.inject.AbstractModule;

import edu.ucsd.dao.SentenceDao;
import edu.ucsd.parser.DisneyParser;

public class SpringTestModule extends AbstractModule {
	private static ApplicationContext getApplicationContext() {
		return new ClassPathXmlApplicationContext("nlp-context-test.xml");	
	}
	
	@Override
	protected void configure() {
		bind(BeanFactory.class).toInstance(getApplicationContext());	
		bind(SentenceDao.class).toProvider(fromSpring(SentenceDao.class, "sentenceDao"));
		bind(DisneyParser.class).toProvider(fromSpring(DisneyParser.class, "parser"));		
		bind(GraphDatabaseService.class).toProvider(fromSpring(GraphDatabaseService.class, "graphDatabaseService"));
	}
}
