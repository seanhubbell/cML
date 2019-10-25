/* Copyright Sean C. Hubbell All Rights Reserved */
package controls.ia.ccis;

public class Reference {
	public String creator = null;
	public String title = null;
	public String version = null;
	public String location = null;
	public String index = null;

	private static String sindent = "  ";

	public void print(String indent) {
		System.out.println(indent + "reference");
		System.out.println(indent + sindent + "creator=" + creator);
		System.out.println(indent + sindent + "title=" + title);
		System.out.println(indent + sindent + "version=" + version);
		System.out.println(indent + sindent + "location=" + location);
		System.out.println(indent + sindent + "index=" + index);
	}
}
