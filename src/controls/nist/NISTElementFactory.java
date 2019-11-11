package controls.nist;

import java.util.ArrayList;
import java.util.List;

import com.nomagic.magicdraw.uml.Finder;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.EnumerationLiteral;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

import cml.MDElementFactory;

/**
 * The NIST Element Factory creates the Magic Draw classes from the NIST SP
 * 800-53 and 800-53a Revision 4 standards data classes.
 * 
 * @author Sean C. Hubbell
 *
 */
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

	/**
	 * Creates the NIST control from the given control
	 * 
	 * @param controlStereotype              - the control stereotype to be applied
	 *                                       to each control created.
	 * @param baselineImpactStereotype       - the baseline impact stereotype to be
	 *                                       applied to each baseline impact
	 *                                       created.
	 * @param statementStereotype            - the statement stereotype to be
	 *                                       applied to each statement created.
	 * @param supplementalGuidanceStereotype - the supplemental guidance stereotype
	 *                                       to be applied to each supplemental
	 *                                       guidance created.
	 * @param controlEnhancementStereotype   - the control enhancement stereotype to
	 *                                       be applied to each control enhancement
	 *                                       created.
	 * @param referenceStereotype            - the reference stereotype to be
	 *                                       applied to each reference created.
	 * @param withdrawnStereotype            - the withdrawn stereotype to be
	 *                                       applied to each withdrawn created.
	 * @param objectiveStereotype            - the objective stereotype to be
	 *                                       applied to each objective created.
	 * @param potentialAssessmentStereotype  - the potential assessment stereotype
	 *                                       to be applied to each potential
	 *                                       assessment created.
	 * @param control                        - the control that contains the data
	 *                                       from the standard.
	 */
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
		if (control.baselineImpacts != null && control.baselineImpacts.size() > 0) {
			ArrayList<EnumerationLiteral> biItems = createBaselineImpacts(baselineImpactStereotype,
					control.baselineImpacts);
			StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Baseline Impacts", biItems,
					true);
		}

		/**
		 * Create & add the statements
		 */
		if (control.statements != null && control.statements.size() > 0) {
			folder = factory.createPackage("Statements", controlNumFolder);
			items = createStatements(folder, statementStereotype, control.statements);
			StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Statements", items, true);
		}

		/**
		 * Create & add the supplemental guidance
		 */
		if (control.supplementalGuidances != null && control.supplementalGuidances.size() > 0) {
			folder = factory.createPackage("Supplemental Guidances", controlNumFolder);
			items = createSupplementalGuidances(folder, supplementalGuidanceStereotype, control.supplementalGuidances);
			StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Supplemental Guidances",
					items, true);
		}

		/**
		 * Create & add the control enhancements
		 */
		if (control.controlEnhancements != null && control.controlEnhancements.size() > 0) {
			folder = factory.createPackage("Control Enhancements", controlNumFolder);
			items = createControlEnhancements(folder, controlEnhancementStereotype, baselineImpactStereotype,
					statementStereotype, supplementalGuidanceStereotype, withdrawnStereotype, objectiveStereotype,
					potentialAssessmentStereotype,
					control.controlEnhancements);
			StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Control Enhancements", items,
					true);
		}

		/**
		 * Create & add the references
		 */
		if (control.references != null && control.references.size() > 0) {
			folder = factory.createPackage("References", controlNumFolder);
			items = createReferences(folder, referenceStereotype, control.references);
			StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "References", items, true);
		}

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
		if (control.objectives != null && control.objectives.size() > 0) {
			folder = factory.createPackage("Objectives", controlNumFolder);
			items = createObjectives(folder, objectiveStereotype, control.objectives);
			StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Objectives", items, true);
		}

		/**
		 * Create & add the potential assessment
		 */

		if (control.potentialAssessments != null && control.potentialAssessments.size() > 0) {
			folder = factory.createPackage("Potential Assessments", controlNumFolder);
			items = createPotentialAssessments(folder, potentialAssessmentStereotype, control.potentialAssessments);
			StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Potential Assessments", items,
					true);
		}
	}

	/**
	 * Creates a collection of Magic Draw classes for each baseline impact provided.
	 * 
	 * @param baselineImpactStereotype - the stereotype for the baseline impact
	 *                                 classes created
	 * @param baselineImpacts          - the baseline impacts collection that need
	 *                                 Magic Draw classes generated against.
	 * @return the collection of new classes created from the given baseline impacts
	 */
	private ArrayList<EnumerationLiteral> createBaselineImpacts(Stereotype baselineImpactStereotype,
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
			if (statement.number != null) {
				StereotypesHelper.setStereotypePropertyValue(statementClass, statementStereotype, "Number",
						statement.number, true);
			}

			if (statement.description != null) {
				StereotypesHelper.setStereotypePropertyValue(statementClass, statementStereotype, "Description",
						statement.description, true);
				result.add(statementClass);
			}

			if (statement.statements != null && statement.statements.size() > 0) {
				Package subFolder = factory.createPackage("Statements", folder);
				ArrayList<Class> subClasses = createStatements(subFolder, statementStereotype, statement.statements);
				StereotypesHelper.setStereotypePropertyValue(statementClass, statementStereotype, "Statements",
						subClasses, true);
			}
		}
		return result;
	}

	/**
	 * Creates a collection of Magic Draw classes for each statement provided.
	 * 
	 * @param folder                         - the folder that will contain the
	 *                                       class created
	 * @param supplementalGuidanceStereotype - the supplemental guidance stereotype
	 *                                       to be applied to each newly created
	 *                                       supplemental guidance.
	 * @param supplementalGuidances          - The supplemental guidances that need
	 *                                       Magic Draw classes generated against.
	 * @return the collection of Magic Draw classes created from the given
	 *         supplemental guidances.
	 */
	private ArrayList<Class> createSupplementalGuidances(Package folder, Stereotype supplementalGuidanceStereotype,
			List<SupplementalGuidance> supplementalGuidances) {
		ArrayList<Class> result = new ArrayList<Class>();
		for (SupplementalGuidance sg : supplementalGuidances) {
			Class sgClass = factory.createClass(folder, "Supplemental Guidances", supplementalGuidanceStereotype);
			sgClass.setName(sg.description);
			sgClass.setOwner(folder);
			result.add(sgClass);
			
			if (sg.description != null) {
				StereotypesHelper.setStereotypePropertyValue(sgClass, supplementalGuidanceStereotype, "Description",
						sg.description, true);
			}

			if (sg.related != null && sg.related.size() > 0) {
				ArrayList<Class> relatedClasses = new ArrayList<Class>();
				for (String related : sg.related) {
					Class relatedClass = Finder.byNameRecursively().find(controlsFolder, Class.class, related);
					relatedClasses.add(relatedClass);
				}
				StereotypesHelper.setStereotypePropertyValue(sgClass, supplementalGuidanceStereotype, "Related",
						relatedClasses, true);
			}
		}
		return result;
	}

	/**
	 * Creates the Magic Draw control enhancements from the given control
	 * enhancements.
	 * 
	 * @param folder                         - the folder to contain the newly
	 *                                       created control enhancements.
	 * @param controlEnhancementStereotype   - the control enhancement stereotype to
	 *                                       be applied to each newly created
	 *                                       control enhancement.
	 * @param baselineImpactStereotype       - the baseline impact stereotype to be
	 *                                       applied to each newly created baseline
	 *                                       impact.
	 * @param statementStereotype            - the statement stereotype to be
	 *                                       applied to each newly created
	 *                                       statement.
	 * @param supplementalGuidanceStereotype - the supplemental guidance stereotype
	 *                                       to be applied to each newly created
	 *                                       supplemental guidance.
	 * @param withdrawnStereotype            - the withdrawn stereotype to be
	 *                                       applied to each newly created
	 *                                       withdrawn.
	 * @param controlEnhancements            - the control enhancements containing
	 *                                       the data to create the Magic Draw
	 *                                       classes from.
	 * @return the newly created collection of Magic Draw classes created from the
	 *         given control enhancements.
	 */
	private ArrayList<Class> createControlEnhancements(Package folder, Stereotype controlEnhancementStereotype,
			Stereotype baselineImpactStereotype, Stereotype statementStereotype,
			Stereotype supplementalGuidanceStereotype, Stereotype withdrawnStereotype,
			Stereotype objectiveStereotype, Stereotype potentialAssessmentStereotype,
			List<ControlEnhancement> controlEnhancements) {
		ArrayList<Class> result = new ArrayList<Class>();
		Package subFolder = null;

		for (ControlEnhancement ce : controlEnhancements) {
			Class ceClass = factory.createClass(folder, ce.number, controlEnhancementStereotype);
			if (ce.number != null) {
				StereotypesHelper.setStereotypePropertyValue(ceClass, controlEnhancementStereotype, "Number", ce.number,
						true);
			}
			if (ce.title != null) {
				StereotypesHelper.setStereotypePropertyValue(ceClass, controlEnhancementStereotype, "Title", ce.title,
						true);
			}

			if (ce.baselineImpacts != null && ce.baselineImpacts.size() > 0) {
				subFolder = factory.createPackage("Baseline Impacts", folder);
				ArrayList<EnumerationLiteral> baselineImpactClasses = createBaselineImpacts(
						baselineImpactStereotype, ce.baselineImpacts);
				StereotypesHelper.setStereotypePropertyValue(ceClass, controlEnhancementStereotype, "Baseline Impacts",
						baselineImpactClasses, true);
			}

			if (ce.statements != null && ce.statements.size() > 0) {
				subFolder = factory.createPackage("Statements", folder);
				ArrayList<Class> statementClasses = createStatements(subFolder, statementStereotype, ce.statements);
				StereotypesHelper.setStereotypePropertyValue(ceClass, controlEnhancementStereotype, "Statements",
						statementClasses, true);
			}

			if (ce.supplementalGuidances != null && ce.supplementalGuidances.size() > 0) {
				subFolder = factory.createPackage("Supplemental Guidances", folder);
				ArrayList<Class> sgClasses = createSupplementalGuidances(subFolder, supplementalGuidanceStereotype,
						ce.supplementalGuidances);
				StereotypesHelper.setStereotypePropertyValue(ceClass, controlEnhancementStereotype,
						"Supplemental Guidances", sgClasses, true);
			}

			if (ce.isWithdrawn()) {
				subFolder = factory.createPackage("Withdrawn", folder);
				Class withdrawnClass = createWithdrawn(subFolder, withdrawnStereotype, ce.withdrawn);
				StereotypesHelper.setStereotypePropertyValue(ceClass, controlEnhancementStereotype, "Withdrawn",
						withdrawnClass, true);
			}
			
			if (ce.objectives != null && ce.objectives.size() > 0) {
				subFolder = factory.createPackage("Objectives", folder);
				ArrayList<Class> subClasses = createObjectives(subFolder, objectiveStereotype, ce.objectives);
				StereotypesHelper.setStereotypePropertyValue(ceClass, objectiveStereotype, "Objectives",
						subClasses, true);
			}
			
			if (ce.potentialAssessments != null && ce.potentialAssessments.size() > 0) {
				subFolder = factory.createPackage("Potential Assessments", folder);
				ArrayList<Class> subClasses = createPotentialAssessments(subFolder, potentialAssessmentStereotype, ce.potentialAssessments);
				StereotypesHelper.setStereotypePropertyValue(ceClass, potentialAssessmentStereotype, "Potential Assessments",
						subClasses, true);
			}

			result.add(ceClass);
		}
		return result;
	}

	/**
	 * Creates the Magic Draw references from the given references.
	 * 
	 * @param folder              - the folder to contain the newly created
	 *                            references.
	 * @param referenceStereotype - the reference stereotype to be applied to each
	 *                            newly created referenceStereotype.
	 * @param references          - the control enhancements containing the data to
	 *                            create the Magic Draw classes from.
	 * @return the newly created collection of Magic Draw classes created from the
	 *         given references.
	 */
	private ArrayList<Class> createReferences(Package folder, Stereotype referenceStereotype,
			List<Reference> references) {
		ArrayList<Class> result = new ArrayList<Class>();
		for (Reference reference : references) {
			Class refClass = createReference(folder, referenceStereotype, reference);
			result.add(refClass);
		}
		return result;
	}

	/**
	 * Creates the Magic Draw reference from the given reference.
	 * 
	 * @param folder              - the folder to contain the newly created
	 *                            reference.
	 * @param referenceStereotype - the reference stereotype to be applied to each
	 *                            newly created reference.
	 * @param references          - the control enhancements containing the data to
	 *                            create the Magic Draw classes from.
	 * @return the newly created Magic Draw class created from the given reference.
	 */
	private Class createReference(Package folder, Stereotype referenceStereotype, Reference reference) {
		Class result = factory.createClass(folder, reference.item, referenceStereotype);
		if (reference.item != null) {
			StereotypesHelper.setStereotypePropertyValue(result, referenceStereotype, "Item", reference.item, true);
		} else {
			System.err.println("Reference item does not exist.");
		}
		return result;
	}

	/**
	 * Creates the Magic Draw objectives from the given objectives.
	 * 
	 * @param folder              - the folder to contain the newly created
	 *                            objectives.
	 * @param objectiveStereotype - the reference stereotype to be applied to each
	 *                            newly created reference
	 * @param objectives          - the objectives containing the data to create the
	 *                            Magic Draw classes from.
	 * @return the newly created collection of Magic Draw classes created from the
	 *         given objectives.
	 */
	private ArrayList<Class> createObjectives(Package folder, Stereotype objectiveStereotype,
			List<Objective> objectives) {
		ArrayList<Class> result = new ArrayList<Class>();

		for (Objective objective : objectives) {
			String name = (objective.number != null) ? objective.number : objective.decision;
			Class objectiveClass = factory.createClass(folder, name, objectiveStereotype);
			if (objective.number != null) {
				StereotypesHelper.setStereotypePropertyValue(objectiveClass, objectiveStereotype, "Number",
						objective.number, true);
			}
			if (objective.decision != null) {
				StereotypesHelper.setStereotypePropertyValue(objectiveClass, objectiveStereotype, "Decision",
						objective.decision, true);
			}
			result.add(objectiveClass);
			
			if (objective.objectives != null && objective.objectives.size() > 0) {
				Package subFolder = factory.createPackage("Objectives", folder);
				ArrayList<Class> subClasses = createObjectives(subFolder, objectiveStereotype, objective.objectives);
				StereotypesHelper.setStereotypePropertyValue(objectiveClass, objectiveStereotype, "Objectives",
						subClasses, true);
			}
			
		}
		return result;
	}

	/**
	 * Creates the Magic Draw potential assessments from the given potential
	 * assessments.
	 * 
	 * @param folder               - the folder to contain the newly created
	 *                             objectives.
	 * @param paStereotype         - the potential assessment stereotype to be
	 *                             applied to each newly created potential
	 *                             assessment.
	 * @param potentialAssessments - the potential assessments containing the data
	 *                             to create the Magic Draw classes from.
	 * @return the newly created collection of Magic Draw classes created from the
	 *         given potential assessments.
	 */
	private ArrayList<Class> createPotentialAssessments(Package folder, Stereotype paStereotype,
			List<PotentialAssessment> potentialAssessments) {
		ArrayList<Class> result = new ArrayList<Class>();

		for (PotentialAssessment pa : potentialAssessments) {
			Class paClass = factory.createClass(folder, pa.method, paStereotype);
			if (pa.method != null) {
				StereotypesHelper.setStereotypePropertyValue(paClass, paStereotype, "Method", pa.method, true);
			}
			result.add(paClass);
			if (pa.objects != null && pa.objects.size() > 0) {
				StereotypesHelper.setStereotypePropertyValue(paClass, paStereotype, "Objects", pa.objects, true);
			}
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
