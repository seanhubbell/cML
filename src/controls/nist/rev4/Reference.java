/* Copyright Sean C. Hubbell All Rights Reserved */
package controls.nist.rev4;

public class Reference {
	public String item = new String();
	
	private static String sindent = "  ";
	
	public void print(String indent) {
		System.out.println(indent + "reference");
			System.out.println(indent + sindent + "item=" + item);
	}
}
