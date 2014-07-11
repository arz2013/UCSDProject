package edu.ucsd.dao;

import org.neo4j.graphdb.Node;

import edu.ucsd.model.WordToOntologyClass;

public interface OntologyDao {
	public Node getOntologyEntityWithFragment(String fragment);
	public void saveWordToOntologyAssociation(WordToOntologyClass assoc);
}
