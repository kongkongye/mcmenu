package com.kongkongye.np.mcmenu.api.action;

import cn.nukkit.Player;
import com.kongkongye.np.mcmenu.api.menu.Menu;

/**
 * action manager
 */
public interface ActionManager {
    /**
     * join menu<br>
     * will exit old menu first(if exist)
     * @return null represents join fail
     */
    Menu join(Player player, int slot, String menuName);

    /**
     * move left
     */
    void left(Player p);

    /**
     * move right
     */
    void right(Player p);

    /**
     * confirm
     */
    void confirm(Player p);

    /**
     * back
     */
    void back(Player p);

    /**
     * exit
     */
    void exit(Player p);
}
