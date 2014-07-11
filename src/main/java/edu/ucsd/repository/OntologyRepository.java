package edu.ucsd.repository;

import org.neo4j.graphdb.Node;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import edu.ucsd.model.OntologyEntity;

public interface OntologyRepository extends GraphRepository<OntologyEntity> {
	@Query("start n = node(*) where has(n.fragment) and n.fragment = {0} return n")
	public Node getOntologyEntityWithFragment(String fragment);
}
