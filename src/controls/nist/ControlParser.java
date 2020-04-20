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

package controls.nist;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The Control Parser extracts the data from the NIST SP 800-53 Revision 4 and
 * then populates the controls with the data.
 * 
 * @author Sean C. Hubbell
 *
 */
public class ControlParser {

	private ArrayList<Control> controls = new ArrayList<Control>();

	/**
	 * Gets the security controls.
	 * 
	 * @return the security controls.
	 */
	public ArrayList<Control> getControls() {
		return controls;
	}

	/**
	 * Parse the security controls from the given children and populate the member
	 * controls with the results.
	 * 
	 * @param children the children containing the control data.
	 */
	public void parse(NodeList children) {
		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals("controls:control")) {
					controls.add(parseControl(child));
				}

				if (child.hasChildNodes()) {
					parse(child.getChildNodes());
				}
			}
		}
	}

	/**
	 * Parse the security control from the given node and populate the appropriate
	 * member control with the results.
	 * 
	 * @param node the node containing the security control data.
	 */
	private Control parseControl(Node node) {
		Control result = new Control();
		NodeList children = node.getChildNodes();
		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals("#text")) {
					continue;
				} else if (child.getNodeName().equals("family")) {
					result.family = child.getChildNodes().item(0).getNodeValue();
				} else if (child.getNodeName().equals("number")) {
					result.number = child.getChildNodes().item(0).getNodeValue();
				} else if (child.getNodeName().equals("title")) {
					result.title = child.getChildNodes().item(0).getNodeValue();
				} else if (child.getNodeName().equals("priority")) {
					result.priority = child.getChildNodes().item(0).getNodeValue();
				} else if (child.getNodeName().equals("baseline-impact")) {
					result.baselineImpacts.add(child.getChildNodes().item(0).getNodeValue());
				} else if (child.getNodeName().equals("statement")) {
					result.statements.add(parseStatement(child));
				} else if (child.getNodeName().equals("supplemental-guidance")) {
					result.supplementalGuidances.add(parseSupplementalGuidance(child));
				} else if (child.getNodeName().equals("references")) {
					result.references = parseReferences(child);
				} else if (child.getNodeName().equals("control-enhancements")) {
					result.controlEnhancements = parseControlEnhancements(child);
				} else if (child.getNodeName().equals("withdrawn")) {
					result.withdrawn = parseWithdrawn(child);
				} else {
					System.err.println("Parsing control unknown node: " + child.getNodeName());
				}
			}
		}
		return result;
	}

	/**
	 * Parse the statement from the given node.
	 * 
	 * @param node the node containing the statement data.
	 * @return the newly created statement containing the data from the node.
	 */
	private Statement parseStatement(Node node) {
		Statement result = new Statement();
		NodeList children = node.getChildNodes();
		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals("#text")) {
					continue;
				} else if (child.getNodeName().equals("description")) {
					result.description = child.getChildNodes().item(0).getNodeValue();
				} else if (child.getNodeName().equals("number")) {
					result.number = child.getChildNodes().item(0).getNodeValue();
				} else if (child.getNodeName().equals("statement")) {
					result.statements.add(parseStatement(child));
				} else {
					System.err.println("Parsing statement unknown node: " + child.getNodeName());
				}
			}
		}
		return result;
	}

	/**
	 * Parse the supplemental guidance from the given node.
	 * 
	 * @param node the node containing the supplemental guidance data.
	 * @return the newly created supplemental guidance containing the data from the
	 *         node.
	 */
	private SupplementalGuidance parseSupplementalGuidance(Node node) {
		SupplementalGuidance result = new SupplementalGuidance();
		NodeList children = node.getChildNodes();
		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals("#text")) {
					continue;
				} else if (child.getNodeName().equals("description")) {
					result.description = child.getChildNodes().item(0).getNodeValue();
				} else if (child.getNodeName().equals("related")) {
					result.related.add(child.getChildNodes().item(0).getNodeValue());
				} else {
					System.err.println("Parsing supplemental-guidance unknown node: " + child.getNodeName());
				}
			}
		}
		return result;
	}

	/**
	 * Parse the control enhancements from the given node.
	 * 
	 * @param node the node containing the control enhancements data.
	 * @return the newly created control enhancements containing the data from the
	 *         node otherwise an empty list is returned.
	 */
	private List<ControlEnhancement> parseControlEnhancements(Node node) {
		List<ControlEnhancement> result = new ArrayList<ControlEnhancement>();
		NodeList children = node.getChildNodes();
		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals("#text")) {
					continue;
				} else if (child.getNodeName().equals("control-enhancement")) {
					result.add(parseControlEnhancement(child));
				} else {
					System.err.println("Parsing control-enhancement unknown node: " + child.getNodeName());
				}
			}
		}
		return result;
	}

	/**
	 * Parse the control enhancement from the given node.
	 * 
	 * @param node the node containing the control enhancement data.
	 * @return the newly created control enhancement containing the data from the
	 *         node.
	 */
	private ControlEnhancement parseControlEnhancement(Node node) {
		ControlEnhancement result = new ControlEnhancement();
		NodeList children = node.getChildNodes();
		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals("#text")) {
					continue;
				} else if (child.getNodeName().equals("number")) {
					result.number = child.getChildNodes().item(0).getNodeValue();
				} else if (child.getNodeName().equals("title")) {
					result.title = child.getChildNodes().item(0).getNodeValue();
				} else if (child.getNodeName().equals("baseline-impact")) {
					result.baselineImpacts.add(child.getChildNodes().item(0).getNodeValue());
				} else if (child.getNodeName().equals("statement")) {
					result.statements.add(parseStatement(child));
				} else if (child.getNodeName().equals("supplemental-guidance")) {
					result.supplementalGuidances.add(parseSupplementalGuidance(child));
				} else if (child.getNodeName().equals("withdrawn")) {
					result.withdrawn = parseWithdrawn(child);
				} else {
					System.err.println("Parsing control-enhancement unknown node: " + child.getNodeName());
				}
			}
		}
		return result;
	}

	/**
	 * Parse the references from the given node.
	 * 
	 * @param node the node containing the references data.
	 * @return the newly created references containing the data from the node.
	 */
	private List<Reference> parseReferences(Node node) {
		List<Reference> result = new ArrayList<Reference>();
		NodeList children = node.getChildNodes();
		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals("#text")) {
					continue;
				} else if (child.getNodeName().equals("reference")) {
					result.add(parseReference(child));
				} else {
					System.err.println("Parsing references unknown node: " + child.getNodeName());
				}
			}
		}
		return result;
	}

	/**
	 * Parse the reference from the given node.
	 * 
	 * @param node the node containing the reference data.
	 * @return the newly created reference containing the data from the node.
	 */
	private Reference parseReference(Node node) {
		Reference result = new Reference();
		NodeList children = node.getChildNodes();
		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals("#text")) {
					continue;
				} else if (child.getNodeName().equals("item")) {
					result.item = child.getChildNodes().item(0).getNodeValue();
				} else {
					System.err.println("Parsing reference unknown node: " + child.getNodeName());
				}
			}
		}
		return result;
	}

	/**
	 * Parse the withdrawn from the given node.
	 * 
	 * @param node the node containing the withdrawn data.
	 * @return the newly created withdrawn containing the data from the node.
	 */
	private Withdrawn parseWithdrawn(Node node) {
		Withdrawn result = new Withdrawn();
		NodeList children = node.getChildNodes();
		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals("#text")) {
					continue;
				} else if (child.getNodeName().equals("incorporated-into")) {
					result.incorporatedInto.add(child.getChildNodes().item(0).getNodeValue());
				} else {
					System.err.println("Parsing reference unknown node: " + child.getNodeName());
				}
			}
		}
		return result;
	}
}
