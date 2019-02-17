package com.kongkongye.np.mcmenu.action;

import cn.nukkit.Player;
import com.google.common.base.Preconditions;
import com.kongkongye.np.mcmenu.McMenuPlugin;
import com.kongkongye.np.mcmenu.api.McMenuApi;
import com.kongkongye.np.mcmenu.api.action.ActionManager;
import com.kongkongye.np.mcmenu.api.display.Display;
import com.kongkongye.np.mcmenu.api.menu.Menu;
import com.kongkongye.np.mcmenu.api.menu.MenuFactory;
import com.kongkongye.np.mcmenu.event.*;
import com.kongkongye.np.mcmenu.menu.McMenuManager;
import com.kongkongye.np.mcmenu.util.Util;

public class McActionManager implements ActionManager {
    @Override
    public Menu join(Player player, int slot, String menuName) {
        //菜单未找到
        MenuFactory menuFactory = McMenuApi.getMenuManager().getMenuFactory(menuName);
        if (menuFactory == null) {
            Util.send(player, "&c菜单未找到: &e{0}", menuName);
            return null;
        }

        //加入前事件
        MenuPreJoinEvent menuPreJoinEvent = new MenuPreJoinEvent(player, menuFactory);
        McMenuPlugin.instance.getServer().getPluginManager().callEvent(menuPreJoinEvent);
        if (menuPreJoinEvent.isCancelled()) {
            return null;
        }

        //开始加入

        //先退出(如果有)
        Menu menu = McMenuApi.getMenuManager().getMenu(player);
        if (menu != null) {
            exit(player);
        }

        //再加入
        menu = menuFactory.produce(player, slot);
        ((McMenuManager)McMenuApi.getMenuManager()).onJoin(menu);

        //显示
        McMenuApi.getDisplayManager().display(player, menu.getDisplay());

        //加入后事件
        MenuJoinEvent menuJoinEvent = new MenuJoinEvent(player, menu);
        McMenuPlugin.instance.getServer().getPluginManager().callEvent(menuJoinEvent);

        //提示
        Util.send(menuJoinEvent.getPlayer(), "&e进入菜单: &d{0}", menuName);

        return menu;
    }

    @Override
    public void left(Player p) {
        Menu menu = McMenuApi.getMenuManager().getMenu(p);
        if (menu != null) {
            //左移
            Display display = Preconditions.checkNotNull(menu.left());
            //显示
            McMenuApi.getDisplayManager().display(p, display);
            //左移后事件
            MenuLeftEvent menuLeftEvent = new MenuLeftEvent(p, menu);
            McMenuPlugin.instance.getServer().getPluginManager().callEvent(menuLeftEvent);
        }
    }

    @Override
    public void right(Player p) {
        Menu menu = McMenuApi.getMenuManager().getMenu(p);
        if (menu != null) {
            //右移
            Display display = Preconditions.checkNotNull(menu.right());
            //显示
            McMenuApi.getDisplayManager().display(p, display);
            //右移后事件
            MenuRightEvent menuRightEvent = new MenuRightEvent(p, menu);
            McMenuPlugin.instance.getServer().getPluginManager().callEvent(menuRightEvent);
        }
    }

    @Override
    public void confirm(Player p) {
        Menu menu = McMenuApi.getMenuManager().getMenu(p);
        if (menu != null) {
            //确认
            Display display = menu.confirm();
            if (display != null) {
                //显示
                McMenuApi.getDisplayManager().display(p, display);
                //确认事件
                MenuConfirmEvent menuConfirmEvent = new MenuConfirmEvent(p, menu);
                McMenuPlugin.instance.getServer().getPluginManager().callEvent(menuConfirmEvent);
            }else {
                exit(p);
            }
        }
    }

    @Override
    public void back(Player p) {
        Menu menu = McMenuApi.getMenuManager().getMenu(p);
        if (menu != null) {
            //返回
            Display display = menu.back();
            if (display != null) {
                //显示
                McMenuApi.getDisplayManager().display(p, display);
                //返回事件
                MenuBackEvent menuBackEvent = new MenuBackEvent(p, menu);
                McMenuPlugin.instance.getServer().getPluginManager().callEvent(menuBackEvent);
            }else {
                exit(p);
            }
        }
    }

    @Override
    public void exit(Player p) {
        Menu menu = McMenuApi.getMenuManager().getMenu(p);
        if (menu != null) {
            ((McMenuManager)McMenuApi.getMenuManager()).onExit(p);
            //显示
            McMenuApi.getDisplayManager().display(p, null);
            //退出事件
            MenuExitEvent menuExitEvent = new MenuExitEvent(p, menu);
            McMenuPlugin.instance.getServer().getPluginManager().callEvent(menuExitEvent);
            //提示
            Util.send(p, "&7退出菜单.");
        }
    }
}
