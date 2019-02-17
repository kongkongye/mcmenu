package com.kongkongye.np.mcmenu.menu;

import cn.nukkit.Player;
import cn.nukkit.level.Location;
import com.kongkongye.np.mcmenu.api.menu.Menu;
import com.kongkongye.np.mcmenu.api.menu.MenuFactory;

/**
 * 抽象菜单
 * (所有菜单实现都应该继承此类)
 */
public abstract class AbstractMenu implements Menu {
    private MenuFactory menuFactory;
    private Player player;
    private int slot;
    /**
     * 进入菜单时玩家的位置
     */
    private Location location;

    public AbstractMenu(MenuFactory menuFactory, Player player, int slot, Location location) {
        this.menuFactory = menuFactory;
        this.player = player;
        this.slot = slot;
        this.location = location;
    }

    @Override
    public MenuFactory getMenuFactory() {
        return menuFactory;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    public Location getLocation() {
        return location;
    }
}
