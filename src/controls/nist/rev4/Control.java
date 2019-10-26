/* Copyright Sean C. Hubbell All Rights Reserved */
package controls.nist.rev4;

import java.util.ArrayList;
import java.util.List;

public class Control {
	// From NIST SP 800-53
	public String family = null;
	public String number = null;
	public String title = null;
	public String priority = null;
	public List<String> baselineImpacts = new ArrayList<String>();
	public List<Statement> statements = new ArrayList<Statement>();
	public List<SupplementalGuidance> supplementalGuidances = new ArrayList<SupplementalGuidance>();
	public List<ControlEnhancement> controlEnhancements = new ArrayList<ControlEnhancement>();
	public List<Reference> references = new ArrayList<Reference>();
	public Withdrawn withdrawn = null;

	// From NIST SP 800-53a Rev. 4
	public List<Objective> objectives = new ArrayList<Objective>();
	public List<PotentialAssessment> potentialAssessments = new ArrayList<PotentialAssessment>();
	// Control Enhancements can come from NIST SP 800-53a Rev. 4 as well

	private static String sindent = "  ";

	public boolean isWithdrawn() {
		return (withdrawn != null && withdrawn.incorporatedInto != null && withdrawn.incorporatedInto.size() > 0);
	}

	public void print(String indent) {
		System.out.println(indent + "control");
		System.out.println(indent + sindent + "number=" + number);
		System.out.println(indent + sindent + "title=" + title);
		System.out.println(indent + sindent + "family=" + family);

		if (withdrawn == null || withdrawn.equals("")) {

			if (priority != null && !priority.equals(""))
				System.out.println(indent + sindent + "priority=" + priority);
			else
				System.out.println(indent + sindent + "priority=NA");

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

			if (controlEnhancements != null && controlEnhancements.size() > 0) {
				System.out.println(indent + sindent + "control-enhancements");
				for (ControlEnhancement ce : controlEnhancements)
					ce.print(indent + sindent + sindent);
			}

			if (references != null && references.size() > 0) {
				System.out.println(indent + sindent + "references");
				for (Reference r : references)
					r.print(indent + sindent + sindent);
			}

			if (objectives != null && objectives.size() > 0) {
				System.out.println(indent + sindent + "objectives");
				for (Objective o : objectives)
					o.print(indent + sindent + sindent);
			}

			if (potentialAssessments != null && potentialAssessments.size() > 0) {
				System.out.println(indent + sindent + "potential-assessments");
				for (PotentialAssessment pa : potentialAssessments)
					pa.print(indent + sindent + sindent);
			}
		} else
			withdrawn.print(indent + sindent);
	}
}
