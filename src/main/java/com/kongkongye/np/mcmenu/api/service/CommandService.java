package com.kongkongye.np.mcmenu.api.service;

import cn.nukkit.Player;

/**
 * command service
 */
public interface CommandService {
    /**
     * execute command
     * @return if success
     */
    boolean execute(Player player, String cmd);
}
