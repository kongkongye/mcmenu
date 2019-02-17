package com.kongkongye.np.mcmenu.service;

import cn.nukkit.Player;
import com.kongkongye.np.mcmenu.McMenuPlugin;
import com.kongkongye.np.mcmenu.api.service.CommandService;

public class McCommandService implements CommandService {
    @Override
    public boolean execute(Player player, String cmd) {
        return McMenuPlugin.instance.getServer().dispatchCommand(player, cmd);
    }
}
