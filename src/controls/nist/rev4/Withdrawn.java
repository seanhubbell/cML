/* Copyright Sean C. Hubbell All Rights Reserved */
package controls.nist.rev4;


import java.util.ArrayList;
import java.util.List;

public class Withdrawn {
	public List<String> incorporatedInto = new ArrayList<String>();
	private static String sindent = "  ";
	
	public void print(String indent) {
		if (incorporatedInto != null && incorporatedInto.size() > 0) {
			System.out.println(indent + "withdrawn");
			for (String i : incorporatedInto)
				System.out.println(indent + sindent + "incorporated-into=" + i);
		}
	}
}
