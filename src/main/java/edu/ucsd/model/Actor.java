package edu.ucsd.model;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class Actor {
	@GraphId 
	private Long id;
	private String name;
	
	@SuppressWarnings("unused")
	private Actor() {
	}
	
	public Actor(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Role playedIn(Movie movie, String roleName) {
        Role role = new Role(this, movie, roleName);
        return role;
    }	
}
