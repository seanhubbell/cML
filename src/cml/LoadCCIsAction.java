/*
Copyright 2019 Sean C. Hubbell

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
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
import com.nomagic.magicdraw.openapi.uml.SessionManager;
import com.nomagic.magicdraw.ui.dialogs.MDDialogParentProvider;
import com.nomagic.magicdraw.uml.Finder;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

import controls.ia.ccis.CCI_Item;
import controls.ia.ccis.CCI_ItemElementFactory;
import controls.ia.ccis.CCI_ItemParser;

/**
 * The Load CCIs Controls Action is the action that will parse the IA standard
 * CCIs file published on 2016-06-27.
 * 
 * @author Sean C. Hubbell
 *
 */
@SuppressWarnings("serial")
class LoadCCIsAction extends MDAction {
	private Package ccisFolder = null;
	private MDElementFactory factory = null;
	private CCI_ItemElementFactory ccisFactory = null;

	/**
	 * Loads the NIST Control actions
	 * 
	 * @param id   - the menu id.
	 * @param name - the menu name.
	 */
	public LoadCCIsAction(@CheckForNull String id, String name) {
		super(id, name, null, null);
	}

	/**
	 * The callback for the invocation of the loading of the NIST Controls.
	 * 
	 * @param e - the action event.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		CCI_ItemParser parser = new CCI_ItemParser();
		File file = null;
		try {
			JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			chooser.setDialogTitle("Select U_CCI_List.xml file");
			chooser.addChoosableFileFilter(new FileNameExtensionFilter("XML Files", "xml"));
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				file = chooser.getSelectedFile();
			} else {
				return;
			}

			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dBuilder.parse(file);

			NodeList nList = doc.getElementsByTagName("cci_items");
			int numControls = nList.getLength();
			System.out.println("# cci_items = " + numControls);

			if (nList.getLength() > 0) {
				parser.parse(nList);
			}

			Project project = Application.getInstance().getProject();
			Package model = project.getPrimaryModel();
			if (project != null && model != null) {
				SessionManager.getInstance().createSession(project, "Generating CCIs");

				Stereotype referenceStereotype = (Stereotype) Finder.byQualifiedName().find(project,
						"cML::IA::Reference::Reference");
				Stereotype cci_ItemStereotype = StereotypesHelper.getStereotype(project, "CCI_Item", (Profile) null);

				ArrayList<CCI_Item> ccis = parser.getCCIs();
				factory = new MDElementFactory(project);
				ccisFolder = factory.createPackage("CCIs (2016-06-27)", model);
				ccisFactory = new CCI_ItemElementFactory(factory, ccisFolder);

				for (CCI_Item item : ccis) {
					try {
						factory.createPackage(item.id, ccisFolder);
						Package itemIdFolder = factory.createPackage(item.id, ccisFolder);
						factory.createClass(itemIdFolder, item.id, cci_ItemStereotype);
					} catch (Exception ex) {
						System.err.println("Failed creating cci. Error (" + item.id + "): " + ex.getMessage());
					}
				}

				Package enumsFolder = (Package) Finder.byQualifiedName().find(project, "cML::IA::Enums");

				for (CCI_Item item : ccis) {
					try {
						ccisFactory.createCCI(enumsFolder, cci_ItemStereotype, referenceStereotype, item);
						System.out.println("Populating control : " + item.id);
					} catch (Exception ex) {
						System.err.println("Failed creating cci. Error (" + item.id + "): " + ex.getMessage());
					}
				}
				SessionManager.getInstance().closeSession(project);
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(MDDialogParentProvider.getProvider().getDialogOwner(), ex.getMessage());
		}
	}
}
