package edu.ucsd.model;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@Deprecated
@RelationshipEntity(type="HAS_PARSE_PARENT")
public class LeafToNonLeaf {
	@GraphId
	private Long id; 
	 
    @StartNode
    private Word start;
    
    @EndNode 
    private NonLeafParseNode end;
    
    private LeafToNonLeaf() {
    }
    
    public LeafToNonLeaf(Word start, NonLeafParseNode end) {
    	if(start == null || end == null) {
    		throw new IllegalArgumentException("Neither Parent or Child can be null.");
    	}
    	
    	this.start = start;
    	this.end = end;
    }

	public Long getId() {
		return id;
	}

	public Word getStart() {
		return start;
	}
	
	public NonLeafParseNode getEnd() {
		return end;
	}
}
