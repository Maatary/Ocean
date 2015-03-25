package ch.usi.ict.dev.ois.interactionKB.fixtures
import ch.usi.ict.dev.ois.interactionKB.InteractionKB
import org.scalatest.Outcome
import com.google.inject.Guice
import ch.usi.ict.dev.ois.interactionKB.KbConfigModule
import ch.usi.ict.dev.ois.interactionKB.OISconfigProfileImpl

import org.scalatest.fixture

trait InteractionKbFixtureTrait { this: fixture.Suite =>
	
	
	type FixtureParam      		   = InteractionKB
	
	private val domainOntoIRI      = "http://www.people.lu.unisi.ch/okouyad/PayDeliveryOntology.owl";
	
	
	val injector = Guice.createInjector(new KbConfigModule(OISconfigProfileImpl(domainOntoIRI)))
	
	private val kb = injector.getInstance(classOf[InteractionKB])
	
	
	def withFixture(test: OneArgTest): Outcome = {
		try {
			withFixture(test.toNoArgTest(kb)) // "loan" the fixture to the test
		}finally None 
		
	}
	
}

