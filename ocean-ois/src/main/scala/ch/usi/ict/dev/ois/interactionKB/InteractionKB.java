package ch.usi.ict.dev.ois.interactionKB;


import ch.usi.ict.dev.utils.resLocator;
import jade.content.ContentManager;
import jade.content.abs.AbsAgentAction;
import jade.content.abs.AbsConcept;
import jade.content.abs.AbsContentElement;
import jade.content.abs.AbsObject;
import jade.content.abs.AbsPredicate;
import jade.content.abs.AbsPrimitive;
import jade.content.abs.AbsPrimitiveSlotsHolder;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.BasicOntology;
import jade.content.onto.OntologyException;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredClassAssertionAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredPropertyAssertionGenerator;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.usi.ict.dev.AclOverSoapHttpMP.AclMessage;
import ch.usi.ict.dev.AclOverSoapHttpMP.Act;
import ch.usi.ict.dev.ois.interactionKB.convertors.JadeJenaConvertor;
import ch.usi.ict.dev.ois.interactionKB.timeUtils.JodaJavaConverter;
import ch.usi.ict.dev.ois.osinfrastructure.Exception.UnsupportedDataTypeException;
import ch.usi.ict.dev.ontology.OceanCL;
import ch.usi.ict.dev.ontology.TimeOntology;
import ch.usi.ict.dev.ontology.UpperOntology;
import ch.usi.ict.dev.swrlTabTemporalBuiltinPelletPlugin.CustomSWRLBuiltin;
import ch.usi.ict.dev.swrlTabTemporalBuiltinPelletPlugin.add;
import ch.usi.ict.dev.swrlTabTemporalBuiltinPelletPlugin.before;
import ch.usi.ict.dev.swrlTabTemporalBuiltinPelletPlugin.equals;
import ch.usi.ict.dev.swrlTabTemporalBuiltinPelletPlugin.notBefore;

import com.clarkparsia.owlapi.explanation.PelletExplanation;
import com.clarkparsia.owlapi.explanation.io.manchester.ManchesterSyntaxExplanationRenderer;
import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import com.clarkparsia.pellet.rules.builtins.BuiltInRegistry;







import com.google.inject.Inject;


import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;















//import org.semanticweb.HermiT.Reasoner;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.DateTime;
import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.PelletOptions;
import org.mindswap.pellet.jena.PelletInfGraph;

public class InteractionKB {

	
	Logger logger = LoggerFactory.getLogger("OIS");
	
	private final String  ontologyURLDomain    = "http://www.people.lu.unisi.ch/okouyad/";

	private final String  domainOntoURL;
	private final File    domainOntoLocalFile;

	//TODO refactor as enum (expect for the domain ontology)
	private final String stateOntoURL          = "http://www.people.lu.unisi.ch/okouyad/stateOntology.owl";
	private final String oblOntoURL    	       = "http://www.people.lu.unisi.ch/okouyad/OblOntology.owl";
	private final String clOntoURL    	       = "http://www.people.lu.unisi.ch/okouyad/OceanCL.owl";
	private final String temporalOntoURL       = "http://swrl.stanford.edu/ontologies/built-ins/3.3/temporal.owl";
    private final String comitOntoURL          = "http://www.people.lu.unisi.ch/okouyad/CommitmentOntology.owl";

	

	private final File stateOntoLocalFile;
	private final File oblOntoLocalFile;
	private final File clOntoLocalFile;
	private final File temporalOntoLocalFile;
	private final File comitOntoLocalFile;
	
	

	private OWLOntologyManager                  manager 		 = null;
	private OWLOntology                         stateOnto     	 = null;
	private PrefixManager                       pm         		 = null;
	private OWLDataFactory                      dataFactory		 = null;
	private PelletReasoner                      reasoner         = null;
	private ManchesterSyntaxExplanationRenderer renderer         = null;
	private PrintWriter                         out				 = null;
	private PelletExplanation                   expGen			 = null;
	
	
	private final OWLDatatype                   dateTimeDatatype; 
	private final OWLDatatype                   anyURIDataType;
	
	/*
	 * Helper flags for controlling the update of the ontology ^^
	 */
	
	//Have we just added a startEvent class that have yet to be populated with one instance?
	private int                                 StartEventCount     = 0;
	
	//The counter of observedAction. Use to name them.
	private int									ObservedActionCount = 0;
	
	//The counter of Statement. Use to name them
	private int                                 reifiedStmtCount    = 0;
	
	
	private final TripleStoreService			tripleStoreService;
	
	
	//register the SWRL-Custom built-ins implementation 
	{
		BuiltInRegistry.instance.registerBuiltIn("http://swrl.stanford.edu/ontologies/built-ins/3.3/temporal.owl#add", new CustomSWRLBuiltin(new add())); 
		BuiltInRegistry.instance.registerBuiltIn("http://swrl.stanford.edu/ontologies/built-ins/3.3/temporal.owl#before", new CustomSWRLBuiltin(new before()));
		BuiltInRegistry.instance.registerBuiltIn("http://swrl.stanford.edu/ontologies/built-ins/3.3/temporal.owl#notBefore", new CustomSWRLBuiltin(new notBefore())); 
		BuiltInRegistry.instance.registerBuiltIn("http://swrl.stanford.edu/ontologies/built-ins/3.3/temporal.owl#equals", new CustomSWRLBuiltin(new equals())); 
	}
	
	
	
