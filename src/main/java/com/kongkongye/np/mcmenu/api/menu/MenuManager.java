package com.kongkongye.np.mcmenu.api.menu;

import cn.nukkit.Player;

import java.util.Collection;

/**
 * menu manager
 */
public interface MenuManager {
    /**
     * register menu factory
     */
    void register(MenuFactory menuFactory);

    /**
     * get all menu factories
     */
    Collection<MenuFactory> getMenuFactories();

    /**
     * get menu factory
     * @param name menu name
     * @return may be null
     */
    MenuFactory getMenuFactory(String name);

    /**
     * delete menu factory
     * @return the menu factory deleted, null represents not found
     */
    MenuFactory removeMenuFactory(String menuName);

    /**
     * get the current menu of player
     * @return may by null
     */
    Menu getMenu(Player player);
}
