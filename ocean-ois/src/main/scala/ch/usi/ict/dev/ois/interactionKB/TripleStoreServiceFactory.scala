package ch.usi.ict.dev.ois.interactionKB

import com.google.inject.assistedinject.Assisted
import com.hp.hpl.jena.rdf.model.InfModel


trait TripleStoreServiceFactory {
	
	def createTripleStoreService(@Assisted jenaInfModel: InfModel): TripleStoreService

}