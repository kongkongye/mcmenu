package com.kongkongye.np.mcmenu.config;

import cn.nukkit.level.Sound;
import com.google.common.base.Strings;
import com.kongkongye.np.mcmenu.util.ColorUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Config {
    private boolean exitMoveEnable;
    private double exitMoveDistance;
    private boolean exitOpenInventoryEnable;

    private String loreCheck;
    private String loreTitle;
    private List<String> loreDescriptions;
    private String loreMenu;
    private int prefixLength;
    private int suffixLength;

    private Map<String, Integer> displayLimit;
    private Map<String, Integer> displayRefresh;
    private String displayBarMain;
    private String displayBarSub;
    private String displayFormatStart;
    private String displayFormatEnd;
    private String displayFormatSeparator;
    private String displayFormatCurrent;
    private String displayFormatNotCurrent;
    private String displayFormatDescription;

    private float soundVolume;
    private float soundPitch;
    private Sound soundJoin;
    private Sound soundLeft;
    private Sound soundRight;
    private Sound soundConfirm;
    private Sound soundBack;

    public void load(cn.nukkit.utils.Config config) {
        exitMoveEnable = config.getBoolean("exit.move.enable");
        exitMoveDistance = config.getDouble("exit.move.distance");
        exitOpenInventoryEnable = config.getBoolean("exit.openInventory.enable");

        loreCheck = ColorUtil.convertColor(config.getString("lore.check"));
        loreTitle = ColorUtil.convertColor(config.getString("lore.title"));
        loreDescriptions = config.getStringList("lore.descriptions").stream().map(ColorUtil::convertColor).collect(Collectors.toList());
        loreMenu = ColorUtil.convertColor(config.getString("lore.menu"));
        prefixLength = loreMenu.indexOf("?");
        suffixLength = loreMenu.length() - 1 - loreMenu.indexOf("?");

        displayLimit = new HashMap<>();
        for (String s:config.getStringList("display.limit")) {
            String[] ss = s.split(" ");
            displayLimit.put(ss[0], Integer.parseInt(ss[1]));
        }
        displayRefresh = new HashMap<>();
        for (String s:config.getStringList("display.refresh")) {
            String[] ss = s.split(" ");
            displayRefresh.put(ss[0], Integer.parseInt(ss[1]));
        }
        displayBarMain = config.getString("display.bar.main");
        displayBarSub = config.getString("display.bar.sub");
        displayFormatStart = ColorUtil.convertColor(config.getString("display.format.start"));
        displayFormatEnd = ColorUtil.convertColor(config.getString("display.format.end"));
        displayFormatSeparator = ColorUtil.convertColor(config.getString("display.format.separator"));
        displayFormatCurrent = ColorUtil.convertColor(config.getString("display.format.current"));
        displayFormatNotCurrent = ColorUtil.convertColor(config.getString("display.format.notCurrent"));
        displayFormatDescription = ColorUtil.convertColor(config.getString("display.format.description"));

        soundVolume = (float) config.getDouble("sound.volume");
        soundPitch = (float) config.getDouble("sound.pitch");
        String soundJoinStr = config.getString("sound.join");
        if (!Strings.isNullOrEmpty(soundJoinStr)) {
            soundJoin = Sound.valueOf(soundJoinStr);
        }
        String soundLeftStr = config.getString("sound.left");
        if (!Strings.isNullOrEmpty(soundLeftStr)) {
            soundLeft = Sound.valueOf(soundLeftStr);
        }
        String soundRightStr = config.getString("sound.right");
        if (!Strings.isNullOrEmpty(soundRightStr)) {
            soundRight = Sound.valueOf(soundRightStr);
        }
        String soundConfirmStr = config.getString("sound.confirm");
        if (!Strings.isNullOrEmpty(soundConfirmStr)) {
            soundConfirm = Sound.valueOf(soundConfirmStr);
        }
        String soundBackStr = config.getString("sound.back");
        if (!Strings.isNullOrEmpty(soundBackStr)) {
            soundBack = Sound.valueOf(soundBackStr);
        }
    }

    public boolean isExitMoveEnable() {
        return exitMoveEnable;
    }

    public double getExitMoveDistance() {
        return exitMoveDistance;
    }

    public boolean isExitOpenInventoryEnable() {
        return exitOpenInventoryEnable;
    }

    public String getLoreCheck() {
        return loreCheck;
    }

    public String getLoreTitle() {
        return loreTitle;
    }

    public List<String> getLoreDescriptions() {
        return loreDescriptions;
    }

    public String getLoreMenu() {
        return loreMenu;
    }

    public int getPrefixLength() {
        return prefixLength;
    }

    public int getSuffixLength() {
        return suffixLength;
    }

    public Map<String, Integer> getDisplayLimit() {
        return displayLimit;
    }

    public Map<String, Integer> getDisplayRefresh() {
        return displayRefresh;
    }

    public String getDisplayBarMain() {
        return displayBarMain;
    }

    public String getDisplayBarSub() {
        return displayBarSub;
    }

    public String getDisplayFormatStart() {
        return displayFormatStart;
    }

    public String getDisplayFormatEnd() {
        return displayFormatEnd;
    }

    public String getDisplayFormatSeparator() {
        return displayFormatSeparator;
    }

    public String getDisplayFormatCurrent() {
        return displayFormatCurrent;
    }

    public String getDisplayFormatNotCurrent() {
        return displayFormatNotCurrent;
    }

    public String getDisplayFormatDescription() {
        return displayFormatDescription;
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    public float getSoundPitch() {
        return soundPitch;
    }

    public Sound getSoundJoin() {
        return soundJoin;
    }

    public Sound getSoundLeft() {
        return soundLeft;
    }

    public Sound getSoundRight() {
        return soundRight;
    }

    public Sound getSoundConfirm() {
        return soundConfirm;
    }

    public Sound getSoundBack() {
        return soundBack;
    }
}
