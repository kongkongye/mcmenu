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
 * tree menu
 */
public class TreeMenu extends AbstractMenu {
    /**
     * index position, start from 0
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
        //index-1
        if (index <= 0) {//in first
            if (getMenuFactory().isLoop()) {//to last
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
        //index+1
        if (index >= brothers.size()-1) {//in last
            if (getMenuFactory().isLoop()) {//to first
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
        //sub menu first
        if (!config.getSub().isEmpty()) {
            indexes.add(0);
            return getDisplay();
        }
        //then command
        if (!Strings.isNullOrEmpty(config.getCommand())) {
            boolean success = McMenuApi.getCommandService().execute(getPlayer(), config.getCommand());
            if (success) {
                //menu mode will take effect when still on this menu
                Menu menu = McMenuApi.getMenuManager().getMenu(getPlayer());
                if (menu == this) {
                    switch (config.getMode()) {
                        case stay:
                            break;
                        case back:
                            McMenuApi.getActionManager().back(getPlayer());
                            break;
                        case exit://return null will exit menu
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

        //exit
        if (indexes.size() == 1) {
            return null;
        }

        //back
        indexes.remove(indexes.size()-1);
        return getDisplay();
    }

    /**
     * get display
     */
    public Display getDisplay() {
        TreeMenuConfig current = getCurrent();
        List<TreeMenuConfig> brothers = getBrothers();
        return new McDisplay(brothers.stream().map(TreeMenuConfig::getName).collect(Collectors.toList()), indexes.get(indexes.size()-1), current.getDescription());
    }

    /**
     * get the config of parent menu
     * @return may return null representing root menu
     */
    private TreeMenuConfig getParent() {
        return get(1);
    }

    /**
     * get brother menu configs
     * @return not null or empty
     */
    private List<TreeMenuConfig> getBrothers() {
        TreeMenuConfig parent = getParent();
        return parent != null?parent.getSub():((TreeMenuFactory)getMenuFactory()).getTreeMenuConfigs();
    }

    /**
     * get the config of current menu
     * @return not null
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
