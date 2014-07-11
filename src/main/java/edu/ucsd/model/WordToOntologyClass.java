package edu.ucsd.model;

import org.neo4j.graphdb.Node;
import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity(type="HAS_ONTOLOGY_RELATION")
public class WordToOntologyClass {
	@GraphId
	private Long id;

	@StartNode
	private Word word;
	
	@EndNode
	private Node node;
	
	@SuppressWarnings("unused")
	private WordToOntologyClass() {
	}
	
	public WordToOntologyClass(Word word, Node node) {
		if(word == null || node == null) {
			throw new IllegalArgumentException("Constructor parameters can not be null!!.");
		}
		
		this.word = word;
		this.node = node;
	}
}
