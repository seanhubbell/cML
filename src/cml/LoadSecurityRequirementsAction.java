/* Copyright Sean C. Hubbell All Rights Reserved */
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
import com.nomagic.magicdraw.openapi.uml.SessionManager;
import com.nomagic.magicdraw.ui.dialogs.MDDialogParentProvider;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

import controls.nist.SecurityRequirement;
import controls.nist.SecurityRequirementParser;

/**
 *  The Load NIST Security Requirements Action to load the requirements from from NIST SP 800-171 Rev. 1.
 * @author Sean C. Hubbell
 *
 */
@SuppressWarnings("serial")
class LoadSecurityRequirementsAction extends MDAction {
	private Package securityRequirementsFolder = null;
	private MDElementFactory factory = null;

	/**
	 * Loads the NIST Security Requirements from 171 Rev. 1. actions
	 * @param id - the menu id.
	 * @param name - the menu name.
	 */
	public LoadSecurityRequirementsAction(@CheckForNull String id, String name) {
		super(id, name, null, null);
	}

	/**
	 * The callback for the invocation of the loading of the NIST Security Requirements from 171 Rev. 1.
	 * @param e - the action event.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		SecurityRequirementParser parser = new SecurityRequirementParser();
		File file = null;
		try {
			JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			chooser.setDialogTitle("Select 171 Appendix F - filtered.xml");
			chooser.addChoosableFileFilter(new FileNameExtensionFilter("XML Files", "xml"));			
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				file = chooser.getSelectedFile();
			}
			
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dBuilder.parse(file);

			NodeList nList = doc.getElementsByTagName("Requirement");
			if (nList.getLength() > 0) {
				parser.parse(nList);
			}
			
			String indent = "  ";
			for (SecurityRequirement requirement : parser.getSecurityRequirements()) {
				requirement.print(indent);
			}

			Project project = Application.getInstance().getProject();
			Package model = project.getPrimaryModel();
			if (project != null && model != null) {
				SessionManager.getInstance().createSession(project, "Generating Security Requirements");
				Stereotype securityRequirementStereotype = StereotypesHelper.getStereotype(project, "Security Requirement", (Profile) null);

				ArrayList<SecurityRequirement> requirements = parser.getSecurityRequirements();
				factory = new MDElementFactory(project);
				securityRequirementsFolder = factory.createPackage("NIST Security Requirements (171 Rev. 1)", model);

				for (SecurityRequirement requirement : requirements) {
					try {
						Class securityRequirementClass = factory.createClass(securityRequirementsFolder, requirement.Identifier, securityRequirementStereotype);
						StereotypesHelper.setStereotypePropertyValue(securityRequirementClass, securityRequirementStereotype, "Id",
								requirement.Identifier, true);
						StereotypesHelper.setStereotypePropertyValue(securityRequirementClass, securityRequirementStereotype, "Text",
								requirement.Text, true);
						
					} catch (Exception ex) {
						System.err.println("Failed creating controls. Error (" + requirement.Identifier + "): " + ex.getMessage());
					}
				}

				SessionManager.getInstance().closeSession(project);
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(MDDialogParentProvider.getProvider().getDialogOwner(), ex.getMessage());
		}
	}
}
