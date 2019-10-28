/* Copyright Sean C. Hubbell All Rights Reserved */
package controls.nist.rev4;

import java.util.Comparator;

/**
 * Sorts controls based on the control number.
 * @author Sean C. Hubbell
 *
 */
public class ControlNumberSorter implements Comparator<Control> {
	public int compare(Control a, Control b) {
		return a.number.compareTo(b.number);
	}
}
