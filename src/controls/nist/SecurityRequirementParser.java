/* Copyright Sean C. Hubbell All Rights Reserved */
package controls.nist;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The Control Parser extracts the data from the NIST SP 800-53 Revision 4 and
 * then populates the controls with the data.
 * 
 * @author Sean C. Hubbell
 *
 */
public class SecurityRequirementParser {

	private ArrayList<SecurityRequirement> securityRequirements = new ArrayList<SecurityRequirement>();

	/**
	 * Gets the security requirements.
	 * 
	 * @return the security requirement.
	 */
	public ArrayList<SecurityRequirement> getSecurityRequirements() {
		return securityRequirements;
	}

	/**
	 * Parse the security requirements from the given children
	 * 
	 * @param children containing the security requirement data
	 */
	public void parse(NodeList children) {
		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals("Requirement")) {
					securityRequirements.add(parseRequirement(child));
				}
			}
		}
	}

	/**
	 * Parse the security requirement from the given node and populate the appropriate
	 * member security requirement with the results.
	 * 
	 * @param node the node containing the security requirement data
	 */
	private SecurityRequirement parseRequirement(Node node) {
		SecurityRequirement result = new SecurityRequirement();
		NodeList children = node.getChildNodes();
		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals("#text")) {
					continue;
				} else if (child.getNodeName().equals("Id")) {
					result.Identifier = child.getChildNodes().item(0).getNodeValue();
					System.out.println("Parsed Identifier: " + result.Identifier);
				} else if (child.getNodeName().equals("Text")) {
					result.Text = child.getChildNodes().item(0).getNodeValue();
				} else {
					System.err.println("Parsing security requirement unknown node: " + child.getNodeName());
				}
			}
		}
		return result;
	}
}
