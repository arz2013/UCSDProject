package edu.ucsd.dao;

import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;

import edu.ucsd.model.Document;
import edu.ucsd.model.DocumentToSentence;
import edu.ucsd.model.NonLeafParseNode;
import edu.ucsd.model.NonLeafToLeaf;
import edu.ucsd.model.ParseChild;
import edu.ucsd.model.Sentence;
import edu.ucsd.model.SentenceToNonLeafParseNode;
import edu.ucsd.model.SentenceToWord;
import edu.ucsd.model.Word;
import edu.ucsd.model.WordToWordDependency;
import edu.ucsd.repository.DocumentRepository;
import edu.ucsd.repository.SentenceRepository;

public class Neo4JSentenceDaoImpl implements SentenceDao {
	@Inject
	private Neo4jTemplate template;
	
	@Inject
	private SentenceRepository repository;
	
	@Inject
	private DocumentRepository docRepository;

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

	public void save(SentenceToNonLeafParseNode sentenceToRoot) {
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

	public void save(Document document) {
		if(document != null) {
			template.save(document);
		}
	}

	public void save(DocumentToSentence documentToSentence) {
		if(documentToSentence != null) {
			template.save(documentToSentence);
		}
	}

	public Document getDocumentByTitleYearAndNumber(String title, int year,
			int documentNumber) {
		return docRepository.getDocumentByTitleYearAndNumber(title, year, documentNumber);
	}
	
	public List<Sentence> getSentencesBasedOnDocument(Long documentId) {
		return repository.getSentencesBasedOnDocument(documentId);
	}
}
