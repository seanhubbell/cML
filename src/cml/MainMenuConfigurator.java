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