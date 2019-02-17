package com.kongkongye.np.mcmenu.menu.tree;

import cn.nukkit.Player;
import com.google.common.base.Preconditions;
import com.kongkongye.np.mcmenu.api.menu.Menu;
import com.kongkongye.np.mcmenu.api.menu.MenuFactory;

import java.util.List;

/**
 * 树状菜单工厂
 */
public class TreeMenuFactory implements MenuFactory {
    private String name;
    private boolean loop;
    private List<TreeMenuConfig> treeMenuConfigs;

    public TreeMenuFactory(String name, boolean loop, List<TreeMenuConfig> treeMenuConfigs) {
        this.name = name;
        this.loop = loop;
        this.treeMenuConfigs = treeMenuConfigs;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isLoop() {
        return loop;
    }

    @Override
    public boolean isUseDefaultExitMethod() {
        return true;
    }

    @Override
    public Menu produce(Player player, int slot) {
        Preconditions.checkArgument(!treeMenuConfigs.isEmpty(), "菜单为空: "+name);
        return new TreeMenu(this, player, slot, player.getLocation());
    }

    public List<TreeMenuConfig> getTreeMenuConfigs() {
        return treeMenuConfigs;
    }
}
