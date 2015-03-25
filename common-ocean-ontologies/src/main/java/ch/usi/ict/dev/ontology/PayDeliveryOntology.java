package ch.usi.ict.dev.ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ConceptSchema;
import jade.content.schema.PrimitiveSchema;

public class PayDeliveryOntology extends Ontology {

	/**
	 * 
	 */
	private static final long serialVersionUID     = 5784611538450297554L;
	private static PayDeliveryOntology theInstance = new PayDeliveryOntology();
	
	
	public enum Vocabulary  {

		/**
		 * Name
		 */
		ONTOLOGY_NAME("http://www.people.lu.unisi.ch/okouyad/PayDeliveryOntology.owl#"),

		/**
		 * Classes
		 */
		PAY("Pay"),
		DELIVER("Deliver"),
		ITEM("Item"),

		/**
		 * Properties
		 */
		HAS_PAYEE("hasPayee"),
		HAS_AMOUNT("hasAmount"),
		HAS_DELIVEREE("hasDeliveree"),
		HAS_OBJECT("hasObject"),
		HAS_ITEM_ID("hasItemId");

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


	public PayDeliveryOntology () {

		super(Vocabulary.ONTOLOGY_NAME.value(), PlatformOntology.getInstance());
		
		try {
			
			/**
			 * Adding schema to the ontology
			 */
			
			add(new ConceptSchema(Vocabulary.ITEM.URIValue()));
			add(new AgentActionSchema(Vocabulary.PAY.URIValue()));
			add(new AgentActionSchema(Vocabulary.DELIVER.URIValue()));
			
			
			/**
			 * Adding schemas' structures.
			 */
			
			//Structure of the generic ITEM concept
			ConceptSchema cs = (ConceptSchema) getSchema(Vocabulary.ITEM.URIValue());
			cs.add(Vocabulary.HAS_ITEM_ID.URIValue(), (PrimitiveSchema) getSchema(BasicOntology.STRING));
			
			//Structure of the PAY ACTION
			AgentActionSchema as = (AgentActionSchema) getSchema(Vocabulary.PAY.URIValue());
			as.addSuperSchema((AgentActionSchema) getSchema(OceanCL.Vocabulary.ACTION.URIValue()));
			as.add(Vocabulary.HAS_PAYEE.URIValue(), (ConceptSchema) getSchema(OceanCL.Vocabulary.AGENT.URIValue()));
			as.add(Vocabulary.HAS_AMOUNT.URIValue(), (PrimitiveSchema) getSchema(BasicOntology.STRING));
			
			//Structure of the DELIVER ACTION
			as = (AgentActionSchema) getSchema(Vocabulary.DELIVER.URIValue());
			as.addSuperSchema((AgentActionSchema) getSchema(OceanCL.Vocabulary.ACTION.URIValue()));
			as.add(Vocabulary.HAS_DELIVEREE.URIValue(), (ConceptSchema) getSchema(OceanCL.Vocabulary.AGENT.URIValue()));
			as.add(Vocabulary.HAS_OBJECT.URIValue(), (ConceptSchema) getSchema(Vocabulary.ITEM.URIValue()));
			
			
			
		}catch (OntologyException e) {
			// TODO: handle exception
		}

	}

	public static PayDeliveryOntology getInstance() {
		return theInstance;
	}

}
