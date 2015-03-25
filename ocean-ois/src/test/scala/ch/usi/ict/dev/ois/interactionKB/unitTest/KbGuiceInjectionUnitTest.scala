package ch.usi.ict.dev.ois.interactionKB.unitTest

import org.scalatest.FeatureSpec
import org.scalatest.GivenWhenThen
import org.scalatest.Matchers
import org.scalatest.FunSpec
import java.net.URI
import ch.usi.ict.dev.ois.interactionKB.OISconfigProfileImpl
import ch.usi.ict.dev.ois.interactionKB.OISconfigProfile
import com.google.inject.AbstractModule
import ch.usi.ict.dev.ois.interactionKB.InteractionKB
import com.google.inject.assistedinject.FactoryModuleBuilder
import com.google.inject.Guice
import ch.usi.ict.dev.ois.OisAbstractGuiceModule
import ch.usi.ict.dev.ois.interactionKB.TripleStoreService
import ch.usi.ict.dev.ois.interactionKB.TripleStoreServiceIml
import ch.usi.ict.dev.ois.interactionKB.TripleStoreServiceFactory
import ch.usi.ict.dev.ois.interactionKB.KbConfigModule


class KbGuiceInjectionUniTest extends FunSpec with GivenWhenThen with Matchers {
		
	describe("Testing the Guice injection into a Kb of some supplied ConfigData ") {
		
		it("should produce a Kb with the domainOntology URL and locafile set according to the ConfigData supplied") {
			
			Given("a ConfigData(domainOntoUrlName, domainOntoLocalFileName) properly initialized") 
				
				val config:OISconfigProfile = OISconfigProfileImpl("http://www.people.lu.unisi.ch/okouyad/PayDeliveryOntology.owl")
			
				
			And("a Guice Injector created out of a ConfigModule (OisAbstractGuiceModule) initialized with it")
				
				val injector = Guice.createInjector(new KbConfigModule(config))
			
				
			When("we create the the Kb ")
			
				val kb = injector.getInstance(classOf[InteractionKB])

			
			Then("The Kb domainOnto File should be equal to the one supplied")
				kb.getDomainOntoURL() should be (config.domainOntoIRIName)
				info("the actual Kb is:" + kb.getDomainOntoURL())
				
			And("The Kb DomainOntoLocalFileName should to the one supplied in the config")
				kb.getDomainOntoLocalFile().toString().split("/").last should be(config.domainOntoName)
				info("the actual Kb is:" + kb.getDomainOntoURL())
			
		}
		
		
	}
	
	

}