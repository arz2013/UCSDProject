package edu.ucsd.neo4j;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OntologyConfiguration {

	@NotEmpty
	@JsonProperty
	private String graphLocation;

	public String getGraphLocation() {
		return graphLocation;
	}

	public void setGraphLocation(String graphLocation) {
		this.graphLocation = graphLocation;
	}

}
