package com.kongkongye.np.mcmenu.api.display;

import java.util.List;

/**
 * display
 */
public interface Display {
    /**
     * get menus
     */
    List<String> getMenus();

    /**
     * get the index of current menu
     * @return index, start from 0
     */
    int getIndex();

    /**
     * get the description of current menu
     */
    String getDescription();
}
