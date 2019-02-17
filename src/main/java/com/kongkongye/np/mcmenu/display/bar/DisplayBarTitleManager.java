package com.kongkongye.np.mcmenu.display.bar;

import cn.nukkit.Player;
import com.kongkongye.np.mcmenu.api.McMenuApi;
import com.kongkongye.np.mcmenu.api.display.DisplayBar;
import com.kongkongye.np.mcmenu.display.McDisplayManager;

import java.util.HashMap;
import java.util.Map;

/**
 * title&subTitle显示条管理器
 */
public class DisplayBarTitleManager {
    public static class DisplayBarTitle implements DisplayBar {
        @Override
        public String getName() {
            return "title";
        }

        @Override
        public void display(Player player, String content) {
            ((McDisplayManager)McMenuApi.getDisplayManager()).getDisplayBarTitleManager().displayTitle(player, content);
        }

        @Override
        public void clear(Player player) {
            ((McDisplayManager)McMenuApi.getDisplayManager()).getDisplayBarTitleManager().clearTitle(player);
        }
    }
    public static class DisplayBarSubTitle implements DisplayBar {
        @Override
        public String getName() {
            return "subTitle";
        }

        @Override
        public void display(Player player, String content) {
            ((McDisplayManager)McMenuApi.getDisplayManager()).getDisplayBarTitleManager().displaySubTitle(player, content);
        }

        @Override
        public void clear(Player player) {
            ((McDisplayManager)McMenuApi.getDisplayManager()).getDisplayBarTitleManager().clearSubTitle(player);
        }
    }

    class Context {
        String title;
        String subTitle;
    }

    private Map<Player, Context> contexts = new HashMap<>();

    /**
     * 清除显示
     */
    public void clearTitle(Player player) {
        Context context = contexts.get(player);
        if (context != null) {
            context.title = "";
            display(player);
        }
    }

    /**
     * 清除显示
     */
    public void clearSubTitle(Player player) {
        Context context = contexts.get(player);
        if (context != null) {
            context.subTitle = "";
            display(player);
        }
    }

    public void displayTitle(Player player, String content) {
        contexts.computeIfAbsent(player, p -> new Context()).title = content;
        display(player);
    }

    public void displaySubTitle(Player player, String content) {
        contexts.computeIfAbsent(player, p -> new Context()).subTitle = content;
        display(player);
    }

    private void display(Player player) {
        Context context = contexts.get(player);
        if (context != null) {
            String title = context.title;
            if (title == null) {
                title = "";
            }
            String subTitle = context.subTitle;
            if (subTitle == null) {
                subTitle = "";
            }
            if (title.isEmpty() && subTitle.isEmpty()) {
                //删除缓存
                contexts.remove(player);
                //清除title
                player.clearTitle();
            }else {
                player.sendTitle(title, subTitle, 0, 20*60*60, 0);
            }
        }
    }
}