	/**
	 * Construct the Interaction Knowledge by passing in a Configuration Object
	 */
	@Inject
	protected InteractionKB(OISconfigProfile config, TripleStoreServiceFactory atripleStoreServiceFactory) {
		
		
		domainOntoURL       	  = config.domainOntoIRIName();
		domainOntoLocalFile 	  = new File(resLocator.locateResource("reasoningOntos/" + config.domainOntoName(), InteractionKB.class));

		stateOntoLocalFile      = new File(resLocator.locateResource("reasoningOntos/stateOntology.owl", InteractionKB.class));
		oblOntoLocalFile        = new File(resLocator.locateResource("reasoningOntos/OblOntology.owl",InteractionKB.class));
		clOntoLocalFile         = new File(resLocator.locateResource("reasoningOntos/OceanCL.owl", InteractionKB.class));
		temporalOntoLocalFile   = new File(resLocator.locateResource("reasoningOntos/temporal.owl", InteractionKB.class) );
		comitOntoLocalFile      = new File(resLocator.locateResource("reasoningOntos/CommitmentOntology.owl", InteractionKB.class));
		
		
		manager          		  = OWLManager.createOWLOntologyManager();
        dataFactory               = manager.getOWLDataFactory(); 
        dateTimeDatatype          = dataFactory.getOWLDatatype(IRI.create("http://www.w3.org/2001/XMLSchema#dateTime"));
        anyURIDataType            = dataFactory.getOWLDatatype(IRI.create("http://www.w3.org/2001/XMLSchema#anyURI"));
        
        try {

			//initialize ontology, reasoner and explanation
        	PelletOptions.USE_TRACING = true;
			PelletExplanation.setup();
			
        
			manager.addIRIMapper(new SimpleIRIMapper(IRI.create(stateOntoURL), 
													 IRI.create(stateOntoLocalFile)));
			manager.addIRIMapper(new SimpleIRIMapper(IRI.create(oblOntoURL), 
													 IRI.create(oblOntoLocalFile)));
			manager.addIRIMapper(new SimpleIRIMapper(IRI.create(domainOntoURL), 
													 IRI.create(domainOntoLocalFile)));
			manager.addIRIMapper(new SimpleIRIMapper(IRI.create(clOntoURL), 
													 IRI.create(clOntoLocalFile)));
			manager.addIRIMapper(new SimpleIRIMapper(IRI.create(temporalOntoURL), 
													 IRI.create(temporalOntoLocalFile)));
			manager.addIRIMapper(new SimpleIRIMapper(IRI.create(comitOntoURL), 
					 								 IRI.create(comitOntoLocalFile)));

			stateOnto        = manager.loadOntology(IRI.create(stateOntoURL)); 
	        pm 			     = manager.getOntologyFormat(stateOnto).asPrefixOWLOntologyFormat();
	        
	        //Code Specific to OWL-API with Pellet. I don't use the generic OWLReasonerFactory and OWLReasoner.
	        reasoner = PelletReasonerFactory.getInstance().createNonBufferingReasoner(stateOnto, new SimpleConfiguration(new ConsoleProgressMonitor()));
	        manager.addOntologyChangeListener(reasoner);
	        
	        
	        
	        // The renderer is used to pretty print explanation
         	renderer = new ManchesterSyntaxExplanationRenderer();
         	// The writer used for the explanation rendered
         	out = new PrintWriter( System.out );
            renderer.startRendering( out );
            
            expGen = new PelletExplanation(reasoner);
            
            reasoner.precomputeInferences();

			if (!reasoner.isConsistent()) {
				System.out.println("the starting Ontology is inconsistent");
				renderer.startRendering(out);
				Set<Set<OWLAxiom>> inconsistency = expGen.getInconsistencyExplanations();
				renderer.render(inconsistency);
				renderer.endRendering();
				System.exit(-1);
			}
			

		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (OWLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        tripleStoreService               = atripleStoreServiceFactory.createTripleStoreService(createJenaInfModelInterfaceOfInteractionStateOwlapiModel(reasoner));
		
	}
	
	
	/**
	 * A getter for the DomainOntoURL(Useful for test)
	 * @return
	 */
	public String getDomainOntoURL() {
		return domainOntoURL;
	}
	
	/**
	 * A getter for the DomainOntoLocalFile (Useful for test)
	 * @return
	 */
	public File getDomainOntoLocalFile() {
		return domainOntoLocalFile;
	}
	
	/**
	 * Query the persisted models of the TripleStoreService
	 * @param askQuerry
	 * @return
	 */
	public boolean answerAskQueryAgainstPersistedModel(String askQuerry) {
		return tripleStoreService.answerAskQueryAgainstPersistedModel(askQuerry);
	}
	
	/**
	 * Query the InMemory model of the TripleStoreService
	 * @param askQuerry
	 * @return
	 */
	public boolean answerAskQueryAgainstInMemoryModel(String askQuerry){
		return tripleStoreService.answerAskQuerryAgainstInMemoryModel(askQuerry);
	}
	
	
	/**
	 * Print the interaction state Kb as OWL i.e. the InMemory model as OWL axioms
	 * @param stream
	 */
	public void printInMemoryInteractionStateAsOWL(PrintStream stream) {
		
		try {
			manager.saveOntology(stateOnto, stream);
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/***
	 * An Helper Functionality
	 * @return
	 */
	public int getAxiomCount() {

		int count = 0;

		for (OWLOntology ont : manager.getImportsClosure(stateOnto)) {
			count += ont.getAxiomCount();
		}

		return count;
	}

	
	/**
	 * Create the semantics of the Communicative Actions  
	 * @param servicedMsg
	 * @param contentmanager
	 * @param count
	 * @return
	 */
	public synchronized int updateStateOnCommunicativeAct(AclMessage servicedMsg, ContentManager contentmanager, int count) {

		if (servicedMsg.getAclPayload().getAct() == Act.PROMISE) {
			createPromiseSemantics(servicedMsg, contentmanager, count);
			//TODO remove the reasoning Process
			doPelletReasoning();
			return count++;
		}
		
		if (servicedMsg.getAclPayload().getAct() == Act.INFORM) {
			createInformSemantics(servicedMsg, contentmanager, count);
			//TODO remove the reasoning Process
			doPelletReasoning();
		}
		
		return count;
	}
	
	
	
	
	/**
	 * Create an individual in the Ontology that represent this action.
	 * Check if it correspond to a startEvent and update the corresponding obligation if necessary
	 * Only Check to deal with a startEvent if startEventCount > 0. If dealt with apply startEventCount--
	 * @param observedAction
	 */
	public synchronized void UpdateStateOnObservedAction(AbsAgentAction observedAction, DateTime actionTime) {
		
		AbsObject absObj                   = null;
		Object    value                    = null;
		String    className                = null;
		int       oblNum                   = 0;
		OWLNamedIndividual obl_n           = null;
		OWLNamedIndividual obl_n_Interval  = null;
		OWLLiteral startTimeAsLiteral      = null;
		OWLLiteral actionTimeAsLiteral     = null;
		OWLNamedIndividual actionInstant   = null;
		DateTimeFormatter fmt              = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");
				
		OWLNamedIndividual obsActIndiv = dataFactory.getOWLNamedIndividual(IRI.create("observedAction_" + ObservedActionCount++ ));
		
		String[] slotsName = observedAction.getNames();

		
		for (int i = 0; i < slotsName.length; i++) {

			absObj = observedAction.getAbsObject(slotsName[i]);

			/*If it is an Absprimitive just asserted the property */
			if (absObj instanceof AbsPrimitive) {
				
				value = ((AbsPrimitive) absObj).getObject();
				//As of now boolean is never used
				if (value instanceof String) {
					manager.addAxiom(stateOnto,
							dataFactory.getOWLDataPropertyAssertionAxiom(dataFactory.getOWLDataProperty(IRI.create(slotsName[i])), obsActIndiv, (String)value)
							);
					continue;
				}
				if (value instanceof Integer) {
					manager.addAxiom(stateOnto,
							dataFactory.getOWLDataPropertyAssertionAxiom(dataFactory.getOWLDataProperty(IRI.create(slotsName[i])), obsActIndiv, (Integer)value)
							);
					continue;
				}
				if (value instanceof Float) {
					manager.addAxiom(stateOnto,
							dataFactory.getOWLDataPropertyAssertionAxiom(dataFactory.getOWLDataProperty(IRI.create(slotsName[i])), obsActIndiv, (Float)value)
							);
					continue;
				}
				if (value instanceof Double) {
					manager.addAxiom(stateOnto,
							dataFactory.getOWLDataPropertyAssertionAxiom(dataFactory.getOWLDataProperty(IRI.create(slotsName[i])), obsActIndiv, (Double)value)
							);
					continue;
				}
				if (value instanceof Date) {
					
					DateTime dateTime  = JodaJavaConverter.asJodaDateTime((Date)value, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));
					manager.addAxiom(stateOnto,
							dataFactory.getOWLDataPropertyAssertionAxiom(dataFactory.getOWLDataProperty(IRI.create(slotsName[i])), obsActIndiv, 
							                                             dataFactory.getOWLLiteral(dateTime.toString(fmt), this.dateTimeDatatype))
							        );
					continue;
				}
				
				System.out.println("Problem in naming the individual, encouter an unknown primitive");
				System.out.println("InteractionKB.UpdateStateOnObservedAction()");
				System.exit(-1);
				
			}
			/*If absObj is an AbsPrimitiveSlotsHolder*/
			else 
			{
				OWLNamedIndividual slotIndiv = createAssertedNamedIndividualFromObject(absObj);
				
				manager.addAxiom(stateOnto, 
								 dataFactory.getOWLObjectPropertyAssertionAxiom(dataFactory.getOWLObjectProperty(IRI.create(slotsName[i])), obsActIndiv, slotIndiv)
								 );
			}

		}
		//Add the time to the action
		actionInstant       = dataFactory.getOWLNamedIndividual(":Instant_" + actionTime.toString(fmt), pm);
		actionTimeAsLiteral = dataFactory.getOWLLiteral(actionTime.toString(fmt), dateTimeDatatype);
		
		manager.addAxiom(stateOnto,
						 dataFactory.getOWLDataPropertyAssertionAxiom(dataFactory.getOWLDataProperty(IRI.create(temporalOntoURL + "#hasTime")), actionInstant, actionTimeAsLiteral)
						 );
		
		manager.addAxiom(stateOnto,
						 dataFactory.getOWLObjectPropertyAssertionAxiom(dataFactory.getOWLObjectProperty(IRI.create(clOntoURL + "#atTime")), obsActIndiv, actionInstant)
						 );		
		
		if (StartEventCount <= 0) 
			return;
		
		//Update The obligation of the corresponding StartEvent
		
		Set<OWLClass> directTypes = reasoner.getTypes(obsActIndiv, true).getFlattened();
		
		for (OWLClass owlClass : directTypes) {
			
			className = owlClass.getIRI().getFragment();
			
			if (className.startsWith("StartEvent", 0)) {
				
				oblNum             = Integer.parseInt(className.substring(className.indexOf("_") + 1));
				obl_n              = dataFactory.getOWLNamedIndividual(":obl_" + oblNum, pm);
				obl_n_Interval     = getAssertedObjectPropertyValue(obl_n, dataFactory.getOWLObjectProperty(IRI.create(oblOntoURL + "#hasInterval"))).asOWLNamedIndividual();
				startTimeAsLiteral = getAssertedDataPropertyValue(obl_n_Interval, dataFactory.getOWLDataProperty(IRI.create(temporalOntoURL + "#hasStartTime")));
				
				if (startTimeAsLiteral != null)
					continue;
				
				manager.addAxiom(stateOnto,
						 dataFactory.getOWLDataPropertyAssertionAxiom(dataFactory.getOWLDataProperty(IRI.create(temporalOntoURL + "#hasStartTime")), obl_n_Interval, actionTimeAsLiteral)
						 );
				//Check if Obligation has its interval finishTime set, if not it must be set using its duration
				OWLObjectProperty  hasInterval   = dataFactory.getOWLObjectProperty(IRI.create(oblOntoURL + "#hasInterval"));
				OWLDataProperty    hasFinishTime = dataFactory.getOWLDataProperty(IRI.create(temporalOntoURL + "#hasFinishTime"));
				OWLLiteral finishTime 			 = getAssertedDataPropertyValue(obl_n, hasFinishTime);
				
				
				if (finishTime == null) {
					
					setOblIntervfinishTime(obl_n, obl_n_Interval);
					
				}
				
				//The Deadline Axiom
				createDeadlineAxiom(oblNum, obl_n_Interval);

				//The monitoning Axioms
				createMonitoringGeneralClassAxioms(obl_n, "obl_" + oblNum, oblNum);
				
				StartEventCount--;
			}
			
		}
		
		doPelletReasoning();	
	}
	/**
	 * Perform the reasoning process of the state of the interaction
	 */
	private void doPelletReasoning () {
		
		try {	

			if (!reasoner.isConsistent()) {
				System.out.println("the asserted Ontology is inconsistent");
				renderer.startRendering(out);
				Set<Set<OWLAxiom>> inconsistency = expGen.getInconsistencyExplanations();
				renderer.render(inconsistency);
				renderer.endRendering();
				System.exit(-1);
			}

			//manager.saveOntology(stateOnto, new SystemOutDocumentTarget());
			
			
			UpdateKClasses();
			
			generateAxiomAndSaveInOnto(reasoner, manager, stateOnto);
			
			
			
			//UpdateKClasses();
			
			//manager.saveOntology(stateOnto, new SystemOutDocumentTarget());
			
			File file = new File("InferredOntos/stateOntology.owl");
			manager.saveOntology(stateOnto, IRI.create(file));
			
			
			if (!reasoner.isConsistent()) {
				
				System.out.println("***the Infered Ontology is inconsistent");
				renderer.startRendering(out);
				Set<Set<OWLAxiom>> inconsistency = expGen.getInconsistencyExplanations();
				renderer.render(inconsistency);
				renderer.endRendering();
				System.exit(-1);
			}
			
			//renderer.endRendering();
			
			
			
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (OWLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			System.out.println("the Inferred Ontology is probably inconsistent");
			e.printStackTrace();
			try {
				renderer.render(expGen.getInconsistencyExplanations());
				manager.saveOntology(stateOnto, new SystemOutDocumentTarget());
			} catch (UnsupportedOperationException e1) {
				e1.printStackTrace();
			} catch (OWLException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.exit(-1);
		}
		return;
	}

	
	
	/**
	 *  Generate the Inferred Knowledge from the State Ontology and save it Back in it.
	 * @param reasoner
	 * @param manager
	 * @param ontology
	 */
	private void generateAxiomAndSaveInOnto(OWLReasoner reasoner, OWLOntologyManager manager, OWLOntology ontology) {
    	
    	// To generate an inferred ontology we use implementations of inferred axiom generators
        // to generate the parts of the ontology we want (e.g. subclass axioms, equivalent classes
       	// axioms, class assertion axiom etc. - see the org.semanticweb.owlapi.util package for more
        // implementations).  
        // Set up our list of inferred axiom generators
        List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
        
        //InferredIndividualAxiomGenerator
        gens.add(new InferredPropertyAssertionGenerator());
        gens.add(new InferredClassAssertionAxiomGenerator());
        
        //InferredClassAxiomGenerator
        //gens.add(new InferredDisjointClassesAxiomGenerator());
        //gens.add(new InferredEquivalentClassAxiomGenerator());
        //gens.add(new InferredSubClassAxiomGenerator());
        
        // Now get the inferred ontology generator to generate some inferred axioms
        // for us (into our fresh ontology).  We specify the reasoner that we want
        // to use and the inferred axiom generators that we want to use.
        InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner, gens);
        
        iog.fillOntology(manager, ontology);
    }
	
	
	/**
	 * Provide an interface to the owl-api model trough the Pellet reasoner: a Jena InfModel.
	 * This model can be used to query the the knowledge based from Jena.
	 * 
	 * Once Pellet is initialized with the owl-api model it create its own interface to that model that it calls its Kb. 
	 * This Kb is passed to the Jena framework to generate an infGraph and then an InfModel.
	 * @param reasoner
	 * @return
	 */
	private InfModel createJenaInfModelInterfaceOfInteractionStateOwlapiModel(final PelletReasoner reasoner){
		if (reasoner == null) {
			logger.debug("reasoner Null: Killing the App");
			System.exit(-1);
		}
		KnowledgeBase kb = reasoner.getKB();
		PelletInfGraph graph = new org.mindswap.pellet.jena.PelletReasoner().bind(kb);
		return ModelFactory.createInfModel(graph);
	}

	

	/**
	 * Create the Semantics of the promise Communicative Action.
	 * @param servicedMsg
	 * @param contentmanager
	 * @param count
	 */
	private void  createPromiseSemantics(AclMessage servicedMsg, ContentManager contentmanager, int count) {

		try {
			
			AbsContentElement  conditionalActionExpr = null;
			AbsAgentAction     action                = null;
			OWLNamedIndividual obl					 = null;  
			AbsObject          condition             = null;

			//Take the content and check if it is a conditionalAction, and then extract its action
			conditionalActionExpr =  contentmanager.extractAbsContent(servicedMsg);
			
			if (conditionalActionExpr.getTypeName().equalsIgnoreCase(OceanCL.Vocabulary.CONDITIONAL_ACTION.URIValue()) == false) 
				return;
			
			action = (AbsAgentAction) conditionalActionExpr.getAbsObject(OceanCL.Vocabulary.HAS_ACTION_PART.URIValue());
			
			
			//The Obligation
			obl = createObligation(servicedMsg, action, count);

			
			//The ContentAxiom
			createContentAxiom(count, action);
			
			
			//The StartEvent Axiom & The beginning of the Interval
			condition = conditionalActionExpr.getAbsObject(OceanCL.Vocabulary.HAS_CONDITION_PART.URIValue());
			
			if (condition == null) {
				
				setStartEventAsObligation(obl, count);
				setOblBiginningIntervFromOneOfStartEvent(count, obl, obl);
				
			}
			else
			{
				if (condition.getTypeName().equals(OceanCL.Vocabulary.TIME_EVENT.URIValue())) {
					
					OWLNamedIndividual timeEvent = setStartEventAsTimeEvent((AbsConcept) condition, count);
					setOblBiginningIntervFromOneOfStartEvent(count, timeEvent, obl);
					
				}
				else // Condition is an Action: We must return and update the Promise semantic creation when we meet an action instance that fits the startEvent
				{
					
					StartEventCount++; 
					
					setStartEventAsPerformedAction((AbsAgentAction) condition, count);
					
					//We have added a startEvent Axiom. 
					//We remember it so when we have an observed action, we know if we need to update an Obligation beginning interval, consequently finalizing
					//the creation of a promise semantics.
					
					//The deadline of the condition action
					AbsConcept   deadlineTimeEvent = (AbsConcept) ((AbsAgentAction) condition).getAbsObject(OceanCL.Vocabulary.HAS_DEADLINE.URIValue());
					
					if (deadlineTimeEvent != null) {
						
						setEndEventAxiomAsDeadline(deadlineTimeEvent, count);
						
						create_Cancel_monitoring_Axiom(obl, count);
						return;
					}
					AbsConcept duration 		    = (AbsConcept) ((AbsAgentAction) condition).getAbsObject(OceanCL.Vocabulary.HAS_DURATION.URIValue());
					if (duration != null)//We have a duration
					{
						setEndEventAxiomAsdurationAddedToOblTime(obl, (AbsAgentAction) condition, count);
						
						create_Cancel_monitoring_Axiom(obl, count);
						return;
					}
					
					return;
				}
			}
			
			//Check if Obligation has its interval finishTime set, if not it must be set using its duration
			OWLObjectProperty  hasInterval   = dataFactory.getOWLObjectProperty(IRI.create(oblOntoURL + "#hasInterval"));
			OWLDataProperty    hasFinishTime = dataFactory.getOWLDataProperty(IRI.create(temporalOntoURL + "#hasFinishTime"));
			OWLNamedIndividual oblInterv     = getAssertedObjectPropertyValue(obl, hasInterval).asOWLNamedIndividual();
			OWLLiteral finishTime 			 = getAssertedDataPropertyValue(oblInterv, hasFinishTime);
			
			
			if (finishTime == null) {
				
				setOblIntervfinishTime(obl, oblInterv);
				
			}
			
			//The Deadline Axiom
			createDeadlineAxiom(count, oblInterv);

			//The monitoning Axioms
			createMonitoringGeneralClassAxioms(obl, "obl_" + count, count);


		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Two cases
	 * -inform(Done(Action))
	 * -inform(pure propositional facts exclusive of actions) = inform(Statement)
	 * For now only the second case is treaded.
	 * Also the full semantics of commitment is not handled yet.
	 * Hence when an inform (done(action)) arrived we update the interaction Ontology by stating that the mentioned action
	 * has been performed : performedAction(action, t)
	 * It use the function UpdateOnObservedAction - It is not ok
	 * @param servicedMsg
	 * @param contentmanager
	 * @param count
	 */
	
	private void createInformSemantics(AclMessage servicedMsg, ContentManager contentmanager, int count) {
		
		try {
			
			
			AbsContentElement predicateExpr     = null;
			AbsPredicate      done              = null;
			AbsAgentAction    ObservedAction    = null;
			AbsConcept        validInstant      = null;

			Date              date              = null;
			SimpleDateFormat  simpleDateFormat  = null; // To read the Date format of the Jade AbsContentElement
			DateTime          dateTime          = null; // As a joda time
			
			String			  senderName        = servicedMsg.getAclPayload().getSender().getName();
			String			  receiverName      = servicedMsg.getAclPayload().getReceiver().get(0).getName();
			
			predicateExpr = contentmanager.extractAbsContent(servicedMsg);

			if (predicateExpr.getTypeName().equalsIgnoreCase(OceanCL.Vocabulary.DONE.URIValue())) {

				
				done = (AbsPredicate) predicateExpr;
				ObservedAction    = (AbsAgentAction) done.getAbsObject(OceanCL.Vocabulary.HAS_STATEDACTION.URIValue());
				validInstant      = (AbsConcept) done.getAbsObject(OceanCL.Vocabulary.HAS_STATEDTIME.URIValue());
				date              = validInstant.getDate(TimeOntology.Vocabulary.HAS_TIME.URIValue());
				dateTime 		  = JodaJavaConverter.asJodaDateTime(date, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));
				
				
				DateTimeFormatter fmt            = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");
				String            now            = DateTime.now().toString(fmt);
				
				String claimIRI             = ontologyURLDomain +  "Claim_Instant_" + now + "_" + senderName + "__" + receiverName + ".owl";
				String pcomitID             = "Pcomit_Instant_" + now + "_" + senderName + "__" + receiverName;
				Set <OWLAxiom> pcomitAxioms = createPropositionalCommitmentToClaimAxioms(dataFactory, stateOnto, senderName, receiverName, pcomitID, claimIRI, now);
				
				
				Model model          		= createPerformedActionClaim(ObservedAction, dateTime, claimIRI);
				String modelId       		= model.getNsPrefixURI(" "); 
				
				manager.addAxioms(stateOnto, pcomitAxioms);
				tripleStoreService.addNamedGraph(claimIRI, model);
				
				//UpdateStateOnObservedAction(ObservedAction, dateTime);
				return;	
			}
			
			if (predicateExpr.getTypeName().equalsIgnoreCase(OceanCL.Vocabulary.REIFIED_STATEMENT.URIValue())) {
				
				addReifiedStatement((AbsPredicate) predicateExpr, reifiedStmtCount);
				addPropositionalCommmitment(servicedMsg.getAclPayload().getSender().getName(),
											servicedMsg.getAclPayload().getReceiver().get(0).getName(),
											reifiedStmtCount);
				reifiedStmtCount++;
				
				return;
			}
				
			
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}	
	}
	
	


	/**
	 * Create a propositional Commitment pointing to Claim IRI:
	 * 
	 * Create named Individual PcomitIRI
	 * add Creditor and Debtor
	 * add creation Time
	 * CommitTo claimIRI
	 * 
	 * @param PcomitIRI
	 * @param claimIRI
	 * @return
	 */
	private Set<OWLAxiom> createPropositionalCommitmentToClaimAxioms(OWLDataFactory dataFactory, OWLOntology stateOnto, 
															         String senderName,String receiverName, String pcomitID, String claimIRI, String instantTime) {
		
		Set<OWLAxiom> pComitAxioms = new LinkedHashSet<OWLAxiom>();
		
		
		OWLNamedIndividual Pcomit          = dataFactory.getOWLNamedIndividual(":" + pcomitID, pm);
		OWLObjectProperty  hasDebtorProp   = dataFactory.getOWLObjectProperty(IRI.create(oblOntoURL + "#hasDebtor"));
		OWLObjectProperty  hasCreditorProp = dataFactory.getOWLObjectProperty(IRI.create(oblOntoURL + "#hasCreditor"));
		OWLObjectProperty  commitToProp    = dataFactory.getOWLObjectProperty(IRI.create(comitOntoURL + "#commitTo"));
		OWLObjectProperty  atTime          = dataFactory.getOWLObjectProperty(IRI.create(clOntoURL + "#atTime"));

		OWLNamedIndividual senderIndiv      = dataFactory.getOWLNamedIndividual(":Agent_" + senderName, pm);
		OWLNamedIndividual receiverIndiv    = dataFactory.getOWLNamedIndividual(":Agent_" + receiverName, pm);
		OWLNamedIndividual claimIndiv       = dataFactory.getOWLNamedIndividual(IRI.create(claimIRI));
		
		
		//Instant_21-08-2012T07:16:00+02 creation if does not already exist		
		//hasTime(Instant_21-08-2012T07:16:00+02, 21-08-2012T07:16:00+02)

		OWLNamedIndividual PComitInstant = dataFactory.getOWLNamedIndividual(":Instant_" + instantTime, pm);

		if (!stateOnto.containsIndividualInSignature(PComitInstant.getIRI())) {

			OWLDataProperty hasTime = dataFactory.getOWLDataProperty((IRI.create(temporalOntoURL + "#hasTime")));
			OWLDataPropertyAssertionAxiom dataAssertionAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(hasTime, PComitInstant, dataFactory.getOWLLiteral(instantTime, dateTimeDatatype));
			pComitAxioms.add(dataAssertionAxiom);

		}
				
		// atTime(obl_n, Instant_21-08-2012T07:16:00+02)
		OWLObjectPropertyAssertionAxiom axiom = dataFactory.getOWLObjectPropertyAssertionAxiom(atTime, Pcomit, PComitInstant);
		pComitAxioms.add(axiom);
		
		
		axiom = dataFactory.getOWLObjectPropertyAssertionAxiom(hasDebtorProp, Pcomit, senderIndiv);
		pComitAxioms.add(axiom);
		
		axiom = dataFactory.getOWLObjectPropertyAssertionAxiom(hasCreditorProp, Pcomit, receiverIndiv);
		pComitAxioms.add(axiom);
		
		axiom = dataFactory.getOWLObjectPropertyAssertionAxiom(commitToProp, Pcomit, claimIndiv);
		pComitAxioms.add(axiom);
		
		
		
		return pComitAxioms;
	}

	/**
	 * Add Performed Action as a claim graph in the triple store
	 * @param observedAction
	 * @param actionTime
	 * @param claimModelIRI 
	 * @return 
	 */
	private Model createPerformedActionClaim(AbsAgentAction observedAction, DateTime actionTime, String claimModelIRI) {
		// TODO Auto-generated method stub	
		return JadeJenaConvertor.CreateJenaModelFromJadeAction(observedAction, actionTime, claimModelIRI);
		
	}

	private void addPropositionalCommmitment(String sender, String receiver, int reifiedStmtCount) {
		
		OWLNamedIndividual Pcomit         = dataFactory.getOWLNamedIndividual(":Pcomit_" + reifiedStmtCount, pm);
		OWLObjectProperty hasDebtorProp   = dataFactory.getOWLObjectProperty(IRI.create(comitOntoURL + "#hasDebtor"));
		OWLObjectProperty hasCreditorProp = dataFactory.getOWLObjectProperty(IRI.create(comitOntoURL + "#hasCreditor"));
		OWLObjectProperty commitToProp    = dataFactory.getOWLObjectProperty(IRI.create(comitOntoURL + "#commitTo"));


		OWLNamedIndividual senderIndiv      = dataFactory.getOWLNamedIndividual(":" + sender, pm);
		OWLNamedIndividual receiverIndiv    = dataFactory.getOWLNamedIndividual(":" + receiver, pm);
		OWLNamedIndividual reifiedStmtIndiv = dataFactory.getOWLNamedIndividual(":reifiedStatement_" + reifiedStmtCount, pm);
		
		OWLObjectPropertyAssertionAxiom axiom = dataFactory.getOWLObjectPropertyAssertionAxiom(hasDebtorProp, Pcomit, senderIndiv);
		manager.addAxiom(stateOnto, axiom);
		
		axiom = dataFactory.getOWLObjectPropertyAssertionAxiom(hasCreditorProp, Pcomit, receiverIndiv);
		manager.addAxiom(stateOnto, axiom);
		
		axiom = dataFactory.getOWLObjectPropertyAssertionAxiom(commitToProp, Pcomit, reifiedStmtIndiv);
		manager.addAxiom(stateOnto, axiom);
		
	}


	/**
	 * Update the Kb with the incoming refiedStatement
	 * 
	 * We do not mark the provononce yet
	 * @param predicateExpr
	 */
	private void addReifiedStatement(AbsPredicate reifiedStatement, int areifiedStmtCount) {

		OWLNamedIndividual statement        = dataFactory.getOWLNamedIndividual(":reifiedStatement_" + areifiedStmtCount, pm);
		OWLDataProperty    haspredicateProp = dataFactory.getOWLDataProperty(IRI.create(oblOntoURL + "#hasPredicate"));
		OWLDataProperty    hasSubjectProp   = dataFactory.getOWLDataProperty(IRI.create(oblOntoURL + "#hasSubject"));
		OWLDataProperty    hasObjectProp    = dataFactory.getOWLDataProperty(IRI.create(oblOntoURL + "#hasObject"));
		
		String subjectValue        = reifiedStatement.getString(OceanCL.Vocabulary.HAS_SUBJECT.URIValue());
		String predicateValue      = reifiedStatement.getString(OceanCL.Vocabulary.HAS_PREDICATE.URIValue());
		OWLLiteral objectasLiteral = null;
		try {
				objectasLiteral = extractReifiedStatementObject(dataFactory, (AbsConcept) reifiedStatement.getAbsObject(OceanCL.Vocabulary.HAS_OBJECT.URIValue()));
		} catch (UnsupportedDataTypeException e) {
			Logger logger = LoggerFactory.getLogger("OISInfrastructure");
			logger.warn("Unsupported DataType for Object of reified statement in upadteOnReifiedStatement, not updating the KB");
			return;
		}
		
		OWLLiteral subjectasLiteral = dataFactory.getOWLLiteral(subjectValue, anyURIDataType);
		OWLLiteral predicateasLiteral = dataFactory.getOWLLiteral(predicateValue, anyURIDataType);
		
		manager.addAxiom(stateOnto, dataFactory.getOWLDataPropertyAssertionAxiom(hasSubjectProp, statement, subjectasLiteral));
		manager.addAxiom(stateOnto, dataFactory.getOWLDataPropertyAssertionAxiom(haspredicateProp, statement, predicateasLiteral));
		manager.addAxiom(stateOnto, dataFactory.getOWLDataPropertyAssertionAxiom(hasObjectProp, statement, objectasLiteral));
	}
	
	

	/**
	 * TODO Refactor extractReifiedStatementObject method to be in a utility class
	 * TODO complete extractReifiedStatementObject method to include other basic type (e.g. Date etc...)
	 * @param adataFactory
	 * @param rdfRessourceObject
	 * @return
	 * @throws UnsupportedDataTypeException 
	 */
	private OWLLiteral extractReifiedStatementObject (OWLDataFactory adataFactory, AbsConcept rdfRessourceObject) throws UnsupportedDataTypeException {

		String type            = rdfRessourceObject.getString(OceanCL.Vocabulary.HAS_RDF_RESSOURCE_TYPE.URIValue());


		if (type == null)
			return adataFactory.getOWLLiteral(rdfRessourceObject.getString(OceanCL.Vocabulary.HAS_RDF_RESSOURCE_VALUE.URIValue()), anyURIDataType);
			//return adataFactory.(rdfRessourceObject.getString(OceanCL.Vocabulary.HAS_RDF_RESSOURCE_VALUE.URIValue()));

		switch (type) {

		case BasicOntology.STRING:

			return adataFactory.getOWLLiteral(rdfRessourceObject.getString(OceanCL.Vocabulary.HAS_RDF_RESSOURCE_VALUE.URIValue()));

			

		case BasicOntology.INTEGER:

			return adataFactory.getOWLLiteral(Integer.valueOf(rdfRessourceObject.getString(OceanCL.Vocabulary.HAS_RDF_RESSOURCE_VALUE.URIValue())));

		default:

			throw new UnsupportedDataTypeException(type);
		}

	}
	


	
	
	/**
	 * Build the name of the individual
	 * Take the key properties:
	 * => if it is a data property take the value as a string and concatenate it
	 * => if it is an individual take the value of the type as a string concatenated it and recall the function on it
	 * 
	 * If that name exist return the owlNamedinvidual of the constructed name, otherwise set it, add it and then return it
	 * This is done recursively for each property
	 * @param object
	 * @return
	 */
	protected OWLNamedIndividual createAssertedNamedIndividualFromObject(AbsObject object)  {
		
		OWLNamedIndividual  indiv             = null;
		Set<OWLHasKeyAxiom> keyAxioms         = null;
		int                 index             = object.getTypeName().indexOf("#");
		String              constructedName   = object.getTypeName().substring(index + 1);
		OWLClass            typeClass         = dataFactory.getOWLClass(IRI.create(object.getTypeName()));
		
		
		//TODO Go trough all the ontologies of the import closure to find all keyAxioms
		//Get the Key axioms for Type of this Object
		for (OWLOntology ont : manager.getImportsClosure(stateOnto)) {
			
				keyAxioms  = ont.getHasKeyAxioms(typeClass);
				
				if (!keyAxioms.isEmpty())
					break;
        }
		
		for (OWLHasKeyAxiom owlHasKeyAxiom : keyAxioms) {
			
			//DataProperty of the KeyAxiom
			Set<OWLDataPropertyExpression> dataPropExpr = owlHasKeyAxiom.getDataPropertyExpressions();
			
			for (OWLDataPropertyExpression owlDataPropertyExpression : dataPropExpr) {
				
				AbsPrimitive primitive = (AbsPrimitive) object.getAbsObject(owlDataPropertyExpression.asOWLDataProperty().getIRI().toString());
				Object value = primitive.getObject();
				
				if (value instanceof Date) {
					
					SimpleDateFormat  simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
					DateTime          dateTime          = new DateTime(simpleDateFormat.format((Date)value));
					DateTimeFormatter fmt               = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");
					constructedName = constructedName.concat("_" + dateTime.toString(fmt));
					
				}
				else 
				{
					constructedName = constructedName.concat("_" + value.toString());
				}
				
			}
			
			//Object Property of the KeyAxiom
			Set<OWLObjectPropertyExpression> objectPropExpr = owlHasKeyAxiom.getObjectPropertyExpressions();
			
			for (OWLObjectPropertyExpression owlObjectPropertyExpression : objectPropExpr) {
				
				AbsObject propertyValue = object.getAbsObject(owlObjectPropertyExpression.asOWLObjectProperty().getIRI().toString());
				
				if (propertyValue.getTypeName().equalsIgnoreCase(UpperOntology.Vocabulary.NAMED_INDIVIDUAL.URIValue())) {
					
					String value = ((AbsPrimitiveSlotsHolder) propertyValue).getString(UpperOntology.Vocabulary.HAS_NAME.URIValue());
					constructedName = constructedName.concat("_" + value.substring(value.indexOf("#") + 1));
					
				}
				else
				{
				
					OWLNamedIndividual propertyValueIndiv = createAssertedNamedIndividualFromObject(propertyValue);
				
					constructedName = constructedName.concat("_" + propertyValueIndiv.getIRI().getFragment());
				}
				
			}
			
		}
		
		indiv = dataFactory.getOWLNamedIndividual(":" + constructedName, pm);
		
		//Assert the property of the individual if the individual do not exist already.
		if (!stateOnto.containsIndividualInSignature(indiv.getIRI())) { //Assert the property of the individual
			
			AbsObject propObj = null;
			Object value     = null;
			
			String[] slotsName = object.getNames();

			for (int i = 0; i < slotsName.length; i++) {

				propObj = object.getAbsObject(slotsName[i]);

				/*If it is an Absprimitive just asserted the property */
				if (propObj instanceof AbsPrimitive) {
					
					value = ((AbsPrimitive) propObj).getObject();
					//As of now boolean is never used
					if (value instanceof String) {
						manager.addAxiom(stateOnto,
								dataFactory.getOWLDataPropertyAssertionAxiom(dataFactory.getOWLDataProperty(IRI.create(slotsName[i])), indiv, (String)value)
								);
						continue;
					}
					if (value instanceof Integer) {
						manager.addAxiom(stateOnto,
								dataFactory.getOWLDataPropertyAssertionAxiom(dataFactory.getOWLDataProperty(IRI.create(slotsName[i])), indiv, (Integer)value)
								);
						continue;
					}
					if (value instanceof Float) {
						manager.addAxiom(stateOnto,
								dataFactory.getOWLDataPropertyAssertionAxiom(dataFactory.getOWLDataProperty(IRI.create(slotsName[i])), indiv, (Float)value)
								);
						continue;
					}
					if (value instanceof Double) {
						manager.addAxiom(stateOnto,
								dataFactory.getOWLDataPropertyAssertionAxiom(dataFactory.getOWLDataProperty(IRI.create(slotsName[i])), indiv, (Double)value)
								);
						continue;
					}
					if (value instanceof Date) {
						
						SimpleDateFormat  simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
						DateTime          dateTime          = new DateTime(simpleDateFormat.format((Date)value));
						DateTimeFormatter fmt               = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");
						
						manager.addAxiom(stateOnto,
								dataFactory.getOWLDataPropertyAssertionAxiom(dataFactory.getOWLDataProperty(IRI.create(slotsName[i])), indiv, 
								                                             dataFactory.getOWLLiteral(dateTime.toString(fmt), this.dateTimeDatatype))
								        );
						continue;
					}
					
					System.out.println("Problem in naming the individual, encouter an unknown primitive");
					System.out.println("InteractionKB.createNamedIndividualFromObject()");
					System.exit(-1);
					
				} // if it is a primitive
				
				if (propObj instanceof AbsPrimitiveSlotsHolder) {
					
					if (propObj.getTypeName().equals(UpperOntology.Vocabulary.NAMED_INDIVIDUAL.URIValue())){
						
						value = ((AbsPrimitiveSlotsHolder) propObj).getString(UpperOntology.Vocabulary.HAS_NAME.URIValue());
					    OWLNamedIndividual ValueAsIndiv = dataFactory.getOWLNamedIndividual(IRI.create((String) value));
						manager.addAxiom(stateOnto,
								dataFactory.getOWLObjectPropertyAssertionAxiom(dataFactory.getOWLObjectProperty(IRI.create(slotsName[i])), indiv, ValueAsIndiv)
								);
					}
					else
					{
						OWLNamedIndividual slotIndiv = createAssertedNamedIndividualFromObject(propObj);
					
						manager.addAxiom(stateOnto, 
									 	dataFactory.getOWLObjectPropertyAssertionAxiom(dataFactory.getOWLObjectProperty(IRI.create(slotsName[i])), indiv, slotIndiv)
									 	);
					}
					
				}
					
			}//For each slots
			
		}//If the individual do not exist
		
		return indiv;
	}
	

	
	
	
	/**
	 * Where possible such as with the name of the individual of type Agent
	 * or the instant, we try to use the same name based on the value of the properties that are the key of the classes. 
	 * E.g. Agent_hasAID is the name of the individual. 
	 * Only interval do not benefit from it and his named after the obligation it belongs to.
	 * In any case, one has to understand that this is just a question of memory and reasoning economy. The haskey property works perfectly
	 * to identify when individuals with different name represent the same individual. 
	 * 
	 * @param servicedMsg
	 * @param conditionalActionExpr ""
	 * @param count
	 * @return
	 */
	private OWLNamedIndividual createObligation(AclMessage servicedMsg, AbsAgentAction action, int count) {
		
		//Obligation Instance creation
		OWLNamedIndividual obl = dataFactory.getOWLNamedIndividual(":obl_" + count, pm);
		OWLClass oblClass = dataFactory.getOWLClass(IRI.create(oblOntoURL + "#Obligation"));
		OWLClassAssertionAxiom classAssertion = dataFactory.getOWLClassAssertionAxiom(oblClass, obl);
		manager.addAxiom(stateOnto, classAssertion);

		//Creditor assertion
		String             agtName  = servicedMsg.getAclPayload().getSender().getName();
		OWLNamedIndividual creditor = dataFactory.getOWLNamedIndividual(":Agent_" + agtName ,pm);
		
		if (!stateOnto.containsIndividualInSignature(creditor.getIRI())) {
			
			OWLDataProperty               hasAID             = dataFactory.getOWLDataProperty((IRI.create(clOntoURL + "#hasAID")));
			OWLDataPropertyAssertionAxiom dataAssertionAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(hasAID, creditor, agtName);
			manager.addAxiom(stateOnto, dataAssertionAxiom);
			
		}
		
		OWLObjectProperty               hasCreditor   = dataFactory.getOWLObjectProperty(IRI.create(oblOntoURL + "#hasCreditor"));
		OWLObjectPropertyAssertionAxiom propAssertion = dataFactory.getOWLObjectPropertyAssertionAxiom(hasCreditor, obl, creditor);
		manager.addAxiom(stateOnto, propAssertion);
		
		
		//Debtor assertion
		agtName                    = servicedMsg.getAclPayload().getReceiver().get(0).getName();
		OWLNamedIndividual debtor  = dataFactory.getOWLNamedIndividual(":Agent_" + agtName ,pm);
		
		if (!stateOnto.containsIndividualInSignature(debtor.getIRI())) {
			
			OWLDataProperty               hasAID             = dataFactory.getOWLDataProperty((IRI.create(clOntoURL + "#hasAID")));
			OWLDataPropertyAssertionAxiom dataAssertionAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(hasAID, debtor, agtName);
			manager.addAxiom(stateOnto, dataAssertionAxiom);
			
		}
		
		OWLObjectProperty hasDebtor = dataFactory.getOWLObjectProperty(IRI.create(oblOntoURL + "#hasDebtor"));
		propAssertion                = dataFactory.getOWLObjectPropertyAssertionAxiom(hasDebtor, obl, debtor);
		manager.addAxiom(stateOnto, propAssertion);


		//Instant assertion
		
		//hasTime(Instant_21-08-2012T07:16:00+02, 21-08-2012T07:16:00+02)
		
		DateTimeFormatter fmt         = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");
		String            now         = DateTime.now().minusSeconds(1).toString(fmt);
		
		
		
		//Instant_21-08-2012T07:16:00+02 creation if does not already exist
		OWLNamedIndividual oblInstant = dataFactory.getOWLNamedIndividual(":Instant_" + now, pm);
				
		if (!stateOnto.containsIndividualInSignature(oblInstant.getIRI())) {
		
			OWLDataProperty   hasTime     = dataFactory.getOWLDataProperty((IRI.create(temporalOntoURL + "#hasTime")));
			OWLDataPropertyAssertionAxiom dataAssertionAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(hasTime, oblInstant, dataFactory.getOWLLiteral(now, dateTimeDatatype));
			manager.addAxiom(stateOnto, dataAssertionAxiom);
			
		}
		
		//atTime(obl_n, Instant_21-08-2012T07:16:00+02)
		OWLObjectProperty atTime = dataFactory.getOWLObjectProperty(IRI.create(clOntoURL + "#atTime"));
		propAssertion            = dataFactory.getOWLObjectPropertyAssertionAxiom(atTime, obl, oblInstant);
		manager.addAxiom(stateOnto, propAssertion);
		
		
		//Interval assertion (even if it will be filled later)
		OWLNamedIndividual oblInterv   = dataFactory.getOWLNamedIndividual(":obl_" + count + "_Interval", pm);
		OWLObjectProperty  hasInterval = dataFactory.getOWLObjectProperty(IRI.create(oblOntoURL + "#hasInterval"));
		propAssertion                  = dataFactory.getOWLObjectPropertyAssertionAxiom(hasInterval, obl, oblInterv);
		manager.addAxiom(stateOnto, propAssertion);
		
		
		//Set Obl_n Interval FinishTime or  Obl_n Duration of Validity	
		
		//Obl_n Interval FinishTime is set after the action deadline property which points to a TimeEvent.
		AbsConcept   deadlineTimeEvent = (AbsConcept) action.getAbsObject(OceanCL.Vocabulary.HAS_DEADLINE.URIValue());
		
		if (!(deadlineTimeEvent == null)) { // Set the end of the interval
			
			AbsConcept        validInstant          = (AbsConcept) deadlineTimeEvent.getAbsObject(OceanCL.Vocabulary.AT_TIME.URIValue());
			Date              date                  = validInstant.getDate(TimeOntology.Vocabulary.HAS_TIME.URIValue());
			SimpleDateFormat  simpleDateFormat      = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			DateTime          dateTime              = new DateTime(simpleDateFormat.format(date));
			DateTimeFormatter dateTimeformatter     = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");
			OWLLiteral        dateTimeAsOWLLitteral = dataFactory.getOWLLiteral(dateTime.toString(dateTimeformatter), dateTimeDatatype);
			
			
			OWLDataProperty               hasFinishTime      = dataFactory.getOWLDataProperty((IRI.create(temporalOntoURL + "#hasFinishTime")));
			OWLDataPropertyAssertionAxiom dataAssertionAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(hasFinishTime, oblInterv, dateTimeAsOWLLitteral);	
			manager.addAxiom(stateOnto, dataAssertionAxiom);
		}
		else //If the action has no deadline, set the Duration of Validity
		{
			//hasDurationOfValidity(Obl, duration), hasCount(duration, 5), hasGranularity(duration, Minutes)
			AbsConcept duration 		    = (AbsConcept) action.getAbsObject(OceanCL.Vocabulary.HAS_DURATION.URIValue());
			int        durationCount   		= duration.getInteger(TimeOntology.Vocabulary.HAS_COUNT.URIValue());
			AbsConcept granularity 		    = (AbsConcept) duration.getAbsObject(TimeOntology.Vocabulary.HAS_GRANULARITY.URIValue());
			String     granularityName	    = granularity.getString(UpperOntology.Vocabulary.HAS_NAME.URIValue());
			String     granularityShortName = granularityName.substring(granularityName.indexOf("#") + 1);
			
			//hasGranularity(OblDuration, granularity)
			
			OWLNamedIndividual oblDuration   		 = dataFactory.getOWLNamedIndividual(":duration_" + durationCount + "_" + granularityShortName, pm);
			
			if (!stateOnto.containsIndividualInSignature(oblDuration.getIRI())) {
				
				//hasGranularity(Oblduration, Minutes)
				OWLNamedIndividual granulIndiv	  = dataFactory.getOWLNamedIndividual(IRI.create(granularityName));
				OWLObjectProperty  hasGranularity = dataFactory.getOWLObjectProperty(IRI.create(temporalOntoURL + "#hasGranularity"));
				propAssertion                     = dataFactory.getOWLObjectPropertyAssertionAxiom(hasGranularity, oblDuration, granulIndiv);
				manager.addAxiom(stateOnto, propAssertion);
				
				//hasCount(Oblduration, 5)
				OWLDataProperty               hasCount           = dataFactory.getOWLDataProperty((IRI.create(temporalOntoURL + "#hasCount")));
				OWLDataPropertyAssertionAxiom dataAssertionAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(hasCount, oblDuration, durationCount);	
				manager.addAxiom(stateOnto, dataAssertionAxiom);
				
			}
			
			//hasDurationOfValidity(obl, Oblduration)
			OWLObjectProperty  hasDurationOfValidity = dataFactory.getOWLObjectProperty(IRI.create(oblOntoURL + "#hasDurationOfValidity"));
			propAssertion                            = dataFactory.getOWLObjectPropertyAssertionAxiom(hasDurationOfValidity, obl, oblDuration);
			manager.addAxiom(stateOnto, propAssertion);

		}

		return obl;
	}
	
	
	/**
	 * Create the Content_n Axiom e.g 
	 * Pay & PerformedAction & hasActor.value(...) & hasAmount.value(...)
	 * The duration and deadline Property are skipped here
	 * 
	 * @param OblName ""
	 * @param servicedMsg ""
	 * @param contentmanager ""
	 * @param count
	 * @param action
	 */
	protected void createContentAxiom (int count, AbsAgentAction action) {

		AbsObject 		   absObj      			    = null;
		OWLClass 		   contentEventClass        = null;
		OWLClassExpression classExpr                = null;
		OWLClassExpression IndivAsclassExpr         = null;

		String currContentEventName 				= "Content_" + count;

		Set<OWLClassExpression> classExprSet        = new HashSet<OWLClassExpression>();

		if (action == null)
			return;

		//Create a class to be the subclass of the ontological class counterpart of the type of "AbsAgentAction.type & PerformedAction"
		contentEventClass = dataFactory.getOWLClass(":" + currContentEventName, pm);
		
		classExprSet.add(dataFactory.getOWLClass( IRI.create(action.getTypeName()) ));
		classExprSet.add(dataFactory.getOWLClass( IRI.create(oblOntoURL + "#PerformedAction") ));


		String[] slotsName = action.getNames();

		for (int i = 0; i < slotsName.length; i++) {

			if (slotsName[i].equalsIgnoreCase(OceanCL.Vocabulary.HAS_DURATION.URIValue()) || slotsName[i].equalsIgnoreCase(OceanCL.Vocabulary.HAS_DEADLINE.URIValue()) ) {
				continue;
			}

			absObj = action.getAbsObject(slotsName[i]);

			/*If it is a primitive create the expression with getOWLDataHasValue according to the type of that primitive,
			  then make the startEvent class a subclass of that expression*/
			if (absObj instanceof AbsPrimitive) {

				classExpr = getDataPropertyClassExpression(slotsName[i], (AbsPrimitive)absObj);
				classExprSet.add(classExpr);

			}

			if (absObj instanceof AbsPrimitiveSlotsHolder) {

				IndivAsclassExpr = getSlotHolderAsClassExpr((AbsPrimitiveSlotsHolder)absObj);
				classExpr = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty( IRI.create(slotsName[i])), IndivAsclassExpr);
				classExprSet.add(classExpr);
			}

		}

		manager.addAxiom(stateOnto, 
				dataFactory.getOWLEquivalentClassesAxiom(contentEventClass, 
						dataFactory.getOWLObjectIntersectionOf(classExprSet)
				)
		);

	}
	
	
	/**
	 * 
	 * @param obl
	 * @param conditionAsaction
	 * @param count
	 */
 	private void setEndEventAxiomAsdurationAddedToOblTime(OWLNamedIndividual obl, AbsAgentAction conditionAsaction, int count) {
		
		//get ConditionAsAction duration
		AbsConcept duration 		    = (AbsConcept) conditionAsaction.getAbsObject(OceanCL.Vocabulary.HAS_DURATION.URIValue());
		int        durationCount   		= duration.getInteger(TimeOntology.Vocabulary.HAS_COUNT.URIValue());
		AbsConcept granularity 		    = (AbsConcept) duration.getAbsObject(TimeOntology.Vocabulary.HAS_GRANULARITY.URIValue());
		String     granularityName	    = granularity.getString(UpperOntology.Vocabulary.HAS_NAME.URIValue());
		
		//Get the obl Time of creation
		OWLObjectProperty  atTime          = dataFactory.getOWLObjectProperty(IRI.create(clOntoURL + "#atTime"));
		OWLDataProperty    hasTime         = dataFactory.getOWLDataProperty(IRI.create(temporalOntoURL + "#hasTime"));
		OWLNamedIndividual oblValidInstant = getAssertedObjectPropertyValue(obl, atTime).asOWLNamedIndividual();
		DateTime           oblTime 		   = new DateTime(getAssertedDataPropertyValue(oblValidInstant, hasTime).getLiteral());

		
		//Calculate The endEvent_timeEvent_time
		DateTime endEventTime           = null;
		
		switch (granularityName) {
		
			case temporalOntoURL + "#Milliseconds":

				endEventTime = oblTime.plusMillis(durationCount);
				break;
				
			case temporalOntoURL + "#Seconds":
				
				endEventTime = oblTime.plusSeconds(durationCount);
				break;
				
			case temporalOntoURL + "#Minutes":
				
				endEventTime = oblTime.plusMinutes(durationCount);
				break;
				
			case temporalOntoURL + "#Hours":
				
				endEventTime = oblTime.plusHours(durationCount);
				break;
				
			case temporalOntoURL + "#Days":
				
				endEventTime = oblTime.plusDays(durationCount);
				break;
				
			case temporalOntoURL + "#Months":
				
				endEventTime = oblTime.plusMonths(durationCount);
				break;
				
			case temporalOntoURL + "#Years":
				
				endEventTime = oblTime.plusYears(durationCount);
				break;
				
			default:
				
				System.out.println("not a Known Granularity");
				System.exit(-1);
				break;
		}
		
		
		DateTimeFormatter  fmt            = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");
		
		//EndEvent timeEvent Instance creation
		OWLNamedIndividual            timeEventvalidInstant  = dataFactory.getOWLNamedIndividual(":Instant_" + endEventTime.toString(fmt), pm);

		if (!stateOnto.containsIndividualInSignature(timeEventvalidInstant.getIRI())) {

			OWLLiteral                    dateTimeAsOwlLitteral = dataFactory.getOWLLiteral(endEventTime.toString(fmt), dateTimeDatatype);
			OWLDataPropertyAssertionAxiom dataAssertionAxiom    = dataFactory.getOWLDataPropertyAssertionAxiom(hasTime, timeEventvalidInstant, dateTimeAsOwlLitteral);	
			manager.addAxiom(stateOnto, dataAssertionAxiom);

		}

		//EndEvent TimeEvent Creation
		OWLNamedIndividual timeEventIndiv = dataFactory.getOWLNamedIndividual(":timeEvent_Instant_" + endEventTime.toString(fmt), pm);

		if (!stateOnto.containsIndividualInSignature(timeEventIndiv.getIRI())) {	

			OWLObjectPropertyAssertionAxiom propAssertion  = dataFactory.getOWLObjectPropertyAssertionAxiom(atTime, timeEventIndiv, timeEventvalidInstant);
			manager.addAxiom(stateOnto, propAssertion);

			OWLClass                        timeEventClass = dataFactory.getOWLClass(IRI.create(clOntoURL + "#TimeEvent"));
			OWLClassAssertionAxiom          classAssertion = dataFactory.getOWLClassAssertionAxiom(timeEventClass, timeEventIndiv);
			manager.addAxiom(stateOnto, classAssertion);

		}
		
		
		OWLClass       endEventClass    = dataFactory.getOWLClass(":EndEvent_" + count, pm);
		manager.addAxiom(stateOnto, dataFactory.getOWLEquivalentClassesAxiom(endEventClass, dataFactory.getOWLObjectOneOf(timeEventIndiv)));
		
	}


	/**
	 * Create the EndEvent axiom defined as EndEvent_n ={TimeEvent}.
	 * This comes from the fact that a condition as an action had a deadline timeEvent.
	 * @param deadlineTimeEvent
	 * @param count
	 */
	private void setEndEventAxiomAsDeadline(AbsConcept deadlineTimeEvent, int count) {
		
		AbsConcept        validInstant      = (AbsConcept) deadlineTimeEvent.getAbsObject(OceanCL.Vocabulary.AT_TIME.URIValue());
		Date              date              = validInstant.getDate(TimeOntology.Vocabulary.HAS_TIME.URIValue());
		SimpleDateFormat  simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		DateTime          dateTime          = new DateTime(simpleDateFormat.format(date));
		DateTimeFormatter fmt               = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");
		
		
		
		//timeEvent Instance creation
		
		OWLNamedIndividual            validInstantIndiv  = dataFactory.getOWLNamedIndividual(":Instant_" + dateTime.toString(fmt), pm);
		
		if (!stateOnto.containsIndividualInSignature(validInstantIndiv.getIRI())) {

			OWLLiteral                    dateTimeAsOwlLitteral = dataFactory.getOWLLiteral(dateTime.toString(fmt), dateTimeDatatype);
			OWLDataProperty               hasTime               = dataFactory.getOWLDataProperty((IRI.create(temporalOntoURL + "#hasTime")));
			OWLDataPropertyAssertionAxiom dataAssertionAxiom    = dataFactory.getOWLDataPropertyAssertionAxiom(hasTime, validInstantIndiv, dateTimeAsOwlLitteral);	
			manager.addAxiom(stateOnto, dataAssertionAxiom);

		}
		
		
		OWLNamedIndividual              timeEventIndiv = dataFactory.getOWLNamedIndividual(":timeEvent_Instant_" + dateTime.toString(fmt), pm);
		
		if (!stateOnto.containsIndividualInSignature(timeEventIndiv.getIRI())) {	
			
			OWLObjectProperty               atTime         = dataFactory.getOWLObjectProperty(IRI.create(clOntoURL + "#atTime"));
			OWLObjectPropertyAssertionAxiom propAssertion  = dataFactory.getOWLObjectPropertyAssertionAxiom(atTime, timeEventIndiv, validInstantIndiv);
			manager.addAxiom(stateOnto, propAssertion);
			
			OWLClass                        timeEventClass = dataFactory.getOWLClass(IRI.create(clOntoURL + "#TimeEvent"));
			OWLClassAssertionAxiom          classAssertion = dataFactory.getOWLClassAssertionAxiom(timeEventClass, timeEventIndiv);
			manager.addAxiom(stateOnto, classAssertion);
			
		}

		OWLClass       endEventClass    = dataFactory.getOWLClass(":EndEvent_" + count, pm);
		
		manager.addAxiom(stateOnto, dataFactory.getOWLEquivalentClassesAxiom(endEventClass, dataFactory.getOWLObjectOneOf(timeEventIndiv)));

		
	}


	/**
	 * 
	 * @param count
	 * @param oblInterv
	 */
	private void createDeadlineAxiom(int count, OWLNamedIndividual oblInterv) {
		
		OWLDataProperty    hasFinishTime   = dataFactory.getOWLDataProperty(IRI.create(temporalOntoURL + "#hasFinishTime"));
		DateTime           finishTime      = new DateTime(getAssertedDataPropertyValue(oblInterv, hasFinishTime).getLiteral());
		DateTimeFormatter  fmt             = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");
		
		
		//timeEvent Instance creation

		OWLNamedIndividual            validInstantIndiv  = dataFactory.getOWLNamedIndividual(":Instant_" + finishTime.toString(fmt), pm);
		
		
		if (!stateOnto.containsIndividualInSignature(validInstantIndiv.getIRI())) { //help checking if the individual exist already
			
			OWLLiteral                    dateTimeAsOwlLitteral = dataFactory.getOWLLiteral(finishTime.toString(fmt), dateTimeDatatype);
			OWLDataProperty               hasTime               = dataFactory.getOWLDataProperty((IRI.create(temporalOntoURL + "#hasTime")));
			OWLDataPropertyAssertionAxiom dataAssertionAxiom    = dataFactory.getOWLDataPropertyAssertionAxiom(hasTime, validInstantIndiv, dateTimeAsOwlLitteral);	
			manager.addAxiom(stateOnto, dataAssertionAxiom);
			
		}

		OWLNamedIndividual              timeEventIndiv = dataFactory.getOWLNamedIndividual(":timeEvent_Instant_" + finishTime.toString(fmt), pm);
		
		if (!stateOnto.containsIndividualInSignature(timeEventIndiv.getIRI())) { //help checking if the individual exist already
			
			OWLObjectProperty               atTime         = dataFactory.getOWLObjectProperty(IRI.create(clOntoURL + "#atTime"));
			OWLObjectPropertyAssertionAxiom propAssertion  = dataFactory.getOWLObjectPropertyAssertionAxiom(atTime, timeEventIndiv, validInstantIndiv);
			manager.addAxiom(stateOnto, propAssertion);
			
			OWLClass                        timeEventClass = dataFactory.getOWLClass(IRI.create(clOntoURL + "#TimeEvent"));
			OWLClassAssertionAxiom          classAssertion = dataFactory.getOWLClassAssertionAxiom(timeEventClass, timeEventIndiv);
			manager.addAxiom(stateOnto, classAssertion);
			
		}
		
		OWLClass       Deadline_n_Class     = dataFactory.getOWLClass(":Deadline_" + count, pm);
		
		manager.addAxiom(stateOnto, dataFactory.getOWLEquivalentClassesAxiom(Deadline_n_Class, dataFactory.getOWLObjectOneOf(timeEventIndiv)));
		
		
	}

	/**
	 * Set the finishTime Property of the oblInterv of the Obligation obl
	 * @param obl
	 * @param oblInterv
	 */
	private void setOblIntervfinishTime(OWLNamedIndividual obl, OWLNamedIndividual oblInterv) {
		
		OWLObjectProperty  hasDurationOfValidity  = dataFactory.getOWLObjectProperty(IRI.create(oblOntoURL + "#hasDurationOfValidity"));
		OWLDataProperty    hasCount               = dataFactory.getOWLDataProperty(IRI.create(temporalOntoURL + "#hasCount"));
		OWLObjectProperty  hasGranularity         = dataFactory.getOWLObjectProperty(IRI.create(temporalOntoURL + "#hasGranularity"));
		OWLDataProperty    hasFinishTime          = dataFactory.getOWLDataProperty(IRI.create(temporalOntoURL + "#hasFinishTime"));
		OWLDataProperty    hasStartTime           = dataFactory.getOWLDataProperty(IRI.create(temporalOntoURL + "#hasStartTime"));
		
		
		OWLNamedIndividual durationOfValidity     = getAssertedObjectPropertyValue(obl, hasDurationOfValidity).asOWLNamedIndividual();
		int                count                  = Integer.parseInt(getAssertedDataPropertyValue(durationOfValidity, hasCount).getLiteral());
		String             granularity            = getAssertedObjectPropertyValue(durationOfValidity, hasGranularity).asOWLNamedIndividual().getIRI().toString();
		DateTime           startTime              = new DateTime(getAssertedDataPropertyValue(oblInterv, hasStartTime).getLiteral());
		DateTime           finishTime             = null;
		DateTimeFormatter  fmt                    = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");
		
		switch (granularity) {
		
			case temporalOntoURL + "#Milliseconds":

				finishTime = startTime.plusMillis(count);
				break;
				
			case temporalOntoURL + "#Seconds":
				
				finishTime = startTime.plusSeconds(count);
				break;
				
			case temporalOntoURL + "#Minutes":
				
				finishTime = startTime.plusMinutes(count);
				break;
				
			case temporalOntoURL + "#Hours":
				
				finishTime = startTime.plusHours(count);
				break;
				
			case temporalOntoURL + "#Days":
				
				finishTime = startTime.plusDays(count);
				break;
				
			case temporalOntoURL + "#Months":
				
				finishTime = startTime.plusMonths(count);
				break;
				
			case temporalOntoURL + "#Years":
				
				finishTime = startTime.plusYears(count);
				break;
				
			default:
				
				System.out.println("not a Known Granularity");
				System.exit(-1);
				break;
		}
		
		OWLLiteral dateTimeAsOwlLitteral = dataFactory.getOWLLiteral(finishTime.toString(fmt), dateTimeDatatype);
		
		manager.addAxiom(stateOnto, dataFactory.getOWLDataPropertyAssertionAxiom(hasFinishTime, oblInterv, dateTimeAsOwlLitteral));
	}

	/**
	 * The OneOfIndiv is a TimedEvent, in this case it could be an Obligation or TimeEvent.
	 * We get its Validinstant of creation via it atTime property,
	 * and then the time of that Validinstant via the hasTime property.
	 * Finaly we assign this value to the hasFinishTime Property of the ValidPeriod Interval of the Obligation.
	 * @param count
	 * @param oneOfIndiv
	 * @param obl
	 */
	private void setOblBiginningIntervFromOneOfStartEvent(int count, OWLNamedIndividual oneOfIndiv, OWLNamedIndividual obl) {
		

		OWLObjectProperty  atTime       = dataFactory.getOWLObjectProperty(IRI.create(clOntoURL + "#atTime"));
		OWLDataProperty    hasTime      = dataFactory.getOWLDataProperty(IRI.create(temporalOntoURL + "#hasTime"));
		OWLObjectProperty  hasInterval  = dataFactory.getOWLObjectProperty(IRI.create(oblOntoURL + "#hasInterval"));
		OWLDataProperty    hasStartTime = dataFactory.getOWLDataProperty(IRI.create(temporalOntoURL + "#hasStartTime"));
		 
		OWLNamedIndividual validInstant          = getAssertedObjectPropertyValue(oneOfIndiv, atTime).asOWLNamedIndividual();
		OWLLiteral         dateTimeAsOwlLitteral = getAssertedDataPropertyValue(validInstant, hasTime);
		OWLNamedIndividual validPeriod           = getAssertedObjectPropertyValue(obl, hasInterval).asOWLNamedIndividual();

		manager.addAxiom(stateOnto, dataFactory.getOWLDataPropertyAssertionAxiom(hasStartTime, validPeriod, dateTimeAsOwlLitteral));
	}


	/**
	 * StartEvent_n = {obl_n}
	 * @param obl
	 * @param count
	 */
	private void setStartEventAsObligation(OWLNamedIndividual obl, int count) {
		
		String         currStartEventName = "StartEvent_" + count;
		OWLClass       startEventClass    = dataFactory.getOWLClass(":" + currStartEventName, pm);
		
		manager.addAxiom(stateOnto, dataFactory.getOWLEquivalentClassesAxiom(startEventClass, dataFactory.getOWLObjectOneOf(obl)));
		
	}
	
	/**
	 * Retrieve the timeEvent as Jade ontology instance
	 * and create a timeEvent as Owl ontology instance
	 * return the individual timeEvent that it creates
	 * @param timeEvent
	 * @return 
	 */
	private OWLNamedIndividual setStartEventAsTimeEvent(AbsConcept timeEvent, int count) {
		
		AbsConcept        validInstant      = (AbsConcept) timeEvent.getAbsObject(OceanCL.Vocabulary.AT_TIME.URIValue());
		Date              date              = validInstant.getDate(TimeOntology.Vocabulary.HAS_TIME.URIValue());
		SimpleDateFormat  simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		DateTime          dateTime          = new DateTime(simpleDateFormat.format(date));
		DateTimeFormatter fmt               = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");
		
		
		
		//timeEvent Instance creation
		
		OWLNamedIndividual            validInstantIndiv  = dataFactory.getOWLNamedIndividual(":Instant_" + dateTime.toString(fmt), pm);
		
		if (!stateOnto.containsIndividualInSignature(validInstantIndiv.getIRI())) {

			OWLLiteral                    dateTimeAsOwlLitteral = dataFactory.getOWLLiteral(dateTime.toString(fmt), dateTimeDatatype);
			OWLDataProperty               hasTime               = dataFactory.getOWLDataProperty((IRI.create(temporalOntoURL + "#hasTime")));
			OWLDataPropertyAssertionAxiom dataAssertionAxiom    = dataFactory.getOWLDataPropertyAssertionAxiom(hasTime, validInstantIndiv, dateTimeAsOwlLitteral);	
			manager.addAxiom(stateOnto, dataAssertionAxiom);

		}
		
		
		OWLNamedIndividual              timeEventIndiv = dataFactory.getOWLNamedIndividual(":timeEvent_Instant_" + dateTime.toString(fmt), pm);
		
		if (!stateOnto.containsIndividualInSignature(timeEventIndiv.getIRI())) {	
			
			OWLObjectProperty               atTime         = dataFactory.getOWLObjectProperty(IRI.create(clOntoURL + "#atTime"));
			OWLObjectPropertyAssertionAxiom propAssertion  = dataFactory.getOWLObjectPropertyAssertionAxiom(atTime, timeEventIndiv, validInstantIndiv);
			manager.addAxiom(stateOnto, propAssertion);
			
			OWLClass                        timeEventClass = dataFactory.getOWLClass(IRI.create(clOntoURL + "#TimeEvent"));
			OWLClassAssertionAxiom          classAssertion = dataFactory.getOWLClassAssertionAxiom(timeEventClass, timeEventIndiv);
			manager.addAxiom(stateOnto, classAssertion);
			
		}

		OWLClass       startEventClass    = dataFactory.getOWLClass(":StartEvent_" + count, pm);
		
		manager.addAxiom(stateOnto, dataFactory.getOWLEquivalentClassesAxiom(startEventClass, dataFactory.getOWLObjectOneOf(timeEventIndiv)));

		return timeEventIndiv;
		
	}

	/**
	 * 
	 * @param condition
	 */
	private void setStartEventAsPerformedAction(AbsAgentAction condition, int count) {
		
		AbsObject               absObj      		      = null;
		OWLClass                startEventClass           = null;
		OWLClassExpression      classExpr                 = null;
		OWLClassExpression      IndivAsclassExpr          = null;
		String                  currStartEventName 		  = "StartEvent_" + count;

		Set<OWLClassExpression> classExprSet        = new HashSet<OWLClassExpression>();

		//Create a class to be the subclass of condition as an AbsAgentAction & PerformedAction
		startEventClass = dataFactory.getOWLClass(":" + currStartEventName, pm);

		classExprSet.add(dataFactory.getOWLClass( IRI.create(condition.getTypeName()) ));
		classExprSet.add(dataFactory.getOWLClass( IRI.create(oblOntoURL + "#PerformedAction") ));


		String[] slotsName = condition.getNames();

		for (int i = 0; i < slotsName.length; i++) {

			if (slotsName[i].equalsIgnoreCase(OceanCL.Vocabulary.HAS_DURATION.URIValue()) || slotsName[i].equalsIgnoreCase(OceanCL.Vocabulary.HAS_DEADLINE.URIValue()) ) {
				continue;
			}

			absObj = condition.getAbsObject(slotsName[i]);

			/*If it is a primitive create the expression with getOWLDataHasValue according to the type of that primitive,
			  then add it to the set of expression that the startEvent  will be subclass of */
			if (absObj instanceof AbsPrimitive) {

				classExpr = getDataPropertyClassExpression(slotsName[i], (AbsPrimitive)absObj);
				classExprSet.add(classExpr);

			}

			if (absObj instanceof AbsPrimitiveSlotsHolder) {

				IndivAsclassExpr = getSlotHolderAsClassExpr((AbsPrimitiveSlotsHolder)absObj);
				classExpr = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty( IRI.create(slotsName[i])), IndivAsclassExpr);
				classExprSet.add(classExpr);
			}

		}

		manager.addAxiom(stateOnto, dataFactory.getOWLEquivalentClassesAxiom(startEventClass, dataFactory.getOWLObjectIntersectionOf(classExprSet)));		
		
	}
	


	/**
	 * Axiom Canceled Obl-n:
	 * {obl-n} ��� ���evBefore.(EndEvent-n ��� ���atTime.Elapsed) ��� Cancelled
	 */
	private void create_Cancel_monitoring_Axiom(OWLNamedIndividual obl_n, int oblNum) {
		
		//Some Event before a passed StartEvent_n = some evBefore (EndEvent_n && somevalueFrom(atTime.Elapsed))
		
		OWLObjectSomeValuesFrom some_atTimeElapsed = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(IRI.create(clOntoURL + "#atTime")), 
													   										        dataFactory.getOWLClass(IRI.create(oblOntoURL + "#ElapsedInstant")));
		OWLClass endEvent_n_Class = dataFactory.getOWLClass(":EndEvent_" + oblNum, pm);
				
		OWLObjectIntersectionOf endEvent_n_and_some_atTimeElapsed = dataFactory.getOWLObjectIntersectionOf(endEvent_n_Class, some_atTimeElapsed);
				
		OWLObjectSomeValuesFrom some_evBefore_endEvent_n_and_some_atTimeElapsed = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(IRI.create(oblOntoURL + "#evBefore")), 
																													 	 endEvent_n_and_some_atTimeElapsed);
		
		//Some Event at the same of a passed StartEvent_n = some evSameTime (startEvent_n && somevalueFrom(atTime.Elapsed))
		OWLObjectSomeValuesFrom some_evSameTime_endEvent_n_and_some_atTimeElapsed = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(IRI.create(oblOntoURL + "#evSameTime")), 
						 																									         endEvent_n_and_some_atTimeElapsed);
		
		//some evBefore (startEvent_n && somevalueFrom(atTime.Elapsed)) or some evSameTime (startEvent_n && somevalueFrom(atTime.Elapsed))
		OWLObjectUnionOf before_OR_sameTime_passed_endEvent_n = dataFactory.getOWLObjectUnionOf(some_evBefore_endEvent_n_and_some_atTimeElapsed, some_evSameTime_endEvent_n_and_some_atTimeElapsed);
		
		
		OWLObjectIntersectionOf obl_n_and_before_OR_sameTime_passed_endEvent_n = dataFactory.getOWLObjectIntersectionOf(dataFactory.getOWLObjectOneOf(obl_n),before_OR_sameTime_passed_endEvent_n);
		
		
		
		/*OWLObjectIntersectionOf obl_n_and_some_evBefore_endEvent_n_and_some_atTimeElapsed = dataFactory.getOWLObjectIntersectionOf(dataFactory.getOWLObjectOneOf(obl_n), 
																										                           some_evBefore_endEvent_n_and_some_atTimeElapsed);*/
		//Canceled Axiom: {obl-n} ��� ���evBefore.(EndEvent-n ��� ���atTime.Elapsed) ��� Cancelled
		/*OWLSubClassOfAxiom canceled_axiom = dataFactory.getOWLSubClassOfAxiom(obl_n_and_some_evBefore_endEvent_n_and_some_atTimeElapsed, 
																			  dataFactory.getOWLClass(IRI.create(oblOntoURL + "#Canceled")));*/
		
		OWLSubClassOfAxiom canceled_axiom = dataFactory.getOWLSubClassOfAxiom(obl_n_and_before_OR_sameTime_passed_endEvent_n, dataFactory.getOWLClass(IRI.create(oblOntoURL + "#Canceled")));
		
		manager.addAxiom(stateOnto, canceled_axiom);
		
		/*
		SWRLVariable oblVar             	= dataFactory.getSWRLVariable(IRI.create(pm.getDefaultPrefix() + "oblVar"));
		Set<SWRLAtom> antecedent = new HashSet<SWRLAtom>();
		antecedent.add(dataFactory.getSWRLClassAtom(obl_n_and_some_evBefore_endEvent_n_and_some_atTimeElapsed, oblVar));
		SWRLRule canceledRule = dataFactory.getSWRLRule(antecedent, Collections.singleton(dataFactory.getSWRLClassAtom(dataFactory.getOWLClass(IRI.create(oblOntoURL + "#Canceled")), oblVar)));//Elapsed(?inst)
		manager.applyChange(new AddAxiom(stateOnto, canceledRule));*/ 
		
	}
	
