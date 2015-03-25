package ch.usi.ict.dev.ois.interactionKB


import ch.usi.ict.dev.utils.resLocator
import com.google.inject.assistedinject.Assisted
import com.hp.hpl.jena.rdf.model.InfModel
import com.google.inject.Inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.hp.hpl.jena.tdb.TDBFactory
import org.apache.jena.riot.RDFDataMgr
import org.apache.jena.riot.Lang
import java.io.PrintStream
import com.google.inject.name.Named
import ch.usi.ict.dev.ois.PropertiesNames
import com.hp.hpl.jena.rdf.model.Model
import com.hp.hpl.jena.query.QueryFactory
import org.apache.jena.atlas.io.IndentedWriter
import com.hp.hpl.jena.query.QueryExecutionFactory
import ch.usi.ict.dev.ois.interactionKB.filesUtils.FileandDirectoryHelper
import com.hp.hpl.jena.query.ReadWrite
import com.hp.hpl.jena.tdb.TDB


trait TripleStoreService {

	/**
	 * Answer an Ask Query against the Persisted Model
	 */
	def  answerAskQueryAgainstPersistedModel(queryString: String): Boolean
	
	/**
	 * Answer an Ask Query against the in-memory Model
	 */
	def answerAskQuerryAgainstInMemoryModel(queryString: String): Boolean
	
	/**
	 * Add a named Graph to the TripleStore
	 */
	def addNamedGraph (iri: String, model: Model): Unit
	
	
	/**
	 * @return The dataset directory of the TripleStore
	 */
	def datasetDir: String
	
	
	def printStore(stream:PrintStream = System.out): Unit
	
	def shutdown: Unit
	
}

/**
 * The basic implementation of a TripleStoreService
 */
class TripleStoreServiceIml @Inject() (@Assisted val jenaInfModel: InfModel) extends TripleStoreService {
	
  val datasetDir           = resLocator.locateResource( "dataset", classOf[TripleStoreServiceIml])
	private val dataset      = {FileandDirectoryHelper.setUpDir(datasetDir); println("dataset set up"); TDBFactory.createDataset("dataset")}
	private val defaultModel = dataset.getDefaultModel()
	
	
	
	
	
	def answerAskQueryAgainstPersistedModel(queryString: String): Boolean = {
		val query = QueryFactory.create(queryString)
		//query.serialize(new IndentedWriter(System.out, true));// Print with line numbers
		//System.out.println()
		val qexec = QueryExecutionFactory.create(query, dataset)
		qexec.execAsk()
	}
	
	def answerAskQuerryAgainstInMemoryModel(queryString: String): Boolean = {
		val query = QueryFactory.create(queryString)
		val qexec = QueryExecutionFactory.create(query, jenaInfModel)
		qexec.execAsk()
	}

	def addNamedGraph (iri: String, model: Model): Unit = {		
		dataset.addNamedModel(iri, model)
		defaultModel.withDefaultMappings(model)
		dataset.getNamedModel(iri).setNsPrefixes(model)
		TDB.sync(dataset)
	}
	
    
	/**
	 * Print the triple Store (by the default System.out is used)
	 */
	def printStore(stream:PrintStream = System.out) {
		 TDB.sync(dataset) 
		 RDFDataMgr.write(stream, dataset, Lang.TRIG)
	}
	
	
	def shutdown() {
		TDB.sync(dataset)
		dataset.close()
	}
	
}