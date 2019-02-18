package com.kongkongye.np.mcmenu.api.item;

import cn.nukkit.item.Item;

/**
 * item manager
 */
public interface ItemManager {
    /**
     * get the name on item
     * @return null represents no menu info
     */
    String getMenuName(Item item) ;

    /**
     * save menu info to item
     * (will clear lore on item first)
     */
    Item saveMenuInfo(Item item, String menuName) throws Exception;
}
