package edu.ucsd.model;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity(type="HAS_PARSE_CHILD")
public class SentenceToRoot {
	@GraphId
	private Long id; 
	
    @StartNode 
    private Sentence sentence;
    
    @EndNode
    private NonLeafParseNode root;
    
    private SentenceToRoot() {
    }
    
    public SentenceToRoot(Sentence sentence, NonLeafParseNode root) {
    	if (sentence == null) {
    		throw new IllegalArgumentException("Sentence can not be null.");
    	}
    	
    	if (root == null || !root.isRoot()) {
    		throw new IllegalArgumentException("Sentence can only be associated with Root");
    	}
    	
    	this.sentence = sentence;
    	this.root = root;
    }
}
