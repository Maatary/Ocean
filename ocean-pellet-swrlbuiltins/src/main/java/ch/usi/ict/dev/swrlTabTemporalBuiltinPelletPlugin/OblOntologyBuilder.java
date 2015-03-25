package ch.usi.ict.dev.swrlTabTemporalBuiltinPelletPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLDArgument;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;

import uk.ac.manchester.cs.owlapi.dlsyntax.DLSyntaxObjectRenderer;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import com.clarkparsia.pellet.rules.builtins.BuiltInRegistry;



/***
 * 
 * This class is used to complete the creation of the obligation ontology
 * with rules that can't be added with the Protege owl editor. 
 * That is rules that contain custom bultins such as those fromt he temporal ontology: add, before, equals 
 * 
 * 
 * @author maatari
 *
 */
public class OblOntologyBuilder {
	
	
	//a simple example ontology 
    private static final String DOC_URL = "http://www.people.lu.unisi.ch/okouyad/OblOntology.owl"; 

	/**
	 * @param args
	 * @throws OWLOntologyCreationException 
	 */
	public static void main(String[] args) throws OWLOntologyCreationException {
		//register my built-in implementation 
        BuiltInRegistry.instance.registerBuiltIn("http://swrl.stanford.edu/ontologies/built-ins/3.3/temporal.owl#add", new CustomSWRLBuiltin(new add())); 
        BuiltInRegistry.instance.registerBuiltIn("http://swrl.stanford.edu/ontologies/built-ins/3.3/temporal.owl#before", new CustomSWRLBuiltin(new before()));
        BuiltInRegistry.instance.registerBuiltIn("http://swrl.stanford.edu/ontologies/built-ins/3.3/temporal.owl#notBefore", new CustomSWRLBuiltin(new notBefore())); 
        BuiltInRegistry.instance.registerBuiltIn("http://swrl.stanford.edu/ontologies/built-ins/3.3/temporal.owl#equals", new CustomSWRLBuiltin(new equals())); 

        //initialize ontology and reasoner 
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager(); 
        
        manager.addIRIMapper(new SimpleIRIMapper(IRI.create("http://www.people.lu.unisi.ch/okouyad/OblOntology.owl"), 
        										 IRI.create(new File("skeletonOntos/OblOntology.owl"))));
        manager.addIRIMapper(new SimpleIRIMapper(IRI.create("http://www.people.lu.unisi.ch/okouyad/OceanCL.owl"), 
				 								 IRI.create(new File("skeletonOntos/OceanCL.owl"))));
        manager.addIRIMapper(new SimpleIRIMapper(IRI.create("http://swrl.stanford.edu/ontologies/built-ins/3.3/temporal.owl"), 
												 IRI.create(new File("skeletonOntos/temporal.owl"))));
        
        OWLOntology ontology = manager.loadOntology(IRI.create(DOC_URL)); 
        
        
        OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance(); 
        //OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, new SimpleConfiguration()); 
        OWLReasoner reasoner = reasonerFactory.createNonBufferingReasoner(ontology, new SimpleConfiguration());
        
        
        
        OWLDataFactory factory = manager.getOWLDataFactory(); 
        PrefixOWLOntologyFormat pm = manager.getOntologyFormat(ontology).asPrefixOWLOntologyFormat(); 
        
        
        //use the rule with the built-in to infer property values 
        //OWLNamedIndividual futurdate = factory.getOWLNamedIndividual(":futurdate", pm); 
        
        
        
        
        //createValidPeriodFinishTimeCalculationRule(manager, ontology, pm);
        createElapsedRule(manager, ontology, pm);
        createElapsedConsistencyRule(manager, ontology, pm);
        createBeforeRule(manager, ontology, pm);
        //createEqualInstantsRule(manager, ontology, pm);
        
        listSWRLRules(ontology, pm);         
        
        reasoner.flush();
        
        System.out.println("Is consistent: " + reasoner.isConsistent());
        
       
        
       
     
        try {
        	
        	File file = new File("reasoningOntos/OblOntology.owl");
			manager.saveOntology(ontology, IRI.create(file));
			
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}

	}


/*	*//**
	 * ValidPeriod(?p) ^ hasStartTime(?p, ?t1) ^ hasDuration(?p, ?dur) ^ hasCount(?dur, ?count) ^ hasGranularity(?dur, ?gran) 
	 * ^ temporal:add(?t2, ?t1, ?count, ?gran) - > hasFinishTime(?p, ?t2)
	 * 
	 * @param manager
	 * @param ontology
	 * @param pm
	 *//*
	private static void createValidPeriodFinishTimeCalculationRule(OWLOntologyManager manager, OWLOntology ontology, PrefixOWLOntologyFormat pm) {
		OWLDataFactory factory          	    = manager.getOWLDataFactory();
		
		SWRLVariable periodVar           		= factory.getSWRLVariable(IRI.create(pm.getDefaultPrefix() + "p"));
		SWRLVariable startTimeVar           	= factory.getSWRLVariable(IRI.create(pm.getDefaultPrefix() + "t1"));
		SWRLVariable finishTimeVar           	= factory.getSWRLVariable(IRI.create(pm.getDefaultPrefix() + "t2"));
		SWRLVariable durVar           			= factory.getSWRLVariable(IRI.create(pm.getDefaultPrefix() + "dur"));
		SWRLVariable countVar           		= factory.getSWRLVariable(IRI.create(pm.getDefaultPrefix() + "count"));
		SWRLVariable granVar           			= factory.getSWRLVariable(IRI.create(pm.getDefaultPrefix() + "granularity"));

		
		OWLClass validPeriodClass      	        	= factory.getOWLClass("temporal:ValidPeriod", pm);
		OWLDataProperty hasStartTimeProperty 		= factory.getOWLDataProperty("temporal:hasStartTime", pm);
		OWLDataProperty hasFinishTimeProperty 		= factory.getOWLDataProperty("temporal:hasFinishTime", pm); 
		OWLObjectProperty hasDurationProperty 		= factory.getOWLObjectProperty("OceanCL:hasDuration", pm); 
		OWLDataProperty   hascountProperty 	    	= factory.getOWLDataProperty("temporal:hasCount", pm); 
		OWLObjectProperty hasGranularityProperty 	= factory.getOWLObjectProperty("temporal:hasGranularity", pm); 

		//Rule construction.

		Set<SWRLAtom> antecedent = new HashSet<SWRLAtom>();
		
		antecedent.add(factory.getSWRLClassAtom(validPeriodClass, periodVar));
		antecedent.add(factory.getSWRLDataPropertyAtom(hasStartTimeProperty, periodVar, startTimeVar));
		antecedent.add(factory.getSWRLObjectPropertyAtom(hasDurationProperty, periodVar, durVar));
		antecedent.add(factory.getSWRLDataPropertyAtom(hascountProperty, durVar, countVar));
		antecedent.add(factory.getSWRLObjectPropertyAtom(hasGranularityProperty, durVar, granVar));
		
		List<SWRLDArgument>  builtargsList =  new ArrayList<SWRLDArgument>();
		builtargsList.add(finishTimeVar);
		builtargsList.add(startTimeVar);
		builtargsList.add(countVar);
		builtargsList.add(granVar);
		antecedent.add(factory.getSWRLBuiltInAtom(IRI.create(pm.getPrefix("temporal:") + "add"), builtargsList));

		SWRLRule validPeriodFinishTimeCalculationRule = factory.getSWRLRule(antecedent, Collections.singleton(factory.getSWRLDataPropertyAtom(hasFinishTimeProperty, periodVar, finishTimeVar)));

		manager.applyChange(new AddAxiom(ontology, validPeriodFinishTimeCalculationRule)); 

	}*/


