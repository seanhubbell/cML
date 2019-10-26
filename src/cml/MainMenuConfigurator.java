/* Copyright Sean C. Hubbell All Rights Reserved */
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