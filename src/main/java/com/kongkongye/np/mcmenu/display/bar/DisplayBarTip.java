package com.kongkongye.np.mcmenu.display.bar;

import cn.nukkit.Player;
import com.kongkongye.np.mcmenu.api.display.DisplayBar;

public class DisplayBarTip implements DisplayBar {
    @Override
    public String getName() {
        return "tip";
    }

    @Override
    public void display(Player player, String content) {
        player.sendTip(content);
    }

    @Override
    public void clear(Player player) {
        player.sendTip("");
    }
}
