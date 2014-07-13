package edu.ucsd.processing;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import edu.ucsd.ontologymapper.WordsToOntologyClassMapper;

// Temporary class just for prototyping
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
