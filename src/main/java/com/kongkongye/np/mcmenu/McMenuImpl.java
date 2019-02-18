package com.kongkongye.np.mcmenu;

import com.kongkongye.np.mcmenu.action.McActionManager;
import com.kongkongye.np.mcmenu.api.display.DisplayManager;
import com.kongkongye.np.mcmenu.api.menu.McMenu;
import com.kongkongye.np.mcmenu.api.menu.tree.TreeMenuManager;
import com.kongkongye.np.mcmenu.api.service.CommandService;
import com.kongkongye.np.mcmenu.display.McDisplayManager;
import com.kongkongye.np.mcmenu.item.McItemManager;
import com.kongkongye.np.mcmenu.lang.McLangManager;
import com.kongkongye.np.mcmenu.menu.McMenuManager;
import com.kongkongye.np.mcmenu.menu.tree.McTreeMenuManager;

public class McMenuImpl implements McMenu {
    private McMenuManager menuManager;
    private McDisplayManager displayManager;
    private McItemManager itemManager;
    private McActionManager actionManager;
    private McTreeMenuManager treeMenuManager;
    private McLangManager langManager;

    public McMenuImpl() {
        menuManager = new McMenuManager();
        displayManager = new McDisplayManager();
        itemManager = new McItemManager();
        actionManager = new McActionManager();
        treeMenuManager = new McTreeMenuManager();
        langManager = new McLangManager();
    }

    public void initOnLoad() {
        displayManager.initOnLoad();
        treeMenuManager.initOnLoad();
    }

    public void initOnEnable() {
        treeMenuManager.initOnEnable();
        displayManager.initOnEnable();
        langManager.initOnEnable();
    }

    public void initOnDisable() {
    }

    @Override
    public McMenuManager getMenuManager() {
        return menuManager;
    }

    @Override
    public DisplayManager getDisplayManager() {
        return displayManager;
    }

    @Override
    public McItemManager getItemManager() {
        return itemManager;
    }

    @Override
    public McActionManager getActionManager() {
        return actionManager;
    }

    @Override
    public CommandService getCommandService() {
        return McMenuPlugin.instance.getServer().getServiceManager().getProvider(CommandService.class).getProvider();
    }

    @Override
    public TreeMenuManager getTreeMenuManager() {
        return treeMenuManager;
    }

    @Override
    public McLangManager getLangManager() {
        return langManager;
    }
}
