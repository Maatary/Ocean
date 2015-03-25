package ch.usi.ict.dev.ois.interactionKB.featureTest

import org.scalatest.GivenWhenThen
import org.scalatest.fixture
import org.scalatest.Matchers
import ch.usi.ict.dev.ois.interactionKB.fixtures.InteractionKbFixtureTrait
import ch.usi.ict.dev.ois.interactionKB.fixtures.JohnToMaryMsgSkeleton
import ch.usi.ict.dev.ois.interactionKB.fixtures.DonePayLordOfRing50ContentManager
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import com.hp.hpl.jena.query.QueryFactory
import org.apache.jena.atlas.io.IndentedWriter

class CreateInformSemanticOnDoneFeatureTest extends fixture.FeatureSpec with GivenWhenThen with InteractionKbFixtureTrait with Matchers {

	feature("Creation of the semantic of an Inform message with as content a Done predicate") {

		info("As the Infrastructure, when I deal with an inform containing a Done predicate")
		info("I create for its semantics a Propositional Commitment (Pcomit)")
		info("A Pcomit that point to - via its CommitTo Property - a NamedGraph representing the claimed Performed action ")

		scenario("An Inform message with content done(Pay(John, Mary, 50, Item(LordOfTheRing))) is received by the Infrastructure") { kbFixture =>
			
			new JohnToMaryMsgSkeleton with DonePayLordOfRing50ContentManager {
				
				Given("An Inform message with as content done(Pay(John, Mary, 50, Item(LordOfTheRing)))") 
					
					val informMsg = getmsgWithDoneContent(msgSkeleton)
					
				And("an Interaction KB initialized with the PayDelivery Ontology")
					
					//@see InteractionKbFixtureTrait: kbFixture
					
				When("the inform message is passed to the updateStateOnCommunicativeAct Method (i.e. to createInformSemantics) of the the KB")
					
					kbFixture.updateStateOnCommunicativeAct(informMsg, contentmanager, 0)

				Then("the KB must contained a Propositional Commitment that commitTo a set of claims represented by a Named Graph")
					val askQuerry = """PREFIX oblOnto: <http://www.people.lu.unisi.ch/okouyad/OblOntology.owl#>
									   PREFIX        : <http://www.people.lu.unisi.ch/okouyad/stateOntology.owl#>
									   PREFIX comitOnto: <http://www.people.lu.unisi.ch/okouyad/CommitmentOntology.owl#>
									   ask  {?pcom oblOnto:hasCreditor <http://www.people.lu.unisi.ch/okouyad/stateOntology.owl#Agent_""" + receiverName + ">; " +
									   "oblOnto:hasDebtor <http://www.people.lu.unisi.ch/okouyad/stateOntology.owl#Agent_" + senderName + ">; " + "comitOnto:commitTo ?claim" + ".}" 
						// does the Kb contain a propositional commitment that point to a claim with as debtor John and creditor Mary
					
					val outstream  =  new ByteArrayOutputStream()
					val printsream =  new PrintStream(outstream) 
					kbFixture.printInMemoryInteractionStateAsOWL(printsream)
					val query = QueryFactory.create(askQuerry)
					query.serialize(new IndentedWriter(printsream, true));
					printsream.flush()
					info(outstream.toString())
					
					kbFixture.answerAskQueryAgainstInMemoryModel(askQuerry) should be (true)
					
				And("the Named Graph must contain the claims of the original message")
					pending
				

			}

		}

	}
}