	public static void listSWRLRules(OWLOntology ontology, PrefixOWLOntologyFormat pm) { 
        OWLObjectRenderer renderer = new DLSyntaxObjectRenderer(); 
        for (SWRLRule rule : ontology.getAxioms(AxiomType.SWRL_RULE)) { 
            System.out.println(renderer.render(rule)); 
        }  
        System.out.println("\n");
    }


	/**
	 * ValidInstant(?inst) ^ hasTime(?inst, ?t) ^ before(?t, "now") -> OblOntology:Elapsed(?inst)
	 * 
	 * @param manager
	 * @param ontology
	 * @param pm
	 */
	public static void createElapsedRule(OWLOntologyManager manager, OWLOntology ontology, PrefixOWLOntologyFormat pm) {

		OWLDataFactory factory          	= manager.getOWLDataFactory();
		OWLClass ValidInstantClass      	= factory.getOWLClass("temporal:ValidInstant", pm);
		OWLClass ElaspedClass      		    = factory.getOWLClass(":ElapsedInstant", pm);
		OWLDataProperty hasTimeProperty 	= factory.getOWLDataProperty("temporal:hasTime", pm); 
		SWRLVariable instVar             	= factory.getSWRLVariable(IRI.create(pm.getDefaultPrefix() + "inst"));
		SWRLVariable timeVar            	= factory.getSWRLVariable(IRI.create(pm.getDefaultPrefix() + "t"));
		List<SWRLDArgument>  builtargsList  =  new ArrayList<SWRLDArgument>();


		//(?t,"now")
		builtargsList.add(timeVar);
		builtargsList.add(factory.getSWRLLiteralArgument(factory.getOWLLiteral("now")));


		Set<SWRLAtom> antecedent = new HashSet<SWRLAtom>();
		antecedent.add(factory.getSWRLClassAtom(ValidInstantClass, instVar));//ValidInstant(?inst)
		antecedent.add(factory.getSWRLDataPropertyAtom(hasTimeProperty, instVar, timeVar));//hasTime(?inst, ?t)
		antecedent.add(factory.getSWRLBuiltInAtom(IRI.create(pm.getPrefix("temporal:") + "before"), builtargsList));//before(?t, "now")


		SWRLRule elapsedTimeRule = factory.getSWRLRule(antecedent, Collections.singleton(factory.getSWRLClassAtom(ElaspedClass, instVar)));//Elapsed(?inst)

		manager.applyChange(new AddAxiom(ontology, elapsedTimeRule)); 
	}
	
