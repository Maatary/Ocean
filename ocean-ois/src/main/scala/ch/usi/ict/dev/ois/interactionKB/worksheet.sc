package ch.usi.ict.dev.ois.interactionKB.featureTest

import org.semanticweb.owlapi.apibinding.OWLManager;
import java.io.File
object worksheet {



  object WeekDay extends Enumeration {
    type WeekDay = Value
    val Mon, Tue, Wed, Thu, Fri, Sat, Sun = Value
  }

	//WeekDay.Mon.toString()
	
	
	println("I'm the test")                   //> I'm the test
	
	
	val manager  = OWLManager.createOWLOntologyManager()
                                                  //> manager  : org.semanticweb.owlapi.model.OWLOntologyManager = uk.ac.mancheste
                                                  //| r.cs.owl.owlapi.OWLOntologyManagerImpl@39a26ac5
	val ontoFile = new File("")               //> ontoFile  : java.io.File = 
	
	//manager.loadOntologyFromOntologyDocument()

}