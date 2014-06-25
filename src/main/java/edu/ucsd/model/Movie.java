package edu.ucsd.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Labels;
import org.springframework.data.neo4j.annotation.NodeEntity;

import edu.ucsd.utils.LabelUtils;

@NodeEntity
public class Movie {
	@GraphId 
	private Long id;

	private String title;
	private String genre;
	private int year;
	
	@Labels
	private Set<String> labels = new HashSet<String>();
	
	/**
	 * Needed to support Spring Neo4J Dependency
	 */
	public Movie() {
	}
	
	public Long getId() {
		return this.id;
	}

	public static Movie newMovie(String title, String genre, int year) {
		Movie newMovie = new Movie();
		newMovie.title = title;
		newMovie.genre = genre;
		newMovie.year = year;
		
		newMovie.addLabel(Movie.class.getSimpleName());
		
		return newMovie;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Set<String> getLabels() {
		return labels;
	}

	public void addLabel(String label) {
		if (label != null) {
			labels.add(LabelUtils.createLabel(label));
		}
	}
}
