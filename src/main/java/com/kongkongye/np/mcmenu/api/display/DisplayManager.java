package com.kongkongye.np.mcmenu.api.display;

import cn.nukkit.Player;

/**
 * 显示管理器
 */
public interface DisplayManager {
    /**
     * 注册显示条
     */
    void registerDisplayBar(DisplayBar displayBar);

    /**
     * 显示
     * @param display null表示清除显示
     */
    void display(Player player, Display display);
}
