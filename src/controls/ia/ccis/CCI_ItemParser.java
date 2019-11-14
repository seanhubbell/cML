/* Copyright Sean C. Hubbell All Rights Reserved */
package controls.ia.ccis;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The CCI Item Parser is used to parse the XML data containing the informastion for the IA standard.
 * 
 * @author Sean C. Hubbell
 *
 */
public class CCI_ItemParser {

	private ArrayList<CCI_Item> ccis = new ArrayList<CCI_Item>();
	
	/**
	 * Gets the current list of CCIs.
	 * @return the current list of CCIs.
	 */
	public ArrayList<CCI_Item> getCCIs() {
		return ccis;
	}

	/**
	 * Parse the CCI items from the given Xml Node children and populate the CCIs.
	 * @param children - the Xml children containing the CCI data.
	 */
	public void parse(NodeList children) {
		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals("cci_item")) {
					CCI_Item item = parseCCIItem(child);
					ccis.add(item);
				}

				if (child.hasChildNodes()) {
					parse(child.getChildNodes());
				}
			}
		}
	}

	/**
	 * Parse the CCI item from the given Xml Node.
	 * @param node - the node containing the CCI data from the standard.
	 * @return the newly created and populated CCI_Item.
	 */
	private CCI_Item parseCCIItem(Node node) {
		CCI_Item result = new CCI_Item();

		Element elem = (Element) node;
		result.id = elem.getAttribute("id");		
		NodeList children = node.getChildNodes();

		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
	
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals("#text")) {
					continue;
				} else if (child.getNodeName().equals("status")) {
					result.status = child.getChildNodes().item(0).getNodeValue();
					continue;
				} else if (child.getNodeName().equals("publishdate")) {
					result.publishDate = child.getChildNodes().item(0).getNodeValue();
					continue;
				} else if (child.getNodeName().equals("contributor")) {
					result.contributor = child.getChildNodes().item(0).getNodeValue();
					continue;
				} else if (child.getNodeName().equals("definition")) {
					result.definition = child.getChildNodes().item(0).getNodeValue();
					continue;
				} else if (child.getNodeName().equals("type")) {
					result.type = child.getChildNodes().item(0).getNodeValue();
					continue;
				} else if (child.getNodeName().equals("parameter")) {
					result.parameter = child.getChildNodes().item(0).getNodeValue();
					continue;
				} else if (child.getNodeName().equals("note")) {
					result.note = child.getChildNodes().item(0).getNodeValue();
					continue;
				} else if (child.getNodeName().equals("references")) {
					result.references = parseReferences(child);
					continue;
				} else {
					System.err.println("Parsing cci_item unknown node: " + child.getNodeName());
				}	
			}
		}
		return result;
	}

	/**
	 * Parses the references associated with a given CCI.
	 * @param node - the parent node containing the reference children.
	 * @return the newly created and populated list of references.
	 */
	public List<Reference> parseReferences(Node node) {
		List<Reference> result = new ArrayList<Reference>();
		NodeList children = node.getChildNodes();
		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals("#text")) {
					continue;
				} else if (child.getNodeName().equals("reference")) {
					System.out.println("parsing reference");
					result.add(parseReference(child));
				} else {
					System.err.println("Parsing references unknown node: " + child.getNodeName());
				}
			}
		}
		return result;
	}
	
	/**
	 * Parses the reference associated with a given CCI.
	 * @param node - the parent node containing the reference.
	 * @return the newly created and populated reference.
	 */
	public Reference parseReference(Node node) {
		Reference result = new Reference();
		Element elem = (Element) node;
		result.creator = elem.getAttribute("creator");
		result.title = elem.getAttribute("title");
		result.version = elem.getAttribute("version");
		result.location = elem.getAttribute("location");
		result.index = elem.getAttribute("index");
		return result;
	}
}