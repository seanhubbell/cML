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

import com.nomagic.actions.ActionsCategory;
import com.nomagic.actions.NMAction;
import com.nomagic.magicdraw.actions.ActionsConfiguratorsManager;
import com.nomagic.magicdraw.plugins.Plugin;

/**
 * The Magic Draw (MD) cML Plug-in.
 * 
 * @author Sean C. Hubbell
 *
 */
public class cMLPlugin extends Plugin {

	/**
	 * Initializes the cML plug-in and creates the cML main menu item.
	 */
	@Override
	public void init() {

		ActionsConfiguratorsManager manager = ActionsConfiguratorsManager.getInstance();
		manager.addMainMenuConfigurator(new MainMenuConfigurator(getSubMenuActions()));
	}

	/**
	 * Provides the sub-menu item menus for cML
	 * 
	 * @return the actions category for each of the sub-menu items.
	 */
	private static NMAction getSubMenuActions() {
		ActionsCategory category = new ActionsCategory(null, "Security Controls");
		category.setNested(true);
		category.addAction(new LoadNISTControlsAction(null, "Load NIST Security Controls (53 & 53a Rev. 4)"));
		category.addAction(new LoadCCIsAction(null, "Load CCIs (2016-06-27)"));
		category.addAction(new LoadSecurityRequirementsAction(null, "Load Security Requirements"));
		return category;
	}

	/**
	 * Handles closing the cML plug-in.
	 */
	@Override
	public boolean close() {
		return true;
	}

	/**
	 * Checks the necessary data to see if the cML plug-in is supported.
	 */
	@Override
	public boolean isSupported() {
		return true;
	}
}
