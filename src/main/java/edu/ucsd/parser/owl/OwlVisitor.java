package edu.ucsd.parser.owl;

import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;

public class OwlVisitor extends OWLOntologyWalkerVisitor<Void> {

	public OwlVisitor(OWLOntologyWalker walker) {
		super(walker);
		// TODO Auto-generated constructor stub
	}

	@Override
    public Void visit(OWLOntology ontology) {
		return null;
	}
	
	@Override
	public Void visit(OWLClass desc) {
		return null;
	}  
	
	@Override
	public Void visit(OWLAnnotationAssertionAxiom axiom) {
		return null;
	}  
	
	@Override
	public Void visit(OWLNamedIndividual individual) {
		return null;
	}
	
	@Override
	public Void visit(OWLSameIndividualAxiom axiom) {
		return null;
	}
	
	@Override
	public Void visit(OWLDifferentIndividualsAxiom axiom) {
		return null;
	}
	
	@Override
	public Void visit(OWLClassAssertionAxiom axiom) {
		return null;
	}
	
	@Override
	public Void visit(OWLDataPropertyAssertionAxiom axiom) {
		return null;
	}
	
	@Override
	public Void visit(OWLSubClassOfAxiom axiom) {
		return null;
	}
	
	@Override
	public Void visit(OWLObjectIntersectionOf desc) {
		return null;
	}
	
	@Override
	public Void visit(OWLObjectUnionOf desc) {
		return null;
	}
	
	@Override
	public Void visit(OWLObjectPropertyAssertionAxiom axiom) {
	    return null;
	}

	@Override
	public Void visit(OWLEquivalentClassesAxiom axiom) {
		return null;
	}	

	@Override
	public Void visit(OWLDisjointClassesAxiom axiom) {
		return null;
	}

	@Override
	public Void visit(OWLObjectComplementOf desc) {
		return null;
	}

	@Override
	public Void visit(OWLSubObjectPropertyOfAxiom axiom) {
		return null;
	}

	@Override
	public Void visit(OWLSubPropertyChainOfAxiom axiom) {

		return null;
	}	
}
