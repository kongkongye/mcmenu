package com.kongkongye.np.mcmenu.event;

import cn.nukkit.Player;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import com.kongkongye.np.mcmenu.api.menu.Menu;

/**
 * menu confirm event
 */
public class MenuConfirmEvent extends Event {
    private static HandlerList handlerList = new HandlerList();

    private Player player;
    private Menu menu;

    public MenuConfirmEvent(Player player, Menu menu) {
        this.player = player;
        this.menu = menu;
    }

    public Player getPlayer() {
        return player;
    }

    public Menu getMenu() {
        return menu;
    }

    public static HandlerList getHandlers() {
        return handlerList;
    }
}
