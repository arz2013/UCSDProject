package edu.ucsd.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import edu.ucsd.model.Sentence;
import edu.ucsd.model.Word;
import edu.ucsd.model.WordToWordDependency;

public interface SentenceRepository extends GraphRepository<Sentence> {
	@Query("match (s:_Sentence{text:{0}}) return s")
	public Sentence getSentenceByText(String text);

	@Query("match (s:_Sentence{text:{0}})-[h:HAS_WORD]->(w:_Word) return w")
	public List<Word> getWordsBySentenceText(String text);
	
	@Query("match (w:_Word)-[h:WORD_DEPENDENCY]->(w1:_Word) where id(w) = {0} and id(w1) = {1} return h.dependency")
	public String getRelationShip(Long startWordId, Long endWordId);
}
