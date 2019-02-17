package com.kongkongye.np.mcmenu.menu.tree;

import cn.nukkit.Player;
import cn.nukkit.level.Location;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.kongkongye.np.mcmenu.api.McMenuApi;
import com.kongkongye.np.mcmenu.api.display.Display;
import com.kongkongye.np.mcmenu.api.menu.Menu;
import com.kongkongye.np.mcmenu.api.menu.MenuFactory;
import com.kongkongye.np.mcmenu.display.McDisplay;
import com.kongkongye.np.mcmenu.menu.AbstractMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 树状菜单
 */
public class TreeMenu extends AbstractMenu {
    /**
     * 索引位置,从0开始
     */
    private List<Integer> indexes = new ArrayList<>();

    public TreeMenu(MenuFactory menuFactory, Player player, int slot, Location location) {
        super(menuFactory, player, slot, location);
        indexes.add(0);
    }

    @Override
    public Display left() {
        Preconditions.checkArgument(indexes.size() >= 1);

        List<TreeMenuConfig> brothers = getBrothers();
        int index = indexes.get(indexes.size()-1);
        //索引-1
        if (index <= 0) {//最前
            if (getMenuFactory().isLoop()) {//到最后
                index = brothers.size()-1;
            }
        }else {
            index--;
        }
        indexes.set(indexes.size()-1, index);

        return getDisplay();
    }

    @Override
    public Display right() {
        Preconditions.checkArgument(indexes.size() >= 1);

        List<TreeMenuConfig> brothers = getBrothers();
        int index = indexes.get(indexes.size()-1);
        //索引+1
        if (index >= brothers.size()-1) {//最后
            if (getMenuFactory().isLoop()) {//到最前
                index = 0;
            }
        }else {
            index++;
        }
        indexes.set(indexes.size()-1, index);

        return getDisplay();
    }

    @Override
    public Display confirm() {
        TreeMenuConfig config = getCurrent();
        //子菜单优先
        if (!config.getSub().isEmpty()) {
            indexes.add(0);
            return getDisplay();
        }
        //命令次之
        if (!Strings.isNullOrEmpty(config.getCommand())) {
            boolean success = McMenuApi.getCommandService().execute(getPlayer(), config.getCommand());
            if (success) {
                //任然是当前菜单,菜单模式才会生效
                Menu menu = McMenuApi.getMenuManager().getMenu(getPlayer());
                if (menu == this) {
                    switch (config.getMode()) {
                        case stay:
                            break;
                        case back:
                            McMenuApi.getActionManager().back(getPlayer());
                            break;
                        case exit://返回null会退出菜单
                            return null;
                        default:
                            throw new RuntimeException();
                    }
                }else {
                    return menu != null?menu.getDisplay():null;
                }
            }
        }
        return getDisplay();
    }

    @Override
    public Display back() {
        Preconditions.checkArgument(indexes.size() >= 1);

        //退出
        if (indexes.size() == 1) {
            return null;
        }

        //返回上一级
        indexes.remove(indexes.size()-1);
        return getDisplay();
    }

    /**
     * 获取显示
     */
    public Display getDisplay() {
        TreeMenuConfig current = getCurrent();
        List<TreeMenuConfig> brothers = getBrothers();
        return new McDisplay(brothers.stream().map(TreeMenuConfig::getName).collect(Collectors.toList()), indexes.get(indexes.size()-1), current.getDescription());
    }

    /**
     * 获取父菜单配置
     * @return 可为null表示已经是根菜单
     */
    private TreeMenuConfig getParent() {
        return get(1);
    }

    /**
     * 获取同级(兄弟)配置列表
     * @return 不为null或空
     */
    private List<TreeMenuConfig> getBrothers() {
        TreeMenuConfig parent = getParent();
        return parent != null?parent.getSub():((TreeMenuFactory)getMenuFactory()).getTreeMenuConfigs();
    }

    /**
     * 获取当前菜单配置
     * @return 不为null
     */
    private TreeMenuConfig getCurrent() {
        return get(0);
    }

    private TreeMenuConfig get(int del) {
        Preconditions.checkArgument(indexes.size() >= 1);

        TreeMenuConfig config = null;
        List<TreeMenuConfig> rootConfigs = ((TreeMenuFactory)getMenuFactory()).getTreeMenuConfigs();
        for (int i=0;i<indexes.size()-del;i++) {
            config = rootConfigs.get(indexes.get(i));
            rootConfigs = config.getSub();
        }
        return config;
    }
}
