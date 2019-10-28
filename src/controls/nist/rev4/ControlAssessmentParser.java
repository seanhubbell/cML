/* Copyright Sean C. Hubbell All Rights Reserved */
package controls.nist.rev4;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.NullArgumentException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The Control Assessment Parser extracts the data from the NIST SP 800-53a
 * Revision 4 and then populates the controls with the data.
 * 
 * @author Sean C. Hubbell
 *
 */
public class ControlAssessmentParser {

	List<Control> controls;

	/**
	 * Creates an instance of the Control Assessment Parser.
	 * 
	 * @param cs the existing controls parsed from the NIST SP 800-53 family of
	 *           controls.
	 */
	public ControlAssessmentParser(List<Control> cs) {
		controls = cs;
	}

	/**
	 * Extend the member controls from the given children.
	 * 
	 * @param children the children containing the data to populate the member
	 *                 controls.
	 */
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

	/**
	 * Extend the control from the given node.
	 * 
	 * @param node the node containing the control data.
	 */
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
					control = findControl(number);
					if (control == null)
						throw new NullPointerException("Failed to find control " + number);
					continue;
				}

				if (control != null) {
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
	}

	/**
	 * Parse the objective from the given node.
	 * 
	 * @param node the node containing the objective data.
	 */
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

	/**
	 * Parse the potential assessments from the given node and return the list of
	 * potential assessments.
	 * 
	 * @param node the node containing the potential assessments data.
	 * @return the newly created potential assessments.
	 */
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

	/**
	 * Parse the potential assessment from the given node and return the potential
	 * assessments.
	 * 
	 * @param node the node containing the potential assessment data.
	 * @return the newly created potential assessment.
	 */
	private PotentialAssessment parsePotentialAssessment(Node node) {
		PotentialAssessment result = new PotentialAssessment();
		NodeList children = node.getChildNodes();

		Element elem = (Element) node;
		result.method = elem.getAttribute("method");
		
		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals("object")) {
					try {
						result.objects.add(child.getChildNodes().item(0).getNodeValue());
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		return result;
	}

	/**
	 * Extend the control enhancements from the given node.
	 * 
	 * @param controlEnhancements the control enhancements to extend.
	 * @param node                the node containing the control data.
	 */
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

	/**
	 * Extend the control enhancement from the given node.
	 * 
	 * @param controlEnhancements the control enhancements to extend.
	 * @param node                the node containing the control data.
	 */
	private void extendControlEnhancement(List<ControlEnhancement> controlEnhancements, Node node) {
		String number = null;
		ControlEnhancement match = null;

		NodeList children = node.getChildNodes();
		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
			if (child.getNodeType() == Node.ELEMENT_NODE) {

				if (match == null) {
					if (child.getNodeName().equals("number")) {
						number = child.getChildNodes().item(0).getNodeValue();
						match = findControlEnhancement(controlEnhancements, number);
						if (match == null) {
							// if you are in this code, this is due to the two standards (800-53 & 80053a)
							// have different text for the number.
							throw new NullArgumentException("Unable to find control enhancement: " + number);
						}
						continue;
					}
				} else if (child.getNodeName().equals("objective")) {
					match.objectives.add(parseObjective(child));
				} else if (child.getNodeName().equals("potential-assessments")) {
					match.potentialAssessments = parsePotentialAssessments(child);
				}
			}
		}
	}

	/**
	 * Find the control from the family, number and title identifiers.
	 * 
	 * @param number the control number for the control you are looking for.
	 * @return the control identified from the given family, number and title
	 *         otherwise null.
	 */
	private Control findControl(String number) {
		Control result = null;
		String mNumber = number.replaceAll("\\s+", "");

		if (controls != null) {
			for (Control control : controls) {
				String tNumber = control.number.replaceAll("\\s+", "");

				if (mNumber.equals(tNumber)) {
					result = control;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Find the control enhancement from the given title contained in the given
	 * controlEnhancements
	 * 
	 * @param controlEnhancements the controls enhancements that will be searched
	 * @param number              the number of the control enhancement
	 * @return the control enhancement matching the given title otherwise null.
	 */
	private ControlEnhancement findControlEnhancement(List<ControlEnhancement> controlEnhancements, String number) {
		// We would like to match the number as well but the standard has issues with
		// the spacing
		// being consistent between NIST SP 800-53 and NIST SP 800-53a
		String searchNumber = number.replaceAll("\\s+", "");
		ControlEnhancement result = null;
		if (controlEnhancements != null && controlEnhancements.size() > 0) {
			for (ControlEnhancement controlEnhancement : controlEnhancements) {
				String tNumber = controlEnhancement.number.replaceAll("\\s+", "");
				if (searchNumber.equals(tNumber)) {
					result = controlEnhancement;
					break;
				}
			}
		}
		return result;
	}
}
