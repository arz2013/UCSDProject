package edu.ucsd.processing;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.transaction.annotation.Transactional;

import edu.ucsd.model.Document;
import edu.ucsd.parser.DisneyParser;
import edu.ucsd.utils.FileUtils;

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
	
	@Transactional
	public void process(List<String> documentNames) throws IOException {
		int documentNumber = 0;
		for(String documentName : documentNames) {
			parser.parseAndLoad(FileUtils.readDisneyFinancialStatement(documentName), new Document("Disney Financial Statement", 2013, documentNumber));
			commonAncestor.findNPAncestorNodeAndMark();
			util.map();
			documentNumber++;
		}
	}
}
