package com.kongkongye.np.mcmenu.display.bar;

import cn.nukkit.Player;
import com.google.common.base.Strings;
import com.kongkongye.np.mcmenu.api.McMenuApi;
import com.kongkongye.np.mcmenu.api.display.DisplayBar;
import com.kongkongye.np.mcmenu.display.McDisplayManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * title&subTitle display manager
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
     * clear title
     */
    public void clearTitle(Player player) {
        Context context = contexts.get(player);
        if (context != null) {
            boolean extend;
            if (!Strings.isNullOrEmpty(context.title)) {
                extend = false;
                context.title = "";
            }else {
                extend = true;
            }
            display(player, extend);
        }
    }

    /**
     * clear sub title
     */
    public void clearSubTitle(Player player) {
        Context context = contexts.get(player);
        if (context != null) {
            boolean extend;
            if (!Strings.isNullOrEmpty(context.subTitle)) {
                extend = false;
                context.subTitle = "";
            }else {
                extend = true;
            }
            display(player, extend);
        }
    }

    public void displayTitle(Player player, String content) {
        String oldTitle = contexts.computeIfAbsent(player, p -> new Context()).title;
        if (Objects.equals(oldTitle, content)) {
            display(player, true);
        }else {
            contexts.computeIfAbsent(player, p -> new Context()).title = content;
            display(player, false);
        }
    }

    public void displaySubTitle(Player player, String content) {
        String oldSubTitle = contexts.computeIfAbsent(player, p -> new Context()).subTitle;
        if (Objects.equals(oldSubTitle, content)) {
            display(player, true);
        }else {
            contexts.computeIfAbsent(player, p -> new Context()).subTitle = content;
            display(player, false);
        }
    }

    /**
     * @param last 是否延长时间
     */
    private void display(Player player, boolean last) {
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
                //delete cache
                contexts.remove(player);
                //clear title
                player.clearTitle();
            }else {
                if (last) {
                    player.setTitleAnimationTimes(0, 20*60*60, 0);
                }else {
                    player.sendTitle(title, subTitle, 0, 20*60*60, 0);
                }
            }
        }
    }
}
