/* Copyright Sean C. Hubbell All Rights Reserved */
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
