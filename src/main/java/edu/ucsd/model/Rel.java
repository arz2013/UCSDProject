package edu.ucsd.model;

import org.neo4j.graphdb.RelationshipType;

public enum Rel implements RelationshipType {
	HAS_PARSE_CHILD, HAS_SENTENCE
}
