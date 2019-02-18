package com.kongkongye.np.mcmenu.api.lang;

/**
 * 语言管理器
 */
public interface LangManager {
    /**
     * 获取语言
     * @param id 语言ID
     * @param args 变量
     */
    String get(int id, Object... args);
}
