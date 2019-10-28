/* Copyright Sean C. Hubbell All Rights Reserved */
package controls.nist.rev4;

import java.util.ArrayList;
import java.util.List;

/**
 * The Objective class is the data provided from NIST SP 800-53 and 800-53a.
 * Revision 4.
 * 
 * @author Sean C. Hubbell
 *
 */
public class Objective {
	public String number = null;
	public String decision = null;
	public List<Objective> objectives = new ArrayList<Objective>();
}
