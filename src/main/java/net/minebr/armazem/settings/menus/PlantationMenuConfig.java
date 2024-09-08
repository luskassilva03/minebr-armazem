package net.minebr.armazem.settings.menus;

import lombok.Getter;

import java.util.List;

@Getter
public class PlantationMenuConfig {
    private final int slot;
    private final String display;
    private final List<String> lore;

    public PlantationMenuConfig(int slot, String display, List<String> lore) {
        this.slot = slot;
        this.display = display;
        this.lore = lore;
    }
}
