package ch.usi.ict.dev.swrlTabTemporalBuiltinPelletPlugin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredClassAssertionAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredPropertyAssertionGenerator;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;

import uk.ac.manchester.cs.owlapi.dlsyntax.DLSyntaxObjectRenderer;

//import com.clarkparsia.owlapi.SWRL;
import com.clarkparsia.owlapi.explanation.PelletExplanation;
import com.clarkparsia.owlapi.explanation.io.manchester.ManchesterSyntaxExplanationRenderer;
import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import com.clarkparsia.pellet.rules.builtins.BuiltInRegistry;



public class MainTest {

	   //a simple example ontology 
    private static final String DOC_URL = "http://www.people.lu.unisi.ch/okouyad/stateOntology.owl"; 
 
    public static void main(String[] args) throws OWLOntologyCreationException { 

        //register my built-in implementation 
        BuiltInRegistry.instance.registerBuiltIn("http://swrl.stanford.edu/ontologies/built-ins/3.3/temporal.owl#add", new CustomSWRLBuiltin(new add())); 
        BuiltInRegistry.instance.registerBuiltIn("http://swrl.stanford.edu/ontologies/built-ins/3.3/temporal.owl#before", new CustomSWRLBuiltin(new before())); 
        BuiltInRegistry.instance.registerBuiltIn("http://swrl.stanford.edu/ontologies/built-ins/3.3/temporal.owl#notBefore", new CustomSWRLBuiltin(new notBefore())); 
        BuiltInRegistry.instance.registerBuiltIn("http://swrl.stanford.edu/ontologies/built-ins/3.3/temporal.owl#equals", new CustomSWRLBuiltin(new equals())); 

        //initialize ontology and reasoner and explanation
        
		PelletExplanation.setup();

        ClassLoader cl = MainTest.class.getClassLoader();

        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        try {
            manager.addIRIMapper(new SimpleIRIMapper(IRI.create("http://www.people.lu.unisi.ch/okouyad/stateOntology.owl"),
                    IRI.create(cl.getResource("reasoningOntos/stateOntology.owl"))));

            manager.addIRIMapper(new SimpleIRIMapper(IRI.create("http://www.people.lu.unisi.ch/okouyad/OblOntology.owl"),
                    IRI.create(cl.getResource("reasoningOntos/OblOntology.owl"))));

            manager.addIRIMapper(new SimpleIRIMapper(IRI.create("http://www.people.lu.unisi.ch/okouyad/PayDeliveryOntology.owl"),
                    IRI.create(cl.getResource("reasoningOntos/PayDeliveryOntology.owl"))));

            manager.addIRIMapper(new SimpleIRIMapper(IRI.create("http://www.people.lu.unisi.ch/okouyad/OceanCL.owl"),
                    IRI.create(cl.getResource("reasoningOntos/OceanCL.owl"))));

            manager.addIRIMapper(new SimpleIRIMapper(IRI.create("http://swrl.stanford.edu/ontologies/built-ins/3.3/temporal.owl"),
                    IRI.create(cl.getResource("reasoningOntos/temporal.owl"))));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        

        OWLOntology ontology = manager.loadOntology(IRI.create(DOC_URL)); 
        OWLDataFactory factory = manager.getOWLDataFactory(); 
        PrefixOWLOntologyFormat pm = manager.getOntologyFormat(ontology).asPrefixOWLOntologyFormat();
        
        //Code Specific to OWL-API with Pellet. I don't use the generic OWLReasonerFactory and OWLReasoner.
        PelletReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance(); 
        PelletReasoner reasoner = reasonerFactory.createNonBufferingReasoner(ontology, new SimpleConfiguration());
        manager.addOntologyChangeListener(reasoner);
        //reasoner.getKB().realize();
        //reasoner.getKB().printClassTree();
        
        //listSWRLRules(ontology, pm);
        //System.out.println("\n\nIs the Ontology consistent 0:" + (reasoner.isConsistent()) + "\n\n");
         
        
        try {
        	
        	// The renderer is used to pretty print explanation
         	ManchesterSyntaxExplanationRenderer renderer = new ManchesterSyntaxExplanationRenderer();
         	// The writer used for the explanation rendered
         	PrintWriter out = new PrintWriter( System.out );
            renderer.startRendering( out );

        	boolean isConsistent = false;
        	
        	PelletExplanation expGen = new PelletExplanation(reasoner);
        	
        	
        	System.out.println("\n\nIs the Ontology consistent 0:" + (isConsistent  = reasoner.isConsistent()) + "\n\n");
        	System.out.println();
        	if (!isConsistent)
        		renderer.render( expGen.getInconsistencyExplanations());

        	System.out.println("Sleeping");
        	Thread.sleep(3000);
        	System.out.println("weaking up");
        	
        	
        	OWLNamedIndividual            validInstantIndiv  = factory.getOWLNamedIndividual(":Instant_" + "2012-08-25T11:55:00+02:00", pm);
    		
    		if (reasoner.getTypes(validInstantIndiv, true).isEmpty()) { //help checking if the individual exist already
    			
    			OWLDatatype dateTimeDatatype = factory.getOWLDatatype(IRI.create("http://www.w3.org/2001/XMLSchema#dateTime"));
    			OWLLiteral                    dateTimeAsOwlLitteral = factory.getOWLLiteral("2001-10-26T19:32:52+00:00", dateTimeDatatype);
    			OWLDataProperty               hasTime               = factory.getOWLDataProperty((IRI.create("http://swrl.stanford.edu/ontologies/built-ins/3.3/temporal.owl" + "#hasTime")));
    			OWLDataPropertyAssertionAxiom dataAssertionAxiom    = factory.getOWLDataPropertyAssertionAxiom(hasTime, validInstantIndiv, dateTimeAsOwlLitteral);	
    			manager.addAxiom(ontology, dataAssertionAxiom);
    			
    		}
        	
        	
        	savedGeneratedAxiomInOnto(reasoner, manager, ontology);

        	System.out.println("\n\nAfter saving the inferred axioms, is the Ontology consistent:" + (isConsistent  = reasoner.isConsistent()) + "\n\n");
        	System.out.println();
        	if (!isConsistent)
        		renderer.render( expGen.getInconsistencyExplanations());
        	
        	System.out.println("Sleeping");
        	Thread.sleep(3000);
        	System.out.println("weaking up");
        	

        	/*secondStartEventTest(reasoner, manager, ontology, factory, pm);
        	
        	System.out.println("\n\nIs the Ontology consistent 2:" + (isConsistent  = reasoner.isConsistent()) + "\n\n");

        	Thread.sleep(3000);
        	
        	savedGeneratedAxiomInOnto(reasoner, manager, ontology);
        	
        	if (!isConsistent)
        		renderer.render( expGen.getInconsistencyExplanations());*/
        	
//        	savedGeneratedAxiomInOnto(reasoner, manager, ontology);
//
//        	System.out.println("\n\nIs the Ontology consistent 3:" + (isConsistent  = reasoner.isConsistent()) + "\n\n");
//        	System.out.println();
//        	if (!isConsistent)
//        		renderer.render( expGen.getInconsistencyExplanations());
        	
        	renderer.endRendering();

        } catch (org.mindswap.pellet.exceptions.InconsistentOntologyException e) {
        	System.out.println(e.getMessage());
        }
        catch (org.semanticweb.owlapi.reasoner.InconsistentOntologyException e) {
        	System.out.println(e.getMessage());
        } catch (UnsupportedOperationException e) {
        	e.printStackTrace();
        } catch (OWLException e) {
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        } catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        
        try {
        	File file = new File(cl.getResource("reasoningOntos/stateOntology.owl").toURI());
			manager.saveOntology(ontology, IRI.create(file));			
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
            e.printStackTrace();
        }

    } 
 


    public static void savedGeneratedAxiomInOnto(OWLReasoner reasoner, OWLOntologyManager manager, OWLOntology ontology) {
    	
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
    
   
/*    public static void checkObligationStatus(OWLReasoner reasoner, OWLOntologyManager manager, OWLOntology ontology, 
    									     OWLDataFactory factory, PrefixOWLOntologyFormat pm){

    	reasoner.precomputeInferences();

    	OWLClass elapsed = factory.getOWLClass("OblOntology:Activated", pm);
    	NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(elapsed, true);
    	Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();
    	System.out.println("Instances of elapsed: ");
    	for(OWLNamedIndividual ind : individuals) {
    		System.out.println("    " + ind);
    	}
    	System.out.println("\n");

    }*/
    
    public static void secondStartEventTest(OWLReasoner reasoner, OWLOntologyManager manager, OWLOntology ontology, 
    										OWLDataFactory factory, PrefixOWLOntologyFormat pm) {
    	
    	OWLNamedIndividual deliver1 = factory.getOWLNamedIndividual(":deliver1", pm);
    	OWLClass deliverClass = factory.getOWLClass("PayDeliveryOntology:Deliver", pm);
    	OWLClassAssertionAxiom deliver1_deliverClass_assertion = factory.getOWLClassAssertionAxiom(deliverClass, deliver1);//Deliver(deliver1)
    	manager.addAxiom(ontology, deliver1_deliverClass_assertion);
    	
    	OWLNamedIndividual mary = factory.getOWLNamedIndividual(":mary", pm);
    	OWLObjectProperty hasActorProperty = factory.getOWLObjectProperty("OceanCL:hasActor", pm);
    	OWLObjectPropertyAssertionAxiom deliver1_hasActor_assertion = factory.getOWLObjectPropertyAssertionAxiom(hasActorProperty, deliver1, mary);//hasActor(deliver1, mary)
    	manager.addAxiom(ontology, deliver1_hasActor_assertion);
    	
    	OWLNamedIndividual john = factory.getOWLNamedIndividual(":john", pm);
    	OWLObjectProperty hasDelivereeProperty = factory.getOWLObjectProperty("PayDeliveryOntology:hasDeliveree", pm);
    	OWLObjectPropertyAssertionAxiom deliver1_hasDeliveree_assertion = factory.getOWLObjectPropertyAssertionAxiom(hasDelivereeProperty, deliver1, john);//hasActor(deliver1, mary)
    	manager.addAxiom(ontology, deliver1_hasDeliveree_assertion);
    	
    	OWLNamedIndividual lord_of_the_ring = factory.getOWLNamedIndividual(":lord_of_the_ring", pm);
    	OWLObjectProperty hasObjectProperty = factory.getOWLObjectProperty("PayDeliveryOntology:hasObject", pm);
    	OWLObjectPropertyAssertionAxiom deliver1_hasObject_assertion = factory.getOWLObjectPropertyAssertionAxiom(hasObjectProperty, deliver1, lord_of_the_ring);//hasObject(deliver1, lord_of_the_ring)
    	manager.addAxiom(ontology, deliver1_hasObject_assertion);
    	
    	OWLNamedIndividual deliver1_inst = factory.getOWLNamedIndividual(":deliver1_inst", pm);
    	OWLObjectProperty atTimeProperty = factory.getOWLObjectProperty("OceanCL:atTime", pm);
    	OWLObjectPropertyAssertionAxiom deliver1_atTime_assertion = factory.getOWLObjectPropertyAssertionAxiom(atTimeProperty, deliver1, deliver1_inst);//hasObject(deliver1, lord_of_the_ring)
    	manager.addAxiom(ontology, deliver1_atTime_assertion);
    	
    	OWLDataProperty hasTimeProperty = factory.getOWLDataProperty("temporal:hasTime", pm);
    	OWLDataPropertyAssertionAxiom hasTimeProperty_assertion = factory.getOWLDataPropertyAssertionAxiom(hasTimeProperty, deliver1_inst, factory.getOWLLiteral("2012-04-24T15:45:00", factory.getOWLDatatype("xsd:dateTime", pm)));
    	manager.addAxiom(ontology, hasTimeProperty_assertion);
    	
    	
    }
    
    
    private static void listClass(OWLNamedIndividual futurdate, OWLReasoner reasoner) {
    	
    	System.out.println(futurdate.toString() + " is of type: ");
    	NodeSet<OWLClass> classNodeSet = reasoner.getTypes(futurdate, true);
    	
        Set<OWLClass> classes          = classNodeSet.getFlattened();
        for (OWLClass owlClass : classes) {
			System.out.println(owlClass.toString());
		}
        System.out.println("\n");
		
	}
    
    
    public static void listSWRLRules(OWLOntology ontology, PrefixOWLOntologyFormat pm) { 
        OWLObjectRenderer renderer = new DLSyntaxObjectRenderer(); 
        for (SWRLRule rule : ontology.getAxioms(AxiomType.SWRL_RULE)) { 
            System.out.println(renderer.render(rule)); 
        }  
        System.out.println("\n");
    } 
 
    public static void listAllDataPropertyValues(OWLNamedIndividual individual, OWLOntology ontology, OWLReasoner reasoner) { 
        OWLObjectRenderer renderer = new DLSyntaxObjectRenderer(); 
        Map<OWLDataPropertyExpression, Set<OWLLiteral>> assertedValues = individual.getDataPropertyValues(ontology); 
        for (OWLDataProperty dataProp : ontology.getDataPropertiesInSignature(true)) { 
        	//System.out.println(dataProp);
            for (OWLLiteral literal : reasoner.getDataPropertyValues(individual, dataProp)) {
            	
                Set<OWLLiteral> literalSet = assertedValues.get(dataProp); 
                boolean asserted = (literalSet != null && literalSet.contains(literal)); 
                System.out.println((asserted ? "asserted" : "inferred") + " data property for " + renderer.render(individual) + " : " 
                        + renderer.render(dataProp) + " -> " + renderer.render(literal)); 
            } 
        } 
    }

}
