/* Copyright Sean C. Hubbell All Rights Reserved */
package controls.ia.ccis;

import java.util.ArrayList;
import java.util.List;

public class CCI_Item {
	public String id = null;
	public String status = null;
	public String publishDate = null;
	public String contributor = null;
	public String definition = null;
	public String parameter = null;
	public String note = null;
	public String type = null;
	public List<Reference> references = new ArrayList<Reference>();
}
