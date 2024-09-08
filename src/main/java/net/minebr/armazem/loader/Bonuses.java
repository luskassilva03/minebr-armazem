package net.minebr.armazem.loader;

import net.minebr.armazem.ArmazemMain;
import net.minebr.armazem.settings.BonusesConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class Bonuses {
    private final ArmazemMain main = ArmazemMain.getPlugin();

    public void load() {
        try {
            ConfigurationSection bonusesSection = main.getBonuses().getConfigurationSection("bonuses");
            if (bonusesSection != null) {
                Bukkit.getConsoleSender().sendMessage("§a(minebr-armazem) Carregando bônus...");
                for (String bonusKey : bonusesSection.getKeys(false)) {
                    ConfigurationSection bonusSection = bonusesSection.getConfigurationSection(bonusKey);

                    String display = bonusSection.getString("display").replace("&", "§");
                    String permission = bonusSection.getString("permission");
                    double bonus = bonusSection.getDouble("bonus");

                    BonusesConfig bonusObject = new BonusesConfig(display, permission, bonus);

                    // Mensagem de depuração
                    Bukkit.getConsoleSender().sendMessage("§a(minebr-armazem) Carregado bônus: " + bonusKey);

                    main.getBonusesType().put(bonusKey, bonusObject);
                }
                Bukkit.getConsoleSender().sendMessage("§a(minebr-armazem) §fforam carregados §a" + main.getBonusesType().size() + " §fbônus em bonuses.yml");
            } else {
                Bukkit.getConsoleSender().sendMessage("§c(minebr-armazem) Não foi possível encontrar a seção 'bonuses' no arquivo de configuração.");
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§cWarn! há um erro em sua bonus.yml");
            e.printStackTrace(); // Melhorar a exibição de erros
        }
    }
}
