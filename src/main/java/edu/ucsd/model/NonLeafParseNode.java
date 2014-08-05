package edu.ucsd.model;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;

import edu.ucsd.utils.Constants;

// Currently not used
@NodeEntity
@TypeAlias("_NonLeafParseNode")
public class NonLeafParseNode {
	public static String TYPE_FIELD = "type";
	
	@GraphId
	private Long id;
	
	private String value;
	
	private String type = ""; // Spring can populate this later
	
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
	
	public String getType() {
		return this.type;
	}

	public boolean isRoot() {
		return Constants.ROOT.equals(value);
	}

	public Long getId() {
		return this.id;
	}
}
