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
				if (child.getNodeName().equals("requirement")) {
					securityRequirements.add(parseRequirement(child));
				}
			}
		}
	}

	/**
	 * Parse the security requirement from the given node and populate the
	 * appropriate member security requirement with the results.
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
				} else if (child.getNodeName().equals("id")) {
					result.Id = child.getChildNodes().item(0).getNodeValue();
					System.out.println("Parsed Identifier: " + result.Id);
				} else if (child.getNodeName().equals("family")) {
					result.Family = child.getChildNodes().item(0).getNodeValue();
				} else if (child.getNodeName().equals("text")) {
					result.Text = child.getChildNodes().item(0).getNodeValue();
				} else if (child.getNodeName().equals("type")) {
					result.Type = child.getChildNodes().item(0).getNodeValue();
				}  else if (child.getNodeName().equals("relevant")) {
					result.Relevant = child.getChildNodes().item(0).getNodeValue();
				} else {
					System.err.println("Parsing security requirement unknown node: " + child.getNodeName());
				}
			}
		}
		return result;
	}
}
