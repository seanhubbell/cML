/*
Copyright 2020 Sean C. Hubbell

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files(the "Software"),
to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

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
