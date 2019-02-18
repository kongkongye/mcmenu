package com.kongkongye.np.mcmenu.lang;

import cn.nukkit.utils.Config;
import com.kongkongye.np.mcmenu.McMenuPlugin;
import com.kongkongye.np.mcmenu.api.lang.LangManager;
import com.kongkongye.np.mcmenu.util.ColorUtil;
import com.kongkongye.np.mcmenu.util.ParamUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class McLangManager implements LangManager{
    private static final String LANG_FILE = "lang.yml";
    private static final String LANG_PREFIX = "lang-";

    private Map<Integer, String> lang;

    public void initOnLoad() {
        //reload lang
        reload();
    }

    @Override
    public String get(int id, Object... args) {
        return ParamUtil.convert(lang.get(id), false, args);
    }

    /**
     * reload lang
     */
    public void reload() {
        File langFile = new File(McMenuPlugin.instance.getDataFolder(), LANG_FILE);
        Config config = new Config(langFile);

        this.lang = new HashMap<>();
        for (String key:config.getKeys(false)) {
            if (key.startsWith(LANG_PREFIX)) {
                int id = Integer.parseInt(key.split("-")[1]);
                String content = ColorUtil.convertColor(config.getString(key));
                this.lang.put(id, content);
            }
        }
    }
}
