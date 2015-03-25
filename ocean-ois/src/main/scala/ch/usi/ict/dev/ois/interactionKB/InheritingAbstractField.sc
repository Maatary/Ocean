package ch.usi.ict.dev.ois.interactionKB.featureTest
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.FileSystems
import com.google.inject.AbstractModule
import com.google.inject.Inject
import com.google.inject.Guice


object worksheet {

	traitSpike TestObjectTrait {
	
		def aValue: String
	}
	
	case class TestObject(val aValue:String) extends TestObjectTrait {
	
			
	}
	
	val testobj: TestObjectTrait = TestObject("hey")
                                                  //> testobj  : ch.usi.ict.dev.ois.interactionKB.featureTest.worksheet.TestObjectTrait = TestObje
                                                  //| ct(hey)

	testobj.aValue                            //> res0: String = hey
	//val config:ConfigData = ConfigDataImpl("http://www.people.lu.unisi.ch/okouyad/PayDeliveryOntology.owl", "data/test/reasoningOntos/PayDeliveryOntology.owl")

}