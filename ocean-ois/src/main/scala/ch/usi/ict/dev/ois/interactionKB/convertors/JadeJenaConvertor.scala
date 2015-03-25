package ch.usi.ict.dev.ois.interactionKB.convertors

import com.hp.hpl.jena.rdf.model.Model
import com.hp.hpl.jena.rdf.model.ModelFactory
import com.hp.hpl.jena.ontology.OntModelSpec
import jade.content.abs.AbsAgentAction
import org.joda.time.DateTime


object JadeJenaConvertor {
  
  
  
  
  
	
	
	
	def CreateJenaModelFromJadeAction(observedAction: AbsAgentAction , dateTime: DateTime , modelIRI: String): Model = {
		
		val model     = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM)
		val ClaimOnto = model.createOntology(modelIRI)
		
		model
	}

}