package edu.ucsd.processing;

import java.util.List;

import javax.inject.Inject;

import edu.ucsd.parser.DisneyParser;

/**
 * The main class that pulls together all processing that needs to be done
 * - Right now it's really focused on processing a single document, but it shoud not be difficult 
 *   to add support for multiple documents
 * 
 * @author rogertan
 *
 */
public class ProcessingMain {
	@Inject
	private DisneyParser parser;
	
	@Inject 
	private MappingUtil util;
	
	@Inject
	private CommonAncestor commonAncestor;
	
	public void process(List<String> documentNames) {
		for(String documentName : documentNames) {
			
		}
	}
}
