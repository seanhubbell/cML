/*
Copyright 2020 Sean C. Hubbell

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files(the "Software"),
to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package cml;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

import javax.annotation.CheckForNull;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.nomagic.magicdraw.actions.MDAction;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.openapi.uml.ModelElementsManager;
import com.nomagic.magicdraw.openapi.uml.SessionManager;
import com.nomagic.magicdraw.ui.dialogs.MDDialogParentProvider;
import com.nomagic.magicdraw.uml.Finder;
import com.nomagic.uml2.ext.jmi.helpers.ModelHelper;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Diagram;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

import controls.nist.Control;
import controls.nist.ControlAssessmentParser;
import controls.nist.ControlParser;
import controls.nist.NISTElementFactory;
import controls.nist.Statement;

/**
 * The Load NIST Controls Action is the action that will parse the NIST SP
 * 800-54 and NIST SP 800-53a Revision 4 files.
 * 
 * @author Sean C. Hubbell
 *
 */
@SuppressWarnings("serial")
class LoadNISTControlsAction extends MDAction {
	private Package controlsFolder = null;
	private MDElementFactory factory = null;
	private NISTElementFactory nistFactory = null;

	/**
	 * Loads the NIST Control actions
	 * 
	 * @param id   - the menu id.
	 * @param name - the menu name.
	 */
	public LoadNISTControlsAction(@CheckForNull String id, String name) {
		super(id, name, null, null);
	}

	/**
	 * The callback for the invocation of the loading of the NIST Controls.
	 * 
	 * @param e - the action event.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		ControlParser parser = new ControlParser();
		File file = null;
		try {
			JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			chooser.setDialogTitle("Select 800-53-controls-mod-pm-priorities-added.xml");
			chooser.addChoosableFileFilter(new FileNameExtensionFilter("XML Files", "xml"));
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				file = chooser.getSelectedFile();
			} else {
				return;
			}

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

			try {
				ControlAssessmentParser caparser = new ControlAssessmentParser(parser.getControls());
				extracteSecurityControlAssessments(caparser);
			} catch (Exception ex) {
				System.err.println("Failed adding NIST SP 800 53a items. Error: " + ex.getMessage());
			}

			// JOptionPane.showMessageDialog(MDDialogParentProvider.getProvider().getDialogOwner(),
			// parser.getControls().size() + " Security Controls Loaded");

			Project project = Application.getInstance().getProject();
			Package model = project.getPrimaryModel();
			if (project != null && model != null) {
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

				Stereotype referenceStereotype = (Stereotype) Finder.byQualifiedName().find(project,
						"cML::NIST::Reference::Reference");

				Stereotype withdrawnStereotype = StereotypesHelper.getStereotype(project, "Withdrawn", (Profile) null);
				Stereotype objectiveStereotype = StereotypesHelper.getStereotype(project, "Objective", (Profile) null);
				Stereotype potentialAssessmentStereotype = StereotypesHelper.getStereotype(project,
						"Potential Assessment", (Profile) null);

				ArrayList<Control> controls = parser.getControls();
				factory = new MDElementFactory(project);
				controlsFolder = factory.createPackage("NIST Security Controls (53 & 53a Rev. 4)", model);
				nistFactory = new NISTElementFactory(factory, controlsFolder);

				for (Control control : controls) {
					try {
						factory.createPackage(control.family, controlsFolder);
						Package familyFolder = factory.createPackage(control.family, controlsFolder);
						Package controlNumFolder = factory.createPackage(control.number, familyFolder);
						factory.createClass(controlNumFolder, control.number, controlStereotype);
					} catch (Exception ex) {
						System.err.println(
								"Failed creating controls. Error (" + control.number + "): " + ex.getMessage());
					}
				}

				Package enumsFolder = (Package) Finder.byQualifiedName().find(project, "cML::NIST::Enums");

				for (Control control : controls) {
					try {
						nistFactory.createControl(enumsFolder, controlStereotype, baselineImpactStereotype,
								statementStereotype, supplementalGuidanceStereotype, controlEnhancementStereotype,
								referenceStereotype, withdrawnStereotype, objectiveStereotype,
								potentialAssessmentStereotype, control);
						System.out.println("Populating control : " + control.number);
					} catch (Exception ex) {
						System.err.println(
								"Failed populating controls. Error (" + control.number + "): " + ex.getMessage());
					}
				}

				Package glossaryPackage = factory.createPackage("Glossary", controlsFolder);
				Diagram glossary = factory.createGlossaryTableDiagram(glossaryPackage);
				Stereotype termStereotype = StereotypesHelper.getStereotype(project, "Term", (Profile) null);
				com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class term;

				for (Control control : controls) {
					term = factory.createClass(glossaryPackage, control.number, termStereotype);
					ModelHelper.setComment(term, control.title);
					ModelElementsManager.getInstance().addElement(glossary, term);

					if (control.statements != null && control.statements.size() > 0) {
						for (Statement statement : control.statements) {
							if (statement.number != null && statement.description != null
									&& !statement.number.equals("") && !statement.description.equals("")) {
								term = factory.createClass(glossaryPackage, statement.number, termStereotype);
								ModelHelper.setComment(term, statement.description);

							}
						}
					}
				}

				SessionManager.getInstance().closeSession(project);
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(MDDialogParentProvider.getProvider().getDialogOwner(), ex.getMessage());
		}
	}

	/**
	 * Extract the security control assessment.
	 * 
	 * @param parser - the parser to used to extract the security control
	 *               assessments from NIST SP 800-53a rev. 4.
	 */
	private static void extracteSecurityControlAssessments(ControlAssessmentParser parser) {
		File file = null;
		try {
			JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			chooser.setDialogTitle("Select 800-53a-objectives.xmll");
			chooser.addChoosableFileFilter(new FileNameExtensionFilter("XML Files", "xml"));
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				file = chooser.getSelectedFile();
			} else {
				return;
			}

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
			e.printStackTrace();
		}
	}
}
