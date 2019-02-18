package com.kongkongye.np.mcmenu.api.menu;

import cn.nukkit.Player;

/**
 * menu factory
 */
public interface MenuFactory {
    /**
     * get the name of menu
     */
    String getName();

    /**
     * get if loop of the menu
     */
    boolean isLoop();

    /**
     * if use default exit method of menu<br>
     * it contains exit on move and exit on open inventory
     */
    boolean isUseDefaultExitMethod();

    /**
     * produce menu
     */
    Menu produce(Player player, int slot);
}
