package edu.ucsd.model;

public enum Dependency {
	NSUB("nsubj");
	
	private String value;
	
	Dependency(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
