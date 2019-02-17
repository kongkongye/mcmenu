package com.kongkongye.np.mcmenu.api.service;

import cn.nukkit.Player;

/**
 * 命令服务
 */
public interface CommandService {
    /**
     * 执行命令
     * @return 是否成功
     */
    boolean execute(Player player, String cmd);
}
