package com.kongkongye.np.mcmenu.item;

import cn.nukkit.item.Item;
import com.kongkongye.np.mcmenu.McMenuPlugin;
import com.kongkongye.np.mcmenu.api.item.ItemManager;
import com.kongkongye.np.mcmenu.util.ParamUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class McItemManager implements ItemManager {
    @Override
    public String getMenuName(Item item) {
        String[] lore = item.getLore();

        //check flag
        if (lore.length >= 1) {
            if (!lore[0].equals(McMenuPlugin.config.getLoreCheck())) {
                return null;
            }
        }else {
            return null;
        }

        //get info
        String data = lore[lore.length-1];
        try {
            return data.substring(McMenuPlugin.config.getPrefixLength(), data.length()-McMenuPlugin.config.getSuffixLength());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Item saveMenuInfo(Item item, String menuName) throws Exception {
        List<String> lore = new ArrayList<>();
        //check
        lore.add(McMenuPlugin.config.getLoreCheck());
        //title
        Map<String, Object> titleParams = new HashMap<>();
        titleParams.put("menu", menuName);
        item = item.setCustomName(ParamUtil.convert(McMenuPlugin.config.getLoreTitle(), titleParams, false));
        //descriptions
        lore.addAll(McMenuPlugin.config.getLoreDescriptions());
        //data
        Map<String, Object> dataParams = new HashMap<>();
        dataParams.put("menu", menuName);
        lore.add(ParamUtil.convert(McMenuPlugin.config.getLoreMenu(), dataParams, false));
        //save
        return item.setLore(lore.toArray(new String[0]));
    }
}
