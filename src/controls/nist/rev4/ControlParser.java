/* Copyright Sean C. Hubbell All Rights Reserved */
package controls.nist.rev4;


import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ControlParser {

	private ArrayList<Control> controls = new ArrayList<Control>();
	
	public ArrayList<Control> getControls() {
		return controls;
	}

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

	public Control parseControl(Node node) {
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

	public Statement parseStatement(Node node) {
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

	public SupplementalGuidance parseSupplementalGuidance(Node node) {
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

	public List<ControlEnhancement> parseControlEnhancements(Node node) {
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

	public ControlEnhancement parseControlEnhancement(Node node) {
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

	public List<Reference> parseReferences(Node node) {
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

	public Reference parseReference(Node node) {
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

	public String parseItem(Node node) {
		String result = new String();
		NodeList children = node.getChildNodes();
		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals("#text")) {
					continue;
				} else if (child.getNodeName().equals("item")) {
					result = child.getChildNodes().item(0).getNodeValue();
				} else {
					System.err.println("Parsing item unknown node: " + child.getNodeName());
				}
			}
		}
		return result;
	}

	public Withdrawn parseWithdrawn(Node node) {
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
