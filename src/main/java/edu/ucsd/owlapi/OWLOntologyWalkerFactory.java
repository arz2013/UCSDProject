package edu.ucsd.owlapi;

import static java.lang.String.format;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

public class OWLOntologyWalkerFactory {
	private final static Logger logger = LoggerFactory.getLogger(OWLOntologyWalkerFactory.class);
	
	public static OWLOntologyWalker createOntologyWalker(OWLLoadConfiguration config, FileCachingIRIMapper mapper) throws OWLOntologyCreationException {
		logger.info("Loading ontologies with owlapi...");
		Stopwatch timer = Stopwatch.createStarted();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		manager.addIRIMapper(mapper);
		for (String url: config.getOntologyUrls()) {
			if (url.startsWith("http://") || url.startsWith("https://")) {
				manager.loadOntology(IRI.create(url));
			} else {
				manager.loadOntologyFromOntologyDocument(new File(url));
			}
		}
		logger.info(format("loaded ontologies with owlapi in %d seconds", timer.elapsed(TimeUnit.SECONDS)));
		return new OWLOntologyWalker(manager.getOntologies());
	}
}
