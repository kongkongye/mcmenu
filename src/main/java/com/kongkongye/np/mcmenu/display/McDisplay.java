package com.kongkongye.np.mcmenu.display;

import com.kongkongye.np.mcmenu.api.display.Display;

import java.util.List;

public class McDisplay implements Display {
    private List<String> menus;
    private int index;
    private String description;

    public McDisplay(List<String> menus, int index, String description) {
        this.menus = menus;
        this.index = index;
        this.description = description;
    }

    @Override
    public List<String> getMenus() {
        return menus;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
