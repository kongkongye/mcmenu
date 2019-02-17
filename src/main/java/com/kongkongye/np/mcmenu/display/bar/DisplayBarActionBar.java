package com.kongkongye.np.mcmenu.display.bar;

import cn.nukkit.Player;
import com.kongkongye.np.mcmenu.api.display.DisplayBar;

public class DisplayBarActionBar implements DisplayBar {
    @Override
    public String getName() {
        return "actionbar";
    }

    @Override
    public void display(Player player, String content) {
        player.sendActionBar(content, 0, 20*60*60, 0);
    }

    @Override
    public void clear(Player player) {
        player.sendActionBar("");
    }
}
