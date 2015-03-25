package ch.usi.ict.dev.ois.interactionKB.featureTest

import org.scalatest.GivenWhenThen
import org.scalatest.fixture
import org.scalatest.Matchers
import ch.usi.ict.dev.ois.interactionKB.fixtures.InteractionKbFixtureTrait


class AskQuerryFeatureTest extends fixture.FeatureSpec with InteractionKbFixtureTrait with Matchers with GivenWhenThen {
	
	
	feature("The answerAskquery method: The ability for the Kb to answer to a supplied Sparql Ask querry "){
		
	
		
		scenario("the kb contain an item(itemId:Lord_Of_the_Ring) and we ask for it"){kbFixture =>
			
			//pending
			
			Given("a kb initialized with a the PaydeliveryOntology containing an item(hasItemId:Lord_Of_the_Ring)")
				
				val kb = kbFixture
				
				
			When("its method askquery is called with as argument the ask {?x CLOntology:hasItemId Lord_Of_the_Ring}")
				val queryString = ""
				val answer = kb.answerAskQueryAgainstPersistedModel(queryString)
				
			Then("the answer should be true")
				answer should be(true)
			
		}
		
		
	}
	
	
	
	

}