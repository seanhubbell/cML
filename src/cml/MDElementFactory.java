/* Copyright Sean C. Hubbell All Rights Reserved */
package cml;

import java.util.List;

import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Enumeration;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.EnumerationLiteral;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.PackageableElement;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;
import com.nomagic.uml2.impl.ElementsFactory;

public class MDElementFactory {

	private Project project = null;
	
	public MDElementFactory(Project p){
		project = p;
	}

	public Package createPackage(String name, Package parent)
	{
		Package result = null;
		if (name != null && !name.equals("") && project != null && parent != null) {
			result = getPackage(parent, name);
			if (result == null) {
				ElementsFactory factory = Project.getProject(parent).getElementsFactory();
				result = factory.createPackageInstance();
				result.setOwner(parent);
				result.setName(name);
			}
		}
		return result;
	}

	public void createPriorities(Package folder, String enumName, List<String> literals)
	{
		ElementsFactory factory = Project.getProject(folder).getElementsFactory();
		Enumeration e = factory.createEnumerationInstance();
		e.setOwner(folder);
		e.setName(enumName);
		
		for(String literal : literals) {
			EnumerationLiteral cliteral = factory.createEnumerationLiteralInstance();
			cliteral.setOwner(e);
			cliteral.setName(literal);
		}
	}

	public Class createClass(Package folder, String name, Stereotype stereotype)
	{
		Class result = null;
		if (name != null && !name.equals("") && project != null && folder != null && stereotype != null) {
			ElementsFactory factory = Project.getProject(folder).getElementsFactory();
			result = factory.createClassInstance();
			result.setOwner(folder);
			result.setName(name);			
			StereotypesHelper.addStereotype(result, stereotype);
		}
		
		return result;
	}

	public Package getPackage(Package owner, String name) {
		if (owner.hasPackagedElement()) {
			for (PackageableElement packageableElement : owner.getPackagedElement()) {
				if (packageableElement instanceof Package && name.equals(packageableElement.getName())) {
					return (Package) packageableElement;
				}
			}
		}
		return null;
	}	
}
