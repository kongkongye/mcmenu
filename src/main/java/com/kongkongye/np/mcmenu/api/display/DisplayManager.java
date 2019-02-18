package com.kongkongye.np.mcmenu.api.display;

import cn.nukkit.Player;

/**
 * display manager
 */
public interface DisplayManager {
    /**
     * register display bar
     */
    void registerDisplayBar(DisplayBar displayBar);

    /**
     * display
     * @param display null represents clear display
     */
    void display(Player player, Display display);
}
