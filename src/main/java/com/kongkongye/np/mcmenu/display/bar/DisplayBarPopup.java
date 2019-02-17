package com.kongkongye.np.mcmenu.display.bar;

import cn.nukkit.Player;
import com.kongkongye.np.mcmenu.api.display.DisplayBar;

public class DisplayBarPopup implements DisplayBar {
    @Override
    public String getName() {
        return "popup";
    }

    @Override
    public void display(Player player, String content) {
        player.sendPopup(content);
    }

    @Override
    public void clear(Player player) {
        player.sendPopup("");
    }
}
