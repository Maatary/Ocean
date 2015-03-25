package ch.usi.ict.dev.ontology;


import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ConceptSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;

public class OceanCL extends Ontology {
	
	private static final long serialVersionUID = -2067673876596430284L;
	private static OceanCL theInstance         = new OceanCL();
	
	
	public enum Vocabulary {
		
		/**
		 * Ontology Name
		 */
		ONTOLOGY_NAME("http://www.people.lu.unisi.ch/okouyad/OceanCL.owl#"),
		
		
		/*****************************
		 *         Classes    		 *
		 *              			 *
		 *****************************/
		
		/** Concepts **/
		AGENT("Agent"),
		EVENT("Event"),
		TIMED_EVENT("TimedEvent"),
		TIME_EVENT("TimeEvent"),
		RDF_RESSOURCE("RdfRessource"),
		
		
		/** Actions **/
		ACTION("Action"),
		
		 /** Predicates **/
		//There is one generic predicate called statement, modeled after RDF statement
		REIFIED_STATEMENT("ReifiedStatement"), 
		DONE("Done"),
		
		/** ConditionalAction **/
		CONDITIONAL_ACTION ("ConditionalAction"),
		ACTION_OR_TIME_EVENT("ActionOrTimeEvent"),
		
		/*****************************
		 *      Properties    		 *
		 *             				 *
		 *****************************/
		
		
		/*****Concepts Properties*****/
		
		/** AGENT **/
		HAS_AID ("hasAID"),

			
		/** TIMED_EVENT **/
		AT_TIME("atTime"),
		
		/** RDF_RESSOURCE **/
		HAS_RDF_RESSOURCE_VALUE("hasRdfRessourceValue"),
		HAS_RDF_RESSOURCE_TYPE("hasType"),
		
		/*****Action Properties*****/
		
		/** ACTION **/
		HAS_ACTOR("hasActor"),
		HAS_DEADLINE("hasDeadline"),
		HAS_DURATION("hasDuration"),

		
		/*****Predicate Properties*****/
		
		/** Statement **/
		HAS_SUBJECT("hasSubject"),
		HAS_PREDICATE("hasPredicate"),
		HAS_OBJECT("hasObject"),
		
		/**As state action and time of the done state.
		 * Also refactor statement and simple statement**/
		HAS_STATEDACTION("hasStatedAction"),
		HAS_STATEDTIME("hasStatedTime"),

		/** ConditionalAction **/
		HAS_ACTION_PART("hasActionPart"),
		HAS_CONDITION_PART("hasConditionPart");
		
		
		private final String name;

		Vocabulary(String name) {
			this.name = name;
		}

		public String value() {
			return name;
		}

		public String URIValue() {
			if (this.name == ONTOLOGY_NAME.value())
				return ONTOLOGY_NAME.value();
			return ONTOLOGY_NAME.value() + name;
		}

