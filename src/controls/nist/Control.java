/*
Copyright 2019 Sean C. Hubbell

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
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
