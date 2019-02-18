package com.kongkongye.np.mcmenu.api.display;

import cn.nukkit.Player;

/**
 * display bar
 */
public interface DisplayBar {
    /**
     * name
     */
    String getName();

    /**
     * display
     * @param content null represents clear display, notice that content may by long, may need cut off on display
     */
    void display(Player player, String content);

    /**
     * clear display
     */
    void clear(Player player);
}
