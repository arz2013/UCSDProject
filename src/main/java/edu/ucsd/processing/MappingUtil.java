package edu.ucsd.processing;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import edu.ucsd.ontologymapper.WordsToOntologyClassMapper;

// Temporary class just for prototyping
/**
 * 1. Maps words whose NETag is PERSON to an OWLClass of Natural-Person
 * 2. Maps words whose NETag is ORGANIZATION to an OWLCLass of Organization
 * 
 * @author rogertan
 *
 */
public class MappingUtil {
	private List<WordsToOntologyClassMapper> mappers;
	
	public void setMappers(List<WordsToOntologyClassMapper> mappers) {
		this.mappers = mappers;
	}
	
	@Transactional
	public void map() {
		for(WordsToOntologyClassMapper mapper : mappers) {
			mapper.map();
		}
	}
}
