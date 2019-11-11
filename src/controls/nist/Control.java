/* Copyright Sean C. Hubbell All Rights Reserved */
package controls.nist;

import java.util.ArrayList;
import java.util.List;

/**
 * The Control class is the data provided from NIST SP 800-53 and 800-53a.
 * Revision 4.
 * 
 * @author Sean C. Hubbell
 *
 */
public class Control {
	/**
	 * The data provided by NIST SP 800-53
	 */
	public String family = null;
	public String number = null;
	public String title = null;
	public String priority = null;
	public List<String> baselineImpacts = new ArrayList<String>();
	public List<Statement> statements = new ArrayList<Statement>();
	public List<SupplementalGuidance> supplementalGuidances = new ArrayList<SupplementalGuidance>();
	/**
	 * Control Enhancements can come from NIST SP 800-53a Rev. 4 as well
	 */
	public List<ControlEnhancement> controlEnhancements = new ArrayList<ControlEnhancement>();
	public List<Reference> references = new ArrayList<Reference>();
	public Withdrawn withdrawn = null;

	/**
	 * The objectives and protential assessments come from NIST SP 800-53a
	 */
	public List<Objective> objectives = new ArrayList<Objective>();
	public List<PotentialAssessment> potentialAssessments = new ArrayList<PotentialAssessment>();

	/**
	 * Has this control been withdrawn?
	 * 
	 * @return true if this control has been withdrawn otherwise false.
	 */
	public boolean isWithdrawn() {
		return (withdrawn != null && withdrawn.incorporatedInto != null && withdrawn.incorporatedInto.size() > 0);
	}
}
