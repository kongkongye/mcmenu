package com.kongkongye.np.mcmenu.util;

import com.kongkongye.np.mcmenu.McMenuPlugin;

public class LogUtil {
    public static final int LEVEL_SPACE = 2;

    /**
     * @param level >=0
     */
    public static void info(int level, String msg) {
        McMenuPlugin.instance.getLogger().info(getSpaces(level)+msg);
    }

    /**
     * @param level >=0
     */
    public static void error(int level, String msg, Throwable t) {
        McMenuPlugin.instance.getLogger().error(getSpaces(level)+msg, t);
    }

    private static String getSpaces(int level) {
        StringBuilder sb = new StringBuilder();
        int max = level*LEVEL_SPACE;
        for (int i=0;i<max;i++) {
            sb.append(" ");
        }
        return sb.toString();
    }
}
