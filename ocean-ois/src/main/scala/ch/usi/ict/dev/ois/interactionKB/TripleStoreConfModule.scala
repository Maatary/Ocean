package ch.usi.ict.dev.ois.interactionKB

import ch.usi.ict.dev.ois.OisAbstractGuiceModule
import com.google.inject.assistedinject.FactoryModuleBuilder

class TripleStoreConfModule(configData: OISconfigProfile) extends OisAbstractGuiceModule(configData){
	
	def configure(configData: OISconfigProfile): Unit = {
		
		val fb = new FactoryModuleBuilder()

		install(fb.implement(classOf[TripleStoreService], classOf[TripleStoreServiceIml]).build(classOf[TripleStoreServiceFactory]))
		
	}

}