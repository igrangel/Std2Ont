package com.ontologies;


import java.io.File;
import java.io.IOException;
import java.net.ConnectException;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.change.AddImportData;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLPropertyAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;
import org.semanticweb.owlapi.formats.ManchesterSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;



public class Ontology {

	@Nonnull
	public IRI saveIri;

	public static final String DCTERMS_URI = "http://purl.org/dc/terms/";
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
	}
	
	public void setSaveIri(String path){
		saveIri = IRI.create("file:///"+ path +"/ont.owl");
		SimpleIRIMapper mapper = new SimpleIRIMapper(ontIRI, saveIri);
		manager.addIRIMapper(mapper);
	}
	

	/**
	 * Add a subClassOf axiom (classB is subclass of classA)
	 * @param String classA
	 * @param String classB
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
	 * 
	 * @param cls
	 * @param parentClass
	 */
	public void addSubclass(OWLClass cls, OWLClass parentClass){ 
		manager.applyChange(new AddAxiom(ont, df.getOWLSubClassOfAxiom(cls, parentClass))); 
	} 

	/**
	 * Add a Class axiom given an String as name 
	 * @param classA
	 */
	public OWLClass addClass(String classA){
		OWLClass entity = df.getOWLEntity(EntityType.CLASS, IRI.create(ontIRI + "#" + classA));
		OWLAxiom axiom = df.getOWLDeclarationAxiom(entity);
		manager.addAxiom(ont,axiom);
		return entity;
	}

	/**
	 * Add a Object property axiom 
	 * @param classA
	 */
	public OWLObjectProperty addObjectProperty(String objProp){
		OWLObjectProperty entity = df.getOWLEntity(EntityType.OBJECT_PROPERTY, IRI.create(ontIRI + "#" + objProp));
		OWLAxiom axiom = df.getOWLDeclarationAxiom((OWLEntity) entity);
		manager.addAxiom(ont,axiom);
		return entity;
	}

	/*
	 * Add a Object property axiom 
	 * @param classA
	 */
	public OWLDataProperty addDatatypeProperty(String dataProp){
		OWLDataProperty entity = df.getOWLEntity(EntityType.DATA_PROPERTY, IRI.create(ontIRI + "#" + dataProp));
		OWLAxiom axiom = df.getOWLDeclarationAxiom((OWLEntity) entity);
		manager.addAxiom(ont,axiom);
		return entity;
	}
	
	/**
	 * @todo
	 * @param property
	 */
	public void dataPropertyAssertion(String propertyName, String value){
		OWLDataProperty dataProp = df.getOWLDataProperty(IRI.create(ontIRI + "#" + propertyName));
		//OWLAxiom axiom4 = df.getOWLDataPropertyAssertionAxiom(dataProp, john, value);
	}

	/**
	 * Adds a domain axiom for a given class
	 * @param classA
	 * @param dataProp
	 */
	public void addDatatypeDomain(String classA, String dataProp, String range){
		OWLClass cls = df.getOWLEntity(EntityType.CLASS, IRI.create(ontIRI + "#" + classA));
		OWLAxiom axiomClass = df.getOWLDeclarationAxiom(cls);
		manager.addAxiom(ont,axiomClass);
		
		OWLDataProperty dataProperty = df.getOWLEntity(EntityType.DATA_PROPERTY, IRI.create(ontIRI + "#" + dataProp));
		OWLAxiom domainAxiom = df.getOWLDataPropertyDomainAxiom(dataProperty,cls);
		//OWLAxiom rangeAxiom = df.getOWLDataPropertyRangeAxiom(dataProperty,range);
		AddAxiom addAxiom = new AddAxiom(ont,domainAxiom);
		manager.applyChange(addAxiom);
	}

	
	/**
	 * Add rdfs:comment to a given entity(class, property)
	 * @param entity
	 * @param annotation
	 * @param lang
	 */
	public void addRDFSLabel(OWLEntity entity, String annotation, String lang){
		OWLAnnotation commentAnno = df.getOWLAnnotation(df.getRDFSLabel(), df.getOWLLiteral(annotation,lang));
		OWLAxiom ax = df.getOWLAnnotationAssertionAxiom(entity.getIRI(), commentAnno);
		manager.applyChange(new AddAxiom(ont, ax));
	}
	
	/**
	 * Add rdfs:comment to a given entity(class, property)
	 * @param entity
	 * @param annotation
	 * @param lang
	 */
	public void addRDFSComment(OWLEntity entity, String annotation, String lang){
		OWLAnnotation commentAnno = df.getOWLAnnotation(df.getRDFSComment(), df.getOWLLiteral(annotation,lang));
		OWLAxiom ax = df.getOWLAnnotationAssertionAxiom(entity.getIRI(), commentAnno);
		manager.applyChange(new AddAxiom(ont, ax));
	}
	
	/**
	 * Add skosPrefLabel to a given entity(class, property)
	 * @param entity
	 * @param annotation
	 * @param lang
	 */
	public void addSkosPrefLabel(OWLEntity entity, String annotation, String lang){
		OWLAnnotationProperty skosPrefLabel = df.getOWLAnnotationProperty(SKOSVocabulary.PREFLABEL.getIRI());
		OWLAnnotation commentAnno2 = df.getOWLAnnotation(skosPrefLabel, df.getOWLLiteral(annotation,lang));
		OWLAxiom ax = df.getOWLAnnotationAssertionAxiom(entity.getIRI(), commentAnno2);
		manager.applyChange(new AddAxiom(ont, ax));
	}
	
	/**
	 * Add altLabel to a given entity(class, property)
	 * @param entity
	 * @param annotation
	 * @param lang
	 */
	public void addSkosAltLabel(OWLEntity entity, String annotation, String lang){
		OWLAnnotationProperty skosPrefLabel = df.getOWLAnnotationProperty(SKOSVocabulary.ALTLABEL.getIRI());
		OWLAnnotation commentAnno2 = df.getOWLAnnotation(skosPrefLabel, df.getOWLLiteral(annotation,lang));
		OWLAxiom ax = df.getOWLAnnotationAssertionAxiom(entity.getIRI(), commentAnno2);
		manager.applyChange(new AddAxiom(ont, ax));
	}
	
	/**
	 * Adding dublin core identifier
	 * @param entity
	 * @param annotation
	 * @param lang
	 */
	public void addDCId(OWLEntity entity, String annotation, String lang){
		OWLAnnotationProperty id = df.getOWLAnnotationProperty(IRI.create(DCTERMS_URI + "identifier"));
		OWLAnnotation commentAnno3 = df.getOWLAnnotation(id, df.getOWLLiteral(annotation));
		OWLAxiom ax = df.getOWLAnnotationAssertionAxiom(entity.getIRI(), commentAnno3);
		manager.applyChange(new AddAxiom(ont, ax));
	}


	public void dataPropAssertion(String dataProp, String ind, String value){
		OWLDataProperty dataProperty = df.getOWLDataProperty(IRI.create(ont.getOntologyID().getOntologyIRI() + "#" + dataProp));
		OWLIndividual individual = df.getOWLNamedIndividual(IRI.create(ont.getOntologyID().getOntologyIRI() + "#" + ind));
		// We create a data property assertion instead of an object property assertion
		OWLAxiom axiom4 = df.getOWLDataPropertyAssertionAxiom(dataProperty, individual, value);
		manager.applyChange(new AddAxiom(ont, axiom4));
	}

	/**
	 * Amount of classes
	 * @return
	 */
	public long countClasses(){ 
		return this.ont.getClassesInSignature().size(); 
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
			RDFXMLDocumentFormat rdfxmlFormat = new RDFXMLDocumentFormat();
			manager.saveOntology(ont, rdfxmlFormat,saveIri);
			break;
		case 2:
			OWLXMLDocumentFormat owlxmlFormat = new OWLXMLDocumentFormat();
			manager.saveOntology(ont, owlxmlFormat,saveIri);
			break;
		case 3: 
			//The Manchester OWL Syntax
			ManchesterSyntaxDocumentFormat manSyntaxFormat = new ManchesterSyntaxDocumentFormat();
			manager.saveOntology(ont, manSyntaxFormat,saveIri);
			break;
		case 4:
			//The TurtleOntologyFormat 
			TurtleDocumentFormat turtleFormat = new TurtleDocumentFormat();
			manager.saveOntology(ont, turtleFormat,saveIri);
			break;
		default:
			RDFXMLDocumentFormat rdfxmlFormatDef = new RDFXMLDocumentFormat();
			manager.saveOntology(ont, rdfxmlFormatDef,saveIri);
			break;
		}
	}

}
