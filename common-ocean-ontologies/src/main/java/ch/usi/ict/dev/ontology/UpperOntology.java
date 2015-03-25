package ch.usi.ict.dev.ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.ConceptSchema;
import jade.content.schema.PrimitiveSchema;

public class UpperOntology extends Ontology {


	private static final long serialVersionUID = -88708486922731302L;
	private static UpperOntology theInstance    = new UpperOntology();

	public enum Vocabulary {

		/**
		 * Ontology Name
		 */
		ONTOLOGY_NAME("http://swrl.stanford.edu/ontologies/built-ins/3.3/temporal.owl#"),

		/*****************************
		 *         Classes    		 *
		 *              			 *
		 *****************************/


		/** Concepts **/
		NAMED_INDIVIDUAL("NamedIndividual"),



		/*****************************
		 *      Properties    		 *
		 *             				 *
		 *****************************/


		/*****Concepts Properties*****/
		HAS_NAME("hasName"),
		HAS_TYPE("hasType");



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


	public UpperOntology () {

		super (Vocabulary.ONTOLOGY_NAME.value(), BasicOntology.getInstance());

		try {

			/**
			 * Adding schema to the ontology
			 */

			add(new ConceptSchema(Vocabulary.NAMED_INDIVIDUAL.URIValue()));


			/**
			 * Adding schemas' structures.
			 */

			ConceptSchema cs = (ConceptSchema) getSchema(Vocabulary.NAMED_INDIVIDUAL.URIValue());
			cs.add(Vocabulary.HAS_NAME.URIValue(), (PrimitiveSchema) getSchema(BasicOntology.STRING));
			cs.add(Vocabulary.HAS_TYPE.URIValue(), (PrimitiveSchema) getSchema(BasicOntology.STRING));


		} catch (OntologyException e) {
			e.printStackTrace();
		}


	}



	public static UpperOntology getInstance() {
		return theInstance;
	}



}
