package edu.ucsd.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.support.index.IndexType;

@NodeEntity
@TypeAlias("_Word")
public class Word {
	@GraphId
	private Long id;
	
	@Indexed(indexType=IndexType.FULLTEXT, indexName = "wordtext")
	private String text;
	
	private Set<String> partOfSpeechTags = new HashSet<String>();
	
	private Set<String> nameEntityTags = new HashSet<String>();
	
	private Word() {	
	}
	
	public static Word newWord(String text) {
		if(text == null) {
			new IllegalArgumentException("A sentence cannot be empty.");
		}
		Word newWord = new Word();
		newWord.text = text;
		
		return newWord;
	}
	
	public String getText() {
		return this.text;
	}
	
	public void addPosTag(String posTag) {
		if (posTag != null) {
			partOfSpeechTags.add(posTag);
		}
	}
	
	public void addNameEntityTag(String neTag) {
		if (neTag != null) {
			nameEntityTags.add(neTag);
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
