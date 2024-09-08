package net.minebr.armazem.settings.menus;

import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@Getter
public class ArmazemMenuConfig {
    private final int slot;
    private final boolean useSkull;
    private final String skullUrl;
    private final Material material;
    private final String display;
    private final List<String> lore;

    public ArmazemMenuConfig(int slot, boolean useSkull, String skullUrl, Material material, String display, List<String> lore) {
        this.slot = slot;
        this.useSkull = useSkull;
        this.skullUrl = skullUrl;
        this.material = material;
        this.display = display;
        this.lore = lore;
    }
}
