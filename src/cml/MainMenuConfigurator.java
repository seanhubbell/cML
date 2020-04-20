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

import com.nomagic.actions.AMConfigurator;
import com.nomagic.actions.ActionsCategory;
import com.nomagic.actions.ActionsManager;
import com.nomagic.actions.NMAction;
import com.nomagic.magicdraw.actions.MDActionsCategory;

/**
 * The main menu for the cML plug-in.
 * 
 * @author Sean C. Hubbell
 *
 */
public class MainMenuConfigurator implements AMConfigurator {
	String plugInName = "cML";
	private NMAction action;

	/**
	 * Creates an instance of the main menu configurator.
	 * 
	 * @param action the action associated with the main menu configurator.
	 */
	public MainMenuConfigurator(NMAction action) {
		this.action = action;
	}

	/**
	 * Configures the main menu.
	 */
	@Override
	public void configure(ActionsManager manager) {
		ActionsCategory category = (ActionsCategory) manager.getActionFor(plugInName);

		if (category == null) {
			category = new MDActionsCategory(plugInName, plugInName);
			category.setNested(true);
			manager.addCategory(category);
		}
		category.addAction(action);
	}

	/**
	 * Provides the priority of the cML plug-in.
	 */
	@Override
	public int getPriority() {
		return AMConfigurator.MEDIUM_PRIORITY;
	}

}