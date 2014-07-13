package edu.ucsd;

import org.springframework.context.ApplicationContext;
import edu.ucsd.system.SystemApplicationContext;

public class UCSDProject {

	public static void main(String[] args) {
		ApplicationContext context = SystemApplicationContext.getApplicationContext();
		/*
		OntologyDao dao = OntologyDao.class.cast(context.getBean("ontologyDao"));
		Node entity = dao.getOntologyEntityWithFragment("Natural-Person");
		if (entity != null) {
			System.out.println(entity.getProperty("uri"));
		}
		SentenceDao sentenceDao = SentenceDao.class.cast(context.getBean("sentenceDao"));
		System.out.println(sentenceDao.getWordsWithNeTag(NeTags.ORGANIZATION.name()).size());
		*/
		MappingUtil mapper = MappingUtil.class.cast(context.getBean("mappingUtil"));
		mapper.map();
	
	}

}
