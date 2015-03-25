package ch.usi.ict.dev.ois.interactionKB.unitTest

import org.scalatest.FunSpec
import org.scalatest.GivenWhenThen
import org.scalatest.Matchers
import ch.usi.ict.dev.ois.interactionKB.OISconfigProfileImpl
import ch.usi.ict.dev.ois.interactionKB.OISconfigProfile
import com.google.inject.Guice
import ch.usi.ict.dev.ois.interactionKB.KbConfigModule
import ch.usi.ict.dev.ois.interactionKB.TripleStoreServiceFactory
import org.scalamock.scalatest.MockFactory
import com.hp.hpl.jena.rdf.model.InfModel
import ch.usi.ict.dev.ois.interactionKB.TripleStoreConfModule

//TODO update to the new Confing profile
class TripleStoreServiceGuiceInjectionUnitTest extends FunSpec with MockFactory with GivenWhenThen with Matchers {

	describe("Test the Guice injection into the triple Store of the datasetDir (with a mock infModel)") {

		it("should initialize properly the datasetDir of the TripleStoreService upon a request of a TripleStoreService instance") {

			Given("a ConfigData(domainOntoUrlName, domainOntoLocalFileName) properly initialized")

				val config: OISconfigProfile = OISconfigProfileImpl("http://www.people.lu.unisi.ch/okouyad/PayDeliveryOntology.owl")

			And("a Guice Injector created out of a ConfigModule (OisAbstractGuiceModule) initialized with it")

				val injector = Guice.createInjector(new TripleStoreConfModule(config))

			When("we get the TripleStoreServiceFactory and create a TripleStoreService from it")

				val factory = injector.getInstance(classOf[TripleStoreServiceFactory])
				val mInfmodel = mock[InfModel]
				val tripleStoreService = factory.createTripleStoreService(mInfmodel)

			Then("datasetDir should the workingDir/dataset")
				tripleStoreService.datasetDir should be("dataset")
				info("the actual tripleStoreService.datasetDir is: " + tripleStoreService.datasetDir)

		}

	}

}