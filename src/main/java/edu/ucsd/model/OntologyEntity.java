package edu.ucsd.model;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class OntologyEntity {
	@GraphId 
	private Long id;

	private String uri;
	private String fragment;
	private String type;
	
	public Long getId() {
		return id;
	}
	
	public String getUri() {
		return uri;
	}
	
	public String getFragment() {
		return fragment;
	}
	
	public String getType() {
		return type;
	}
}
