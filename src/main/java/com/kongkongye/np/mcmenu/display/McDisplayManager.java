package com.kongkongye.np.mcmenu.display;

import cn.nukkit.Player;
import com.creeperface.nukkit.placeholderapi.PlaceholderAPIIml;
import com.google.common.base.Preconditions;
import com.kongkongye.np.mcmenu.McMenuPlugin;
import com.kongkongye.np.mcmenu.api.display.Display;
import com.kongkongye.np.mcmenu.api.display.DisplayBar;
import com.kongkongye.np.mcmenu.api.display.DisplayManager;
import com.kongkongye.np.mcmenu.config.Config;
import com.kongkongye.np.mcmenu.display.bar.*;
import com.kongkongye.np.mcmenu.util.ColorUtil;
import com.kongkongye.np.mcmenu.util.ParamUtil;
import com.kongkongye.np.mcmenu.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class McDisplayManager implements DisplayManager {
    class Context {
        Map<Player, String> contexts;
        Integer taskId;

        public Context() {
            contexts = new HashMap<>();
        }
    }

    private DisplayBarTitleManager displayBarTitleManager;
    private DisplayBarTitleManager.DisplayBarTitle displayBarTitle;
    private DisplayBarTitleManager.DisplayBarSubTitle displayBarSubTitle;
    private DisplayBarChat displayBarChat;
    private DisplayBarTip displayBarTip;
    private DisplayBarActionBar displayBarActionBar;
    private DisplayBarPopup displayBarPopup;

    private Map<String, DisplayBar> displayBars = new HashMap<>();
    /**
     * display-bar player content
     */
    private Map<DisplayBar, Context> displayBarContexts = new HashMap<>();

    public void initOnLoad() {
        displayBarTitleManager = new DisplayBarTitleManager();
        //register display bar: title
        displayBarTitle = new DisplayBarTitleManager.DisplayBarTitle();
        registerDisplayBar(displayBarTitle);
        //register display bar: subTitle
        displayBarSubTitle = new DisplayBarTitleManager.DisplayBarSubTitle();
        registerDisplayBar(displayBarSubTitle);
        //register display bar: chat
        displayBarChat = new DisplayBarChat();
        registerDisplayBar(displayBarChat);
        //register display bar: tip
        displayBarTip = new DisplayBarTip();
        registerDisplayBar(displayBarTip);
        //register display bar: actionbar
        displayBarActionBar = new DisplayBarActionBar();
        registerDisplayBar(displayBarActionBar);
        //register display bar: popup
        displayBarPopup = new DisplayBarPopup();
        registerDisplayBar(displayBarPopup);
    }

    /**
     * registering display bar must be finished before this phase
     */
    public void initOnEnable() {
        reload();
    }

    public void reload() {
        //refresh display scheduler
        displayBarContexts.forEach((displayBar, displayContext) -> {
            //cancel old
            if (displayContext.taskId != null) {
                McMenuPlugin.instance.getServer().getScheduler().cancelTask(displayContext.taskId);
                displayContext.taskId = null;
            }
            //set new
            Integer refresh = McMenuPlugin.config.getDisplayRefresh().get(displayBar.getName());
            if (refresh != null) {
                displayContext.taskId = McMenuPlugin.instance.getServer().getScheduler().scheduleRepeatingTask(McMenuPlugin.instance,
                        () -> displayContext.contexts.forEach((p, content) -> display(displayBar, p, content))
                        , refresh, false).getTaskId();
            }
        });
    }

    @Override
    public void registerDisplayBar(DisplayBar displayBar) {
        Preconditions.checkArgument(displayBars.putIfAbsent(displayBar.getName(), displayBar) == null,
                "display bar is registered: "+displayBar.getName());
        displayBarContexts.put(displayBar, new Context());
        //log
        Util.info(0, 1080, displayBar.getName());
    }

    @Override
    public void display(Player player, Display display) {
        //first clear display
        DisplayBar mainBar = displayBars.get(McMenuPlugin.config.getDisplayBarMain());
        if (mainBar != null) {
            displayBarContexts.get(mainBar).contexts.remove(player);
            mainBar.clear(player);
        }
        DisplayBar subBar = displayBars.get(McMenuPlugin.config.getDisplayBarSub());
        if (subBar != null) {
            displayBarContexts.get(subBar).contexts.remove(player);
            subBar.clear(player);
        }

        //then display
        if (display != null) {
            //show menu
            HoleLine holeLine = getHoleLine(convertGlobalParam(player, display.getMenus()), display.getIndex());
            if (mainBar != null) {
                Integer limit = McMenuPlugin.config.getDisplayLimit().get(McMenuPlugin.config.getDisplayBarMain());
                if (limit == null) {//no limit
                    display(mainBar, player, holeLine.pre+holeLine.current+holeLine.post);
                }else {//has limit
                    if (limit <= holeLine.current.length()) {//can display one menu at most
                        display(mainBar, player, holeLine.current);
                    }else {//can display more than one menu
                        int left = limit - holeLine.current.length();
                        int leftSingle = left/2;

                        int preMore = leftSingle - holeLine.pre.length();//more spaces forward
                        int postMore = leftSingle - holeLine.post.length();//more spaces afterward

                        int preTotalSpace = leftSingle;//all spaces allocated forward
                        int postTotalSpace = leftSingle;//all spaces allocated afterward
                        if (preMore > 0) {
                            preTotalSpace = holeLine.pre.length();
                            postTotalSpace = leftSingle+preMore;
                        }else if (postMore > 0) {
                            postTotalSpace = holeLine.post.length();
                            preTotalSpace = leftSingle+postMore;
                        }

                        //forward
                        String pre;
                        if (preTotalSpace >= holeLine.pre.length()) {//can display all
                            pre = holeLine.pre;
                        }else {//can display part
                            pre = holeLine.pre.substring(holeLine.pre.length()-preTotalSpace);
                            //need process forward, loss a bit,
                            //throw away all contents before first color (&7 for example)
                            //(to avoid display a fragment of text that has no color, affecting the effect of display)
                            int startIndex = -1;
                            for (int i = 0;i<pre.length();i++) {
                                if (pre.charAt(i) == ColorUtil.MC_COLOR_CHAR.charAt(0)) {
                                    //no next char
                                    if (i >= pre.length()-1) {
                                        break;
                                    }

                                    //check if is color string (format string is not included)
                                    if ("0123456789abcdef".indexOf(pre.charAt(i+1)) != -1) {
                                        startIndex = i;
                                        break;
                                    }
                                }
                            }
                            if (startIndex == -1) {//throw away all contents when no color string is found
                                pre = "";
                            }else {
                                pre = pre.substring(startIndex);
                            }
                        }

                        //afterward
                        String post;
                        if (postTotalSpace >= holeLine.post.length()) {//can display all
                            post = holeLine.post;
                        }else {//can display part
                            post = holeLine.post.substring(0, postTotalSpace);
                        }

                        display(mainBar, player, pre+holeLine.current+post);
                    }
                }
            }
            //display description
            if (subBar != null) {
                Map<String, Object> paramsDescription = new HashMap<>();
                paramsDescription.put("content", convertGlobalParam(player, display.getDescription()));
                display(subBar, player, ParamUtil.convert(McMenuPlugin.config.getDisplayFormatDescription(), paramsDescription, false));
            }
        }
    }

    public DisplayBarTitleManager getDisplayBarTitleManager() {
        return displayBarTitleManager;
    }

    private void display(DisplayBar displayBar, Player player, String content) {
        if (content != null) {
            content = ColorUtil.trimExtraColors(content);
            content = content.trim();
        }
        displayBar.display(player, content);
        //cache
        Preconditions.checkNotNull(displayBarContexts.get(displayBar)).contexts.put(player, content);
    }

    private class HoleLine {
        String pre;
        String post;
        String current;

        public HoleLine(String pre, String post, String current) {
            this.pre = pre;
            this.post = post;
            this.current = current;
        }
    }

    /**
     * get the hole line of string
     */
    private HoleLine getHoleLine(List<String> menus, int index) {
        Config config = McMenuPlugin.config;

        //pre
        List<String> preMenus = new ArrayList<>();//may be null
        for (int i=0;i<index;i++) {
            preMenus.add(ParamUtil.convert(config.getDisplayFormatNotCurrent(), false, menus.get(i)));
        }
        String pre = String.join(config.getDisplayFormatSeparator(), preMenus);
        if (!pre.isEmpty()) {
            pre = pre+config.getDisplayFormatSeparator();
        }
        pre = config.getDisplayFormatStart()+pre;
        pre = ColorUtil.trimExtraColors(pre);

        //post
        List<String> postMenus = new ArrayList<>();//may be null
        for (int i=index+1;i<menus.size();i++) {
            Map<String, Object> paramsNotCurrent = new HashMap<>();
            paramsNotCurrent.put("content", menus.get(i));
            postMenus.add(ParamUtil.convert(config.getDisplayFormatNotCurrent(), paramsNotCurrent, false));
        }
        String post = String.join(config.getDisplayFormatSeparator(), postMenus);
        if (!post.isEmpty()) {
            post = config.getDisplayFormatSeparator()+post;
        }
        post = post+config.getDisplayFormatEnd();
        post = ColorUtil.trimExtraColors(post);

        //current
        Map<String, Object> paramsCurrent = new HashMap<>();
        paramsCurrent.put("content", menus.get(index));
        String current = ParamUtil.convert(config.getDisplayFormatCurrent(), paramsCurrent, false);

        return new HoleLine(pre, post, current);
    }

    private List<String> convertGlobalParam(Player player, List<String> list) {
        return list.stream().map(e -> this.convertGlobalParam(player, e)).collect(Collectors.toList());
    }

    private String convertGlobalParam(Player player, String s) {
        return PlaceholderAPIIml.getInstance().translateString(s, player);
    }
}