		public static Vocabulary fromValue(String v) {
			for (Vocabulary c: Vocabulary.values()) {
				if (c.name.equals(v)) {
					return c;
				}
			}
			throw new IllegalArgumentException(v);
		}
	}
	
	
	public OceanCL() {
		
		super(Vocabulary.ONTOLOGY_NAME.value(), TimeOntology.getInstance());
		
		try {

			/**
			 * Adding schema to the ontology
			 */

				/**Concepts**/
				add(new ConceptSchema(Vocabulary.AGENT.URIValue()));
				add(new ConceptSchema(Vocabulary.EVENT.URIValue()));
				add(new ConceptSchema(Vocabulary.TIMED_EVENT.URIValue()));
				add(new ConceptSchema(Vocabulary.TIME_EVENT.URIValue()));

				/**Actions**/
				add(new AgentActionSchema(Vocabulary.ACTION.URIValue()));

				/**Predicates**/
				add(new PredicateSchema(Vocabulary.REIFIED_STATEMENT.URIValue()));
				add(new PredicateSchema(Vocabulary.DONE.URIValue()));

				/** ConditionalAction **/
				add(new AgentActionSchema(Vocabulary.CONDITIONAL_ACTION.URIValue()));
				//Used for representing the union of Action or Time Event as range of hasConditionPart of the conditionalAction
				add(new ConceptSchema(Vocabulary.ACTION_OR_TIME_EVENT.URIValue()));
			
			/**
			 * Adding schemas' structures.
			 */
			
				//Structure of AGENT
				ConceptSchema cs = (ConceptSchema) getSchema(Vocabulary.AGENT.URIValue());
				cs.add(Vocabulary.HAS_AID.URIValue(), (PrimitiveSchema) getSchema(BasicOntology.STRING));
				
				
				//Structure of the TIMED_EVENT
				cs = (ConceptSchema) getSchema(Vocabulary.TIMED_EVENT.URIValue());
				cs.addSuperSchema((ConceptSchema) getSchema(Vocabulary.EVENT.URIValue()));
				cs.add(Vocabulary.AT_TIME.URIValue(), (ConceptSchema) getSchema(TimeOntology.Vocabulary.VALID_INSTANT.URIValue()));
				
				
				//Structure of the Time Event
				cs = (ConceptSchema) getSchema(Vocabulary.TIME_EVENT.URIValue());
				cs.addSuperSchema((ConceptSchema) getSchema(Vocabulary.TIMED_EVENT.URIValue()));
				//Used for representing the union of Action or Time Event as range of hasConditionPart of the conditionalAction
				cs.addSuperSchema((ConceptSchema) getSchema(Vocabulary.ACTION_OR_TIME_EVENT.URIValue()));
				
				
				//Structure of the OCEAN_AGENT_ACTION
				AgentActionSchema as = (AgentActionSchema) getSchema(Vocabulary.ACTION.URIValue());
				as.addSuperSchema((ConceptSchema) getSchema(Vocabulary.EVENT.URIValue()));
				as.add(Vocabulary.HAS_ACTOR.URIValue(), (ConceptSchema) getSchema(Vocabulary.AGENT.URIValue()));
				as.add(Vocabulary.HAS_DEADLINE.URIValue(), (ConceptSchema) getSchema(OceanCL.Vocabulary.TIME_EVENT.URIValue()), ObjectSchema.OPTIONAL);
				as.add(Vocabulary.HAS_DURATION.URIValue(), (ConceptSchema) getSchema(TimeOntology.Vocabulary.DURATION.URIValue()), ObjectSchema.OPTIONAL);
				//Used for representing the union of Action or Time Event as range of hasConditionPart of the conditionalAction
				as.addSuperSchema((ConceptSchema) getSchema(Vocabulary.ACTION_OR_TIME_EVENT.URIValue()));
				
				
				//Structure of the  SimpleStatement statement (inspired by RDF)
				PredicateSchema ps = (PredicateSchema) getSchema(Vocabulary.REIFIED_STATEMENT.URIValue());
				ps.add(Vocabulary.HAS_SUBJECT.URIValue(), (PrimitiveSchema) getSchema(BasicOntology.STRING));
				ps.add(Vocabulary.HAS_PREDICATE.URIValue(), (PrimitiveSchema) getSchema(BasicOntology.STRING));
				//ps.add(Vocabulary.HAS_OBJECT.URIValue(), (PrimitiveSchema) getSchema(BasicOntology.STRING));
				
				//Structure of the Done statement
				ps = (PredicateSchema) getSchema(Vocabulary.DONE.URIValue());
				ps.add(Vocabulary.HAS_STATEDACTION.URIValue(), (ConceptSchema) getSchema(Vocabulary.ACTION.URIValue()));
				ps.add(Vocabulary.HAS_STATEDTIME.URIValue(), (ConceptSchema) getSchema(TimeOntology.Vocabulary.VALID_INSTANT.URIValue()));
				
				//Structure of the Conditional Action
				as = (AgentActionSchema) getSchema(Vocabulary.CONDITIONAL_ACTION.URIValue());
				as.add(Vocabulary.HAS_ACTION_PART.URIValue(), (AgentActionSchema) getSchema(Vocabulary.ACTION.URIValue()));
				as.add(Vocabulary.HAS_CONDITION_PART.URIValue(), (ConceptSchema) getSchema(Vocabulary.ACTION_OR_TIME_EVENT.URIValue()));
				
		}
		catch (OntologyException e) {
			e.printStackTrace();
		}
		
	}
	
	public static OceanCL getInstance() {
		return theInstance;
	}
}