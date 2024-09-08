package net.minebr.armazem.loader.menus;

import net.minebr.armazem.ArmazemMain;
import net.minebr.armazem.settings.menus.ArmazemMenuConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Armazem {
    private final ArmazemMain main = ArmazemMain.getPlugin();

    public void load() {
        try {
            ConfigurationSection armazemSection = main.getArmazemMenu().getConfigurationSection("armazem");
            if (armazemSection != null) {
                Bukkit.getConsoleSender().sendMessage("§a(minebr-armazem) Carregando configuração do armazém...");

                for (String key : armazemSection.getKeys(false)) {
                    ConfigurationSection itemSection = armazemSection.getConfigurationSection(key);
                    if (itemSection != null) {
                        int slot = itemSection.getInt("slot");
                        boolean useSkull = itemSection.getBoolean("useSkull");
                        String skullUrl = itemSection.getString("skullUrl", "");
                        String materialString = itemSection.getString("material");
                        Material material = Material.matchMaterial(materialString.split(":")[0]);
                        String display = itemSection.getString("display").replace("&", "§");
                        List<String> lore = itemSection.getStringList("lore").stream()
                                .map(line -> line.replace("&", "§"))
                                .collect(Collectors.toList());

                        ArmazemMenuConfig armazemMenuConfig = new ArmazemMenuConfig(slot, useSkull, skullUrl, material, display, lore);
                        main.getArmazemMenuConfig().put(key, armazemMenuConfig);
                    }
                }

                Bukkit.getConsoleSender().sendMessage("§a(minebr-armazem) §fConfiguração do armazém carregada em armazem.yml");
            } else {
                Bukkit.getConsoleSender().sendMessage("§c(minebr-armazem) Não foi possível encontrar a seção 'armazem' no arquivo de configuração.");
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§cWarn! Há um erro em sua armazem.yml");
            e.printStackTrace(); // Melhorar a exibição de erros
        }
    }
}
