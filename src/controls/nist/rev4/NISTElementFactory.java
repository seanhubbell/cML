package controls.nist.rev4;

import java.util.ArrayList;
import java.util.List;

import com.nomagic.magicdraw.uml.Finder;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.EnumerationLiteral;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

import cml.MDElementFactory;

public class NISTElementFactory {

	private MDElementFactory factory;
	private Package controlsFolder = null;
	private Package baselineImpactsFolder = null;
	private Package prioritiesFolder = null;
	
	/**
	 * Creates an instance of the NIST Element Factory to be able to generate NIST
	 * elements.
	 * 
	 * @param mdFactory      the Magic Draw factory used to create Magic Draw
	 *                       specific elements.
	 * @param controlsFolder the Magic Draw package used to start the search for
	 *                       controls from.
	 */
	public NISTElementFactory(MDElementFactory mdFactory, Package pFolder, Package cFolder, Package biFolder) {
		factory = mdFactory;
		controlsFolder = cFolder;
		baselineImpactsFolder = biFolder;
		prioritiesFolder = pFolder;
	}

	public void createControl(Stereotype controlStereotype, Stereotype baselineImpactStereotype,
			Stereotype statementStereotype, Stereotype supplementalGuidanceStereotype,
			Stereotype controlEnhancementStereotype, Stereotype referenceStereotype, Stereotype withdrawnStereotype,
			Stereotype objectiveStereotype, Stereotype potentialAssessmentStereotype, Control control) {
		Package folder = null;
		ArrayList<Class> items = null;
		
		Package controlNumFolder = Finder.byNameRecursively().find(controlsFolder, Package.class, control.number);
		Class controlClass = Finder.byNameRecursively().find(controlsFolder, Class.class, control.number);
		StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Family", control.family, true);
		StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Number", control.number, true);
		StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Title", control.title, true);
		EnumerationLiteral priorityLiteral = Finder.byNameRecursively().find(prioritiesFolder, EnumerationLiteral.class,
				control.priority);
		StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Priority", priorityLiteral,
				true);

