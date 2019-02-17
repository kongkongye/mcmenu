package com.kongkongye.np.mcmenu.api.item;

import cn.nukkit.item.Item;

/**
 * 物品管理器
 */
public interface ItemManager {
    /**
     * 获取物品上的菜单名
     * @return null表示无菜单
     */
    String getMenuName(Item item) ;

    /**
     * 保存菜单信息到物品
     * (会先清空物品上的lore)
     */
    Item saveMenuInfo(Item item, String menuName) throws Exception;
}
