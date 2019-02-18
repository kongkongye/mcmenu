package com.kongkongye.np.mcmenu.menu.tree;

import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import com.google.common.collect.Lists;
import com.kongkongye.np.mcmenu.Constants;
import com.kongkongye.np.mcmenu.McMenuPlugin;
import com.kongkongye.np.mcmenu.api.McMenuApi;
import com.kongkongye.np.mcmenu.api.menu.tree.TreeMenuManager;
import com.kongkongye.np.mcmenu.util.Util;

import java.io.File;
import java.util.*;

/**
 * tree menu manager
 */
public class McTreeMenuManager implements TreeMenuManager {
    private Set<File> folders = new HashSet<>();
    private Set<File> files = new HashSet<>();

    public void initOnLoad() {
        //register default tree menu folder
        registerFolder(new File(McMenuPlugin.instance.getDataFolder(), Constants.FOLDER_MENUS));
    }

    public void initOnEnable() {
        reload();
    }

    public void reload() {
        //delete old registers
        Lists.newArrayList(McMenuApi.getMenuManager().getMenuFactories()).forEach(menuFactory -> {
            if (menuFactory instanceof TreeMenuFactory) {
                McMenuApi.getMenuManager().removeMenuFactory(menuFactory.getName());
            }
        });
        //register new ones
        folders.forEach(this::reloadFolder);
        files.forEach(this::reloadFile);
    }

    @Override
    public void registerFolder(File folder) {
        folders.add(folder);
        //log
        Util.info(0, 1060, folder.getPath());
    }

    @Override
    public void registerFile(File file) {
        files.add(file);
        //log
        Util.info(0, 1070, file.getPath());
    }

    private void reloadFolder(File folder) {
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                Arrays.stream(files).forEach(this::reloadFile);
            }
        }
    }

    private void reloadFile(File file) {
        if (file.exists() && file.isFile() && file.getName().endsWith(".yml")) {
            Config config = new Config(file);
            String name = file.getName().substring(0, file.getName().lastIndexOf("."));
            boolean loop = config.getBoolean("loop");
            List<TreeMenuConfig> treeMenuConfigs = new ArrayList<>();
            List menus = config.getList("menus", new ArrayList());
            for (Object o:menus) {
                TreeMenuConfig treeMenuConfig = new TreeMenuConfig();
                treeMenuConfig.load((ConfigSection) o);
                treeMenuConfigs.add(treeMenuConfig);
            }
            McMenuApi.getMenuManager().register(new TreeMenuFactory(name, loop, treeMenuConfigs));
        }
    }
}
