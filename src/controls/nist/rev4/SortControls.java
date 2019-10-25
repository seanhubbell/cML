/* Copyright Sean C. Hubbell All Rights Reserved */
package controls.nist.rev4;


import java.util.Comparator;

public class SortControls implements Comparator<Control> {
	public int compare(Control a, Control b) {
		return a.number.compareTo(b.number);
	}

}
