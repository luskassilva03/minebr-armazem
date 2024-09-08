package net.minebr.armazem.loader.menus;

import net.minebr.armazem.ArmazemMain;
import net.minebr.armazem.settings.menus.PlantationMenuConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.stream.Collectors;

public class PlantationsMenu {
    private final ArmazemMain main = ArmazemMain.getPlugin();

    public void load() {
        try {
            // Obtém a seção de plantações do arquivo de configuração
            ConfigurationSection plantationsSection = main.getPlantationMenu().getConfigurationSection("plantations");

            if (plantationsSection != null) {
                Bukkit.getConsoleSender().sendMessage("§a(minebr-armazem) Carregando inventário...");

                // Itera sobre as chaves das plantações
                for (String plantationKey : plantationsSection.getKeys(false)) {
                    ConfigurationSection plantationSection = plantationsSection.getConfigurationSection(plantationKey);

                    // Obtém os detalhes da plantação
                    int slot = plantationSection.getInt("slot");
                    String display = plantationSection.getString("display").replace("&", "§");
                    List<String> lore = plantationSection.getStringList("lore").stream()
                            .map(line -> line.replace("&", "§"))
                            .collect(Collectors.toList());

                    // Cria o objeto PlantationConfig
                    PlantationMenuConfig plantationMenu = new PlantationMenuConfig(slot, display, lore);

                    // Mensagem de depuração
                    Bukkit.getConsoleSender().sendMessage("§a(minebr-armazem) Inventário carregado: " + plantationKey);

                    // Armazena a configuração da plantação
                    main.getPlantationMenuConfig().put(plantationKey, plantationMenu);
                }

                Bukkit.getConsoleSender().sendMessage("§a(minebr-armazem) §fforam carregados §a" + main.getPlantationsType().size() + " §finventários em plantations.yml");
            } else {
                Bukkit.getConsoleSender().sendMessage("§c(minebr-armazem) Não foi possível encontrar a seção 'plantations' no arquivo de configuração.");
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§cWarn! Há um erro em sua plantations.yml");
            e.printStackTrace(); // Melhorar a exibição de erros
        }
    }
}
