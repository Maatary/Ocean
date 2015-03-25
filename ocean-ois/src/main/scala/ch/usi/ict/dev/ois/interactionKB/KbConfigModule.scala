package ch.usi.ict.dev.ois.interactionKB

import ch.usi.ict.dev.ois.OisAbstractGuiceModule
import com.google.inject.assistedinject.FactoryModuleBuilder


/**
 * It is a the basic typical Guice module containing the minimal graph of dependence
 * to instantiate the Interaction knowledge base. It could serve as prototype to create
 * test module for instance. 
 * 
 * ConfigData represents the data coming from the Inputs. Hence, the Class is bound to the instance coming as parameter
 * of the module constructor (i.e. in represents the data coming from the inputs). 
 * 
 * 
 * A FactoryModule builder is added to create a TripleStoreServiceFactory that enable to create TripleStoreService
 * with the assistance of its consumer that must supply an infModel. Indeed the TripleStoreService requires an infModel in its constructor. 
 * The target consumer is the the Kb, which create an Infmodel and supplies it to the TripleStoreServiceFactory creationMethod to create 
 * the TripleStoreService that it requires. The constructor of the InteractionKb takes as parameter, the configData and the TripleStoreServiceFactory
 * 
 */
 class KbConfigModule(configData: OISconfigProfile) extends OisAbstractGuiceModule(configData) {

	def configure(configData: OISconfigProfile): Unit = {	
		//TODO bind(classOf[InteractionKbService]).to(InteractionKbServiceImpl)
		
		val fb = new FactoryModuleBuilder()
		install(fb.implement(classOf[TripleStoreService], classOf[TripleStoreServiceIml]).build(classOf[TripleStoreServiceFactory]))
	}

}