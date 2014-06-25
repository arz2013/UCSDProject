package edu.ucsd.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import edu.ucsd.model.Movie;

public interface MovieRepository extends GraphRepository<Movie> {
	@Query("MATCH (m:_Movie {title:{0}, genre:{1}, year:{2}}) return m")
	public Movie findByTitleGenreAndYear(String title, String genre, int year);
}
