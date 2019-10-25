/* Copyright Sean C. Hubbell All Rights Reserved */
package controls.nist.rev4;


import java.util.ArrayList;
import java.util.List;

public class PotentialAssessment {
	public String method = new String();
	public List<String> objects = new ArrayList<String>();

	private static String sindent = "  ";

	public void print(String indent) {
		System.out.println(indent + "potential-assessment");
		if (method != null && !method.equals(""))
			System.out.println(indent + sindent + "method=" + method);

		if (objects != null && objects.size() > 0) {
			System.out.println(indent + sindent + "objects");
			for (String o : objects)
				System.out.println(indent + sindent + sindent + "object=" + o);
		}
	}
}
