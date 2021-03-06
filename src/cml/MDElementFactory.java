/*
Copyright 2020 Sean C. Hubbell

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files(the "Software"),
to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package cml;

import java.util.List;

import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.openapi.uml.ModelElementsManager;
import com.nomagic.magicdraw.openapi.uml.ReadOnlyElementException;
import com.nomagic.magicdraw.uml.DiagramTypeConstants;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Diagram;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Enumeration;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.EnumerationLiteral;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.PackageableElement;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;
import com.nomagic.uml2.impl.ElementsFactory;

/**
 * The Magic Draw (MD) Element Factory creates all of the specific elements to
 * be leveraged by cML.
 * 
 * @author Sean C. Hubbell
 *
 */
public class MDElementFactory {

	private Project project = null;

	/**
	 * Creates an instance of the Magic Draw Element Factory. This factory creates
	 * the Magic Draw specific elements.
	 * 
	 * @param p the project instance.
	 */
	public MDElementFactory(Project p) {
		project = p;
	}

	/**
	 * Creates a package with the given name under the parent package. If the
	 * package exists already, the package will be returned.
	 * 
	 * @param name   the name of the newly created package.
	 * @param parent the parent package to store the created package under.
	 * @return the newly created package or null.
	 */
	public Package createPackage(String name, Package parent) {
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

	/**
	 * Creates the enumeration to be re-used.
	 * 
	 * @param folder   the name of the folder where the priorities will be stored
	 * @param enumName the name of the proprity enumeration
	 * @param literals the literal names to give the enum.
	 */
	public void createEnumeration(Package folder, String enumName, List<String> literals) {
		ElementsFactory factory = Project.getProject(folder).getElementsFactory();
		Enumeration e = factory.createEnumerationInstance();
		e.setOwner(folder);
		e.setName(enumName);

		for (String literal : literals) {
			EnumerationLiteral cliteral = factory.createEnumerationLiteralInstance();
			cliteral.setOwner(e);
			cliteral.setName(literal);
		}
	}

	/**
	 * Creates a class with the given stereotype in the folder and sets the name of
	 * the class.
	 * 
	 * @param folder     the name of the folder where the class will reside in Magic
	 *                   Draw.
	 * @param name       the name to give the newly created class.
	 * @param stereotype the stereotype to apply to the given class.
	 * @return the newly created class.
	 */
	public Class createClass(Package folder, String name, Stereotype stereotype) {
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

	/**
	 * Gets the given name of the package from the given owner.
	 * 
	 * @param owner the owner of the package.
	 * @param name  the name of the package you are searching for.
	 * @return the package if the package exists or null.
	 */
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

	/**
	 * Creates a Glossary Table Diagram.
	 * 
	 * @param parent the parent package to contain the diagram
	 * @return the newly created glossary table diagram created or null;
	 */
	public Diagram createGlossaryTableDiagram(Package parent) {
		Diagram result = null;
		try {
			result = ModelElementsManager.getInstance().createDiagram(DiagramTypeConstants.GLOSSARY_TABLE, parent);
		} catch (ReadOnlyElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
