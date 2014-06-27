package edu.ucsd.model;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@Deprecated
@RelationshipEntity(type="HAS_PARSE_PARENT")
public class ParseParent {
	@GraphId
	private Long id; 
	
    @StartNode 
    private NonLeafParseNode child;
    
    @EndNode
    private NonLeafParseNode parent;
    
    private ParseParent() {
    }

	public ParseParent(NonLeafParseNode child, NonLeafParseNode parent) {
		super();
		if(parent == null || child == null) {
			throw new IllegalArgumentException("Neither Parent or Child can be null.");
		}
		
		this.parent = parent;
		this.child = child;
	}

	public NonLeafParseNode getParent() {
		return parent;
	}

	public NonLeafParseNode getChild() {
		return child;
	}
}
