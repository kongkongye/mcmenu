package com.kongkongye.np.mcmenu.api.lang;

/**
 * lang manager
 */
public interface LangManager {
    /**
     * get the lang
     * @param id lang ID
     * @param args params
     */
    String get(int id, Object... args);
}
