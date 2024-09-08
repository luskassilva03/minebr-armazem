package net.minebr.armazem.settings;

import lombok.Getter;
import org.bukkit.Material;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class HoeConfig {
    private final Material material;
    private final String name;
    private final List<String> lore;
    private final Map<String, Integer> enchants;

    public HoeConfig(Material material, String name, List<String> lore, Map<String, Integer> enchants) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.enchants = enchants;
    }
}
