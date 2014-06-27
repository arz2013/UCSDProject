package edu.ucsd.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Labels;
import org.springframework.data.neo4j.annotation.NodeEntity;

import edu.ucsd.utils.LabelUtils;

@NodeEntity
public class Word {
	@GraphId
	private Long id;
	
	private String text;
	
	private List<String> posTags;
	
	private List<String> neTags;
	
	@Labels
	private Set<String> labels = new HashSet<String>();
	
	private Word() {	
	}
	
	public static Word newWord(String text) {
		if(text == null) {
			new IllegalArgumentException("A sentence cannot be empty.");
		}
		Word newWord = new Word();
		newWord.text = text;
		newWord.addLabel(newWord.getClass().getSimpleName());
		
		return newWord;
	}
	
	public String getText() {
		return this.text;
	}
	
	public Set<String> getLabels() {
		return Collections.unmodifiableSet(this.labels);
	}

	public void addLabel(String label) {
		if (label != null) {
			labels.add(LabelUtils.createLabel(label));
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Word other = (Word) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
}
