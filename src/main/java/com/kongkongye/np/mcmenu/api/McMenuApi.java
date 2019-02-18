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
     * 重载插件
     */
    public static void reload() {
        Util.info(0, 1000);
        //所有玩家退出菜单
        Util.info(1, 1010);
        McMenuPlugin.instance.getServer().getOnlinePlayers().values().forEach(getActionManager()::exit);
        //配置重载
        Util.info(1, 1020);
        McMenuPlugin.instance.reload();
        //语言重载
        Util.info(1, 1030);
        ((McLangManager)getLangManager()).reload();
        //树状菜单重载
        Util.info(1, 1040);
        ((McTreeMenuManager)getTreeMenuManager()).reload();
        //显示管理器重载
        ((McDisplayManager)getDisplayManager()).reload();
        //事件
        MenuReloadEvent menuReloadEvent = new MenuReloadEvent();
        McMenuPlugin.instance.getServer().getPluginManager().callEvent(menuReloadEvent);
    }

    /**
     * 获取菜单管理器
     */
    public static MenuManager getMenuManager() {
        return mcMenu.getMenuManager();
    }

    /**
     * 获取树状菜单管理器
     */
    public static TreeMenuManager getTreeMenuManager() {
        return mcMenu.getTreeMenuManager();
    }

    /**
     * 获取显示管理器
     */
    public static DisplayManager getDisplayManager() {
        return mcMenu.getDisplayManager();
    }

    /**
     * 获取物品管理器
     */
    public static ItemManager getItemManager() {
        return mcMenu.getItemManager();
    }

    /**
     * 获取动作管理器
     */
    public static ActionManager getActionManager() {
        return mcMenu.getActionManager();
    }

    /**
     * 获取命令服务
     */
    public static CommandService getCommandService() {
        return mcMenu.getCommandService();
    }

    /**
     * 获取语言管理器
     */
    public static LangManager getLangManager() {
        return mcMenu.getLangManager();
    }
}