	private void createMonitoringGeneralClassAxioms(OWLNamedIndividual obl, String obl_name, int count) {
		
		create_activated_axiom(obl, obl_name, count);
		create_violated_axiom(obl, obl_name, count);
		
		//TODO Fulfilled
		
	}

	private void create_activated_axiom(OWLNamedIndividual obl, String obl_name, int count) {
		
		//{obl_n}
		OWLNamedIndividual obl_n = dataFactory.getOWLNamedIndividual(":" + obl_name, pm);
		
		//not Kcancelled
		OWLObjectComplementOf not_KCancelled = dataFactory.getOWLObjectComplementOf(dataFactory.getOWLClass(IRI.create(oblOntoURL + "#KCanceled")));
		
		
		//Some Event before a passed StartEvent_n = some evBefore (startEvent_n && somevalueFrom(atTime.Elapsed))
		
		OWLObjectSomeValuesFrom some_atTimeElapsed = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(IRI.create(clOntoURL + "#atTime")), 
											   										        dataFactory.getOWLClass(IRI.create(oblOntoURL + "#ElapsedInstant")));
		OWLClass startEvent_n_Class = dataFactory.getOWLClass(":StartEvent_" + count, pm);
		
		OWLObjectIntersectionOf startEvent_n_and_some_atTimeElapsed = dataFactory.getOWLObjectIntersectionOf(startEvent_n_Class, some_atTimeElapsed);
		
