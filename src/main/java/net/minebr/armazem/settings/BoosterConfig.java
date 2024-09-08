package net.minebr.armazem.settings;

import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@Getter
public class BoosterConfig {
    private final boolean useSkull;
    private final String skullUrl;
    private final Material material;
    private final String name;
    private final List<String> lore;
    private final int time;
    private final double multiply;

    public BoosterConfig(boolean useSkull, String skullUrl, Material material, String name, List<String> lore, int time, double multiply) {
        this.useSkull = useSkull;
        this.skullUrl = skullUrl;
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.time = time;
        this.multiply = multiply;
    }
}
