package edu.ucsd.model;

import java.util.HashSet;
import java.util.Set;

public enum ParseTreeTags {
	NP,
	VB,   // verb, base form 
	VBD,  // verb, past tense
	VBG,  // verb, gerund/present participle  e.g. taking
	VBN,  // verb, past participle e.g. taken
	VBP,  // verb, sing. present, non-3d
	VBZ	  // verb, 3rd person sing. present
	;
	public final static Set<String> getVerbTagsAsString() {
		Set<String> verbTags = new HashSet<String>();
		for(ParseTreeTags tag : ParseTreeTags.values()) {
			if(tag.name().startsWith("VB")) {
				verbTags.add(tag.name());
			}
		}
		
		return verbTags;
	}
}
