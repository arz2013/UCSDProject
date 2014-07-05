package edu.ucsd.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import edu.ucsd.model.Document;

public interface DocumentRepository extends GraphRepository<Document> {
	@Query("match (d:_Document{title:{0}, year:{1}, documentNumber:{2}}) return d")
	public Document getDocumentByTitleYearAndNumber(String title, int year, int documentNumber);
}
