//package ch.usi.ict.dev.ois.unitTest
//
//import org.scalatest.FunSpec
//import org.scalatest.GivenWhenThen
//import org.scalatest.Matchers
//import ch.usi.ict.dev.ois.interactionKB.OISconfigProfileImpl
//
//class OISconfigProfileSpec extends FunSpec with GivenWhenThen with Matchers {
//
//
//	describe("When building a new OISconfigProfile") {
//
//		//it("should return the Ontology name out of the IRIName Supplied to build the OISconfigProfile") {
//
//			Given("an Ontology IRI that point to a file that exist: ontologyIRI = \"http://www.people.lu.unisi.ch/okouyad/PayDeliveryOntology.owl\"")
//
//				val ontologyIRI = "http://www.people.lu.unisi.ch/okouyad/PayDeliveryOntology.owl"
//
//			And("the URL of an an existing local folder where the dataset will be heald that exist: datasetURL= \"data/test\"")
//
//				val datasetURL = "data/test"
//
//			When("a OISconfigProfile is initialized with ontologyIRI and datasetURL")
//
//				val conf = OISconfigProfileImpl(_OntoUrlName = ontologyIRI)
//
//			Then("the domainOntoName of the OISconfigProfile object should be the name of the ontology without its path: \"PayDeliveryOntology.owl\" ")
//
//				conf.domainOntoName should be("PayDeliveryOntology.owl")
//
//		//}
//
//	}
//
//
//
//
//	describe("Test that ConfigData constructor throws an Exception if the supplied argument are not valid") {
//
//		pending
//
//		it("should yield MalformedURLException for the empty String URL") {
//
//			Given("a OISconfigProfile initialized with (\"\", \"\", \"\")")
//
//				val conf = OISconfigProfileImpl("")
//
//
//			Then("Tring to get the OntUrl should throw MalformedURLException")
//
//				//an [java.net.MalformedURLException] should be thrownBy conf.aOntoUrlName
//
//		}
//
//		it("should yield MalformedURLException for the \"dfsfsfds\" URL") {
//
//			//an [java.net.MalformedURLException] should be thrownBy OISconfigProfileImpl("dfsfsfds", "")
//
//		}
//
//		it("should not yield any exception for the \"http://yup\" URL and \"data/test/reasoningOntos/InferenceTestingOnto.owl\"") {
//
//			//noException should be thrownBy OISconfigProfileImpl("http://www.yup.com", "data/test/reasoningOntos/InferenceTestingOnto.owl")
//
//		}
//
//		it("should not yield any exception for the \"http://yup\" URL and \"data/test/reasoningOntos/PayDeliveryOntology.owl\"") {
//
//			//noException should be thrownBy OISconfigProfileImpl("http://www.yup.com", "data/test/reasoningOntos/PayDeliveryOntology.owl")
//
//		}
//
//	}
//
//}