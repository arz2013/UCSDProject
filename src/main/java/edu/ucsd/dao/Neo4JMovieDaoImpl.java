package edu.ucsd.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;

import edu.ucsd.model.Actor;
import edu.ucsd.model.Movie;
import edu.ucsd.model.Role;
import edu.ucsd.repository.MovieRepository;

/**
 * Transactions are not supported by default through the REST API 
 * 
 * @author rogertan
 *
 */
public class Neo4JMovieDaoImpl {
	@Autowired
	private Neo4jTemplate template;
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Transactional
	public void save(Movie newMovie, Actor actor, Role role) {
		template.save(newMovie);
		template.save(actor);
		template.save(role);
	}
	
	@Transactional
	public void update(Long id, Movie updatedMovie) {
		if(id == null) {
			throw new IllegalArgumentException("Movie id can not be null.");
		}
		Movie movie = movieRepository.findOne(id);
		movie.setGenre(updatedMovie.getGenre());
		movie.setTitle(updatedMovie.getTitle());
		movie.setYear(updatedMovie.getYear());
		template.save(movie);
	}
	
	public Movie findByTitleGenreAndYear(String title, String genre, int year) {
		return movieRepository.findByTitleGenreAndYear(title, genre, year);
	}
}
