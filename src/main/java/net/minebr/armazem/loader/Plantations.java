package net.minebr.armazem.loader;

import net.minebr.armazem.ArmazemMain;
import net.minebr.armazem.settings.PlantationConfig;
import net.minebr.armazem.settings.PriceConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class Plantations {

    private final ArmazemMain main = ArmazemMain.getPlugin();

    public void load() {
        try {
            ConfigurationSection plantationsSection = main.getPlantations().getConfigurationSection("plantations");
            if (plantationsSection != null) {
                Bukkit.getConsoleSender().sendMessage("§a(minebr-armazem) Carregando plantações...");

                for (String plantationKey : plantationsSection.getKeys(false)) {
                    ConfigurationSection plantationSection = plantationsSection.getConfigurationSection(plantationKey);

                    // Carrega os preços
                    Map<String, PriceConfig> prices = new HashMap<>();
                    ConfigurationSection pricesSection = plantationSection.getConfigurationSection("prices");
                    if (pricesSection != null) {
                        Bukkit.getConsoleSender().sendMessage("§a(minebr-armazem) Carregando preços para plantação: " + plantationKey);

                        for (String priceKey : pricesSection.getKeys(false)) {
                            ConfigurationSection priceSection = pricesSection.getConfigurationSection(priceKey);
                            String provider = priceSection.getString("provider");
                            double price = priceSection.getDouble("price");

                            PriceConfig priceConfig = new PriceConfig();
                            priceConfig.setProvider(provider);
                            priceConfig.setPrice(price);

                            prices.put(priceKey, priceConfig);
                        }
                    } else {
                        Bukkit.getConsoleSender().sendMessage("§c(minebr-armazem) Não foram encontrados preços para a plantação: " + plantationKey);
                    }

                    // Cria e armazena a configuração da plantação
                    PlantationConfig plantationConfig = new PlantationConfig();
                    plantationConfig.setPrices(prices);

                    main.getPlantationsType().put(plantationKey, plantationConfig);

                    // Mensagem de depuração para cada plantação carregada
                    Bukkit.getConsoleSender().sendMessage("§a(minebr-armazem) Plantação carregada: " + plantationKey);
                }

                // Mensagem final de carregamento
                Bukkit.getConsoleSender().sendMessage("§a(minebr-armazem) §fforam carregadas §a" + main.getPlantationsType().size() + " §fplantação(ões) em plantations.yml");
            } else {
                Bukkit.getConsoleSender().sendMessage("§c(minebr-armazem) Não foi possível encontrar a seção 'plantations' no arquivo de configuração.");
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§cWarn! Há um erro em sua plantations.yml");
            e.printStackTrace(); // Melhorar a exibição de erros
        }
    }
}
