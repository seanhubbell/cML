/* Copyright Sean C. Hubbell All Rights Reserved */
package cml;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

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
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

import controls.nist.rev4.Control;
import controls.nist.rev4.ControlAssessmentParser;
import controls.nist.rev4.ControlParser;
import controls.nist.rev4.NISTElementFactory;

@SuppressWarnings("serial")
class LoadNISTControlsAction extends MDAction {
	private Package controlsFolder = null;
	private Package prioritiesFolder = null;
	private Package baselineImpactsFolder = null;
	private MDElementFactory factory = null;
	private NISTElementFactory nistFactory = null;

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

			ControlAssessmentParser caparser = new ControlAssessmentParser(parser.getControls());
			extracteSecurityControlAssessments(caparser);

			// JOptionPane.showMessageDialog(MDDialogParentProvider.getProvider().getDialogOwner(),
			// parser.getControls().size() + " Security Controls Loaded");

			Project project = Application.getInstance().getProject();
			Package model = project.getPrimaryModel();
			if (project != null) {
				SessionManager.getInstance().createSession(project, "Generating NIST SP Rev 4 Security Controls");
				Stereotype controlStereotype = StereotypesHelper.getStereotype(project, "Security Control",
						(Profile) null);
				Stereotype baselineImpactStereotype = StereotypesHelper.getStereotype(project, "Baseline Impact",
						(Profile) null);
				Stereotype statementStereotype = StereotypesHelper.getStereotype(project, "Statement", (Profile) null);
				Stereotype supplementalGuidanceStereotype = StereotypesHelper.getStereotype(project,
						"Supplemental Guidance", (Profile) null);
				Stereotype controlEnhancementStereotype = StereotypesHelper.getStereotype(project,
						"Control Enhancement", (Profile) null);
				Stereotype referenceStereotype = StereotypesHelper.getStereotype(project, "Reference", (Profile) null);
				Stereotype withdrawnStereotype = StereotypesHelper.getStereotype(project, "Withdrawn", (Profile) null);
				Stereotype objectiveStereotype = StereotypesHelper.getStereotype(project, "Objective", (Profile) null);
				Stereotype potentialAssessmentStereotype = StereotypesHelper.getStereotype(project,
						"Potential Assessment", (Profile) null);

				ArrayList<Control> controls = parser.getControls();
				factory = new MDElementFactory(project);
				controlsFolder = factory.createPackage("NIST SP 800-53 Rev 4", model);

				Package baseFolder = factory.createPackage("base", controlsFolder);

				prioritiesFolder = factory.createPackage("priorities", baseFolder);
				factory.createEnumeration(prioritiesFolder, "Priority", Arrays.asList("P1", "P2", "P3"));

				baselineImpactsFolder = factory.createPackage("baseline-impacts", baseFolder);
				factory.createEnumeration(baselineImpactsFolder, "BaselineImpact",
						Arrays.asList("LOW", "MODERATE", "HIGH"));

				nistFactory = new NISTElementFactory(factory, prioritiesFolder, controlsFolder, baselineImpactsFolder);

				for (Control control : controls) {
					factory.createPackage(control.family, controlsFolder);
					Package familyFolder = factory.createPackage(control.family, controlsFolder);
					Package controlNumFolder = factory.createPackage(control.number, familyFolder);
					factory.createClass(controlNumFolder, control.number, controlStereotype);
				}

				for (Control control : controls) {
					nistFactory.createControl(controlStereotype, baselineImpactStereotype, statementStereotype,
							supplementalGuidanceStereotype, controlEnhancementStereotype, referenceStereotype,
							withdrawnStereotype, objectiveStereotype, potentialAssessmentStereotype, control);
				}
				SessionManager.getInstance().closeSession(project);
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(MDDialogParentProvider.getProvider().getDialogOwner(), ex.getMessage());
		}
	}

	/**
	 * 
	 * @param parser
	 */
	private static void extracteSecurityControlAssessments(ControlAssessmentParser parser) {
		try {
			File file = new File("S:\\References\\NIST\\NIST_SP_800_53a\\800-53a-objectives.xml");
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dBuilder.parse(file);

			// NodeList nList = doc.getElementsByTagName("controls:control");
			// int numControls = nList.getLength();
			// System.out.println("# countrols with assessments = " + numControls);

			if (doc.getDocumentElement().getNodeName().equals("controls:controls")) {
				if (doc.hasChildNodes()) {
					parser.extend(doc.getChildNodes());
				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
