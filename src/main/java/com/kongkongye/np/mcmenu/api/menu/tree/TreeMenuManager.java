package com.kongkongye.np.mcmenu.api.menu.tree;

import java.io.File;

/**
 * 树状菜单管理器
 */
public interface TreeMenuManager {
    /**
     * 注册菜单目录(会自动读取目录内的全部直接子文件)
     */
    void registerFolder(File folder);

    /**
     * 注册菜单文件
     */
    void registerFile(File file);
}