	/**
	 * Elapsed(?inst) ^ hasTime(?inst, ?t) ^ notBefore(?t, "now") -> owl:Nothing(?inst)
	 * 
	 * @param manager
	 * @param ontology
	 * @param pm
	 */
	public static void createElapsedConsistencyRule(OWLOntologyManager manager, OWLOntology ontology, PrefixOWLOntologyFormat pm) {

		OWLDataFactory factory          	= manager.getOWLDataFactory();
		OWLClass NothingClass      		    = factory.getOWLClass("owl:Nothing", pm);
		OWLClass ElaspedClass      		    = factory.getOWLClass(":ElapsedInstant", pm);
		OWLDataProperty hasTimeProperty 	= factory.getOWLDataProperty("temporal:hasTime", pm); 
		SWRLVariable instVar                = factory.getSWRLVariable(IRI.create(pm.getDefaultPrefix() + "inst"));
		SWRLVariable timeVar            	= factory.getSWRLVariable(IRI.create(pm.getDefaultPrefix() + "t"));

		List<SWRLDArgument>  builtargsList  = new ArrayList<SWRLDArgument>();
		Set<SWRLAtom> antecedent            = new HashSet<SWRLAtom>();
		
		//Elapsed(?vt) ^ hasTime(?vt, ?t) ^ notBefore(?t, "now")		
		
		antecedent.add(factory.getSWRLClassAtom(ElaspedClass, instVar));//Elapsed(?vt)
		antecedent.add(factory.getSWRLDataPropertyAtom(hasTimeProperty, instVar, timeVar));//hasTime(?inst, ?t)
		
		builtargsList.add(timeVar);
		builtargsList.add(factory.getSWRLLiteralArgument(factory.getOWLLiteral("now")));
		antecedent.add(factory.getSWRLBuiltInAtom(IRI.create(pm.getPrefix("temporal:") + "notBefore"), builtargsList));//notBefore(?t, "now")

		//The rule
		SWRLRule elapsedConsistencyRule = factory.getSWRLRule(antecedent, Collections.singleton(factory.getSWRLClassAtom(NothingClass, instVar)));//owl:Nothing(?inst)

		manager.applyChange(new AddAxiom(ontology, elapsedConsistencyRule)); 
	}

