package edu.ucsd;

import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.Node;
import org.springframework.context.ApplicationContext;

import edu.ucsd.dao.SentenceDao;
import edu.ucsd.model.NeTags;
import edu.ucsd.system.SystemApplicationContext;

public class CommonAncestor {
	public static void main(String[] args) {
		ApplicationContext context = SystemApplicationContext.getApplicationContext();
		SentenceDao sentenceDao = SentenceDao.class.cast(context.getBean("sentenceDao"));
		Iterable<Map<String, Object>> words = sentenceDao.getWordsKeyedBySentenceNumberWithSpecificNeTag(NeTags.ORGANIZATION);
		for(Map<String, Object> entry : words) {
			for(String key : entry.keySet()) {
				System.out.println(entry.get(key));
			}
		}
	}
}
