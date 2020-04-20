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
