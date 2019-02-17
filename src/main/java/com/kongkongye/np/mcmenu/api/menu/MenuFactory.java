package com.kongkongye.np.mcmenu.api.menu;

import cn.nukkit.Player;

/**
 * 菜单工厂
 */
public interface MenuFactory {
    /**
     * 获取菜单名
     */
    String getName();

    /**
     * 获取菜单是否循环
     */
    boolean isLoop();

    /**
     * 是否使用默认的菜单退出方式
     * 包括移动退出与打开容器界面退出
     */
    boolean isUseDefaultExitMethod();

    /**
     * 生产出菜单
     */
    Menu produce(Player player, int slot);
}
