package edu.ucsd.model;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity(type="ACTS_IN")
public class Role {
	@GraphId
	private Long id; 
	
    @StartNode 
    private Actor actor;
    @EndNode 
    private Movie movie;
    
    private String role;

    // Only for Spring and Neo4J
    @SuppressWarnings("unused")
	private Role() {
    }
    
	public Role(Actor actor, Movie movie, String roleName) {
		this.actor = actor;
		this.movie = movie;
		this.role = roleName;
	}

	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
