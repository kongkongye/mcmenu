package com.kongkongye.np.mcmenu.api.display;

import cn.nukkit.Player;

/**
 * 显示条
 */
public interface DisplayBar {
    /**
     * 名称
     */
    String getName();

    /**
     * 显示
     * @param content null表示清除显示,注意内容可能过长,显示时注意截断
     */
    void display(Player player, String content);

    /**
     * 清除显示
     */
    void clear(Player player);
}