		/**
		 * Create & add the baseline impacts
		 */
		folder = factory.createPackage("Baseline Impacts", controlNumFolder);
		ArrayList<EnumerationLiteral> biItems = createBaselineImpacts(folder, baselineImpactStereotype,
				control.baselineImpacts);
		StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Baseline Impacts", biItems,
				true);

		/**
		 * Create & add the statements
		 */
		folder = factory.createPackage("Statements", controlNumFolder);
		items = createStatements(folder, statementStereotype, control.statements);
		StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Statements", items, true);

		/**
		 * Create & add the supplemental guidance
		 */
		folder = factory.createPackage("Supplemental Guidances", controlNumFolder);
		items = createSupplementalGuidances(folder, supplementalGuidanceStereotype, control.supplementalGuidances);
		StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Supplemental Guidances", items,
				true);

		/**
		 * Create & add the control enhancements
		 */
		folder = factory.createPackage("Control Enhancements", controlNumFolder);
		items = createControlEnhancements(folder, controlEnhancementStereotype, baselineImpactStereotype,
				statementStereotype, supplementalGuidanceStereotype, withdrawnStereotype, control.controlEnhancements);
		StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Control Enhancements", items,
				true);

		/**
		 * Create & add the references
		 */
		folder = factory.createPackage("References", controlNumFolder);
		items = createReferences(folder, referenceStereotype, control.references);
		StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "References", items, true);

		/**
		 * Create & add the withdrawn
		 */
		if (control.isWithdrawn()) {
			folder = factory.createPackage("Withdrawn", controlNumFolder);
			Class cclass = createWithdrawn(folder, withdrawnStereotype, control.withdrawn);
			StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Withdrawn", cclass, true);
		}

		/**
		 * Create & add the objective
		 */
		folder = factory.createPackage("Objectives", controlNumFolder);
		items = createObjectives(folder, objectiveStereotype, control.objectives);
		StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Objectives", items, true);

		/**
		 * Create & add the potential assessment
		 */
		folder = factory.createPackage("Potential Assessments", controlNumFolder);
		items = createPotentialAssessments(folder, potentialAssessmentStereotype, control.potentialAssessments);
		StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Potential Assessment", items,
				true);
	}

	/**
	 * Creates a collection of Magic Draw classes for each baseline impact provided.
	 * 
	 * @param folder                   - the folder that will contain the class
	 *                                 created
	 * @param baselineImpactStereotype - the stereotype for the baseline impact
	 *                                 classes created
	 * @param baselineImpacts          - the baseline impacts collection that need
	 *                                 Magic Draw classes generated against.
	 * @return the collection of new classes created from the given baseline impacts
	 */
	private ArrayList<EnumerationLiteral> createBaselineImpacts(Package folder, Stereotype baselineImpactStereotype,
			List<String> baselineImpacts) {
		ArrayList<EnumerationLiteral> items = new ArrayList<EnumerationLiteral>();
		for (String bli : baselineImpacts) {
			EnumerationLiteral baselineImpactLiteral = Finder.byNameRecursively().find(baselineImpactsFolder,
					EnumerationLiteral.class, bli);
			items.add(baselineImpactLiteral);
		}
		return items;
	}

	/**
	 * Creates a collection of Magic Draw classes for each statement provided.
	 * 
	 * @param folder              - the folder that will contain the class created
	 * @param statementStereotype - The stereotype to apply to the nested
	 *                            statement(s) found in the given statements
	 *                            collection.
	 * @param statements          - the statements collection that need Magic Draw
	 *                            classes generated against.
	 * @return the collection of Magic Draw classes created from the given
	 *         statements
	 */
	private ArrayList<Class> createStatements(Package folder, Stereotype statementStereotype,
			List<Statement> statements) {
		ArrayList<Class> result = new ArrayList<Class>();

		for (Statement statement : statements) {
			String name = statement.number;
			if (name == null || name.equals("")) {
				name = statement.description;
			}

			Class statementClass = factory.createClass(folder, name, statementStereotype);
			StereotypesHelper.setStereotypePropertyValue(statementClass, statementStereotype, "Number",
					statement.number, true);
			StereotypesHelper.setStereotypePropertyValue(statementClass, statementStereotype, "Description",
					statement.description, true);
			result.add(statementClass);

			Package subFolder = factory.createPackage("Statements", folder);
			ArrayList<Class> subClasses = createStatements(subFolder, statementStereotype, statement.statements);
			StereotypesHelper.setStereotypePropertyValue(statementClass, statementStereotype, "Statements", subClasses,
					true);
		}
		return result;
	}

	/**
	 * Creates a collection of Magic Draw classes for each statement provided.
	 * 
	 * @param folder                         - the folder that will contain the
	 *                                       class created
	 * @param supplementalGuidanceStereotype -
	 * @param supplementalGuidances          - The supplemental guidances that need
	 *                                       Magic Draw classes generated against.
	 * @returnthe collection of Magic Draw classes created from the given
	 *            supplemental guidances
	 */
	private ArrayList<Class> createSupplementalGuidances(Package folder, Stereotype supplementalGuidanceStereotype,
			List<SupplementalGuidance> supplementalGuidances) {
		ArrayList<Class> result = new ArrayList<Class>();
		for (SupplementalGuidance sg : supplementalGuidances) {
			Class sgClass = factory.createClass(folder, "Supplemental Guidances", supplementalGuidanceStereotype);
			sgClass.setName(sg.description);
			sgClass.setOwner(folder);
			result.add(sgClass);
			StereotypesHelper.setStereotypePropertyValue(sgClass, supplementalGuidanceStereotype, "Description",
					sg.description, true);

			ArrayList<Class> relatedClasses = new ArrayList<Class>();
			for (String related : sg.related) {
				Class relatedClass = Finder.byNameRecursively().find(controlsFolder, Class.class, related);
				relatedClasses.add(relatedClass);
			}
			StereotypesHelper.setStereotypePropertyValue(sgClass, supplementalGuidanceStereotype, "Related",
					relatedClasses, true);
		}
		return result;
	}

	/**
	 * 
	 * @param folder
	 * @param controlEnhancementStereotype
	 * @param baselineImpactStereotype
	 * @param statementStereotype
	 * @param supplementalGuidanceStereotype
	 * @param withdrawnStereotype
	 * @param controlEnhancements
	 * @return
	 */
	private ArrayList<Class> createControlEnhancements(Package folder, Stereotype controlEnhancementStereotype,
			Stereotype baselineImpactStereotype, Stereotype statementStereotype,
			Stereotype supplementalGuidanceStereotype, Stereotype withdrawnStereotype,
			List<ControlEnhancement> controlEnhancements) {
		ArrayList<Class> result = new ArrayList<Class>();
		Package subFolder = null;

		for (ControlEnhancement ce : controlEnhancements) {
			Class ceClass = factory.createClass(folder, ce.number, controlEnhancementStereotype);
			StereotypesHelper.setStereotypePropertyValue(ceClass, controlEnhancementStereotype, "Number", ce.number,
					true);
			StereotypesHelper.setStereotypePropertyValue(ceClass, controlEnhancementStereotype, "Title", ce.title,
					true);

			subFolder = factory.createPackage("Baseline Impacts", folder);
			ArrayList<EnumerationLiteral> baselineImpactClasses = createBaselineImpacts(subFolder,
					baselineImpactStereotype, ce.baselineImpacts);
			StereotypesHelper.setStereotypePropertyValue(ceClass, controlEnhancementStereotype, "Baseline Impacts",
					baselineImpactClasses, true);

			subFolder = factory.createPackage("Statements", folder);
			ArrayList<Class> statementClasses = createStatements(subFolder, statementStereotype, ce.statements);
			StereotypesHelper.setStereotypePropertyValue(ceClass, controlEnhancementStereotype, "Statements",
					statementClasses, true);

			subFolder = factory.createPackage("Supplemental Guidances", folder);
			ArrayList<Class> sgClasses = createSupplementalGuidances(subFolder, supplementalGuidanceStereotype,
					ce.supplementalGuidances);
			StereotypesHelper.setStereotypePropertyValue(ceClass, controlEnhancementStereotype,
					"Supplemental Guidances", sgClasses, true);

			if (ce.isWithdrawn()) {
				subFolder = factory.createPackage("Withdrawn", folder);
				Class withdrawnClass = createWithdrawn(subFolder, withdrawnStereotype, ce.withdrawn);
				StereotypesHelper.setStereotypePropertyValue(ceClass, controlEnhancementStereotype, "Withdrawn",
						withdrawnClass, true);
			}

			result.add(ceClass);
		}
		return result;
	}

	/**
	 * 
	 * @param folder
	 * @param referenceStereotype
	 * @param references
	 * @return
	 */
	private ArrayList<Class> createReferences(Package folder, Stereotype referenceStereotype,
			List<Reference> references) {
		ArrayList<Class> result = new ArrayList<Class>();
		for (Reference reference : references) {
			Class refClass = createReference(folder, referenceStereotype, reference);
			// StereotypesHelper.setStereotypePropertyValue(refClass, referenceStereotype,
			// "Reference", reference.item, true);
			result.add(refClass);
		}
		return result;
	}

	/**
	 * 
	 * @param folder
	 * @param referenceStereotype
	 * @param reference
	 * @return
	 */
	private Class createReference(Package folder, Stereotype referenceStereotype, Reference reference) {
		Class result = factory.createClass(folder, reference.item, referenceStereotype);
		StereotypesHelper.setStereotypePropertyValue(result, referenceStereotype, "Item", reference.item, true);
		return result;
	}

	/**
	 * 
	 * @param folder
	 * @param objectiveStereotype
	 * @param objectives
	 * @return
	 */
	private ArrayList<Class> createObjectives(Package folder, Stereotype objectiveStereotype,
			List<Objective> objectives) {
		ArrayList<Class> result = new ArrayList<Class>();

		for (Objective objective : objectives) {
			Class objectiveClass = factory.createClass(folder, objective.number, objectiveStereotype);
			StereotypesHelper.setStereotypePropertyValue(objectiveClass, objectiveStereotype, "Number",
					objective.number, true);
			StereotypesHelper.setStereotypePropertyValue(objectiveClass, objectiveStereotype, "Decision",
					objective.decision, true);
			result.add(objectiveClass);

			Package subFolder = factory.createPackage("Objectives", folder);
			ArrayList<Class> subClasses = createObjectives(subFolder, objectiveStereotype, objective.objectives);
			StereotypesHelper.setStereotypePropertyValue(objectiveClass, objectiveStereotype, "Objectives", subClasses,
					true);
		}
		return result;
	}

	/**
	 * 
	 * @param folder
	 * @param paStereotype
	 * @param potentialAssessments
	 * @return
	 */
	private ArrayList<Class> createPotentialAssessments(Package folder, Stereotype paStereotype,
			List<PotentialAssessment> potentialAssessments) {
		ArrayList<Class> result = new ArrayList<Class>();

		for (PotentialAssessment pa : potentialAssessments) {
			Class ceClass = factory.createClass(folder, pa.method, paStereotype);
			StereotypesHelper.setStereotypePropertyValue(ceClass, paStereotype, "Method", pa.method, true);
			StereotypesHelper.setStereotypePropertyValue(ceClass, paStereotype, "Objects", pa.objects, true);

			result.add(ceClass);
		}
		return result;
	}

	/**
	 * Creates the Withdrawn Element.
	 * 
	 * @param folder              - the location the created element will be
	 *                            located.
	 * @param withdrawnStereotype - the stereotype that will be applied to the
	 *                            created class.
	 * @param withdrawn           - the withdrawn structure containing the data from
	 *                            the NIST standard.
	 * @return the newly created withdrawn instance.
	 */
	private Class createWithdrawn(Package folder, Stereotype withdrawnStereotype, Withdrawn withdrawn) {
		Class result = factory.createClass(folder, "Withdrawn", withdrawnStereotype);
		ArrayList<Class> incorporatedIntos = new ArrayList<Class>();
		for (String incorporatedInto : withdrawn.incorporatedInto) {
			Class ref = Finder.byNameRecursively().find(controlsFolder, Class.class, incorporatedInto);
			incorporatedIntos.add(ref);
		}
		StereotypesHelper.setStereotypePropertyValue(result, withdrawnStereotype, "Incorporated Into",
				incorporatedIntos, true);

		return result;
	}

}
