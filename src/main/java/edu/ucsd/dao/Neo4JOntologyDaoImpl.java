package edu.ucsd.dao;

import java.util.HashMap;

import javax.inject.Inject;

import org.neo4j.graphdb.Node;
import org.springframework.data.neo4j.support.Neo4jTemplate;

import edu.ucsd.model.WordToOntologyClass;
import edu.ucsd.repository.OntologyRepository;

public class Neo4JOntologyDaoImpl implements OntologyDao {
	@Inject
	private Neo4jTemplate template;
	
	@Inject
	private OntologyRepository repository;

	@Override
	public Node getOntologyEntityWithFragment(String fragment) {
		return repository.getOntologyEntityWithFragment(fragment);
	}

	@Override
	public void saveWordToOntologyAssociation(WordToOntologyClass assoc) {
		if(assoc != null) {
			// We have to do this because of a conflict between Spring and the Ontology classes
			Node word = template.getNode(assoc.getWord().getId());
			template.createRelationshipBetween(word, assoc.getNode(), "HAS_ONTOLOGY_RELATION", new HashMap<String, Object>());
		}
	}
	
	
}
