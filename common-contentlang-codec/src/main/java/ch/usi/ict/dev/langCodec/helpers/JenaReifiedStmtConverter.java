package ch.usi.ict.dev.langCodec.helpers;


import jade.content.abs.AbsConcept;
import jade.content.abs.AbsPredicate;
import jade.content.onto.BasicOntology;
import ch.usi.ict.dev.ontology.OceanCL;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ReifiedStatement;

public class JenaReifiedStmtConverter {
	
	
	//TODO Refactor JenaReifiedStmtConverter with Enum
	
	
	 static enum XSDType {
		 
		 INT("int"),
		 
		 STRING("STRING");
		 
		 
		 XSDType (String type) {
			 
		 }
	 
	 }

	//TODO Support all necessary XSD Type
	public static AbsPredicate toContentExprStmt(ReifiedStatement reifiedStmt) {
		
		RDFNode object = reifiedStmt.getStatement().getObject();
		
		
		AbsPredicate StatementObject = new AbsPredicate(OceanCL.Vocabulary.REIFIED_STATEMENT.URIValue());
	    StatementObject.set(OceanCL.Vocabulary.HAS_SUBJECT.URIValue(), reifiedStmt.getStatement().getSubject().getURI());
		StatementObject.set(OceanCL.Vocabulary.HAS_PREDICATE.URIValue(), reifiedStmt.getStatement().getPredicate().getURI());
		
		AbsConcept rdfRessourceObject = new AbsConcept(OceanCL.Vocabulary.RDF_RESSOURCE.URIValue());
		
		if (object.isLiteral()) {
			
			rdfRessourceObject.set(OceanCL.Vocabulary.HAS_RDF_RESSOURCE_VALUE.URIValue(), object.asLiteral().getLexicalForm());
			
			String xsdType = object.asLiteral().getDatatype().getURI();
			String xsdTypeShorten = xsdType.substring(xsdType.lastIndexOf("#") + 1 );
			
			switch (xsdTypeShorten) {
			
			case "string":
				
				rdfRessourceObject.set(OceanCL.Vocabulary.HAS_RDF_RESSOURCE_TYPE.URIValue(), BasicOntology.STRING);
				break;
				
			case "int":
				
				rdfRessourceObject.set(OceanCL.Vocabulary.HAS_RDF_RESSOURCE_TYPE.URIValue(), BasicOntology.INTEGER);
				break;

			default:
				break;
			}
			
			
		}
		else {
			
			rdfRessourceObject.set(OceanCL.Vocabulary.HAS_RDF_RESSOURCE_VALUE.URIValue(), object.asResource().getURI());
			
		}
		
		

	    StatementObject.set(OceanCL.Vocabulary.HAS_OBJECT.URIValue(), rdfRessourceObject);
		
		return StatementObject;
	}

}
