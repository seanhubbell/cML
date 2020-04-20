/*
Copyright 2019 Sean C. Hubbell

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package controls.ia.ccis;

import java.util.ArrayList;
import java.util.List;

import com.nomagic.magicdraw.uml.Finder;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.EnumerationLiteral;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

import cml.MDElementFactory;

/**
 * The IA CCI Item Element Factory. This class is used to create the MagicDraw
 * class representing the CCI with the appropriate stereotypes populated.
 * 
 * @author Sean C. Hubbell
 *
 */
public class CCI_ItemElementFactory {

	private MDElementFactory factory = null;
	private Package ccisFolder = null;

	/**
	 * Creates an instance of the CCI Item Element Factory.
	 * 
	 * @param factory    - the Magic Draw element factory
	 * @param ccisFolder - the folder to hold all of the generated CCIs.
	 */
	public CCI_ItemElementFactory(MDElementFactory factory, Package ccisFolder) {
		this.factory = factory;
		this.ccisFolder = ccisFolder;
	}

	/**
	 * Creates the CCI in Magic Draw and populates the information from the given
	 * item.
	 * 
	 * @param enumsFolder         - the folder containing the reusable enumerations
	 *                            to add to the newly created class.
	 * @param cci_ItemStereotype  - the CCI Item stereotype to apply to the newly
	 *                            created class to support adding the CCI item data.
	 * @param referenceStereotype - the reference Item stereotype to apply to the
	 *                            newly created class to support adding the CCI item
	 *                            data.
	 * @param item                - the CCI data item containing the data parsed
	 *                            from the standard.
	 */
	public void createCCI(Package enumsFolder, Stereotype cci_ItemStereotype, Stereotype referenceStereotype,
			CCI_Item item) {
		Package folder = null;
		ArrayList<Class> items = null;

		Package idFolder = Finder.byNameRecursively().find(ccisFolder, Package.class, item.id);
		Class cciClass = Finder.byNameRecursively().find(ccisFolder, Class.class, item.id);
		StereotypesHelper.setStereotypePropertyValue(cciClass, cci_ItemStereotype, "Id", item.id, true);

		EnumerationLiteral statusLiteral = Finder.byNameRecursively().find(enumsFolder, EnumerationLiteral.class,
				item.status);
		StereotypesHelper.setStereotypePropertyValue(cciClass, cci_ItemStereotype, "Status", statusLiteral, true);

		StereotypesHelper.setStereotypePropertyValue(cciClass, cci_ItemStereotype, "Publish Date", item.publishDate,
				true);
		StereotypesHelper.setStereotypePropertyValue(cciClass, cci_ItemStereotype, "Contributor", item.contributor,
				true);
		StereotypesHelper.setStereotypePropertyValue(cciClass, cci_ItemStereotype, "Definition", item.definition, true);
		StereotypesHelper.setStereotypePropertyValue(cciClass, cci_ItemStereotype, "Parameter", item.parameter, true);
		StereotypesHelper.setStereotypePropertyValue(cciClass, cci_ItemStereotype, "Note", item.note, true);

		EnumerationLiteral typeLiteral = Finder.byNameRecursively().find(enumsFolder, EnumerationLiteral.class,
				item.type);
		StereotypesHelper.setStereotypePropertyValue(cciClass, cci_ItemStereotype, "Type", typeLiteral, true);

		/**
		 * Create & add the references
		 */
		if (item.references != null && item.references.size() > 0) {
			folder = factory.createPackage("References", idFolder);
			items = createReferences(enumsFolder, folder, referenceStereotype, item.references);
			StereotypesHelper.setStereotypePropertyValue(cciClass, cci_ItemStereotype, "References", items, true);
		}
	}

	/**
	 * Creates the Magic Draw references from the given references.
	 * 
	 * @param enumsFolder         - the folder containing the versions.
	 * @param folder              - the folder to contain the newly created
	 *                            references.
	 * @param referenceStereotype - the reference stereotype to be applied to each
	 *                            newly created referenceStereotype.
	 * @param references          - the control enhancements containing the data to
	 *                            create the Magic Draw classes from.
	 * @return the newly created collection of Magic Draw classes created from the
	 *         given references.
	 */
	private ArrayList<Class> createReferences(Package enumsFolder, Package folder, Stereotype referenceStereotype,
			List<Reference> references) {
		ArrayList<Class> result = new ArrayList<Class>();
		for (Reference reference : references) {
			Class refClass = createReference(enumsFolder, folder, referenceStereotype, reference);
			result.add(refClass);
		}
		return result;
	}

	/**
	 * Creates the Magic Draw reference from the given reference.
	 * 
	 * @param enumsFolder         - the folder containing the versions.
	 * @param folder              - the folder to contain the newly created
	 *                            reference.
	 * @param referenceStereotype - the reference stereotype to be applied to each
	 *                            newly created reference.
	 * @param references          - the control enhancements containing the data to
	 *                            create the Magic Draw classes from.
	 * @return the newly created Magic Draw class created from the given reference.
	 */
	private Class createReference(Package enumsFolder, Package folder, Stereotype referenceStereotype,
			Reference reference) {
		Class result = factory.createClass(folder, reference.index, referenceStereotype);
		if (reference.index != null) {
			StereotypesHelper.setStereotypePropertyValue(result, referenceStereotype, "Creator", reference.creator,
					true);
			StereotypesHelper.setStereotypePropertyValue(result, referenceStereotype, "Title", reference.title, true);

			EnumerationLiteral versionLiteral = Finder.byNameRecursively().find(enumsFolder, EnumerationLiteral.class,
					reference.version);
			StereotypesHelper.setStereotypePropertyValue(result, referenceStereotype, "Version", versionLiteral, true);

			StereotypesHelper.setStereotypePropertyValue(result, referenceStereotype, "Location", reference.location,
					true);
			StereotypesHelper.setStereotypePropertyValue(result, referenceStereotype, "Index", reference.index, true);
		} else {
			System.err.println("Reference index does not exist.");
		}

		return result;
	}

}
