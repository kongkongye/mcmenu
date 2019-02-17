package com.kongkongye.np.mcmenu.event;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import com.kongkongye.np.mcmenu.api.menu.MenuFactory;

/**
 * 菜单加入前事件<br>
 * 取消事件则取消加入
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
