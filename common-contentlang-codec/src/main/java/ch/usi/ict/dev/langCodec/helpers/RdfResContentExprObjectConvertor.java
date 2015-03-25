package ch.usi.ict.dev.langCodec.helpers;



import ch.usi.ict.dev.ontology.OceanCL;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.RDFNode;

import jade.content.abs.AbsConcept;
import jade.content.onto.BasicOntology;

/**
 * A class that helps convert Jade Ontology data type into Jena data type
 * TODO Other dataType
 * 
 * @author maatary
 *
 */

public class RdfResContentExprObjectConvertor {

	public static RDFNode toJenaRdfConvertableObject(OntModel ontomodel, AbsConcept rdfRessourceObject) {
		
		RDFNode ressourceValue = null;
		String type            = rdfRessourceObject.getString(OceanCL.Vocabulary.HAS_RDF_RESSOURCE_TYPE.URIValue());
		
		
		if (type == null)
			return ontomodel.createOntResource(rdfRessourceObject.getString(OceanCL.Vocabulary.HAS_RDF_RESSOURCE_VALUE.URIValue()));
		
		switch (type) {
			
			case BasicOntology.STRING:
				
				ressourceValue = ontomodel.createTypedLiteral(rdfRessourceObject.getString(OceanCL.Vocabulary.HAS_RDF_RESSOURCE_VALUE.URIValue()));
			
				break;
			
			case BasicOntology.INTEGER:
				
				ressourceValue = ontomodel.createTypedLiteral(Integer.valueOf(rdfRessourceObject.getString(OceanCL.Vocabulary.HAS_RDF_RESSOURCE_VALUE.URIValue())));
			
				break;

			default:
				
				break;
		}	
		
		return ressourceValue;
	}


}
