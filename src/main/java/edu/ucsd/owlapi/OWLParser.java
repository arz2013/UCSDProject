package edu.ucsd.owlapi;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OWLParser {
	private static Logger logger = LoggerFactory.getLogger(OWLParser.class);
	
	private OWLOntologyManager manager;
	
	public OWLParser(String fileName) throws OWLOntologyCreationException {
		if(fileName == null) {
			throw new IllegalArgumentException("File Name has to be specified.");
		}
		String file = OWLParser.class.getClassLoader().getResource(fileName).getFile();
		manager = OWLManager.createOWLOntologyManager();
		manager.addIRIMapper(new AutoIRIMapper(new File(file), true));
		// Need the line below otherwise, the visitor will not be fired
		manager.loadOntologyFromOntologyDocument(new File(file));
		logger.info("Number of ontologies: " + manager.getOntologies().size());
		
	}
	
	public void walkTheDocument() {
		OWLOntologyWalker walker = new OWLOntologyWalker(manager.getOntologies());
		walker.walkStructure(new OWLVisitor(walker));
	}
	
	public static void main(String[] args) throws Exception {
		OWLParser parser = new OWLParser("DOLCE2.0-Lite-v3.owl");
		parser.walkTheDocument();
	}
}
