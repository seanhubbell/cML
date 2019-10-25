/* Copyright Sean C. Hubbell All Rights Reserved */
package cml;

import com.nomagic.actions.AMConfigurator;
import com.nomagic.actions.ActionsCategory;
import com.nomagic.actions.ActionsManager;
import com.nomagic.actions.NMAction;
import com.nomagic.magicdraw.actions.MDActionsCategory;

public class MainMenuConfigurator implements AMConfigurator
{
	String plugInName = "cML";

	private NMAction action;

	public MainMenuConfigurator(NMAction action)
	{
		this.action = action;
	}

	@Override
	public void configure(ActionsManager manager)
	{
		ActionsCategory category = (ActionsCategory) manager.getActionFor(plugInName);

		if( category == null )
		{
			category = new MDActionsCategory(plugInName,plugInName);
			category.setNested(true);
			manager.addCategory(category);
		}
		category.addAction(action);
	}
	@Override
	public int getPriority()
	{
		return AMConfigurator.MEDIUM_PRIORITY;
	}

}