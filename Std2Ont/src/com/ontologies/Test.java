package com.ontologies;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Ontology o;
		try {
			o = new Ontology("http://www.semanticweb.org/ontologies/ont.owl");
			o.addSubClassAxioms("classA","classB");
			OWLClass classA = o.addClass("test");
			o.addRDFSComment(classA, "Class A", "en");
			OWLObjectProperty objProp = o.addObjectProperty("PropertyA");
			o.addRDFSComment(objProp, "Object property ", "en");
			o.dataPropAssertion("Code","ind1","33");
			o.saveOntFormat(4);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		} catch (OWLException e) {
			e.printStackTrace();
		}
		
	}

}
