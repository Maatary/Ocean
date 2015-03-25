package ch.usi.ict.dev.ontology;


import jade.content.abs.AbsConcept;
import jade.content.abs.AbsObject;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.ConceptSchema;
import jade.content.schema.Facet;

import jade.content.schema.PrimitiveSchema;

public class TimeOntology extends Ontology {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6023521499356492240L;
	private static TimeOntology theInstance    = new TimeOntology();


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
		ENTITY("Entity"),
		DURATION("Duration"),
		PROPOSITION("Proposition"),
		EXTENDED_PROPOSITION("ExtendedProposition"),
		VALID_TIME("ValidTime"),
		VALID_INSTANT("ValidInstant"),
		VALID_PERIOD("ValidPeriod"),

		
		/**
		 * THOSE ARE JUST CONSTANT
		 * THEY DO NOT REPRESENT CONCEPT OF THE JAVA ONTOLOGY
		 * THEY ARE PART OF THE PATTERN TO REPRESENT OneOf CLASS
		 * 
		 * Because we are only interested in the individual of those classes, 
		 * which have no properties but there names to identify them, 
		 * we only represent the individual here.
		 * Hence a concept of NamedIndividual (see UpperOntology) is created,
		 * which will have as property the name of the individual and its type.
		 * 
		 */
		//Use as a Type for Named Individual
		GRANULARITY("Granularity"),
		//The possible NamedIndividual of type Granularity
		DAYS("Days"),
		HOURS("hours"),
		MINUTES("Minutes"),
		SECONDS("Seconds"),
		MILLISECONDS("Milliseconds"),
		MONTHS("Months"),
		YEARS("Years"),
		


		/*****************************
		 *      Properties    		 *
		 *             				 *
		 *****************************/


		/*****Concepts Properties*****/
		HAS_VALID_TIME("hasValidTime"),
		HAS_GRANULARITY("hasGranularity"),
		HAS_COUNT("hasCount"),
		HAS_TIME("hasTime"),
		HAS_START_TIME("hasStartTime"),
		HAS_FINISH_TIME("hasFinishTime");




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




	public TimeOntology () {

		super (Vocabulary.ONTOLOGY_NAME.value(), UpperOntology.getInstance());

		try {

			/**
			 * Adding schema to the ontology
			 */

			add(new ConceptSchema(Vocabulary.ENTITY.URIValue()));
			add(new ConceptSchema(Vocabulary.DURATION.URIValue()));
			//add(new ConceptSchema(Vocabulary.GRANULARITY.URIValue())); No concept of this type see. OneOf Pattern above
			add(new ConceptSchema(Vocabulary.PROPOSITION.URIValue()));
			add(new ConceptSchema(Vocabulary.EXTENDED_PROPOSITION.URIValue()));
			add(new ConceptSchema(Vocabulary.VALID_TIME.URIValue()));
			add(new ConceptSchema(Vocabulary.VALID_INSTANT.URIValue()));
			add(new ConceptSchema(Vocabulary.VALID_PERIOD.URIValue()));


			/**
			 * Adding schemas' structures.
			 */

			ConceptSchema cs = (ConceptSchema) getSchema(Vocabulary.DURATION.URIValue());
			cs.add(Vocabulary.HAS_COUNT.URIValue(), (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
			cs.add(Vocabulary.HAS_GRANULARITY.URIValue(), (ConceptSchema) getSchema(UpperOntology.Vocabulary.NAMED_INDIVIDUAL.URIValue()));
			cs.addFacet(Vocabulary.HAS_GRANULARITY.URIValue(), new GranularityFacet());
			
			
			cs = (ConceptSchema) getSchema(Vocabulary.VALID_PERIOD.URIValue());
			cs.add(Vocabulary.HAS_START_TIME.URIValue(), (PrimitiveSchema) getSchema(BasicOntology.DATE));
			cs.add(Vocabulary.HAS_FINISH_TIME.URIValue(), (PrimitiveSchema) getSchema(BasicOntology.DATE));
				
			cs = (ConceptSchema) getSchema(Vocabulary.VALID_INSTANT.URIValue());
			cs.add(Vocabulary.HAS_TIME.URIValue(), (PrimitiveSchema) getSchema(BasicOntology.DATE));




		} catch (OntologyException e) {
			e.printStackTrace();
		}


	}

	public static TimeOntology getInstance() {
		return theInstance;
	}


	/**
	 * Nested Class Facet to deal with the granularity property restriction
	 * Indeed, it is restricted to  a set of individual that represent the possible value of Granularity
	 * Granularity is defined in the ontology with a OneOf expression.
	 * @author maatari
	 *
	 */
	public class GranularityFacet implements Facet {

		@Override
		public void validate(AbsObject value, Ontology onto) throws OntologyException {
			
			AbsConcept gran = (AbsConcept)value;
			
			if (gran.getString(UpperOntology.Vocabulary.HAS_TYPE.URIValue()).equals(TimeOntology.Vocabulary.GRANULARITY.URIValue()) == false)
				throw new OntologyException("NamedIndividual should be of type Granularity");
			String name = gran.getString(UpperOntology.Vocabulary.HAS_NAME.URIValue());
			if (!name.equals(Vocabulary.DAYS.URIValue()) && !name.equals(Vocabulary.HOURS.URIValue())
				&& !name.equals(Vocabulary.MINUTES.URIValue()) && !name.equals(Vocabulary.SECONDS.URIValue())
				&& !name.equals(Vocabulary.MILLISECONDS.URIValue()) && !name.equals(Vocabulary.MONTHS.URIValue())
				&& !name.equals(Vocabulary.YEARS.URIValue()))
				throw new OntologyException("NamedIndividual must be OneOf Days, Hours, Minutes, Seconds, Milliseconds, Months, Years");;
			
		}
		
	/*	@Override
		void validate(AbsObject abs, Ontology onto) throws OntologyException {
			try {
				AbsPrimitive p = (AbsPrimitive) abs;
				if (p.getInteger() <= 0) {
					throw new OntologyException(�Integer value <= 0�);
				}
			}
			catch (Exception e) {
				throw new OntologyException(�Not an Integer value�, e);
			}
		}*/
		
		

		 
	}


}
