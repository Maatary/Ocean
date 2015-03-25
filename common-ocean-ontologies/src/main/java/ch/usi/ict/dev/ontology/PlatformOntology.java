package ch.usi.ict.dev.ontology;


import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ConceptSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PrimitiveSchema;

public class PlatformOntology extends Ontology {
	
	/**
	 * 
	 */
	private static final long serialVersionUID  = -2903228827706000297L;
	private static PlatformOntology theInstance = new PlatformOntology();
	
	
	public enum Vocabulary {
		
		/**
		 * Ontology Name
		 */
		ONTOLOGY_NAME("http://www.people.lu.unisi.ch/okouyad/PlatformOntology.owl#"),
		
		/*****************************
		 *         Classes    		 *
		 *              			 *
		 *****************************/
		
		/** Concepts **/
		MESSAGE("Message"),
		REQUEST_NAME_MESSAGE("RequestNameMessage"),
		
		
		/** Actions **/
		LISTPARTICIPANT("ListParticipant"),
		REGISTER_SELF("RegisterSelf"),
		
		
		/*****************************
		 *      Properties    		 *
		 *             				 *
		 *****************************/
		
		
		/*****Concepts Properties*****/
		
		/** Message **/
		HAS_MSG_ID("hasMsgId"),
		HAS_UNKNOW_RECEIVER("hasUnknowReceiver"),
		HAS_UNKNOW_SENDER("hasUnknowSender"),
		
		
		/** REQUEST_NAME_MESSAGE **/
		HAS_NAME_IN_USE("hasNameInUse"),

		/** Agent properties **/
		IS_REGISTERED_AS("isRegisteredAs");
		
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

	public PlatformOntology() {
		

		super(Vocabulary.ONTOLOGY_NAME.value(), OceanCL.getInstance());

		try {


			/**
			 * Adding schema to the ontology
			 */

			//Concepts
			add(new ConceptSchema(Vocabulary.MESSAGE.URIValue()));
			add(new ConceptSchema(Vocabulary.REQUEST_NAME_MESSAGE.URIValue()));
			//Actions
			add(new AgentActionSchema(Vocabulary.LISTPARTICIPANT.URIValue()));
			add(new AgentActionSchema(Vocabulary.REGISTER_SELF.URIValue()));

			
			/**
			 * Adding schemas' structures.
			 */
		
			//The LISTPARTICIPANT, REGISTERSELF ACTION
			AgentActionSchema as = (AgentActionSchema) getSchema(Vocabulary.LISTPARTICIPANT.URIValue());
			as.addSuperSchema((AgentActionSchema) getSchema(OceanCL.Vocabulary.ACTION.URIValue()));
			as = (AgentActionSchema) getSchema(Vocabulary.REGISTER_SELF.URIValue());
			as.addSuperSchema((AgentActionSchema) getSchema(OceanCL.Vocabulary.ACTION.URIValue()));
			
			
			
			//Structure of Message: It is used to talk about message
			ConceptSchema cs = (ConceptSchema) getSchema(Vocabulary.MESSAGE.URIValue());
			cs.add(Vocabulary.HAS_MSG_ID.URIValue(), (PrimitiveSchema) getSchema(BasicOntology.STRING));
			cs.add(Vocabulary.HAS_UNKNOW_RECEIVER.URIValue(), (ConceptSchema) getSchema(OceanCL.Vocabulary.AGENT.URIValue()), ObjectSchema.OPTIONAL);
			cs.add(Vocabulary.HAS_UNKNOW_SENDER.URIValue(), (ConceptSchema) getSchema(OceanCL.Vocabulary.AGENT.URIValue()), ObjectSchema.OPTIONAL);
			
			//Structure  RequestNameMessage: It is used to talk
			cs = (ConceptSchema) getSchema(Vocabulary.REQUEST_NAME_MESSAGE.URIValue());
			cs.addSuperSchema((ConceptSchema) getSchema(Vocabulary.MESSAGE.URIValue()));
			cs.add(Vocabulary.HAS_NAME_IN_USE.URIValue(), (PrimitiveSchema) getSchema(BasicOntology.STRING));


			//Update the structure
			cs = (ConceptSchema) getSchema(OceanCL.Vocabulary.AGENT.URIValue());
			//Don't need to add the property in the ontology, it doesn't have to exist in absolute
			//Indeed in the statement the predicate is just a string. 
			cs.add(Vocabulary.IS_REGISTERED_AS.URIValue(), (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);



		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
	public static PlatformOntology getInstance() {
		return theInstance;
	}

}
