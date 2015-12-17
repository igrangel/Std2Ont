package com.ontologies;


import java.io.File;
import java.io.IOException;
import java.net.ConnectException;

import javax.annotation.Nonnull;

import org.coode.owlapi.latex.LatexOntologyFormat;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxOntologyFormat;
import org.coode.owlapi.turtle.TurtleOntologyFormat;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLFunctionalSyntaxOntologyFormat;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLPropertyAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import de.uulm.ecs.ai.owlapi.krssparser.KRSS2OntologyFormat;

public class Ontology {

    @Nonnull
    public static final IRI EXAMPLE_SAVE_IRI = IRI.create("file:///d:/tmp/ontos/ont.owl");
    @Nonnull
    OWLDataFactory df = OWLManager.getOWLDataFactory();
    @Nonnull
    OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
    OWLOntology ont;
    OWLOntologyManager manager;
    IRI ontIRI;
    
    /**
     * Creates an Ontology with the given String URI
     * @param URI
     * @throws OWLOntologyCreationException
     */
	public Ontology(String URI) throws OWLOntologyCreationException {
		super();
		this.ontIRI = org.semanticweb.owlapi.model.IRI.create(URI);
		this.manager = OWLManager.createOWLOntologyManager();
		this.ont = manager.createOntology(ontIRI);
		SimpleIRIMapper mapper = new SimpleIRIMapper(ontIRI, EXAMPLE_SAVE_IRI);
		manager.addIRIMapper(mapper);
	}

	/**
	 * Add a subClassOf axiom (classB is subclass of classA)
	 * @param classA
	 * @param classB
	 * @throws OWLException
	 */
	public void addSubClassAxioms(String classA, String classB) throws OWLException {
        OWLClass clsA = df.getOWLClass(IRI.create(ontIRI + "#" + classA));
        OWLClass clsB = df.getOWLClass(IRI.create(ontIRI + "#" + classB));
        OWLAxiom axiom = df.getOWLSubClassOfAxiom(clsB, clsA);
        AddAxiom addAxiom = new AddAxiom(ont, axiom);
        manager.applyChange(addAxiom);
    }
	
    /**
     * Add a Class axiom 
     * @param classA
     */
	public void addClass(String classA){
		 OWLClass entity = df.getOWLEntity(EntityType.CLASS, IRI.create(ontIRI + "#" + classA));
		 OWLAxiom axiom = df.getOWLDeclarationAxiom(entity);
		 manager.addAxiom(ont,axiom);
	}
	
    /**
     * Add a Object property axiom 
     * @param classA
     */
	public void addObjectProperty(String objProp){
		 OWLObject entity = df.getOWLEntity(EntityType.OBJECT_PROPERTY, IRI.create(ontIRI + "#" + objProp));
		 OWLAxiom axiom = df.getOWLDeclarationAxiom((OWLEntity) entity);
		 manager.addAxiom(ont,axiom);
	}
	
	/*
	 * Add a Object property axiom 
     * @param classA
     */
	public void addDatatypeProperty(String dataProp){
		 OWLObject entity = df.getOWLEntity(EntityType.DATA_PROPERTY, IRI.create(ontIRI + "#" + dataProp));
		 OWLAxiom axiom = df.getOWLDeclarationAxiom((OWLEntity) entity);
		 manager.addAxiom(ont,axiom);
	}
	
	
	/**
	 * Saves ontologies in different formats
	 * @param int The format to be used
	 * @throws OWLOntologyStorageException
	 */
	public void saveOntFormat(int format) throws OWLOntologyStorageException{
		
		switch (format) {
		case 1:
			//The RDFXMLOntologyFormat Syntax
			RDFXMLOntologyFormat rdfxmlFormat = new RDFXMLOntologyFormat();
			manager.saveOntology(ont, rdfxmlFormat,EXAMPLE_SAVE_IRI);
			break;
		case 2:
			OWLXMLOntologyFormat owlxmlFormat = new OWLXMLOntologyFormat();
			manager.saveOntology(ont, owlxmlFormat,EXAMPLE_SAVE_IRI);
			break;
		case 3: 
			//The Manchester OWL Syntax
			ManchesterOWLSyntaxOntologyFormat manSyntaxFormat = new ManchesterOWLSyntaxOntologyFormat();
			manager.saveOntology(ont, manSyntaxFormat,EXAMPLE_SAVE_IRI);
			break;
		case 4:
			//The OWLFunctionalSyntaxOntologyFormat OWL Syntax
			OWLFunctionalSyntaxOntologyFormat owlFunctionalSyntaxFormat = new OWLFunctionalSyntaxOntologyFormat();
			manager.saveOntology(ont, owlFunctionalSyntaxFormat,EXAMPLE_SAVE_IRI);
			break;
		case 5:
			//The KRSS2OntologyFormat 
			KRSS2OntologyFormat krss2FunctionalSyntaxFormat = new KRSS2OntologyFormat();
			manager.saveOntology(ont, krss2FunctionalSyntaxFormat,EXAMPLE_SAVE_IRI);
			break;
		case 6:
			//The TurtleOntologyFormat 
			TurtleOntologyFormat turtleFormat = new TurtleOntologyFormat();
			manager.saveOntology(ont, turtleFormat,EXAMPLE_SAVE_IRI);
			break;
		case 7: 
			//The LatexOntologyFormat
			LatexOntologyFormat latexFormat = new LatexOntologyFormat();
			manager.saveOntology(ont, latexFormat,EXAMPLE_SAVE_IRI);

			break;
		default:
			RDFXMLOntologyFormat rdfxmlFormatDef = new RDFXMLOntologyFormat();
			manager.saveOntology(ont, rdfxmlFormatDef,EXAMPLE_SAVE_IRI);
			break;
		}
	}
	
}
