/* Copyright Sean C. Hubbell All Rights Reserved */
package controls.nist.rev4;


import java.util.ArrayList;
import java.util.List;

public class ControlEnhancement {
	// NIST SP 800-53a
	public String number = new String();
	public String title = new String();
	public List<String> baselineImpacts = new ArrayList<String>();
	public List<Statement> statements = new ArrayList<Statement>();
	public List<SupplementalGuidance> supplementalGuidances = new ArrayList<SupplementalGuidance>();
	public Withdrawn withdrawn = new Withdrawn();
	
	// NIST SP 800-53a
	public List<Objective> objectives = new ArrayList<Objective>();
	
	private static String sindent = "  ";

	public boolean isWithdrawn() {
		return (withdrawn != null && withdrawn.incorporatedInto != null && withdrawn.incorporatedInto.size() > 0);
	}
	
	public void print(String indent) {
		System.out.println(indent + "control-enhancement");
		if (number != null && !number.equals(""))
			System.out.println(indent + sindent + "number=" + number);

		if (title != null && !title.equals(""))
			System.out.println(indent + sindent + "title=" + title);

		if (withdrawn != null)
			withdrawn.print(indent + sindent);
		
		if (baselineImpacts != null && baselineImpacts.size() > 0) {
			System.out.println(indent + sindent + "baseline-impacts");
			for (String bi : baselineImpacts)
				System.out.println(indent + sindent + sindent + "baseline-impact=" + bi);
		}

		if (statements != null && statements.size() > 0) {
			System.out.println(indent + sindent + "statements");
			for (Statement s : statements)
				s.print(indent + sindent + sindent);
		}

		if (supplementalGuidances != null && supplementalGuidances.size() > 0) {
			System.out.println(indent + sindent + "suplemental-guidances");
			for (SupplementalGuidance sg : supplementalGuidances)
				sg.print(indent + sindent + sindent);
		}

		if (objectives != null && objectives.size() > 0) {
			System.out.println(indent + sindent + "objectives");
			for (Objective o : objectives)
				o.print(indent + sindent + sindent);
		}
	}
}