		OWLObjectSomeValuesFrom some_evBefore_startEvent_n_and_some_atTimeElapsed = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(IRI.create(oblOntoURL + "#evBefore")), 
																											 			   startEvent_n_and_some_atTimeElapsed);
		
		//Some Event at the same of a passed StartEvent_n = some evSameTime (startEvent_n && somevalueFrom(atTime.Elapsed))
		OWLObjectSomeValuesFrom some_evSameTime_startEvent_n_and_some_atTimeElapsed = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(IRI.create(oblOntoURL + "#evSameTime")), 
				 																									         startEvent_n_and_some_atTimeElapsed);
		
		//some evBefore (startEvent_n && somevalueFrom(atTime.Elapsed)) or some evSameTime (startEvent_n && somevalueFrom(atTime.Elapsed))
		OWLObjectUnionOf before_OR_sameTime_passed_StartEvent_n = dataFactory.getOWLObjectUnionOf(some_evBefore_startEvent_n_and_some_atTimeElapsed, some_evSameTime_startEvent_n_and_some_atTimeElapsed);
		
		//{obl_n} && not Kcancelled && ( some evBefore (startEvent_n && somevalueFrom(atTime.Elapsed)) or some evSameTime (startEvent_n && somevalueFrom(atTime.Elapsed)) )
		OWLObjectIntersectionOf obl_n_and_not_KCancelled_and_before_OR_sameTime_passed_StartEvent_n = dataFactory.getOWLObjectIntersectionOf(dataFactory.getOWLObjectOneOf(obl_n),not_KCancelled, before_OR_sameTime_passed_StartEvent_n );
		
		//Activated Axiom
		OWLSubClassOfAxiom activated_axiom = dataFactory.getOWLSubClassOfAxiom(obl_n_and_not_KCancelled_and_before_OR_sameTime_passed_StartEvent_n, dataFactory.getOWLClass(IRI.create(oblOntoURL + "#Activated")));
		
	
		manager.addAxiom(stateOnto, activated_axiom);
	}

	private void create_violated_axiom(OWLNamedIndividual obl, String obl_name, int count) {
		
		//{obl_n}
		OWLNamedIndividual obl_n = dataFactory.getOWLNamedIndividual(":" + obl_name, pm);
		
		//not Kcancelled
		OWLObjectComplementOf not_KFulfilled = dataFactory.getOWLObjectComplementOf(dataFactory.getOWLClass(IRI.create(oblOntoURL + "#KFulfilled")));
		
		//Activated
		OWLClass activated = dataFactory.getOWLClass(IRI.create(oblOntoURL + "#Activated"));
		
		
		
		//Some Event before a passed StartEvent_n = some evBefore (startEvent_n && somevalueFrom(atTime.Elapsed))
		
		OWLObjectSomeValuesFrom some_atTimeElapsed = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(IRI.create(clOntoURL + "#atTime")), 
											   										        dataFactory.getOWLClass(IRI.create(oblOntoURL + "#ElapsedInstant")));
		//Deadline_n
		OWLClass deadline_n_class = dataFactory.getOWLClass(":Deadline_" + count, pm);
		
		OWLObjectIntersectionOf deadline_n_and_some_atTimeElapsed = dataFactory.getOWLObjectIntersectionOf(deadline_n_class, some_atTimeElapsed);
		
		OWLObjectSomeValuesFrom some_evBefore_deadline_n_and_some_atTimeElapsed = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(IRI.create(oblOntoURL + "#evBefore")), 
																														   deadline_n_and_some_atTimeElapsed);

		
		//{obl_n} && not Kcancelled && ( some evBefore (startEvent_n && somevalueFrom(atTime.Elapsed)) or some evSameTime (startEvent_n && somevalueFrom(atTime.Elapsed)) )
		OWLObjectIntersectionOf obl_n_and_Activated_not_KFulfilled_and_before_passed_Deadline_n = dataFactory.getOWLObjectIntersectionOf(dataFactory.getOWLObjectOneOf(obl_n), activated, 
																																			 not_KFulfilled, some_evBefore_deadline_n_and_some_atTimeElapsed);
		
		//Activated Axiom
		OWLSubClassOfAxiom violated_axiom = dataFactory.getOWLSubClassOfAxiom(obl_n_and_Activated_not_KFulfilled_and_before_passed_Deadline_n, dataFactory.getOWLClass(IRI.create(oblOntoURL + "#Violated")));
		
	
		manager.addAxiom(stateOnto, violated_axiom);
	}
	
	/**
	 * Update all the KClass: 
	 * 
	 * 	-KActivated
	 *  -KCanceled
	 *  -KViolated
	 *  -KFulfilled
	 */
	private void UpdateKClasses() {
		
		updateKClass("KCanceled", "Canceled");
		updateKClass("KActivated", "Activated");
		updateKClass("KFulfilled", "Fulfilled");
		updateKClass("KViolated", "Violated");
		
	}
	
	/**
	 * Replace the know instance of a class with the new set of know instance
	 * Problem: the update is done any time regardless of if there is a change in the set or not.
	 * A comparison of the set could be made, but it might just had more cost. 
	 * As of now we just replace everytime.
	 * 
	 * TODO: ??? Optimize the update process ???
	 * 
	 * @param kClassName
	 */
	private void updateKClass(String kClassName, String ClassName) {
		
		OWLClass theClass = dataFactory.getOWLClass(IRI.create(oblOntoURL + "#" + ClassName));
		OWLClass kClass   = dataFactory.getOWLClass(IRI.create(oblOntoURL + "#" + kClassName));
		
		
		Set<OWLNamedIndividual> instancesSet 			= reasoner.getInstances(theClass, false).getFlattened();
		Set<OWLEquivalentClassesAxiom> kClassEquivalent = stateOnto.getEquivalentClassesAxioms(kClass);
		manager.removeAxioms(stateOnto, kClassEquivalent);
		
		if (instancesSet.isEmpty())
			manager.addAxiom(stateOnto, dataFactory.getOWLEquivalentClassesAxiom(kClass, dataFactory.getOWLNothing()));
		else
			manager.addAxiom(stateOnto, dataFactory.getOWLEquivalentClassesAxiom(kClass, dataFactory.getOWLObjectOneOf(instancesSet)));
			
	}
	
	
	/**
	 * Helper function to Retrieve the asserted object property value of a given object property for a specific Individual
	 * 
	 * @param indiv
	 * @param prop
	 * @return
	 */
	protected OWLIndividual getAssertedObjectPropertyValue(OWLNamedIndividual indiv, OWLObjectProperty prop){
		
		Set<OWLObjectPropertyAssertionAxiom> axioms = stateOnto.getObjectPropertyAssertionAxioms(indiv);
		
		for (OWLObjectPropertyAssertionAxiom owlObjectPropertyAssertionAxiom : axioms) {
			
			if (owlObjectPropertyAssertionAxiom.getProperty().equals(prop))
				return owlObjectPropertyAssertionAxiom.getObject();
			
		}
		
		return null;
	}
	
	/**
	 * Helper function to Retrieve the asserted data property value of a given data property for a specific Individual
	 * 
	 * @param indiv
	 * @param prop
	 * @return 
	 */
	protected OWLLiteral getAssertedDataPropertyValue(OWLNamedIndividual indiv, OWLDataProperty pro){
		
		Set<OWLDataPropertyAssertionAxiom> axioms = stateOnto.getDataPropertyAssertionAxioms(indiv);
		
		for (OWLDataPropertyAssertionAxiom owlDataPropertyAssertionAxiom : axioms) {
			
			if (owlDataPropertyAssertionAxiom.getProperty().equals(pro))
				return owlDataPropertyAssertionAxiom.getObject();
			
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param absPrimSlotHolder
	 * @return
	 */
	private OWLClassExpression getSlotHolderAsClassExpr(AbsPrimitiveSlotsHolder absPrimSlotHolder) {
		
		Set<OWLClassExpression> classExprSet  = new HashSet<OWLClassExpression>();
		String[] slotsName 					  = absPrimSlotHolder.getNames();
		AbsObject absObject                   = null;
		OWLClassExpression classExpr          = null;
		OWLClassExpression IndivAsclassExpr   = null;
		
		for (int i = 0; i < slotsName.length; i++) {
			
			absObject = absPrimSlotHolder.getAbsObject(slotsName[i]);
			
			
			if (absObject instanceof AbsPrimitive) {
				
				classExpr = getDataPropertyClassExpression(slotsName[i], (AbsPrimitive)absObject);
				classExprSet.add(classExpr);
				
			}
			
			if (absObject instanceof AbsPrimitiveSlotsHolder) {
				
				IndivAsclassExpr = getSlotHolderAsClassExpr((AbsPrimitiveSlotsHolder)absObject);
				classExpr = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty( IRI.create(slotsName[i])), IndivAsclassExpr);
				classExprSet.add(classExpr);
				
			}
			
		}
		
		return classExprSet.size() > 1 ? dataFactory.getOWLObjectIntersectionOf(classExprSet) : (OWLClassExpression) classExprSet.toArray()[0];
	}	
	
	/**
	 * Get the OWLDataHasValue class expression based the AbsPrimitive value
	 * Special case for Double and flot: Need to convert them to decimal type some times. 
	 * This is because, decimal type do not exist in java and we approximate it with double or float in the java ontology representation.
	 * TODO Handle more than string and integer
	 * @param slotName
	 * @param absPrim
	 * @return
	 */
	private OWLClassExpression getDataPropertyClassExpression(String slotName, AbsPrimitive absPrim) {

		OWLDatatype decimalType = dataFactory.getOWLDatatype(IRI.create("http://www.w3.org/2001/XMLSchema#decimal"));
		Object value            = absPrim.getObject();

		if (value instanceof Integer)
			return dataFactory.getOWLDataHasValue(dataFactory.getOWLDataProperty( IRI.create(slotName) ), 
					dataFactory.getOWLLiteral(  ((Integer)value) )  
			);

		if (value instanceof Float) {
			/*Set<OWLDataRange> dataRanges = dataFactory.getOWLDataProperty( IRI.create(slotName) ).getRanges(stateOnto.getImportsClosure());
			//Not safe but work at this point TODO make the retreiving of the OWLDataType safe :p
			OWLDatatype datatype = dataRanges.iterator().next().asOWLDatatype();
			 
			if (datatype.toString().equalsIgnoreCase("xsd:decimal"))
				return dataFactory.getOWLDataHasValue(dataFactory.getOWLDataProperty( IRI.create(slotName) ), 
					dataFactory.getOWLLiteral(  ((Float)value).toString() , decimalType) );
			else*/
				return dataFactory.getOWLDataHasValue(dataFactory.getOWLDataProperty( IRI.create(slotName) ), 
						dataFactory.getOWLLiteral(  ((Float)value)) );
			
		}
		
		if (value instanceof Double) {
			/*Set<OWLDataRange> dataRanges = dataFactory.getOWLDataProperty( IRI.create(slotName) ).getRanges(stateOnto.getImportsClosure());
			//Not safe but work at this point TODO make the retreiving of the OWLDataType safe :p
			OWLDatatype datatype = dataRanges.iterator().next().asOWLDatatype();
			 
			if (datatype.toString().equalsIgnoreCase("xsd:decimal"))
				return dataFactory.getOWLDataHasValue(dataFactory.getOWLDataProperty( IRI.create(slotName) ), 
					dataFactory.getOWLLiteral(  ((Double)value).toString() , decimalType) );
			else*/
				return dataFactory.getOWLDataHasValue(dataFactory.getOWLDataProperty( IRI.create(slotName) ), 
						dataFactory.getOWLLiteral(  ((Double)value)) );
		}

		if (value instanceof String)
			return dataFactory.getOWLDataHasValue(dataFactory.getOWLDataProperty( IRI.create(slotName) ), 
					dataFactory.getOWLLiteral(  (String)value )  
			);
		
		if (value instanceof Date) {
			
			SimpleDateFormat  simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			DateTime          dateTime          = new DateTime(simpleDateFormat.format((Date)value));
			DateTimeFormatter fmt               = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");
			
			return dataFactory.getOWLDataHasValue(dataFactory.getOWLDataProperty( IRI.create(slotName) ), 
					dataFactory.getOWLLiteral(  dateTime.toString(fmt), this.dateTimeDatatype )  
			);
		}
		
		System.out.println("Oops return null in getDataPropertyClassExpression");
		return null;
	}

	
	/**
	 * Check if a statement's set of axioms is contained
	 * 
	 * @return
	 */
	public synchronized boolean containAxioms(OWLAxiom [] owlAxiomlist) {
		
		boolean isContained = false;
		
		for (OWLAxiom owlAxiom : owlAxiomlist) {
			//Logger logger = LoggerFactory.getLogger("OIS");
			//logger.info(owlAxiom.toString());
			for (OWLOntology ont : manager.getImportsClosure(stateOnto)) {
				if (ont.containsAxiom(owlAxiom)) {
					isContained = true;
				}	
			}
			if (isContained == false)
				return false;
		}
		
		return isContained;
	}

}
