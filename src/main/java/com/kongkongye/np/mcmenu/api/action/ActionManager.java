package com.kongkongye.np.mcmenu.api.action;

import cn.nukkit.Player;
import com.kongkongye.np.mcmenu.api.menu.Menu;

/**
 * 动作管理
 */
public interface ActionManager {
    /**
     * 加入菜单
     * 会先退出旧菜单(如果有)
     * @return null表示加入失败
     */
    Menu join(Player player, int slot, String menuName);

    /**
     * 左移
     */
    void left(Player p);

    /**
     * 右移
     */
    void right(Player p);

    /**
     * 确认
     */
    void confirm(Player p);

    /**
     * 返回
     */
    void back(Player p);

    /**
     * 退出
     */
    void exit(Player p);
}
