package net.minebr.armazem.loader;

import net.minebr.armazem.ArmazemMain;
import net.minebr.armazem.settings.BoosterConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class Boosters {
    private final ArmazemMain main = ArmazemMain.getPlugin();

    public void load() {
        try {
            ConfigurationSection boostersSection = main.getBoosters().getConfigurationSection("boosters");
            if (boostersSection != null) {
                Bukkit.getConsoleSender().sendMessage("§a(minebr-armazem) Carregando boosters...");
                for (String boosterKey : boostersSection.getKeys(false)) {
                    ConfigurationSection boosterSection = boostersSection.getConfigurationSection(boosterKey);

                    boolean useSkull = boosterSection.getBoolean("useSkull");
                    String skullUrl = boosterSection.getString("skullUrl");
                    String materialString = boosterSection.getString("Material");
                    Material material = Material.matchMaterial(materialString.split(":")[0]);

                    String name = boosterSection.getString("name").replace("&", "§");
                    List<String> lore = boosterSection.getStringList("lore");

                    ConfigurationSection boosterConfigSection = boosterSection.getConfigurationSection("booster");
                    int time = boosterConfigSection.getInt("time");
                    double multiply = boosterConfigSection.getDouble("multiply");

                    BoosterConfig boosterObject = new BoosterConfig(useSkull, skullUrl, material, name, lore, time, multiply);

                    // Mensagem de depuração
                    Bukkit.getConsoleSender().sendMessage("§a(minebr-armazem) Carregado booster: " + boosterKey);

                    main.getBoostersType().put(boosterKey, boosterObject);
                }
                Bukkit.getConsoleSender().sendMessage("§a(minebr-armazem) §fforam carregados §a" + main.getBoostersType().size() + " §fboosters em boosters.yml");
            } else {
                Bukkit.getConsoleSender().sendMessage("§c(minebr-armazem) Não foi possível encontrar a seção 'boosters' no arquivo de configuração.");
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§cWarn! há um erro em sua boosters.yml");
            e.printStackTrace(); // Melhorar a exibição de erros
        }
    }
}
