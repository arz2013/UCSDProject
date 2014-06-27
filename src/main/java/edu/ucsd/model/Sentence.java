package edu.ucsd.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Labels;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedToVia;

import edu.ucsd.utils.LabelUtils;

@NodeEntity
public class Sentence {
	@GraphId
	private Long id;
	
	private String text;
	
	@Labels
	private Set<String> labels = new HashSet<String>();
	
	@RelatedToVia(type="HAS_WORD")
	private Set<SentenceToWord> words = new HashSet<SentenceToWord>();
	
	private Sentence() {	
	}
	
	public static Sentence newSentence(String text) {
		if(text == null) {
			new IllegalArgumentException("A sentence cannot be empty.");
		}
		Sentence newSentence = new Sentence();
		newSentence.text = text;
		newSentence.addLabel(newSentence.getClass().getSimpleName());
		
		return newSentence;
	}
	
	public String getText() {
		return this.text;
	}
	
	public Set<String> getLabels() {
		return Collections.unmodifiableSet(labels);
	}

	public void addLabel(String label) {
		if (label != null) {
			labels.add(LabelUtils.createLabel(label));
		}
	}
	
	public SentenceToWord associateSentenceToWord(Word word) {
		return SentenceToWord.newSentenceToWord(this, word);
	}
	
	public void addWord(Word word) {
		this.words.add(associateSentenceToWord(word));
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
		Sentence other = (Sentence) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}

	public int countNumberOfWords() {
		return this.words.size();
	}
}
