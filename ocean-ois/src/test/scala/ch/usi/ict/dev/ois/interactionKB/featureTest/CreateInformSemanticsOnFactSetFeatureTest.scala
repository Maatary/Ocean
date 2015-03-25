package ch.usi.ict.dev.ois.interactionKB.featureTest

import org.scalatest.fixture
import org.scalatest.GivenWhenThen
import org.scalatest.Matchers
import ch.usi.ict.dev.ois.interactionKB.fixtures.InteractionKbFixtureTrait
import ch.usi.ict.dev.ois.interactionKB.fixtures.JohnToMaryMsgSkeleton



class CreateInformSemanticsOnFactSetFeatureTest extends fixture.FeatureSpec with InteractionKbFixtureTrait with GivenWhenThen with Matchers {


	
//	def LordOfTheRingMsgFixture() = new {
//
//		private val sender         = AgentIdentifier.getAgentIdentifierWithParams("http://import ch.usi.ict.dev.ois.interactionKB.featureTest.JohnToMaryMsgSkeleton(192.168.0.1/Agents#John", "http://192.168.0.1")
//		private val receiver       = AgentIdentifier.getAgentIdentifierWithParams("http://192.168.0.2/Agents#Mary", "http://192.168.0.2")
//		private val language       = "OceanCL"
//		private val replywith      = "02"
//		private val convId         = "testId"
//		private val ontologyIRI    = domainOntoIRI //From fixture
//
//		private val langCodec      = new OceanRdfCodec("OceanCL");
//	    private val ontology       = PayDeliveryOntology.getInstance();
//	    
//		val contentmanager 		   = new ContentManager();
//		val msg      			   = AclMessage.createMsgFrom(Act.INFORM, 
//		      												  sender,
//		      												  receiver, 
//		      												  language, 
//		      												  ontologyIRI, 
//		      												  replywith,
//		      												  convId)
//		
//	      												 
//	    contentmanager.registerLanguage(langCodec);
//	    contentmanager.registerOntology(ontology);
//
//	    contentmanager.fillContent(msg, createLordOfTheRingContent())
//	     
//	    
//	    
//		def createLordOfTheRingContent ():AbsPredicate = {
//	
//	    	pending
//	    	null
//		}
//		
//		
//	}
	

	
	feature("Creation of the semantic of an Inform with as content a set of facts") {
		
		info("As the Infrastructure, when I deal with an inform containing a set of facts (claims)")
		info("I create for its semantics a Propositional Commitment")
		info("that point to - via its CommitTo Property - a NamedGraph representing the claims ")

		scenario("An inform with as content: the book Lord_of_the_ring cost 15 euros") { kbFixture =>
			new JohnToMaryMsgSkeleton {

				Given("an Inform Message with as content: a book with ItemId \"the lord of the ring\" has price 15")
				pending
				val informMsgSkeleton = msgSkeleton

				And("an Interaction KB initialized with the PayDelivery Ontology")
				val kb = kbFixture

				When("the inform message is passed to the updateStateOnCommunicativeAct Method (i.e. to createInformSemantics) of the the KB")
				pending
				//kb.updateStateOnCommunicativeAct(informMsgSkeleton, msgFixture.contentmanager, 0)

				Then("the KB must contained a propositional commitment that commitTo a set of claims represented by a Named Graph")
				pending

				And("the Named Graph must contain the claims of the original message")
				pending
			}

		}
		
		
		
		scenario("An inform with as content: the book Lord_of_the_ring cost 15 euros and the book learn scala cost 30 euros") { Kb =>
			
		
			Given("an Inform Message with as content: a book with ItemId \"the lord of the ring\" has price 15")
				info("needs to a way to represent a set (conjunction) of statement in a ch.usi.ict.jade content expression")
				pending
			
			And("an Interaction KB initialized with the PayDelivery Ontology")
				pending
			
			When("the inform message is passed to the CreateInformSemantics Method of the the KB")
				pending
				
			Then("the KB must contained a propositional commitment that commitTo a set of claims represented by a Named Graph")
				pending
				
			And("the ")
				pending
			
			
		
		
		}
		
		
		

	}

}