package com.kongkongye.np.mcmenu.api;

import com.kongkongye.np.mcmenu.McMenuPlugin;
import com.kongkongye.np.mcmenu.api.action.ActionManager;
import com.kongkongye.np.mcmenu.api.display.DisplayManager;
import com.kongkongye.np.mcmenu.api.item.ItemManager;
import com.kongkongye.np.mcmenu.api.lang.LangManager;
import com.kongkongye.np.mcmenu.api.menu.McMenu;
import com.kongkongye.np.mcmenu.api.menu.MenuManager;
import com.kongkongye.np.mcmenu.api.menu.tree.TreeMenuManager;
import com.kongkongye.np.mcmenu.api.service.CommandService;
import com.kongkongye.np.mcmenu.display.McDisplayManager;
import com.kongkongye.np.mcmenu.event.MenuReloadEvent;
import com.kongkongye.np.mcmenu.lang.McLangManager;
import com.kongkongye.np.mcmenu.menu.tree.McTreeMenuManager;
import com.kongkongye.np.mcmenu.util.Util;

public class McMenuApi {
    public static McMenu mcMenu;

    /**
     * reload plugin
     */
    public static void reload() {
        Util.info(0, 1000);
        //all player exit menu
        Util.info(1, 1010);
        McMenuPlugin.instance.getServer().getOnlinePlayers().values().forEach(getActionManager()::exit);
        //reload config
        Util.info(1, 1020);
        McMenuPlugin.instance.reload();
        //reload language
        Util.info(1, 1030);
        ((McLangManager)getLangManager()).reload();
        //reload tree menus
        Util.info(1, 1040);
        ((McTreeMenuManager)getTreeMenuManager()).reload();
        //reload display manager
        ((McDisplayManager)getDisplayManager()).reload();
        //event after reload
        MenuReloadEvent menuReloadEvent = new MenuReloadEvent();
        McMenuPlugin.instance.getServer().getPluginManager().callEvent(menuReloadEvent);
    }

    /**
     * get menu manager
     */
    public static MenuManager getMenuManager() {
        return mcMenu.getMenuManager();
    }

    /**
     * get tree menu manager
     */
    public static TreeMenuManager getTreeMenuManager() {
        return mcMenu.getTreeMenuManager();
    }

    /**
     * get display manager
     */
    public static DisplayManager getDisplayManager() {
        return mcMenu.getDisplayManager();
    }

    /**
     * get item manager
     */
    public static ItemManager getItemManager() {
        return mcMenu.getItemManager();
    }

    /**
     * get action manager
     */
    public static ActionManager getActionManager() {
        return mcMenu.getActionManager();
    }

    /**
     * get command service
     */
    public static CommandService getCommandService() {
        return mcMenu.getCommandService();
    }

    /**
     * get language manager
     */
    public static LangManager getLangManager() {
        return mcMenu.getLangManager();
    }
}
