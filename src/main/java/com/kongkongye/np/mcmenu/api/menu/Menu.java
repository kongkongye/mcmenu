package com.kongkongye.np.mcmenu.api.menu;

import cn.nukkit.Player;
import com.kongkongye.np.mcmenu.api.display.Display;

/**
 * 菜单
 */
public interface Menu {
    /**
     * 获取所属的菜单工厂
     */
    MenuFactory getMenuFactory();

    /**
     * 获取菜单绑定的玩家
     */
    Player getPlayer();

    /**
     * 获取快捷栏中菜单中心的位置
     */
    int getSlot();

    /**
     * 获取显示
     * @return 不为null
     */
    Display getDisplay();

    /**
     * 左移
     * @return 不为null
     */
    Display left();

    /**
     * 右移
     * @return 不为null
     */
    Display right();

    /**
     * 确认
     * @return null会退出菜单
     */
    Display confirm();

    /**
     * 返回
     * @return null会退出菜单
     */
    Display back();
}
