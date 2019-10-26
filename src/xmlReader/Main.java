package xmlReader;

import java.io.File;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import controls.nist.rev4.ControlAssessmentParser;
import controls.nist.rev4.ControlParser;
import controls.nist.rev4.SortControls;

class Main {

	public static void main(String argv[]) {
		//int withdrawn = 0;

		ControlParser parser = new ControlParser();
		extracteSecurityControls(parser);

		ControlAssessmentParser caparser = new ControlAssessmentParser(parser.getControls());
		extracteSecurityControlAssessments(caparser);

		Collections.sort(parser.getControls(), new SortControls());

		// for (Control c : parser.getControls()) {
		// if(c.isWithdrawn())
		// withdrawn += 1;
		// }
		// int size = parser.getControls().size();
		// System.out.println("# controls parsed = " + size);
		// System.out.println("# controls withdrawn = " + withdrawn);
		// System.out.println("# controls remaining = " + (size - withdrawn));

		/*
		 * for (Control c : parser.controls) { StringBuilder sb = new StringBuilder();
		 * sb.append("\"" + c.number + "\"" + "," + "\"" + c.title + "\"" + ",");
		 * for(String bi : c.baselineImpacts) sb.append(bi + ","); sb.append("\"" +
		 * c.priority + "\"" + "," + c.isWithdrawn() + "," + "\"" + c.family + "\"");
		 * System.out.println(sb.toString()); }
		 */

		// for (Control c : parser.getControls())
		// c.print(" ");

		controls.ia.ccis.CCI_ItemParser cciparser = new controls.ia.ccis.CCI_ItemParser();
		extracteCCIs(cciparser);
	}

	private static void extracteSecurityControls(ControlParser parser) {
		try {
			File file = new File("S:\\References\\NIST\\NIST_SP_800-53\\800-53-controls-mod-pm-priorities-added.xml");
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dBuilder.parse(file);

			// NodeList nList = doc.getElementsByTagName("controls:control");
			// int numControls = nList.getLength();
			// System.out.println("# countrols in xml = " + numControls);

			if (doc.getDocumentElement().getNodeName().equals("controls:controls")) {
				if (doc.hasChildNodes()) {
					parser.parse(doc.getChildNodes());
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static void extracteSecurityControlAssessments(ControlAssessmentParser parser) {
		try {
			File file = new File("S:\\References\\NIST\\NIST_SP_800_53a\\800-53a-objectives.xml");
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dBuilder.parse(file);

			// NodeList nList = doc.getElementsByTagName("controls:control");
			// int numControls = nList.getLength();
			// System.out.println("# countrols with assessments = " + numControls);

			if (doc.getDocumentElement().getNodeName().equals("controls:controls")) {
				if (doc.hasChildNodes()) {
					parser.extend(doc.getChildNodes());
				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static void extracteCCIs(controls.ia.ccis.CCI_ItemParser parser) {
		try {
			File file = new File("S:\\References\\CCIs\\u_cci_list\\U_CCI_List.xml");
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			NodeList nList = doc.getElementsByTagName("cci_item");
			parser.parse(nList);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}