package edu.ucsd.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import edu.ucsd.model.Sentence;
import edu.ucsd.model.Word;

public interface SentenceRepository extends GraphRepository<Sentence> {
	@Query("start s = node:__types__(className=\"_Sentence\") where s.text = {0} return s")
	public Sentence getSentenceByText(String text);

	@Query("start s = node:__types__(className=\"_Sentence\"), w = node:__types__(className=\"_Word\") match (s)-[:HAS_WORD]->(w) where s.text = {0} return w")
	public List<Word> getWordsBySentenceText(String text);
	
	@Query("start w = node:__types__(className=\"_Word\"), w1 = node:__types__(className=\"_Word\") match (w)-[h:WORD_DEPENDENCY]->(w1) where id(w) = {0} and id(w1) = {1} return h.dependency")
	public String getRelationShip(Long startWordId, Long endWordId);

	@Query("start d = node:__types__(className=\"_Document\"), s = node:__types__(className=\"_Sentence\") match (d)-[:HAS_SENTENCE]->(s) where id(d) = {0} return s order by s.sNum")
	public List<Sentence> getSentencesBasedOnDocument(Long documentId);
	
	@Query("start w = node:__types__(className=\"_Word\") where w.text <> \"ROOT\" and w.neTag = {0} return w")
	public List<Word> getWordsWithNeTag(String neTag);
}
