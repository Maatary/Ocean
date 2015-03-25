package ch.usi.ict.dev.langCodec;

import jade.content.abs.AbsConcept;
import jade.content.abs.AbsContentElement;
import jade.content.abs.AbsContentElementList;
import jade.content.abs.AbsObject;
import jade.content.abs.AbsPredicate;
import jade.content.abs.AbsPrimitive;
import jade.content.abs.AbsPrimitiveSlotsHolder;
import jade.content.lang.StringCodec;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PredicateSchema;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import ch.usi.ict.dev.langCodec.helpers.JenaReifiedStmtConverter;
import ch.usi.ict.dev.langCodec.helpers.RdfResContentExprObjectConvertor;
import ch.usi.ict.dev.langCodec.helpers.ReifiedstamentCreatorHelper;
import ch.usi.ict.dev.ontology.OceanCL;
import ch.usi.ict.dev.ontology.UpperOntology;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ReifiedStatement;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class OceanRdfCodec extends StringCodec {

	
	protected static String xsd = "http://www.w3.org/2001/XMLSchema";
	
	public static void main(String[] args) {
		
		System.out.println(">>>>>>Test 1\n\n");
		
		//Given:
		         //An rdfRessource content expression object (i.e. an AbsConcept object of Type OceanCL.Vocabulary.RDF_RESSOURCE)
				 //that has its string field value set with the lexical form of the value of a resource
				 //and its string field dataType set with the type of the resource value or null for an URIRessource
				 //together with a Jena Ontological model
		
		
		AbsConcept rdfRessourceObject = new AbsConcept(OceanCL.Vocabulary.RDF_RESSOURCE.URIValue());
		rdfRessourceObject.set(OceanCL.Vocabulary.HAS_RDF_RESSOURCE_VALUE.URIValue(), "2");
		rdfRessourceObject.set(OceanCL.Vocabulary.HAS_RDF_RESSOURCE_TYPE.URIValue(), BasicOntology.STRING);//Optionel
		
		OntModel ontomodel = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
		
		
		//When:
				//We create a Jena Statement object that we initialize with a mocked subject and predicate,
				//together with the "rdfRessource-content-expression-object" converted as java object of a type 
				//that jena knows how to transform in an RDF object of the appropriate type, 
				//with the help of the "RdfResContentExprObjectConvertor" utility class,
				//and then convert it to a reified statement.

		
		Statement statement = ontomodel.createStatement(ontomodel.createOntResource("http://somewhere/John"), 
							  ontomodel.createProperty("http://somewhere/has_age"), 
							  RdfResContentExprObjectConvertor.toJenaRdfConvertableObject(ontomodel, rdfRessourceObject));
		
		ReifiedStatement refiedStatement = statement.createReifiedStatement();
		
		//Then:
				//upon writing it we get a well-formed RDF
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ontomodel.write(outputStream, "N3", null);
		//ontomodel.write(outputStream, "RDF/XML-ABBREV", null);
		System.out.println(outputStream);
		
		
		
		
		System.out.println("\n>>>>>>Test 2\n\n");		
		
		//Given:
				//A statement content expression object (i.e. an AbsPredicate object of Type OceanCL.Vocabulary.REIFIED_STATEMENT)
				//properly initialized, i.e. with URIs as string for its subject and predicate field and An rdfRessource content expression as object,
				//and the jena model that created it
		AbsPredicate StatementObject = new AbsPredicate(OceanCL.Vocabulary.REIFIED_STATEMENT.URIValue());
	    StatementObject.set(OceanCL.Vocabulary.HAS_SUBJECT.URIValue(), "http://somewhere/John");
		StatementObject.set(OceanCL.Vocabulary.HAS_PREDICATE.URIValue(), "http://somewhere/love");
		 
		rdfRessourceObject = new AbsConcept(OceanCL.Vocabulary.RDF_RESSOURCE.URIValue());
		rdfRessourceObject.set(OceanCL.Vocabulary.HAS_RDF_RESSOURCE_VALUE.URIValue(), "http://somewhere/Mary");
		//rdfRessourceObject.set(OceanCL.Vocabulary.HAS_RDF_RESSOURCE_TYPE.URIValue(), BasicOntology.STRING);

	    StatementObject.set(OceanCL.Vocabulary.HAS_OBJECT.URIValue(), rdfRessourceObject);
		 
		ontomodel = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
		

		
		//When: 
				//passed to the method createAndAddJenaReifiedStatementoModel of the ReifiedstamentCreatorHelper
		ReifiedstamentCreatorHelper.createAndAddJenaReifiedStatementToModel(StatementObject, ontomodel);
		
		
		//Then: 
				//It shall return a legal RDF model containing the appropriate ReifiedStatement
		
		outputStream = new ByteArrayOutputStream();
		ontomodel.write(outputStream, "N3", null);
		ontomodel.write(outputStream, "RDF/XML-ABBREV", null);
		System.out.println(outputStream);
		
		
		
		System.out.println("\n>>>>>>Test 3\n\n");
		
		//Given:
				//AbsContentElement that is a predicate of type statement and the RDF codec
				//both properly initialized
		
		StatementObject = new AbsPredicate(OceanCL.Vocabulary.REIFIED_STATEMENT.URIValue());
	    StatementObject.set(OceanCL.Vocabulary.HAS_SUBJECT.URIValue(), "http://somewhere/John");
		StatementObject.set(OceanCL.Vocabulary.HAS_PREDICATE.URIValue(), "http://somewhere/has_age");
		 
		rdfRessourceObject = new AbsConcept(OceanCL.Vocabulary.RDF_RESSOURCE.URIValue());
		rdfRessourceObject.set(OceanCL.Vocabulary.HAS_RDF_RESSOURCE_VALUE.URIValue(), "2");
		rdfRessourceObject.set(OceanCL.Vocabulary.HAS_RDF_RESSOURCE_TYPE.URIValue(), BasicOntology.INTEGER);

	    StatementObject.set(OceanCL.Vocabulary.HAS_OBJECT.URIValue(), rdfRessourceObject);
		 
		
		//When:
				//the content element is passed to the encode function of the RDFCodec
	    String rdfEncodedData = null;
	    try {
			rdfEncodedData = new OceanRdfCodec("OceanRdfCodec").encode( OceanCL.getInstance(), StatementObject);
		} catch (CodecException e) {
			e.printStackTrace();
		}
	    
	    assert (rdfEncodedData != null);
	    
		
		//Then:
				//It shall yield to a proper RDF document with a reified statement inside
	    
	    System.out.println(rdfEncodedData);
	    
	    
	    
	    System.out.println("\n>>>>>>Test 4\n\n");
	    
	    //Given:
		
	    		//An input stream with an RDF reified statement and an RDF model object
	    ByteArrayInputStream rdfstream = new ByteArrayInputStream(rdfEncodedData.getBytes());
	    ontomodel = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
	    
	    
	    //When:
	    
	    		//The input stream is read by the model
	    ontomodel.read(rdfstream, "");
	    
	    
	    //Then:
	    
	    		//It must contain a constructed Reified statement object in the model
	    outputStream = new ByteArrayOutputStream();
		ontomodel.write(outputStream, "N3", null);
		ontomodel.write(outputStream, "RDF/XML-ABBREV", null);
		System.out.println(outputStream);
		
		

		TestConstructStatementContentExprObjFromJenaReifiedStatement(ontomodel.listReifiedStatements().next());
		TestdecodingRDFInputContainingRdfReifiedStmt(rdfEncodedData);

	}
	
	
	
	
	private static void TestConstructStatementContentExprObjFromJenaReifiedStatement (ReifiedStatement aReifiedStmt) {
		
		System.out.println("TEST: Construct Statement Content Expr Obj From Jena ReifiedStatement");
		
		//Given:
				//a ReifiedStatement
				ReifiedStatement reifiedStmt = aReifiedStmt;
		
		//When: 
				//Passed to the JenaReifiedStmtConverter.toToContentExprStmt() method
				AbsPredicate statementObject = JenaReifiedStmtConverter.toContentExprStmt(reifiedStmt);
		
		//Then: 
		
				//It shall yield to proper content expression object representing a statement
				System.out.println(statementObject.toString());
		
	}
	
	
	private static void TestdecodingRDFInputContainingRdfReifiedStmt(String rdfInput) {
		
		System.out.println("TEST: decoding RDF Input Containing Rdf ReifiedStmt");
		
		//Given:
				//an RDF input containing an RDF reified statement
				String ardfInput = rdfInput;
				
				
	    //When:
				//decoded with the RDF decoder (including validation)
				AbsContentElement reifiedStmtContentExpr = null;
				try {
					reifiedStmtContentExpr = new OceanRdfCodec("RdfCodec").decode(OceanCL.getInstance(), ardfInput);
				} catch (CodecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		//Then:
				//It shall yield a proper content  expression element i.e. an AbsPredicate of type Statement
				assert(reifiedStmtContentExpr != null);
				System.out.println(reifiedStmtContentExpr.toString());
		
	}
	
	
	public OceanRdfCodec(String name) {
		super(name);
	}

	
	@Override
	public String encode(AbsContentElement content) throws CodecException {
		System.out.println("Calling oceanRdf Encode without ontology");
		return null;
	}

	@Override
	public String encode(Ontology ontology, AbsContentElement content) throws CodecException {
				
		OntModel instanceData = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
		
		if (content instanceof AbsContentElementList ) { //As of now, a list is a list of 2 action in a promise and that's it.
			
			createInstanceDataFromAbsPrimitiveSlotsHolder(instanceData, ontology, (AbsPrimitiveSlotsHolder) ((AbsContentElementList)content).get(0));
			createInstanceDataFromAbsPrimitiveSlotsHolder(instanceData, ontology, (AbsPrimitiveSlotsHolder) ((AbsContentElementList)content).get(1));
			
		}
		else
			createInstanceDataFromAbsPrimitiveSlotsHolder(instanceData, ontology, (AbsPrimitiveSlotsHolder) content);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		instanceData.write(outputStream, "RDF/XML-ABBREV");
		
		return outputStream.toString();
	}
	
	/**
	 * Get an instance data and create a corresponding OWL data (for now RDF).
	 * A special case for NamedIndividual: 
	 * As in OWL2 we represent namedIndividual with a type NamedIndividual created
	 * in the java Jade UpperOntology. There is no IRI in the ch.usi.ict.jade ontology, in other words
	 * no way to represent a NamedIndividual, starting from its class e.g. a class granularity
	 * as several individual such as "Minutes". It is not an individual of type granularity 
	 * with a property name set to "Minutes", but instead, it is a "Minutes" individual of type Granularity.
	 * 
	 * Therefore, when we encounter a NamedIndividual we treat it differently from the other instance. 
	 * A name individual has a property name and type. Hence, we get the name and the type and create the 
	 * appropriate RDF/OWL Instance data.
	 * 
	 * @param instanceData
	 * @param ontology
	 * @param content
	 * @return
	 */
	protected Resource createInstanceDataFromAbsPrimitiveSlotsHolder(OntModel instanceData, Ontology ontology, AbsPrimitiveSlotsHolder content) {

		Resource  resObj    = null;
		Resource  resTyp    = null;
		AbsObject abs       = null;
		String    typeName  = content.getTypeName();
		String[]  slotsName = content.getNames(); // always return the name in use, not the exhaustive list of the names of the Type.
		
		//System.out.println("In content: " + content.toString() + " of type: " + content.getTypeName() + " defined in ontology.. ");
		
	
		//NamedIndividual Case
		if (typeName.equals(UpperOntology.Vocabulary.NAMED_INDIVIDUAL.URIValue())) {
			String indivName = content.getString(UpperOntology.Vocabulary.HAS_NAME.URIValue());
			String indivType = content.getString(UpperOntology.Vocabulary.HAS_TYPE.URIValue());
			resTyp = instanceData.createResource(indivType);
			resObj = instanceData.createResource(indivName, resTyp);
		}
		else {
			
			if (typeName.equals(OceanCL.Vocabulary.REIFIED_STATEMENT.URIValue())) {
				
				ReifiedstamentCreatorHelper.createAndAddJenaReifiedStatementToModel((AbsPredicate) content, instanceData);
				
			}
			else //All the other case
			{
				resTyp = instanceData.createResource(typeName);
				resObj = instanceData.createResource(resTyp);//anonymous resource of type resTyp
				for (int i = 0; i < slotsName.length; i++) {
					//System.out.println("has slot: " + slotsName[i]);
					abs = content.getAbsObject(slotsName[i]);

					if (abs instanceof AbsPrimitive) {				
						//This is buggy, it only treat the case where the AbsPrimitive is a string, if not we have a class cast exception
						//resObj.addProperty(instanceData.createProperty(slotsName[i]), ((AbsPrimitive)abs).getString());
						if (abs.getTypeName().equals(BasicOntology.DATE)){

							Date date = ((AbsPrimitive) abs).getDate();
							SimpleDateFormat  simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
							DateTime          dateTime          = new DateTime(simpleDateFormat.format(date));
							DateTimeFormatter dateTimeformatter = ISODateTimeFormat.dateTime();

							//Two Z is the Timezone with semicolon (xs:dateTimeFormat)
							dateTimeformatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");
							resObj.addLiteral(instanceData.createProperty(slotsName[i]), instanceData.createTypedLiteral(dateTime.toString(dateTimeformatter), xsd + "dateTime"));
						}
						else {
							resObj.addLiteral(instanceData.createProperty(slotsName[i]), instanceData.createTypedLiteral(((AbsPrimitive)abs).getObject()));
						}
					}
					if (abs instanceof AbsPrimitiveSlotsHolder) {
						//System.out.println("**In slot: " + slotsName[i]);
						Resource res = createInstanceDataFromAbsPrimitiveSlotsHolder(instanceData, ontology, (AbsPrimitiveSlotsHolder) abs);
						resObj.addProperty(instanceData.createProperty(slotsName[i]), res);
						//System.out.println("**Out of slot: " + slotsName[i]);
					}
				}
			}
			
		}
		
		
		//instanceData.write(System.out, "RDF/XML-ABBREV");
		//System.out.println("Out of content: " + content.toString() + " of type: " + content.getTypeName() + " defined in ontology.. ");
		return resObj;
	}
	

	@Override
	public AbsContentElement decode(String content) throws CodecException {
		System.err.println("Calling oceanRdf decode without ontology");
		return null;
	}

	@Override
	public AbsContentElement decode(Ontology ontology, String content) throws CodecException {
		
		InputStream inputcontent = (content == null? new ByteArrayInputStream("".getBytes()): new ByteArrayInputStream(content.getBytes()));
		OntModel instanceData 	     = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );

		instanceData.read(inputcontent, "");

		return getContentElement(instanceData, ontology);
	}
		
	public AbsContentElement getContentElement (OntModel model, Ontology ontology) {
		
		AbsContentElementList abscontentEltList = new AbsContentElementList();
		ResIterator resIt               		= model.listSubjects();
		List<AbsObject> ObjOfPropList			= new Vector<AbsObject>();
		Resource res                   		    = null;
		AbsObject absObj                        = null;
		ObjectSchema schema 		    	    = null;
		String type			            	    = null;
		
		if (model.listReifiedStatements().hasNext()) {
			
			ReifiedStatement reifiedStmt = model.listReifiedStatements().next();
			AbsPredicate reifiedStatementContentExpr  = null;
			try {
				
				reifiedStatementContentExpr = JenaReifiedStmtConverter.toContentExprStmt(reifiedStmt);
				ontology.getSchema(OceanCL.Vocabulary.REIFIED_STATEMENT.URIValue()).validate(reifiedStatementContentExpr, ontology);
				
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return reifiedStatementContentExpr;
		}

		while (resIt.hasNext()) {

			/*if (abscontentEltList.size() > 1) 
				return abscontentEltList;*/
			
			res = resIt.nextResource();

			try {

				//System.out.println("****Dealing with resource: " + res.toString());
				type   = getType(res.listProperties());
				schema = ontology.getSchema(type);
				
					
				if (schema == null) {
					//System.err.println("type: " + type + " do not exist, skipping\n");
					continue;
				}

				
				if (((schema instanceof AgentActionSchema) == false) && ((schema instanceof PredicateSchema) == false) ) {
					continue;
				}

				absObj = schema.newInstance();
				setAbsObject(absObj, schema, ontology, res, ObjOfPropList);
				schema.validate(absObj, ontology);
			} 
			catch (OntologyException e) {
				
				System.err.println(e.getMessage());
				e.printStackTrace();
				continue;
			}
			abscontentEltList.add((AbsContentElement)absObj);
			
		}//while there are subject resource in the model
		if (abscontentEltList.size() > 1) 
			cleanList(abscontentEltList, ObjOfPropList);
		
		return abscontentEltList.size() == 0 ? null:abscontentEltList.get(0);
	}

	private void cleanList(AbsContentElementList abscontentEltList, List<AbsObject> ObjOfPropList) {
		
		for (Iterator<AbsContentElement> iterator = abscontentEltList.iterator(); iterator.hasNext();) {
			AbsContentElement absClt = iterator.next();
			if (ObjOfPropList.contains(absClt)) {
				ObjOfPropList.remove(absClt);
				iterator.remove();
			}
			
		} 
		
	}
	
	private Boolean setAbsObject(AbsObject absObj, ObjectSchema slotsHolderSchema, Ontology ontology, Resource res, List<AbsObject> ObjOfPropList) {
		//TODO Include all slot holder, concept, action, predicate and other that i don't need or that i don't deal with right now
		  //Hence i will have to review that
		if (absObj instanceof AbsPrimitiveSlotsHolder)  {

			AbsPrimitiveSlotsHolder absSlotHolder    = (AbsPrimitiveSlotsHolder)absObj;
			String[] names                 			 = slotsHolderSchema.getNames();
			AbsObject absAttributeObj      			 = null;
			RDFNode rdfNod 							 = null;
			Resource tempRes						 = null;
			ObjectSchema schema 		    	     = null;
			
			for (int i = 0; i < names.length; i++) {
				
				
				try {
					//Take each property and check if it is primitive or resource nod.
					//If it is a ressource, get its type and find its schema with it in the ontology
					//and an absObj with the schema is created to be filled later on. (see. inheritance )
					//Otherwise take the outter schema, to find the schema of that
					rdfNod = getPropertyNodObject(res.listProperties(), names[i]);
					if (rdfNod != null )  {
						if (rdfNod.isResource()) {
							
							tempRes = rdfNod.asResource();
							
							if (slotsHolderSchema.getSchema(names[i]).getTypeName().equals(UpperOntology.Vocabulary.NAMED_INDIVIDUAL.URIValue())) {
								
								schema = slotsHolderSchema.getSchema(names[i]);
								absAttributeObj = schema.newInstance();
								
								((AbsConcept)absAttributeObj).set(UpperOntology.Vocabulary.HAS_NAME.URIValue(), tempRes.getURI());
								
								((AbsConcept)absAttributeObj).set(UpperOntology.Vocabulary.HAS_TYPE.URIValue(), getType(tempRes.listProperties()));
								
								absSlotHolder.set(names[i], (AbsConcept)absAttributeObj);
								
								return true;
							}
							
							schema = ontology.getSchema(getType(tempRes.listProperties()));
							absAttributeObj = schema.newInstance();
						}
						else
							absAttributeObj = slotsHolderSchema.getSchema(names[i]).newInstance();
					}
					else
						continue;
					
				} catch (OntologyException e) {
					System.err.println(e.getMessage());
					e.printStackTrace();
					return false;
				}
				
				if (absAttributeObj instanceof AbsPrimitive) {
					if (setAbsPrimitive((AbsPrimitive)absAttributeObj, getPropertyNodObject(res.listProperties(), names[i])))
						absSlotHolder.set(names[i], (AbsPrimitive)absAttributeObj);
					//else
						//return false;//new
				}
				if (absAttributeObj instanceof AbsPrimitiveSlotsHolder) {
					/*try*/ {
						
						/*rdfNod = getPropertyNodObject(res.listProperties(), names[i]);
						if (rdfNod != null && ((tempRes = rdfNod.asResource()) != null)) {
							schema = ontology.getSchema(getType(tempRes.listProperties()));*/
							if (setAbsObject(absAttributeObj, schema, ontology, tempRes, ObjOfPropList)) {
								if (absAttributeObj.getAbsType() == AbsObject.ABS_AGENT_ACTION)
									ObjOfPropList.add(absAttributeObj);//Fill in the list of object of properties
								absSlotHolder.set(names[i], (AbsConcept)absAttributeObj);
							}
							//else
								//return false;//false
						/*}*/
						//else
							//return false; //new
					} /*catch (OntologyException e) {
						System.err.println(e.getMessage());
						e.printStackTrace();
						return false;
					}*/
				}

			}
		}
		return true;
	}	
	

	/**
	 * Set the AbsPrimitive object from the RDFNode by getting its litteral value with
	 * respect to the already set type of the AbsPrimitive object.
	 * 
	 * The Date case: 
	 * the ontology only store the xs:dateTime
	 * hence we extract its value with a joda.dateTime (which is more standard)
	 * then convert it to a format that is understandable by Date
	 * 
	 * @param absObj
	 * @param litteralNod
	 * @return
	 */
	private boolean setAbsPrimitive(AbsPrimitive absObj, RDFNode litteralNod) {
		
		if (absObj == null || litteralNod == null )
			return false;
		
		if (absObj.getTypeName().equalsIgnoreCase(BasicOntology.STRING)){	
			absObj.set(litteralNod.asLiteral().getString());
			return true;
		}
		
		if (absObj.getTypeName().equalsIgnoreCase(BasicOntology.INTEGER)){	
			absObj.set(litteralNod.asLiteral().getInt());
			return true;
		}
		
		if (absObj.getTypeName().equalsIgnoreCase(BasicOntology.FLOAT)){	
			absObj.set(litteralNod.asLiteral().getFloat());
			return true;
		}
		
		if (absObj.getTypeName().equalsIgnoreCase(BasicOntology.DATE)){	
			
			DateTime          dateTime 				 = new DateTime(litteralNod.asLiteral().getLexicalForm());
			DateTimeFormatter dateTimeformatter      = ISODateTimeFormat.dateTime();
			SimpleDateFormat  simpleDateFormat       = null;
			
			//one Z is the timezone without semicolon
			dateTimeformatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
			simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			
			try {
				absObj.set(simpleDateFormat.parse(dateTime.toString(dateTimeformatter)));
			} catch (ParseException e) {
				System.err.println("problem parsing xs:date to be converted in java Date, inside the OceanCLcodec");
				e.printStackTrace();
			}
			
			return true;
		}
		
		if (absObj.getTypeName().equalsIgnoreCase(BasicOntology.BOOLEAN)){	
			absObj.set(litteralNod.asLiteral().getBoolean());
			return true;
		}
		
		return false;
	}

	/**
	 * Get the the object of a statement and return it as an RDFNODE
	 * This correspond to the value of an attribute as a node in the graph.
	 * It could be another resource or a simple literal
	 * @param stmtIt
	 * @param propName
	 * @return
	 */
	public RDFNode getPropertyNodObject(StmtIterator stmtIt, String propName) {

		Property prop       = null;
		RDFNode nod         = null;
		Statement statement = null;


		while (stmtIt.hasNext()) {
			statement = stmtIt.nextStatement();
			prop = statement.getPredicate();
			nod  = statement.getObject();
			if (prop.getURI().equalsIgnoreCase(propName)) {
				//System.out.println("Found property Object: " + prop.getURI() + ": " + nod.toString()) ;
				return nod;
			}
		}
		//System.out.println("oops property: " + propName +   " not found in getPropObjectasNod. It will not be assign");
		return null;	
	}

	/**
	 * Retrieve the Type property slot and return its value as string
	 * @param stmtIt
	 * @return
	 */
	public String getType(StmtIterator stmtIt) {

		Property prop       = null;
		RDFNode nod         = null;
		Statement statement = null;


		while (stmtIt.hasNext()) {
			statement = stmtIt.nextStatement();
			prop = statement.getPredicate();
			nod  = statement.getObject();
			if (prop.getLocalName().equalsIgnoreCase("type")) {
				return nod.asResource().getURI();
			}

		}
		System.err.println("oops no type found in the ontology");
		return null;
	}
}
