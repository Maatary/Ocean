package ch.usi.ict.dev.ois.interactionKB.unitTest


import org.scalatest.GivenWhenThen
import org.scalatest.Matchers
import ch.usi.ict.dev.ois.interactionKB.OISconfigProfile
import ch.usi.ict.dev.ois.interactionKB.OISconfigProfileImpl
import com.google.inject.Guice
import ch.usi.ict.dev.ois.interactionKB.TripleStoreConfModule
import ch.usi.ict.dev.ois.interactionKB.TripleStoreServiceFactory
import com.hp.hpl.jena.rdf.model.InfModel
import org.scalamock.scalatest.MockFactory
import ch.usi.ict.dev.ois.interactionKB.fixtures.JenaModelLordOfTheRinghasPriceTrait
import org.scalatest.fixture
import ch.usi.ict.dev.ois.interactionKB.fixtures.InteractionKbFixtureTrait
import ch.usi.ict.dev.ois.interactionKB.fixtures.TripleStoreServiceFixtureTrait
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class TripleStoreServiceAddNamedGraphUnitTest extends fixture.FunSpec with TripleStoreServiceFixtureTrait with GivenWhenThen with Matchers {

	

	describe("Test the addition of a named Graph to a TripleStoreService via its AddNamedGraph method to the : ") {

		it("should create a named Graph when the method is called") { tripleStr =>

			new JenaModelLordOfTheRinghasPriceTrait {

				Given("a TripleStoreService and a jena model with a proper Iri")
					//See TripleStoreServiceFixtureTrait
					//see JenaModelLordOfTheRinghasPriceTrait

				When("we add this model as a named model named after its IRI trough the AddNamedGraph method")
					tripleStr.addNamedGraph(modelIRI, model)
				
				Then("the tripleStoreService should contain a stored namedmodel version of it")
					val askresult = tripleStr.answerAskQueryAgainstPersistedModel("ask  where {GRAPH " + "<" + modelIRI + ">"+ "{?a ?b ?c} }")
					askresult should be(true)
					val outstream  =  new ByteArrayOutputStream()
					val printsream =  new PrintStream(outstream) 
					tripleStr.printStore(printsream)
					printsream.flush()
					info(outstream.toString())
					tripleStr.shutdown

			}
		}
		
		
		
		it("should states that the named model do not exits when querying a populated store for a model that not exist in ") { tripleStr =>

			new JenaModelLordOfTheRinghasPriceTrait {

				Given("a TripleStoreService and a jena model with a proper Iri")
					//@See TripleStoreServiceFixtureTrait: tripleStr
					//@see JenaModelLordOfTheRinghasPriceTrait: model

				When("we add this model as a named model named after its IRI trough the AddNamedGraph method")
					tripleStr.addNamedGraph("titi0", model)
				
				And("querry the tripleStoreService for a namedmodel with an IRI slightly different from the previous one ")
					val askresult = tripleStr.answerAskQueryAgainstPersistedModel("ask  where {GRAPH " + "<" + modelIRI + "3>"+ "{?a ?b ?c} }") 
				
				Then("the querry should return false")
					askresult should be(false)
					val outstream  =  new ByteArrayOutputStream()
					val printsream =  new PrintStream(outstream) 
					tripleStr.printStore(printsream)
					printsream.flush()
					info(outstream.toString())
					tripleStr.shutdown
			}
		}

	}

}