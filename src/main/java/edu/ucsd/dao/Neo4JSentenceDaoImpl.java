package edu.ucsd.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;

import edu.ucsd.model.NonLeafParseNode;
import edu.ucsd.model.NonLeafToLeaf;
import edu.ucsd.model.ParseChild;
import edu.ucsd.model.Sentence;
import edu.ucsd.model.SentenceToRoot;
import edu.ucsd.model.SentenceToWord;
import edu.ucsd.model.Word;
import edu.ucsd.model.WordToWordDependency;
import edu.ucsd.repository.SentenceRepository;

public class Neo4JSentenceDaoImpl implements SentenceDao {
	@Autowired
	private Neo4jTemplate template;
	
	@Autowired
	private SentenceRepository repository;

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

	public void save(NonLeafParseNode nonLeaf) {
		if(nonLeaf != null) {
			template.save(nonLeaf);
		}
	}

	public void save(ParseChild parseChild) {
		if(parseChild != null) {
			template.save(parseChild);
		}
	}

	public void save(SentenceToRoot sentenceToRoot) {
		if(sentenceToRoot != null) {
			template.save(sentenceToRoot);
		}		
	}

	public void save(NonLeafToLeaf nonLeafToLeaf) {
		if(nonLeafToLeaf != null) {
			template.save(nonLeafToLeaf);
		}
	}

	public Sentence getSentenceByText(String text) {
		return repository.getSentenceByText(text);
	}

	public List<Word> getWordsBySentenceText(String text) {
		return repository.getWordsBySentenceText(text);
	}

	public String getRelationShip(Long startWordId, Long endWordId) {
		return repository.getRelationShip(startWordId, endWordId);
	}
}
