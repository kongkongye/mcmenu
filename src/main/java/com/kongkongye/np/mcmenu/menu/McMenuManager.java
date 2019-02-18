package com.kongkongye.np.mcmenu.menu;

import cn.nukkit.Player;
import com.google.common.base.Preconditions;
import com.kongkongye.np.mcmenu.api.menu.Menu;
import com.kongkongye.np.mcmenu.api.menu.MenuFactory;
import com.kongkongye.np.mcmenu.api.menu.MenuManager;
import com.kongkongye.np.mcmenu.util.Util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class McMenuManager implements MenuManager {
    /**
     * menu-name menu-factory
     */
    private Map<String, MenuFactory> menuFactories = new HashMap<>();

    /**
     * player menu
     */
    private Map<Player, Menu> menus = new HashMap<>();

    @Override
    public void register(MenuFactory menuFactory) {
        menuFactories.put(menuFactory.getName(), menuFactory);
        //log
        Util.info(1, 1050, menuFactory.getName());
    }

    @Override
    public Collection<MenuFactory> getMenuFactories() {
        return menuFactories.values();
    }

    @Override
    public MenuFactory getMenuFactory(String name) {
        return menuFactories.get(name);
    }

    @Override
    public MenuFactory removeMenuFactory(String menuName) {
        return menuFactories.remove(menuName);
    }

    @Override
    public Menu getMenu(Player player) {
        return menus.get(player);
    }

    public void onJoin(Menu menu) {
        Preconditions.checkArgument(menus.put(menu.getPlayer(), menu) == null);
    }

    public void onExit(Player player) {
        menus.remove(player);
    }
}
