package edu.ucsd.model;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;

import edu.ucsd.utils.Constants;

// Currently not used
@NodeEntity
@TypeAlias("_NonLeafParseNode")
public class NonLeafParseNode {
	@GraphId
	private Long id;
	
	private String value;
	
	private NonLeafParseNode() {
	}
	
	public static NonLeafParseNode newNonLeafParseNode(String value) {
		if(value == null) {
			throw new IllegalArgumentException("Value can not be null.");
		}
		NonLeafParseNode node = new NonLeafParseNode();
		node.value = value;
		return node;
	}
	
	public String getValue() {
		return this.value;
	}

	public boolean isRoot() {
		return Constants.ROOT.equals(value);
	}
}
