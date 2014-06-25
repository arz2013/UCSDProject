package edu.ucsd.repository;


import org.springframework.data.neo4j.repository.GraphRepository;

import edu.ucsd.model.Sentence;

public interface SentenceRepository extends GraphRepository<Sentence> {
}
