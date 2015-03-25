package ch.usi.ict.dev.ois.interactionKB.fixtures
import org.scalatest.fixture
import org.scalatest.Outcome
import ch.usi.ict.dev.ois.interactionKB.OISconfigProfile
import ch.usi.ict.dev.ois.interactionKB.OISconfigProfileImpl
import com.google.inject.Guice
import ch.usi.ict.dev.ois.interactionKB.TripleStoreConfModule
import ch.usi.ict.dev.ois.interactionKB.TripleStoreServiceFactory
import com.hp.hpl.jena.rdf.model.InfModel
import org.scalamock.scalatest.MockFactory
import ch.usi.ict.dev.ois.interactionKB.TripleStoreService


trait TripleStoreServiceFixtureTrait extends MockFactory { this: fixture.Suite =>

	type FixtureParam      		           = TripleStoreService
	private val config: OISconfigProfile = OISconfigProfileImpl("http://www.people.lu.unisi.ch/okouyad/PayDeliveryOntology.owl")
	private val injector                 = Guice.createInjector(new TripleStoreConfModule(config))
	private val factory                  = injector.getInstance(classOf[TripleStoreServiceFactory])
	private val mInfmodel                = mock[InfModel]
	
	def withFixture(test: OneArgTest): Outcome = {
		
		val tripleStoreService       = factory.createTripleStoreService(mInfmodel)
		try {
			withFixture(test.toNoArgTest(tripleStoreService)) // "loan" the fixture to the test
		}finally None 
		
	}

}