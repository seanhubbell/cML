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

import org.osgi.resource.Requirement;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.nomagic.magicdraw.actions.MDAction;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.openapi.uml.SessionManager;
import com.nomagic.magicdraw.ui.dialogs.MDDialogParentProvider;
import com.nomagic.magicdraw.uml.DiagramType;
import com.nomagic.magicdraw.uml.DiagramTypeConstants;
import com.nomagic.magicdraw.uml.Finder;
import com.nomagic.magicdraw.uml.symbols.DiagramPresentationElement;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Diagram;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.EnumerationLiteral;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

import controls.nist.SecurityRequirement;
import controls.nist.SecurityRequirementParser;

/**
 * The Load NIST Security Requirements Action to load the requirements from from
 * NIST SP 800-171 Rev. 1.
 * 
 * @author Sean C. Hubbell
 *
 */
@SuppressWarnings("serial")
class LoadSecurityRequirementsAction extends MDAction {
	private Package securityRequirementsFolder = null;
	private MDElementFactory factory = null;

	/**
	 * Loads the NIST Security Requirements from 171 Rev. 1. actions
	 * 
	 * @param id   - the menu id.
	 * @param name - the menu name.
	 */
	public LoadSecurityRequirementsAction(@CheckForNull String id, String name) {
		super(id, name, null, null);
	}

	/**
	 * The callback for the invocation of the loading of the NIST Security
	 * Requirements from 171 Rev. 1.
	 * 
	 * @param e - the action event.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		SecurityRequirementParser parser = new SecurityRequirementParser();
		File file = null;
		try {
			JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			chooser.setDialogTitle("Select 171-Requirements.xml");
			chooser.addChoosableFileFilter(new FileNameExtensionFilter("XML Files", "xml"));
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				file = chooser.getSelectedFile();
			} else {
				return;
			}

			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dBuilder.parse(file);

			NodeList nList = doc.getElementsByTagName("requirement");
			if (nList.getLength() > 0) {
				parser.parse(nList);
			}

			Project project = Application.getInstance().getProject();
			
			Package model = project.getPrimaryModel();
			if (project != null && model != null) {
				SessionManager.getInstance().createSession(project, "Generating Security Requirements");
				Stereotype securityRequirementStereotype = StereotypesHelper.getStereotype(project,
						"Security Requirement", (Profile) null);

				ArrayList<SecurityRequirement> requirements = parser.getSecurityRequirements();
				factory = new MDElementFactory(project);
				securityRequirementsFolder = factory.createPackage("NIST Security Requirements (171 Rev. 1)", model);
				
				for (SecurityRequirement requirement : requirements) {
					try {
						Class securityRequirementClass = factory.createClass(securityRequirementsFolder,
								requirement.Id, securityRequirementStereotype);
						StereotypesHelper.setStereotypePropertyValue(securityRequirementClass,
								securityRequirementStereotype, "Id", requirement.Id, true);
						StereotypesHelper.setStereotypePropertyValue(securityRequirementClass,
								securityRequirementStereotype, "Text", requirement.Text, true);
						StereotypesHelper.setStereotypePropertyValue(securityRequirementClass,
								securityRequirementStereotype, "Family", requirement.Family, true);
						
						Package requirementFolder = (Package)securityRequirementStereotype.getObjectParent();
						EnumerationLiteral type = Finder.byNameRecursively().find(requirementFolder,
								EnumerationLiteral.class, requirement.Type);
						
						StereotypesHelper.setStereotypePropertyValue(securityRequirementClass,
								securityRequirementStereotype, "Type", type, true);
						
						ArrayList<Class> rces = new ArrayList<Class>();
						ArrayList<Class> rscs = new ArrayList<Class>();
						
						if(requirement.Relevant != null) {
							String items[] = requirement.Relevant.split(",");
							for(String item : items) {
								item = item.replace("\"", "");
								item = item.replace(" ", "");
								Class control = Finder.byNameRecursively().find(model, Class.class, item);
								
								if(item.contains("(")) {
									rces.add(control);
								}
								else {
									rscs.add(control);
								}
							}
							if(rces.size() > 0) {
								StereotypesHelper.setStereotypePropertyValue(securityRequirementClass,
										securityRequirementStereotype, "Related Control Enhancements", rces, true);
							}
							if(rscs.size() > 0) {
								StereotypesHelper.setStereotypePropertyValue(securityRequirementClass,
										securityRequirementStereotype, "Related Security Controls", rscs, true);
							}
						}

					} catch (Exception ex) {
						System.err.println(
								"Failed creating controls. Error (" + requirement.Id + "): " + ex.getMessage());
					}
				}

				SessionManager.getInstance().closeSession(project);
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(MDDialogParentProvider.getProvider().getDialogOwner(), ex.getMessage());
		}
	}
}
