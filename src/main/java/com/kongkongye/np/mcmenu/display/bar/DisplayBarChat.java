package com.kongkongye.np.mcmenu.display.bar;

import cn.nukkit.Player;
import com.google.common.base.Strings;
import com.kongkongye.np.mcmenu.api.display.DisplayBar;

public class DisplayBarChat implements DisplayBar {
    @Override
    public String getName() {
        return "chat";
    }

    @Override
    public void display(Player player, String content) {
        if (!Strings.isNullOrEmpty(content)) {
            player.sendMessage(content);
        }
    }

    @Override
    public void clear(Player player) {
    }
}
