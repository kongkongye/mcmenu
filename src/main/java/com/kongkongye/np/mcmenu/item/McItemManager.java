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

        //标识检测
        if (lore.length >= 1) {
            if (!lore[0].equals(McMenuPlugin.config.getLoreCheck())) {
                return null;
            }
        }else {
            return null;
        }

        //获取信息
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
        //保存
        return item.setLore(lore.toArray(new String[0]));
    }

//    public String save(ConfigSection config) throws Exception {
//        String content = Util.encode(config);
//        byte[] bytes = content.getBytes(Charsets.UTF_8);
//        bytes = Util.compress(bytes);
//        bytes = Util.toBase64(bytes);
//        return new String(bytes, Charsets.UTF_8);
//    }
//
//    public ConfigSection load(String content) throws Exception {
//        byte[] bytes = content.getBytes(Charsets.UTF_8);
//        bytes = Util.fromBase64(bytes);
//        bytes = Util.uncompress(bytes);
//        String s = new String(bytes, Charsets.UTF_8);
//        return Util.decode(s);
//    }
}
