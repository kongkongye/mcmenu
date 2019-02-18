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
        //menu not found
        MenuFactory menuFactory = McMenuApi.getMenuManager().getMenuFactory(menuName);
        if (menuFactory == null) {
            Util.send(player, 5090, menuName);
            return null;
        }

        //pre join menu event
        MenuPreJoinEvent menuPreJoinEvent = new MenuPreJoinEvent(player, menuFactory);
        McMenuPlugin.instance.getServer().getPluginManager().callEvent(menuPreJoinEvent);
        if (menuPreJoinEvent.isCancelled()) {
            return null;
        }

        //start joining

        //exit first(if has)
        Menu menu = McMenuApi.getMenuManager().getMenu(player);
        if (menu != null) {
            exit(player);
        }

        //then join
        menu = menuFactory.produce(player, slot);
        ((McMenuManager)McMenuApi.getMenuManager()).onJoin(menu);

        //display
        McMenuApi.getDisplayManager().display(player, menu.getDisplay());

        //event after join
        MenuJoinEvent menuJoinEvent = new MenuJoinEvent(player, menu);
        McMenuPlugin.instance.getServer().getPluginManager().callEvent(menuJoinEvent);

        //tip
        Util.send(menuJoinEvent.getPlayer(), 5100, menuName);

        return menu;
    }

    @Override
    public void left(Player p) {
        Menu menu = McMenuApi.getMenuManager().getMenu(p);
        if (menu != null) {
            //move left
            Display display = Preconditions.checkNotNull(menu.left());
            //display
            McMenuApi.getDisplayManager().display(p, display);
            //event after move
            MenuLeftEvent menuLeftEvent = new MenuLeftEvent(p, menu);
            McMenuPlugin.instance.getServer().getPluginManager().callEvent(menuLeftEvent);
        }
    }

    @Override
    public void right(Player p) {
        Menu menu = McMenuApi.getMenuManager().getMenu(p);
        if (menu != null) {
            //move right
            Display display = Preconditions.checkNotNull(menu.right());
            //display
            McMenuApi.getDisplayManager().display(p, display);
            //event after move
            MenuRightEvent menuRightEvent = new MenuRightEvent(p, menu);
            McMenuPlugin.instance.getServer().getPluginManager().callEvent(menuRightEvent);
        }
    }

    @Override
    public void confirm(Player p) {
        Menu menu = McMenuApi.getMenuManager().getMenu(p);
        if (menu != null) {
            //confirm
            Display display = menu.confirm();
            if (display != null) {
                //display
                McMenuApi.getDisplayManager().display(p, display);
                //event after confirm
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
            //back
            Display display = menu.back();
            if (display != null) {
                //display
                McMenuApi.getDisplayManager().display(p, display);
                //event after back
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
            //display
            McMenuApi.getDisplayManager().display(p, null);
            //event after exit
            MenuExitEvent menuExitEvent = new MenuExitEvent(p, menu);
            McMenuPlugin.instance.getServer().getPluginManager().callEvent(menuExitEvent);
            //tip
            Util.send(p, 5110);
        }
    }
}
