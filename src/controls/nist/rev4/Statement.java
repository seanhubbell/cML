/* Copyright Sean C. Hubbell All Rights Reserved */
package controls.nist.rev4;


import java.util.ArrayList;
import java.util.List;

public class Statement {
	public String description = new String();
	public String number = null;
	public List<Statement> statements = new ArrayList<Statement>();
	
	private static String sindent = "  ";
	
	public void print(String indent) {
		System.out.println(indent + "statement");
		
		if(description != null  && !description.equals(""))
			System.out.println(indent + sindent + "description=" + description);

		if(number != null  && !number.equals(""))
			System.out.println(indent + sindent + "number=" + number);

		if (statements != null && statements.size() > 0) {
			System.out.println(indent + sindent + "statements");
			for (Statement s : statements)
				s.print(indent + sindent + sindent);
		}
	}	
}
