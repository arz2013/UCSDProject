package edu.ucsd.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;

import edu.ucsd.model.Sentence;
import edu.ucsd.model.SentenceToWord;
import edu.ucsd.model.Word;
import edu.ucsd.model.WordToWordDependency;

public class Neo4JSentenceDaoImpl implements SentenceDao {
	@Autowired
	private Neo4jTemplate template;

	public void save(Sentence newSentence) {
		if(newSentence != null) {
			template.save(newSentence);
		}
	}

	public void save(Word newWord) {
		if(newWord != null) {
			template.save(newWord);
		}
		
	}

	public void save(SentenceToWord sentenceToWord) {
		// TODO Auto-generated method stub
		
	}

	public void save(WordToWordDependency dependency) {
		if(dependency != null) {
			template.save(dependency);
		}
	}

}
