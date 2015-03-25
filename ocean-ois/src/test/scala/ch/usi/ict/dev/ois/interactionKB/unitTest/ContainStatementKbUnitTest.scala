//package ch.usi.ict.dev.ois.interactionKB.unitTest
//
//import org.scalatest.FunSpec
//import org.scalatest.Matchers
//import org.scalatest.GivenWhenThen
//import java.io.File
//import org.semanticweb.owlapi.apibinding.OWLManager
//import org.semanticweb.owlapi.model.IRI
//import ch.usi.ict.dev.ontology.PayDeliveryOntology
//import ch.usi.ict.dev.ontology.OceanCL
//import ch.usi.ict.dev.ois.interactionKB.InteractionKB
//import ch.usi.ict.dev.ois.interactionKB.KbFactory
//
//
//class ContainStatementKbMethodTest extends FunSpec with GivenWhenThen with Matchers {
//
//
//	val PayIsActionAxiomFixture = new {
//
//	  private val manager = OWLManager.createOWLOntologyManager()
//	  private val dataFactory = manager.getOWLDataFactory()
//
//	  private val payclass = dataFactory.getOWLClass(IRI.create(PayDeliveryOntology.Vocabulary.PAY.URIValue()))
//	  private val actionclass = dataFactory.getOWLClass(IRI.create(OceanCL.Vocabulary.ACTION.URIValue()))
//
//	  val PayIsActionAxiom = dataFactory.getOWLSubClassOfAxiom(payclass, actionclass)
//
//	}
//
//	val DeliverIsActionAxiomFixture = new {
//
//	  private val manager = OWLManager.createOWLOntologyManager()
//	  private val dataFactory = manager.getOWLDataFactory()
//
//	  private val deliverclass = dataFactory.getOWLClass(IRI.create(PayDeliveryOntology.Vocabulary.DELIVER.URIValue()))
//	  private val actionclass = dataFactory.getOWLClass(IRI.create(OceanCL.Vocabulary.ACTION.URIValue()))
//
//	  val DeliverIsActionAxiom = dataFactory.getOWLSubClassOfAxiom(deliverclass, actionclass)
//
//	}
//
//
//	describe("The Method Kb.containStatementAxioms") { pending
//
//
//	  it("should tell if the set of axioms making the statement exist in the KB") {
//
//
//	    Given("A Kb Loaded with the PayDelivery Ontology containing the statements:")
//
//	    	info("Pay is an Action")
//	    	info("Deliver is an Action")
//
//	        val kb = KbFactory.createKb("http://www.people.lu.unisi.ch/okouyad/PayDeliveryOntology.owl", "data/main/reasoningOntos/PayDeliveryOntology.owl");
//
//	    And("the axioms recreated by hand so it can be verify to be in the ontology")
//
//	    	//info(PayIsActionAxiomFixture.PayIsActionAxiom.toString())
//
//	    	val payAxiom = PayIsActionAxiomFixture.PayIsActionAxiom
//	    	val DeliverAxiom = DeliverIsActionAxiomFixture.DeliverIsActionAxiom
//
//	    When("the list of recreated axioms are passed to the Kb.containStatementAxioms Method")
//
//	    val isContained = kb.containAxioms(Array(payAxiom, DeliverAxiom));
//
//
//	    Then("the Kb should confirm that it contains the axioms")
//	    	isContained should be (true)
//
//
//	  }
//
//
//
//
//	}
//
//}