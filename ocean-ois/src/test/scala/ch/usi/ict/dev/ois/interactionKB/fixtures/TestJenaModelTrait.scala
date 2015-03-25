package ch.usi.ict.dev.ois.interactionKB.fixtures

import com.hp.hpl.jena.rdf.model.ModelFactory
import com.hp.hpl.jena.ontology.OntModelSpec
import com.hp.hpl.jena.vocabulary.OWL2

trait JenaModelLordOfTheRinghasPriceTrait {
	
	
	val modelIRI				       = "http://www.people.usi.ch/okouyad/Mary_to_john_Claim_01.owl"
	val model 			         	   = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM)	
	
	
	
	private val maryTojohnClaimOnto    = model.createOntology(modelIRI)
	private val MaryRepoOntoPrefix     = "http://www.people.usi.ch/okouyad/MaryRepositoryOntology.owl#"
	private val payDeliverPrefix       = "http://www.people.lu.unisi.ch/okouyad/PayDeliveryOntology.owl#"

	model.setNsPrefix("", modelIRI + "#")
	model.setNsPrefix("MaryRepoOnto", MaryRepoOntoPrefix)
	model.setNsPrefix("PayDelivery", payDeliverPrefix)

	
		
	private val indiv 			       = model.createResource(MaryRepoOntoPrefix  + "Lord_of_the_Ring", OWL2.NamedIndividual)
	private val hasPriceProperty 	   = model.createProperty(payDeliverPrefix    + "hasPrice")
	
	
	indiv.addLiteral(hasPriceProperty, 15)

}