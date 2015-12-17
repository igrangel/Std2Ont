package com.ontologies;

import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Ontology o;
		try {
			o = new Ontology("http://www.semanticweb.org/ontologies/ont.owl");
			o.addSubClassAxioms("classA","classB");
			o.addClass("test");
			o.addObjectProperty("PropertyA");
			o.addDatatypeProperty("PropertyB");
			o.saveOntFormat(6);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		} catch (OWLException e) {
			e.printStackTrace();
		}
		
	}

}
