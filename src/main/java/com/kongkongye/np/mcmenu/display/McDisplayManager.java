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
     * 显示条 玩家 内容
     */
    private Map<DisplayBar, Context> displayBarContexts = new HashMap<>();

    public void initOnLoad() {
        displayBarTitleManager = new DisplayBarTitleManager();
        //注册显示条: title
        displayBarTitle = new DisplayBarTitleManager.DisplayBarTitle();
        registerDisplayBar(displayBarTitle);
        //注册显示条: subTitle
        displayBarSubTitle = new DisplayBarTitleManager.DisplayBarSubTitle();
        registerDisplayBar(displayBarSubTitle);
        //注册显示条: chat
        displayBarChat = new DisplayBarChat();
        registerDisplayBar(displayBarChat);
        //注册显示条: tip
        displayBarTip = new DisplayBarTip();
        registerDisplayBar(displayBarTip);
        //注册显示条: actionbar
        displayBarActionBar = new DisplayBarActionBar();
        registerDisplayBar(displayBarActionBar);
        //注册显示条: popup
        displayBarPopup = new DisplayBarPopup();
        registerDisplayBar(displayBarPopup);
    }

    /**
     * 在此阶段前需要注册完毕显示条
     */
    public void initOnEnable() {
        reload();
    }

    public void reload() {
        //刷新显示计时器
        displayBarContexts.forEach((displayBar, displayContext) -> {
            //取消旧的
            if (displayContext.taskId != null) {
                McMenuPlugin.instance.getServer().getScheduler().cancelTask(displayContext.taskId);
                displayContext.taskId = null;
            }
            //设置新的
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
                "显示条已经注册: "+displayBar.getName());
        displayBarContexts.put(displayBar, new Context());
    }

    @Override
    public void display(Player player, Display display) {
        //先清除显示
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

        //再显示
        if (display != null) {
            //显示菜单
            HoleLine holeLine = getHoleLine(convertGlobalParam(player, display.getMenus()), display.getIndex());
            if (mainBar != null) {
                Integer limit = McMenuPlugin.config.getDisplayLimit().get(McMenuPlugin.config.getDisplayBarMain());
                if (limit == null) {//无限制
                    display(mainBar, player, holeLine.pre+holeLine.current+holeLine.post);
                }else {//有限制
                    if (limit <= holeLine.current.length()) {//最多显示一个菜单
                        display(mainBar, player, holeLine.current);
                    }else {//还有余地
                        int left = limit - holeLine.current.length();
                        int leftSingle = left/2;

                        int preMore = leftSingle - holeLine.pre.length();//前多余的空格
                        int postMore = leftSingle - holeLine.post.length();//后多余的空格

                        int preTotalSpace = leftSingle;//分配给前的总空格
                        int postTotalSpace = leftSingle;//分配给后的总空格
                        if (preMore > 0) {
                            preTotalSpace = holeLine.pre.length();
                            postTotalSpace = leftSingle+preMore;
                        }else if (postMore > 0) {
                            postTotalSpace = holeLine.post.length();
                            preTotalSpace = leftSingle+postMore;
                        }

                        //前
                        String pre;
                        if (preTotalSpace >= holeLine.pre.length()) {//可以全部显示
                            pre = holeLine.pre;
                        }else {//只能显示部分
                            pre = holeLine.pre.substring(holeLine.pre.length()-preTotalSpace);
                            //前需要处理,损失一点,把第一个颜色(如&7)前的内容全部抛弃(防止显示一段缺颜色格式的文本,影响显示效果)
                            int startIndex = -1;
                            for (int i = 0;i<pre.length();i++) {
                                if (pre.charAt(i) == ColorUtil.MC_COLOR_CHAR.charAt(0)) {
                                    //没有下个字符了
                                    if (i >= pre.length()-1) {
                                        break;
                                    }

                                    //检测是颜色字符(不包括格式字符)
                                    if ("0123456789abcdef".indexOf(pre.charAt(i+1)) != -1) {
                                        startIndex = i;
                                        break;
                                    }
                                }
                            }
                            if (startIndex == -1) {//没有找到颜色字符,全部抛弃
                                pre = "";
                            }else {
                                pre = pre.substring(startIndex);
                            }
                        }

                        //后
                        String post;
                        if (postTotalSpace >= holeLine.post.length()) {//可以全部显示
                            post = holeLine.post;
                        }else {//只能显示部分
                            post = holeLine.post.substring(0, postTotalSpace);
                        }

                        display(mainBar, player, pre+holeLine.current+post);
                    }
                }
            }
            //显示描述
            if (subBar != null) {
                display(subBar, player, ParamUtil.convert(McMenuPlugin.config.getDisplayFormatDescription(), false, convertGlobalParam(player, display.getDescription())));
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
        //缓存
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
     * 获取整行字符串
     */
    private HoleLine getHoleLine(List<String> menus, int index) {
        Config config = McMenuPlugin.config;

        //pre
        List<String> preMenus = new ArrayList<>();//可能为空
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
        List<String> postMenus = new ArrayList<>();//可能为空
        for (int i=index+1;i<menus.size();i++) {
            postMenus.add(ParamUtil.convert(config.getDisplayFormatNotCurrent(), false, menus.get(i)));
        }
        String post = String.join(config.getDisplayFormatSeparator(), postMenus);
        if (!post.isEmpty()) {
            post = config.getDisplayFormatSeparator()+post;
        }
        post = post+config.getDisplayFormatEnd();
        post = ColorUtil.trimExtraColors(post);

        //current
        String current = ParamUtil.convert(config.getDisplayFormatCurrent(), false, menus.get(index));

        return new HoleLine(pre, post, current);
    }

    private List<String> convertGlobalParam(Player player, List<String> list) {
        return list.stream().map(e -> this.convertGlobalParam(player, e)).collect(Collectors.toList());
    }

    private String convertGlobalParam(Player player, String s) {
        return PlaceholderAPIIml.getInstance().translateString(s, player);
    }
}
