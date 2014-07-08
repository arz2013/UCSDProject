package edu.ucsd.owlapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Objects;

import edu.ucsd.neo4j.OntologyConfiguration;

public class OWLLoadConfiguration {
	private OntologyConfiguration ontologyConfiguration;
	private List<String> ontologyUrls;
	private Map<String, String> curies = new HashMap<>();
	private Map<String, String> categories = new HashMap<>();
	private List<MappedProperty> mappedProperties = new ArrayList<>();

	public OntologyConfiguration getOntologyConfiguration() {
		return ontologyConfiguration;
	}

	public List<String> getOntologyUrls() {
		return ontologyUrls;
	}

	public Map<String, String> getCuries() {
		return curies;
	}

	public Map<String, String> getCategories() {
		return categories;
	}

	public List<MappedProperty> getMappedProperties() {
		return mappedProperties;
	}

	public static class MappedProperty {
		String name;
		List<String> properties;

		public String getName() {
			return name;
		}

		public List<String> getProperties() {
			return properties;
		}

		@Override
		public String toString() {
			return Objects.toStringHelper(this.getClass())
					.add("name", name)
					.add("properties", properties)
					.toString();
		}
	}
}
