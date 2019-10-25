/* Copyright Sean C. Hubbell All Rights Reserved */
package cml;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

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
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.InstanceSpecification;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;
import com.nomagic.uml2.impl.ElementsFactory;

import controls.ia.ccis.*;

@SuppressWarnings("serial")
class IACCIsAction extends MDAction {
	public CCI_ItemParser parser = new CCI_ItemParser();

	public IACCIsAction(@CheckForNull String id, String name) {
		super(id, name, null, null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			File file = new File("S:\\References\\CCIs\\u_cci_list\\U_CCI_List.xml");
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			NodeList nList = doc.getElementsByTagName("cci_item");
			parser.parse(nList);
			ArrayList<CCI_Item> ccis = parser.getCCIs();
			
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(MDDialogParentProvider.getProvider().getDialogOwner(), ex.getMessage());
		}
	}
}
