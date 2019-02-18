package com.kongkongye.np.mcmenu.api.menu.tree;

import java.io.File;

/**
 * tree menu manager
 */
public interface TreeMenuManager {
    /**
     * register menu folder (will load all direct files in the folder)
     */
    void registerFolder(File folder);

    /**
     * register menu file
     */
    void registerFile(File file);
}
