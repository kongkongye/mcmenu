package com.kongkongye.np.mcmenu.api.menu;

import cn.nukkit.Player;

import java.util.Collection;

/**
 * 菜单管理器
 */
public interface MenuManager {
    /**
     * 注册菜单工厂
     */
    void register(MenuFactory menuFactory);

    /**
     * 获取全部菜单工厂
     */
    Collection<MenuFactory> getMenuFactories();

    /**
     * 获取菜单工厂
     * @param name 菜单名
     * @return 可为null
     */
    MenuFactory getMenuFactory(String name);

    /**
     * 删除菜单工厂
     * @return 删除的菜单工厂,null表示无
     */
    MenuFactory removeMenuFactory(String menuName);

    /**
     * 获取玩家当前打开的菜单
     * @return 可为null
     */
    Menu getMenu(Player player);
}
