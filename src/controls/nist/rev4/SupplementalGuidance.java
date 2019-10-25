/* Copyright Sean C. Hubbell All Rights Reserved */
package controls.nist.rev4;


import java.util.ArrayList;
import java.util.List;

public class SupplementalGuidance {
	public String description = new String();
	public List<String> related = new ArrayList<String>();
	
	private static String sindent = "  ";

	public void print(String indent) {
		System.out.println(indent + "supplemental-guidance");
		if(description != null  && !description.equals(""))
			System.out.println(indent + sindent + "description=" + description);
		
		if (related != null && related.size() > 0) {
			System.out.println(indent + sindent + "related");
			for (String r : related)
				System.out.println(indent + sindent + sindent + "related=" + r);
		}
	}
}
