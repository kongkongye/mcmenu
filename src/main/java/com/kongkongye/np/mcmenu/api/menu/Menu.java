package com.kongkongye.np.mcmenu.api.menu;

import cn.nukkit.Player;
import com.kongkongye.np.mcmenu.api.display.Display;

/**
 * menu
 */
public interface Menu {
    /**
     * get the MenuFactory the menu belongs
     */
    MenuFactory getMenuFactory();

    /**
     * get the player the menu belongs
     */
    Player getPlayer();

    /**
     * get the slot in quickbar of menu item
     */
    int getSlot();

    /**
     * get display
     * @return not null
     */
    Display getDisplay();

    /**
     * move left
     * @return not null
     */
    Display left();

    /**
     * move right
     * @return not null
     */
    Display right();

    /**
     * confirm
     * @return null will exit menu
     */
    Display confirm();

    /**
     * back
     * @return null will exit menu
     */
    Display back();
}
