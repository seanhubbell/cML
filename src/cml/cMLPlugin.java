/* Copyright Sean C. Hubbell All Rights Reserved */
package cml;

import com.nomagic.actions.ActionsCategory;
import com.nomagic.actions.NMAction;
import com.nomagic.magicdraw.actions.ActionsConfiguratorsManager;
import com.nomagic.magicdraw.plugins.Plugin;

public class cMLPlugin extends Plugin {

	@Override
	public void init() {

		ActionsConfiguratorsManager manager = ActionsConfiguratorsManager.getInstance();
		manager.addMainMenuConfigurator(new MainMenuConfigurator(getSubMenuActions()));
	}

	private static NMAction getSubMenuActions() {
		ActionsCategory category = new ActionsCategory(null, "Security Controls");
		category.setNested(true);
		category.addAction(new LoadNISTControlsAction(null, "Load NIST Security Controls"));
		return category;
	}

	@Override
	public boolean close() {
		return true;
	}

	@Override
	public boolean isSupported() {
		return true;
	}
}
