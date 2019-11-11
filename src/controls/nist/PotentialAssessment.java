/* Copyright Sean C. Hubbell All Rights Reserved */
package controls.nist;

import java.util.ArrayList;
import java.util.List;

/**
 * The Potential Assessment class is the data provided from NIST SP 800-53 and
 * 800-53a. Revision 4.
 * 
 * @author Sean C. Hubbell
 *
 */
public class PotentialAssessment {
	public String method = null;
	public List<String> objects = new ArrayList<String>();
}
