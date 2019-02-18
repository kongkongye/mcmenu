package com.kongkongye.np.mcmenu.menu.tree;

import cn.nukkit.utils.ConfigSection;
import com.kongkongye.np.mcmenu.util.ColorUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * tree menu config
 */
public class TreeMenuConfig {
    private String name;
    private String description;
    private String command;
    private Mode mode;
    private List<TreeMenuConfig> sub;

    public void load(ConfigSection config) {
        name = config.getString("name");
        description = ColorUtil.convertColor(config.getString("description"));
        command = config.getString("command");
        if (config.containsKey("mode")) {
            mode = Mode.valueOf(config.getString("mode"));
        }else {
            mode = Mode.exit;
        }
        sub = new ArrayList<>();
        for (Object o:config.getList("sub", new ArrayList())) {
            TreeMenuConfig subConfig = new TreeMenuConfig();
            subConfig.load((ConfigSection) o);
            sub.add(subConfig);
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCommand() {
        return command;
    }

    public Mode getMode() {
        return mode;
    }

    /**
     * @return not null
     */
    public List<TreeMenuConfig> getSub() {
        return sub;
    }
}
