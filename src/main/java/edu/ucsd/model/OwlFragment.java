package edu.ucsd.model;

public enum OwlFragment {
	ORGANIZATION("Organization"), NATURAL_PERSON("Natural-Person");
	
	private String fragmentName;
	
	OwlFragment(String fragmentName) {
		this.fragmentName = fragmentName;
	}
	
	public String getFragmentName() {
		return this.fragmentName;
	}
}
