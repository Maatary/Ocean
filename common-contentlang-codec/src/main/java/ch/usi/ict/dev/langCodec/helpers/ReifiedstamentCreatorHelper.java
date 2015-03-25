package ch.usi.ict.dev.langCodec.helpers;

import jade.content.abs.AbsConcept;
import jade.content.abs.AbsPredicate;
import ch.usi.ict.dev.ontology.OceanCL;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ReifiedStatement;
import com.hp.hpl.jena.rdf.model.Statement;

public class ReifiedstamentCreatorHelper {

	public static ReifiedStatement createAndAddJenaReifiedStatementToModel(AbsPredicate statementObject, OntModel ontomodel) {
		
		Statement statement = ontomodel.createStatement(
				ontomodel.createOntResource(statementObject.getString(OceanCL.Vocabulary.HAS_SUBJECT.URIValue())), 
				ontomodel.createProperty(statementObject.getString(OceanCL.Vocabulary.HAS_PREDICATE.URIValue())), 
				RdfResContentExprObjectConvertor.toJenaRdfConvertableObject(
															 ontomodel, 
															 (AbsConcept) statementObject.getAbsObject((OceanCL.Vocabulary.HAS_OBJECT.URIValue())))
               );

		return statement.createReifiedStatement();

		
	}

}
