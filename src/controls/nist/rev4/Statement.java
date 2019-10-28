/* Copyright Sean C. Hubbell All Rights Reserved */
package controls.nist.rev4;

import java.util.ArrayList;
import java.util.List;
/**
 * The Reference class is the data provided from NIST SP 800-53 and 800-53a.
 * Revision 4.
 * 
 * @author Sean C. Hubbell
 *
 */
public class Statement {
	public String description = null;
	public String number = null;
	public List<Statement> statements = new ArrayList<Statement>();	
}
