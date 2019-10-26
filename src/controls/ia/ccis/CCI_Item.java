/* Copyright Sean C. Hubbell All Rights Reserved */
package controls.ia.ccis;

import java.util.List;

public class CCI_Item {
	public String id = null;
	public String status = null;
	public String publishDate = null;
	public String contributor = null;
	public String definition = null;
	public String parameter = null;
	public String note = null;
	public String type = null;
	public List<Reference> references = null;

	private static String sindent = "  ";

	public void print(String indent) {
		System.out.println(indent + "cci_item");
		System.out.println(indent + sindent + "id=" + id);
		System.out.println(indent + sindent + "status=" + status);
		System.out.println(indent + sindent + "publishdate=" + publishDate);
		System.out.println(indent + sindent + "contributor=" + contributor);
		System.out.println(indent + sindent + "definition=" + definition);
		System.out.println(indent + sindent + "parameter=" + parameter);
		System.out.println(indent + sindent + "note=" + note);
		System.out.println(indent + sindent + "type=" + type);

		if (references != null && references.size() > 0) {
			System.out.println(indent + sindent + "references");
			for (Reference r : references)
				r.print(indent + sindent + sindent);
		}
	}
}
