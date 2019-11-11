package controls.nist;

public class SecurityRequirement {
	public String Identifier = null;
	public String Text = null;
	
	public void print(String indent) {
		System.out.println("Security Requirement");
		if(Identifier != null)
			System.out.println(indent + "Id: " + Identifier);
		
		if(Text != null)
			System.out.println(indent + "Text: " + Text);
	}
}
