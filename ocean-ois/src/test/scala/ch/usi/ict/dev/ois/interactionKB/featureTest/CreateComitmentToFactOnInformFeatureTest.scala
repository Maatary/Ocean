//package ch.usi.ict.dev.ois.interactionKB.featureTest
//import org.scalatest.FeatureSpec
//import org.scalatest.GivenWhenThen
//import org.scalatest.Matchers
//import org.semanticweb.owlapi.apibinding.OWLManager
//import org.semanticweb.owlapi.model.IRI
//import org.semanticweb.owlapi.model.OWLAxiom
//import org.semanticweb.owlapi.util.DefaultPrefixManager
//import ch.usi.ict.dev.AclOverSoapHttpMP.AclMessage
//import ch.usi.ict.dev.AclOverSoapHttpMP.Act
//import ch.usi.ict.dev.AclOverSoapHttpMP.AgentIdentifier
//import OceanRdfCodec
//import ch.usi.ict.dev.ontology.OceanCL
//import ch.usi.ict.dev.ontology.PayDeliveryOntology
//import ContentManager
//import AbsConcept
//import AbsPredicate
//import BasicOntology
//
//
//
//
//class CreateComitmentToFactOnInformFeatureTest extends FeatureSpec with GivenWhenThen with Matchers {
//
//  info("As the KB of the Infrastucture I want to be able to register")
//
//  info("new commitment to pure facts unpon receiving an inform message")
//
//
//  def TestMessageParamsFixture = new {
//
//    val language  = "OceanCL";
//    val ontology  = PayDeliveryOntology.Vocabulary.ONTOLOGY_NAME.URIValue();
//    val replywith = "02"
//    val convId    = "testId"
//    val sender    = AgentIdentifier.getAgentIdentifierWithParams("sender", "http://127.0.0.145")
//    val receiver  = AgentIdentifier.getAgentIdentifierWithParams("receiver", "http://127.0.0.146")
//
//  }
//
//
//  def LordOftheRing_hasPrice_15_content_Fixture = new {
//
//	    private val statementObject = new AbsPredicate(OceanCL.Vocabulary.REIFIED_STATEMENT.URIValue());
//	    statementObject.set(OceanCL.Vocabulary.HAS_SUBJECT.URIValue(), PayDeliveryOntology.Vocabulary.ONTOLOGY_NAME.URIValue() + "Lord_of_the_ring");
//	    statementObject.set(OceanCL.Vocabulary.HAS_PREDICATE.URIValue(), PayDeliveryOntology.Vocabulary.ONTOLOGY_NAME.URIValue() + "has_price");
//
//	    private val rdfRessourceObject = new AbsConcept(OceanCL.Vocabulary.RDF_RESSOURCE.URIValue());
//	    rdfRessourceObject.set(OceanCL.Vocabulary.HAS_RDF_RESSOURCE_VALUE.URIValue(), "15");
//	    rdfRessourceObject.set(OceanCL.Vocabulary.HAS_RDF_RESSOURCE_TYPE.URIValue(), BasicOntology.INTEGER);
//
//	    statementObject.set(OceanCL.Vocabulary.HAS_OBJECT.URIValue(), rdfRessourceObject);
//
//
//	    private val langCodec      = new OceanRdfCodec("OceanCL");
//	    private val ontology       = PayDeliveryOntology.getInstance();
//	    val contentmanager 		   = new ContentManager();
//
//	    contentmanager.registerLanguage(langCodec);
//	    contentmanager.registerOntology(ontology);
//
//	    val manager      = OWLManager.createOWLOntologyManager()
//	    val dataFactory  = manager.getOWLDataFactory()
//	    val pm           = new DefaultPrefixManager();
//	    val oblOntoURL   = "http://www.people.lu.unisi.ch/okouyad/OblOntology.owl"
//	    val comitOntoURL = "http://www.people.lu.unisi.ch/okouyad/CommitmentOntology.owl"
//
//	    pm.setDefaultPrefix("http://www.people.lu.unisi.ch/okouyad/stateOntology.owl#")
//
//    def setContent(msg:AclMessage) = {
//
//    	contentmanager.fillContent(msg, statementObject)
//
//    }
//
//    def getverifyingStatemtmentAxiom(reifiedStmtCount: Int): Array[OWLAxiom] = {
//
//	    val statement         = dataFactory.getOWLNamedIndividual(":reifiedStatement_" + reifiedStmtCount, pm)
//	    val haspredicateProp  = dataFactory.getOWLDataProperty(IRI.create(oblOntoURL + "#hasPredicate"));
//	    val hasSubjectProp    = dataFactory.getOWLDataProperty(IRI.create(oblOntoURL + "#hasSubject"));
//	    val hasObjectProp     = dataFactory.getOWLDataProperty(IRI.create(oblOntoURL + "#hasObject"));
//
//	    var axiomlist: List[OWLAxiom] = List()
//
//
//	    var value     = dataFactory.getOWLLiteral(PayDeliveryOntology.Vocabulary.ONTOLOGY_NAME.URIValue() + "Lord_of_the_ring",
//	    								 		  dataFactory.getOWLDatatype(IRI.create("http://www.w3.org/2001/XMLSchema#anyURI")))
//	    var owlAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(hasSubjectProp, statement, value)
//	    axiomlist = owlAxiom::axiomlist;
//
//
//        value  = dataFactory.getOWLLiteral(PayDeliveryOntology.Vocabulary.ONTOLOGY_NAME.URIValue() + "has_price",
//	    								   dataFactory.getOWLDatatype(IRI.create("http://www.w3.org/2001/XMLSchema#anyURI")))
//	    owlAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(haspredicateProp, statement, value)
//	    axiomlist = owlAxiom::axiomlist;
//
//        owlAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(hasObjectProp, statement, 15)
//	    axiomlist = owlAxiom::axiomlist;
//
//        axiomlist.toArray
//
//    }
//
//
//    def getverifyingPropositionalComitmtAxiom (reifiedStmtCount: Int): Array[OWLAxiom] = {
//
//       var axiomlist: List[OWLAxiom] = List()
//
//       val Pcomit          = dataFactory.getOWLNamedIndividual(":Pcomit_" + reifiedStmtCount, pm)
//       val hasDebtorProp   = dataFactory.getOWLObjectProperty(IRI.create(comitOntoURL + "#hasDebtor"))
//	   val hasCreditorProp = dataFactory.getOWLObjectProperty(IRI.create(comitOntoURL + "#hasCreditor"))
//	   val commitToProp    = dataFactory.getOWLObjectProperty(IRI.create(comitOntoURL + "#commitTo"))
//
//
//	   val senderIndiv      = dataFactory.getOWLNamedIndividual(":" + TestMessageParamsFixture.sender.getName(), pm)
//       val receiverIndiv    = dataFactory.getOWLNamedIndividual(":" + TestMessageParamsFixture.receiver.getName(), pm)
//       val reifiedStmtIndiv = dataFactory.getOWLNamedIndividual(":reifiedStatement_" + reifiedStmtCount, pm)
//
//	   var owlAxiom = dataFactory.getOWLObjectPropertyAssertionAxiom(hasDebtorProp, Pcomit, senderIndiv)
//	   axiomlist = owlAxiom::axiomlist;
//
//	   owlAxiom = dataFactory.getOWLObjectPropertyAssertionAxiom(hasCreditorProp, Pcomit, receiverIndiv)
//	   axiomlist = owlAxiom::axiomlist;
//
//	   owlAxiom = dataFactory.getOWLObjectPropertyAssertionAxiom(commitToProp, Pcomit, reifiedStmtIndiv)
//	   axiomlist = owlAxiom::axiomlist;
//
//       axiomlist.toArray
//    }
//
//
//
//  }
//
//
//
//  feature("Creating Commitment to Pure fact upon new informs") {
//
//
//    scenario("An inform message about the fact: \"the lord of the ring has_price 15 euros\" is provided") {
//
//
//      Given("an ACL Inform message with content has_price(Lord_of_the_ring, 15)")
//
//      	val contentFixture = LordOftheRing_hasPrice_15_content_Fixture
//      	val defaultparms = TestMessageParamsFixture
//      	val informMsg    = AclMessage.createMsgFrom(Act.INFORM,
//      												defaultparms.sender,
//      												defaultparms.receiver,
//      												defaultparms.language,
//      												defaultparms.ontology,
//      												defaultparms.replywith,
//      												defaultparms.convId)
//
//      contentFixture.setContent(informMsg);
//
//      And("a Kb initialized with the the PayDeliveryOntology")
//
//      	val kb = KbFactory.createKb("http://www.people.lu.unisi.ch/okouyad/PayDeliveryOntology.owl", "data/main/reasoningOntos/PayDeliveryOntology.owl");
//
//      When("the inform message is passed to the KB")
//
//      	kb.updateStateOnCommunicativeAct(informMsg, contentFixture.contentmanager, 10);
//
//      Then("the KB should contain a corresponding reified statement")
//
//      	kb.containAxioms(contentFixture.getverifyingStatemtmentAxiom(0)) should be (true)
//
//      And("a corresponding propositional commitment")
//
//        kb.containAxioms(contentFixture.getverifyingPropositionalComitmtAxiom(0)) should be (true)
//
//    }
//  }
//}
//
//
//
//
//
