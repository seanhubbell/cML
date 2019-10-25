/* Copyright Sean C. Hubbell All Rights Reserved */
package cml;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.CheckForNull;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.nomagic.magicdraw.actions.MDAction;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.openapi.uml.SessionManager;
import com.nomagic.magicdraw.ui.dialogs.MDDialogParentProvider;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

import controls.nist.rev4.Control;
import controls.nist.rev4.ControlEnhancement;
import controls.nist.rev4.ControlParser;
import controls.nist.rev4.Reference;
import controls.nist.rev4.Statement;
import controls.nist.rev4.SupplementalGuidance;
import controls.nist.rev4.Withdrawn;

@SuppressWarnings("serial")
class LoadNISTControlsAction extends MDAction {

	private MDElementFactory factory = null;
	
	public LoadNISTControlsAction(@CheckForNull String id, String name) {
		super(id, name, null, null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ControlParser parser = new ControlParser();
		
		try {
			File file = new File("S:\\References\\NIST\\NIST_SP_800-53\\800-53-controls-mod-pm-priorities-added.xml");
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dBuilder.parse(file);

			NodeList nList = doc.getElementsByTagName("controls:control");
			int numControls = nList.getLength();
			System.out.println("# countrols = " + numControls);

			if (doc.getDocumentElement().getNodeName().equals("controls:controls")) {
				if (doc.hasChildNodes()) {
					parser.parse(doc.getChildNodes());
				}
			}

			// JOptionPane.showMessageDialog(MDDialogParentProvider.getProvider().getDialogOwner(),
			// parser.getControls().size() + " Security Controls Loaded");

			Project project = Application.getInstance().getProject();
			Package model = project.getPrimaryModel();
			if (project != null) {
				SessionManager.getInstance().createSession(project, "Generating NIST SP Rev 4 Security Controls");
				Stereotype controlStereotype = StereotypesHelper.getStereotype(project, "Security Control", (Profile) null);
				Stereotype baselineImpactStereotype = StereotypesHelper.getStereotype(project, "Baseline Impact", (Profile) null);
				Stereotype statementStereotype = StereotypesHelper.getStereotype(project, "Statement", (Profile) null);
				Stereotype supplementalGuidanceStereotype = StereotypesHelper.getStereotype(project, "Supplemental Guidance", (Profile) null);
				Stereotype controlEnhancementStereotype = StereotypesHelper.getStereotype(project, "Control Enhancement", (Profile) null);
				Stereotype referenceStereotype = StereotypesHelper.getStereotype(project, "Reference", (Profile) null);
				Stereotype withdrawnStereotype = StereotypesHelper.getStereotype(project, "Withdrawn", (Profile) null);
				Stereotype objectiveStereotype = StereotypesHelper.getStereotype(project, "Objective", (Profile) null);
				Stereotype potentialAssessmentStereotype = StereotypesHelper.getStereotype(project, "Potential Assessment", (Profile) null);

				ArrayList<Control> controls = parser.getControls();
				factory = new MDElementFactory(project);

				/**
				 * Create the family of security control packages
				 */
				Package controlsFolder = factory.createPackage("NIST SP 800-53 Rev 4", model);
				for (Control control : controls) {
					factory.createPackage(control.family, controlsFolder);
				}

				for (int i = 0; i < controls.size(); i++) {
					Control control = controls.get(i);
					Package folder = null;
					ArrayList<Class> items = null;
					Class cclass = null;

					Package familyFolder = factory.createPackage(control.family, controlsFolder);
					Package controlNumFolder = factory.createPackage(control.number, familyFolder);
					Class controlClass = factory.createClass(controlNumFolder, control.number, controlStereotype);
					StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Family", control.family, true);
					StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Number", control.number, true);
					StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Title", control.title, true);
					StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Priority", control.priority, true);

					/**
					 * Create & add the baseline impacts
					 */
					folder = factory.createPackage("Baseline Impacts", controlNumFolder);
					items = createBaselineImpacts(folder, baselineImpactStereotype, control.baselineImpacts);
					StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Baseline Impacts", items, true);

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
					StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Supplemental Guidances", items, true);

					/**
					 * Create & add the control enhancements
					 */		
					folder = factory.createPackage("Control Enhancements", controlNumFolder);
					items = createControlEnhancements(folder,
							controlEnhancementStereotype,
							baselineImpactStereotype,
							statementStereotype,
							supplementalGuidanceStereotype,
							withdrawnStereotype,
							control.controlEnhancements);
					StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Control Enhancements", items, true);

					/**
					 * Create & add the references
					 */
					folder = factory.createPackage("References", controlNumFolder);
					items = createReferences(folder, referenceStereotype, control.references);
					StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "References", items, true);

					/**
					 * Create & add the withdrawn
					 */
					if(control.isWithdrawn()) {
						folder = factory.createPackage("Withdrawn", controlNumFolder);
						cclass = createWithdrawn(folder, withdrawnStereotype, control.withdrawn);
						StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Withdrawn", cclass, true);
					}

					/**
					 * Create & add the references
					 */
					//folder = factory.createPackage("Objectives", controlNumFolder);
					//items = createObjectives(folder, controlStereotype, objectiveStereotype, control.objectives);
					//StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Objectives", items, true);
					
					/**
					 * Create & add the potential assessment
					 */
					//folder = factory.createPackage("Potential Assessments", controlNumFolder);
					//items = createPotentialAssessments(folder, controlStereotype, potentialAssessmentStereotype, control.potentialAssessments);
					//StereotypesHelper.setStereotypePropertyValue(controlClass, controlStereotype, "Potential Assessment", items, true);
				}
				SessionManager.getInstance().closeSession(project);
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(MDDialogParentProvider.getProvider().getDialogOwner(), ex.getMessage());
		}
	}

	/**
	 * Creates a collection of Magic Draw classes for each baseline impact provided.
	 * @param folder - the folder that will contain the class created
	 * @param baselineImpactStereotype - the stereotype for the baseline impact classes created
	 * @param baselineImpacts - the baseline impacts collection that need Magic Draw classes generated against.
	 * @return the collection of new classes created from the given baseline impacts
	 */
	private ArrayList<Class> createBaselineImpacts(Package folder, Stereotype baselineImpactStereotype, List<String> baselineImpacts) {
		ArrayList<Class> items = new ArrayList<Class>();
		for (String bli : baselineImpacts) {
			items.add(factory.createClass(folder, bli, baselineImpactStereotype));
		}
		return items;
	}

	/**
	 * Creates a collection of Magic Draw classes for each statement provided.
	 * @param folder - the folder that will contain the class created
	 * @param statementStereotype - The stereotype to apply to the nested statement(s) found in the given statements collection.
	 * @param statements - the statements collection that need Magic Draw classes generated against.
	 * @return the collection of Magic Draw classes created from the given statements
	 */
	private ArrayList<Class> createStatements(Package folder, Stereotype statementStereotype, List<Statement> statements) {
		ArrayList<Class> result = new ArrayList<Class>();

		for (Statement statement : statements) {
			String name = statement.number;
			if (name == null || name.equals("")) {
				name = statement.description;
			}

			Class statementClass = factory.createClass(folder, name, statementStereotype);
			StereotypesHelper.setStereotypePropertyValue(statementClass, statementStereotype, "Number", statement.number, true);
			StereotypesHelper.setStereotypePropertyValue(statementClass, statementStereotype, "Description", statement.description, true);
			result.add(statementClass);

			Package subFolder = factory.createPackage("Statements", folder);
			ArrayList<Class> subClasses = createStatements(subFolder, statementStereotype, statement.statements);
			StereotypesHelper.setStereotypePropertyValue(statementClass, statementStereotype, "Statements", subClasses, true);
		}
		return result;
	}

	/**
	 * Creates a collection of Magic Draw classes for each statement provided.
	 * @param folder - the folder that will contain the class created
	 * @param supplementalGuidanceStereotype - 
	 * @param supplementalGuidances - The supplemental guidances that need Magic Draw classes generated against.
	 * @returnthe collection of Magic Draw classes created from the given supplemental guidances
	 */
	private ArrayList<Class> createSupplementalGuidances(Package folder,
			Stereotype supplementalGuidanceStereotype, List<SupplementalGuidance> supplementalGuidances) {
		ArrayList<Class> result = new ArrayList<Class>();
		for (SupplementalGuidance sg : supplementalGuidances) {
			Class sgClass = factory.createClass(folder, "Supplemental Guidances", supplementalGuidanceStereotype);
			sgClass.setName(sg.description);
			sgClass.setOwner(folder);
			result.add(sgClass);
			StereotypesHelper.setStereotypePropertyValue(sgClass, supplementalGuidanceStereotype, "Description", sg.description, true);
			StereotypesHelper.setStereotypePropertyValue(sgClass, supplementalGuidanceStereotype, "Related", sg.related, true);
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
	private ArrayList<Class> createControlEnhancements(Package folder,
			Stereotype controlEnhancementStereotype,
			Stereotype baselineImpactStereotype,
			Stereotype statementStereotype,
			Stereotype supplementalGuidanceStereotype,
			Stereotype withdrawnStereotype,
			List<ControlEnhancement> controlEnhancements) {
		ArrayList<Class> result = new ArrayList<Class>();
		Package subFolder = null;
		
		for (ControlEnhancement ce : controlEnhancements) {
			Class ceClass = factory.createClass(folder, ce.number, controlEnhancementStereotype);
			StereotypesHelper.setStereotypePropertyValue(ceClass, controlEnhancementStereotype, "Number", ce.number, true);
			StereotypesHelper.setStereotypePropertyValue(ceClass, controlEnhancementStereotype, "Title", ce.title, true);

			subFolder = factory.createPackage("Baseline Impacts", folder);
			ArrayList<Class> baselineImpactClasses = createBaselineImpacts(subFolder, baselineImpactStereotype, ce.baselineImpacts);
			StereotypesHelper.setStereotypePropertyValue(ceClass, controlEnhancementStereotype, "Baseline Impacts", baselineImpactClasses, true);
			
			subFolder = factory.createPackage("Statements", folder);
			ArrayList<Class> statementClasses = createStatements(subFolder, statementStereotype, ce.statements);
			StereotypesHelper.setStereotypePropertyValue(ceClass, controlEnhancementStereotype, "Statements", statementClasses, true);
			
			subFolder = factory.createPackage("Supplemental Guidances", folder);
			ArrayList<Class> sgClasses = createSupplementalGuidances(subFolder, supplementalGuidanceStereotype, ce.supplementalGuidances);
			StereotypesHelper.setStereotypePropertyValue(ceClass, controlEnhancementStereotype, "Supplemental Guidances", sgClasses, true);
			
			if(ce.isWithdrawn()) {
				subFolder = factory.createPackage("Withdrawn", folder);
				Class withdrawnClass = createWithdrawn(subFolder, withdrawnStereotype, ce.withdrawn);
				StereotypesHelper.setStereotypePropertyValue(ceClass, controlEnhancementStereotype, "Withdrawn", withdrawnClass, true);
			}
			
			result.add(ceClass);
		}
		return result;
	}
	
	/**
	 * 
	 * @param folder
	 * @param withdrawnStereotype
	 * @param withdrawn
	 * @return
	 */
	private Class createWithdrawn(Package folder, Stereotype withdrawnStereotype, Withdrawn withdrawn) {
		Class result = factory.createClass(folder, "Withdrawn", withdrawnStereotype);
		StereotypesHelper.setStereotypePropertyValue(result, withdrawnStereotype, "Incorporated Into", withdrawn.incorporatedInto, true);
		return result;
	}
	
	/**
	 * 
	 * @param folder
	 * @param referenceStereotype
	 * @param references
	 * @return
	 */
	private ArrayList<Class> createReferences(Package folder, Stereotype referenceStereotype, List<Reference> references) {
		ArrayList<Class> result = new ArrayList<Class>();
		for (Reference reference : references) {
			Class refClass = createReference(folder, referenceStereotype, reference);
			//StereotypesHelper.setStereotypePropertyValue(refClass, referenceStereotype, "Reference", reference.item, true);
			result.add(refClass);
		}
		return result;
	}
	
	private Class createReference(Package folder, Stereotype referenceStereotype, Reference reference) {
		Class result = factory.createClass(folder, reference.item, referenceStereotype);
		StereotypesHelper.setStereotypePropertyValue(result, referenceStereotype, "Item", reference.item, true);
		return result;
	}
}
