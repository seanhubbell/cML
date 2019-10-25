/* Copyright Sean C. Hubbell All Rights Reserved */
package controls.ia.ccis;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CCI_ItemParser {

	private ArrayList<CCI_Item> ccis = new ArrayList<CCI_Item>();

	public ArrayList<CCI_Item> getCCIs() {
		return ccis;
	}

	public void parse(NodeList children) {
		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals("cci_item")) {
					CCI_Item item = parseCCIItem(child);
					item.print("  ");
					ccis.add(item);
				}

				if (child.hasChildNodes()) {
					parse(child.getChildNodes());
				}
			}
		}
	}

	private CCI_Item parseCCIItem(Node node) {
		CCI_Item result = new CCI_Item();
		NodeList children = node.getChildNodes();
		for (int count = 0; count < children.getLength(); count++) {
			Node child = children.item(count);
			Element elem = (Element) node;
			result.id = elem.getAttribute("id");
			
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
		Element elem = (Element) node;
		result.creator = elem.getAttribute("creator");
		result.title = elem.getAttribute("title");
		result.version = elem.getAttribute("version");
		result.location = elem.getAttribute("location");
		result.index = elem.getAttribute("index");
		return result;
	}
}