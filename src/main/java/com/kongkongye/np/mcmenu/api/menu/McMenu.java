package com.kongkongye.np.mcmenu.api.menu;

import com.kongkongye.np.mcmenu.api.action.ActionManager;
import com.kongkongye.np.mcmenu.api.display.DisplayManager;
import com.kongkongye.np.mcmenu.api.item.ItemManager;
import com.kongkongye.np.mcmenu.api.menu.tree.TreeMenuManager;
import com.kongkongye.np.mcmenu.api.service.CommandService;

public interface McMenu {
    MenuManager getMenuManager();

    DisplayManager getDisplayManager();

    ItemManager getItemManager();

    ActionManager getActionManager();

    CommandService getCommandService();

    TreeMenuManager getTreeMenuManager();
}
