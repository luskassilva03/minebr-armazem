package net.minebr.armazem.loader;

import net.minebr.armazem.ArmazemMain;
import net.minebr.armazem.settings.HoeConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hoes {
    private final ArmazemMain main = ArmazemMain.getPlugin();

    public void load() {
        try {
            ConfigurationSection hoesSection = main.getHoes().getConfigurationSection("hoes");
            if (hoesSection != null) {
                Bukkit.getConsoleSender().sendMessage("§a(minebr-armazem) Carregando hoes...");
                for (String hoeKey : hoesSection.getKeys(false)) {
                    ConfigurationSection hoeSection = hoesSection.getConfigurationSection(hoeKey);

                    String materialString = hoeSection.getString("material");
                    Material material = Material.matchMaterial(materialString.split(":")[0]);

                    String name = hoeSection.getString("name").replace("&", "§");
                    List<String> lore = hoeSection.getStringList("lore");

                    ConfigurationSection enchantsSection = hoeSection.getConfigurationSection("enchants");
                    Map<String, Integer> enchants = new HashMap<>();
                    if (enchantsSection != null) {
                        for (String enchantKey : enchantsSection.getKeys(false)) {
                            enchants.put(enchantKey, enchantsSection.getInt(enchantKey));
                        }
                    }

                    HoeConfig hoeObject = new HoeConfig(material, name, lore, enchants);

                    // Mensagem de depuração
                    Bukkit.getConsoleSender().sendMessage("§a(minebr-armazem) Carregado hoe: " + hoeKey);

                    main.getHoesType().put(hoeKey, hoeObject);
                }
                Bukkit.getConsoleSender().sendMessage("§a(minebr-armazem) §fforam carregados §a" + main.getHoesType().size() + " §fhoes em hoes.yml");
            } else {
                Bukkit.getConsoleSender().sendMessage("§c(minebr-armazem) Não foi possível encontrar a seção 'hoes' no arquivo de configuração.");
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§cWarn! há um erro em sua hoes.yml");
            e.printStackTrace(); // Melhorar a exibição de erros
        }
    }
}
