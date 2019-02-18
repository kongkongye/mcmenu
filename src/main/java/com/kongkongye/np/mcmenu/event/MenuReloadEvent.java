package com.kongkongye.np.mcmenu.event;

import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;

/**
 * menu reload event
 */
public class MenuReloadEvent extends Event {
    private static HandlerList handlerList = new HandlerList();

    public static HandlerList getHandlers() {
        return handlerList;
    }
}
