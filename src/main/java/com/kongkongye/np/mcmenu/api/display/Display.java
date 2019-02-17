package com.kongkongye.np.mcmenu.api.display;

import java.util.List;

/**
 * 显示
 */
public interface Display {
    /**
     * 获取菜单列表
     */
    List<String> getMenus();

    /**
     * 获取当前菜单位置
     * @return 位置,从0开始
     */
    int getIndex();

    /**
     * 获取当前菜单项描述
     */
    String getDescription();
}
