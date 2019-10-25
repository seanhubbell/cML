/* Copyright Sean C. Hubbell All Rights Reserved */
package controls.nist.rev4;


import java.util.ArrayList;
import java.util.List;

public class Objective {
	public String number = new String();
	public String decision = new String();
	public List<Objective> objectives = new ArrayList<Objective>();
	
	private static String sindent = "  ";
	
	public void print(String indent) {
		System.out.println(indent + "objective");
		if (number != null && !number.equals(""))
			System.out.println(indent + sindent +"number=" + number);

		if (decision != null && !decision.equals(""))
			System.out.println(indent + sindent +"decision=" + decision);

		if (objectives != null && objectives.size() > 0) {
			System.out.println(indent + sindent + "objectives");
			for (Objective o : objectives)
				o.print(indent + sindent + sindent);
		}
	}
}