	/**
	 * 
	 * ValidInstant(?inst1) ^ hasTime(?inst1, ?t1) ^ ValidInstant(?inst2) ^ hasTime(?inst2, ?t2) ^ before (?t1,?t2) -> OblOntology:before(?inst1, ?inst2)
	 * 
	 * @param manager
	 * @param ontology
	 * @param pm
	 */
	public static void createBeforeRule(OWLOntologyManager manager, OWLOntology ontology, PrefixOWLOntologyFormat pm) {

		OWLDataFactory factory          	= manager.getOWLDataFactory();
		OWLClass ValidInstantClass      	= factory.getOWLClass("temporal:ValidInstant", pm);
		OWLDataProperty hasTimeProperty 	= factory.getOWLDataProperty("temporal:hasTime", pm);
		OWLObjectProperty beforeProperty 	= factory.getOWLObjectProperty(":before", pm); 
		SWRLVariable instVar1           	= factory.getSWRLVariable(IRI.create(pm.getDefaultPrefix() + "inst1"));
		SWRLVariable instVar2           	= factory.getSWRLVariable(IRI.create(pm.getDefaultPrefix() + "inst2"));
		SWRLVariable timeVar1             	= factory.getSWRLVariable(IRI.create(pm.getDefaultPrefix() + "t1"));
		SWRLVariable timeVar2             	= factory.getSWRLVariable(IRI.create(pm.getDefaultPrefix() + "t2"));
		List<SWRLDArgument>  builtargsList  =  new ArrayList<SWRLDArgument>();


		//(?t1,?t2)
		builtargsList.add(timeVar1);
		builtargsList.add(timeVar2);


		Set<SWRLAtom> antecedent = new HashSet<SWRLAtom>();
		antecedent.add(factory.getSWRLClassAtom(ValidInstantClass, instVar1));//ValidInstant(?inst1)
		antecedent.add(factory.getSWRLDataPropertyAtom(hasTimeProperty, instVar1, timeVar1));//hasTime(?inst1, ?t1)
		antecedent.add(factory.getSWRLClassAtom(ValidInstantClass, instVar2));//ValidInstant(?inst2)
		antecedent.add(factory.getSWRLDataPropertyAtom(hasTimeProperty, instVar2, timeVar2));//hasTime(?inst2, ?t2)
		antecedent.add(factory.getSWRLBuiltInAtom(IRI.create(pm.getPrefix("temporal:") + "before"), builtargsList));//before(?t1,?t2)


		SWRLRule beforeRule = factory.getSWRLRule(antecedent, 
													   Collections.singleton(factory.getSWRLObjectPropertyAtom(beforeProperty, instVar1, instVar2)));//OblOntology:before(?inst1, ?inst2)

		manager.applyChange(new AddAxiom(ontology, beforeRule)); 
	}
	
	
	/**
	 * 
	 * ValidInstant(?inst1) ^ hasTime(?inst1, ?t1) ^ ValidInstant(?inst2) ^ hasTime(?inst2, ?t2) ^ equals(?t1,?t2) -> sameIndividual(?inst1, ?inst2)
	 * 
	 * @param manager
	 * @param ontology
	 * @param pm
	 */
/*	public static void createEqualInstantsRule(OWLOntologyManager manager, OWLOntology ontology, PrefixOWLOntologyFormat pm) {

		OWLDataFactory factory          	= manager.getOWLDataFactory();
		OWLClass ValidInstantClass      	= factory.getOWLClass("temporal:ValidInstant", pm);
		OWLDataProperty hasTimeProperty 	= factory.getOWLDataProperty("temporal:hasTime", pm);
		OWLObjectProperty beforeProperty 	= factory.getOWLObjectProperty(":before", pm); 
		SWRLVariable instVar1           	= factory.getSWRLVariable(IRI.create(pm.getDefaultPrefix() + "inst1"));
		SWRLVariable instVar2           	= factory.getSWRLVariable(IRI.create(pm.getDefaultPrefix() + "inst2"));
		SWRLVariable timeVar1             	= factory.getSWRLVariable(IRI.create(pm.getDefaultPrefix() + "t1"));
		SWRLVariable timeVar2             	= factory.getSWRLVariable(IRI.create(pm.getDefaultPrefix() + "t2"));
		List<SWRLDArgument>  builtargsList  =  new ArrayList<SWRLDArgument>();


		//(?t1,?t2)
		builtargsList.add(timeVar1);
		builtargsList.add(timeVar2);


		Set<SWRLAtom> antecedent = new HashSet<SWRLAtom>();
		antecedent.add(factory.getSWRLClassAtom(ValidInstantClass, instVar1));//ValidInstant(?inst1)
		antecedent.add(factory.getSWRLDataPropertyAtom(hasTimeProperty, instVar1, timeVar1));//hasTime(?inst1, ?t1)
		antecedent.add(factory.getSWRLClassAtom(ValidInstantClass, instVar2));//ValidInstant(?inst2)
		antecedent.add(factory.getSWRLDataPropertyAtom(hasTimeProperty, instVar2, timeVar2));//hasTime(?inst2, ?t2)
		antecedent.add(factory.getSWRLBuiltInAtom(IRI.create(pm.getPrefix("temporal:") + "equals"), builtargsList));//before(?t1,?t2)


		SWRLRule equalInstantsRule = factory.getSWRLRule(antecedent, 
													   Collections.singleton(factory.getSWRLSameIndividualAtom(instVar1, instVar2)));//OblOntology:before(?inst1, ?inst2)

		manager.applyChange(new AddAxiom(ontology, equalInstantsRule)); 
	}*/
	






	//TODO sameTime rule


}
