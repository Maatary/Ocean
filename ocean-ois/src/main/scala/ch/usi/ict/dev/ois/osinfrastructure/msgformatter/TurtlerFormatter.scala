package ch.usi.ict.dev.ois.osinfrastructure.msgformatter

import java.io.ByteArrayInputStream
import com.hp.hpl.jena.ontology.OntModel
import com.hp.hpl.jena.rdf.model.ModelFactory
import com.hp.hpl.jena.ontology.OntModelSpec
import java.io.ByteArrayOutputStream

object TurtlerFormatter {
  
  
  
  def asTurtle(content: String): String = {
    
    
		
		//var inputcontent = (content == null? new ByteArrayInputStream("".getBytes()): new ByteArrayInputStream(content.getBytes()));
		val inputcontent = if (content == null) new ByteArrayInputStream("".getBytes()) else new ByteArrayInputStream(content.getBytes())
		val instanceData: OntModel	   = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );

		instanceData.read(inputcontent, "")
		
		val outputStream = new ByteArrayOutputStream();
		instanceData.write(outputStream, "TURTLE");
		
		println(outputStream.toString())
		outputStream.toString()

  }

}