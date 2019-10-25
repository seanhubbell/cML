/* Copyright Sean C. Hubbell All Rights Reserved */
package controls.nist.rev4;


import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ControlAssessmentParser {

	List<Control> controls;

	public ControlAssessmentParser(List<Control> cs) {
		controls = cs;
	}

	public void extend(NodeList children) {
		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals("controls:control")) {
					extendControl(child);
				}

				if (child.hasChildNodes()) {
					extend(child.getChildNodes());
				}
			}
		}
	}

	private void extendControl(Node node) {
		String family = null;
		String number = null;
		String title = null;
		Control control = null;

		NodeList children = node.getChildNodes();
		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
			if (child.getNodeType() == Node.ELEMENT_NODE) {

				if (control == null) {
					if (child.getNodeName().equals("family")) {
						family = child.getChildNodes().item(0).getNodeValue();
					} else if (child.getNodeName().equals("number")) {
						number = child.getChildNodes().item(0).getNodeValue();
					} else if (child.getNodeName().equals("title")) {
						title = child.getChildNodes().item(0).getNodeValue();
					}
				}

				if (control == null && family != null && number != null && title != null) {
					if(control == null) {
						control = findControl(family, number, title);
						if (control == null)
							System.err.println("Failed to find control : " + family + ":" + number + ":" + title);
						continue;
					}
				}
				
				if (child.getNodeName().equals("objective")) {
					control.objectives.add(parseObjective(child));
				} else if (child.getNodeName().equals("potential-assessments")) {
					control.potentialAssessments = parsePotentialAssessments(child);
				} else if (child.getNodeName().equals("control-enhancements")) {
					extendControlEnhancements(control.controlEnhancements, child);
				}
			}
		}
	}

	private Objective parseObjective(Node node) {
		Objective result = new Objective();
		NodeList children = node.getChildNodes();
		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals("decision")) {
					result.decision = child.getChildNodes().item(0).getNodeValue();
				} else if (child.getNodeName().equals("number")) {
					result.number = child.getChildNodes().item(0).getNodeValue();
				} else if (child.getNodeName().equals("objective")) {
					result.objectives.add(parseObjective(child));
				}
			}
		}
		return result;
	}

	private List<PotentialAssessment> parsePotentialAssessments(Node node) {
		List<PotentialAssessment> result = new ArrayList<PotentialAssessment>();
		NodeList children = node.getChildNodes();
		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals("potential-assessment")) {
					result.add(parsePotentialAssessment(child));
				}
			}
		}
		return result;
	}

	private PotentialAssessment parsePotentialAssessment(Node node) {
		PotentialAssessment result = new PotentialAssessment();
		NodeList children = node.getChildNodes();
		
		Element elem = (Element) node;
		result.method = elem.getAttribute("method");
		
		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals("object")) {
					result.objects.add(child.getChildNodes().item(0).getNodeValue());
				}
			}
		}
		return result;
	}

	private void extendControlEnhancements(List<ControlEnhancement> controlEnhancements, Node node) {
		NodeList children = node.getChildNodes();
		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals("control-enhancement")) {
					extendControlEnhancement(controlEnhancements, child);
				}
			}
		}
	}

	private void extendControlEnhancement(List<ControlEnhancement> controlEnhancements, Node node) {
		String title = null;
		ControlEnhancement match = null;

		NodeList children = node.getChildNodes();
		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
			if (child.getNodeType() == Node.ELEMENT_NODE) {

				if (match == null) {
					if (child.getNodeName().equals("title")) {
						title = child.getChildNodes().item(0).getNodeValue();
						match = findControlEnhancement(controlEnhancements, title);
						continue;
					}
				} else if (child.getNodeName().equals("objective")) {
					match.objectives.add(parseObjective(child));
				}
			}
		}
	}
	
	private Control findControl(String family, String number, String title) {
		Control result = null;
		if(controls != null) {
			for(Control control : controls) {
				if(family.equals(control.family) && number.equals(number) && title.equals(title)) {
					result = control;
					break;
				}
			}
		}
		return result;
	}
	
	private ControlEnhancement findControlEnhancement(List<ControlEnhancement> controlEnhancements, String title) {
		// We would like to match the number as well but the standard has issues with
		// the spacing
		// being consistent between NIST SP 800-53 and NIST SP 800-53a
		ControlEnhancement result = null;
		if (controlEnhancements != null && controlEnhancements.size() > 0) {
			for (ControlEnhancement controlEnhancement : controlEnhancements) {
				if (title.equals(title)) {
					result = controlEnhancement;
					break;
				}
			}
		}
		return result;
	}
}
