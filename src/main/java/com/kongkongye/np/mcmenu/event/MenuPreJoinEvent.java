package com.kongkongye.np.mcmenu.event;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import com.kongkongye.np.mcmenu.api.menu.MenuFactory;

/**
 * menu pre join event<br>
 * cancel event will cancel join
 */
public class MenuPreJoinEvent extends Event implements Cancellable {
    private static HandlerList handlerList = new HandlerList();

    private Player player;
    private MenuFactory menuInfo;

    public MenuPreJoinEvent(Player player, MenuFactory menuInfo) {
        this.player = player;
        this.menuInfo = menuInfo;
    }

    public Player getPlayer() {
        return player;
    }

    public MenuFactory getMenuInfo() {
        return menuInfo;
    }

    public static HandlerList getHandlers() {
        return handlerList;
    }
}
