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
import com.nomagic.magicdraw.ui.dialogs.MDDialogParentProvider;

import controls.ia.ccis.CCI_Item;
import controls.ia.ccis.CCI_ItemParser;

/**
 * The CCIs action is executed when the user selects the associated item in
 * Magic Draw. be leveraged by cML.
 * 
 * @author Sean C. Hubbell
 *
 */
@SuppressWarnings("serial")
class IACCIsAction extends MDAction {
	public CCI_ItemParser parser = new CCI_ItemParser();

	/**
	 * Creates an instance of the CCIs action.
	 * 
	 * @param id   the id of the action.
	 * @param name the name of the action.
	 */
	public IACCIsAction(@CheckForNull String id, String name) {
		super(id, name, null, null);
	}

	/**
	 * Action performed is called when the user selects the associate item in Magic
	 * Draw.
	 */
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
