package net.minebr.armazem.loader;

import net.minebr.armazem.ArmazemMain;
import net.minebr.armazem.settings.UpgradeConfig;
import org.bukkit.Bukkit;

public class Upgrade {

    ArmazemMain main = ArmazemMain.getPlugin();

    public void load() {
        try {
            double perLevel = main.configuration.getDouble("Upgrade.perLevel");

            double priceCoins = main.configuration.getDouble("Upgrade.coins.price");
            double multiplyCoins = main.configuration.getDouble("Upgrade.coins.multiply");

            double priceTokens = main.configuration.getDouble("Upgrade.tokens.price");
            double multiplyTokens = main.configuration.getDouble("Upgrade.tokens.multiply");
            UpgradeConfig upgradeValue = new UpgradeConfig(perLevel, priceCoins, multiplyCoins, priceTokens, multiplyTokens);
            main.getUpgradeConfig().put("Upgrade", upgradeValue);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§cWarn! há um erro em sua config.yml");
        }
    }

}
