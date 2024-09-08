package net.minebr.armazem.functions;

import net.minebr.armazem.ArmazemMain;
import net.minebr.armazem.loader.menus.Armazem;
import net.minebr.armazem.settings.BonusesConfig;
import org.bukkit.entity.Player;

import java.util.Map;

public class BonusCalculate {

    public static double getMaxBonus(Player player, Map<String, BonusesConfig> bonusesConfigMap) {
        return bonusesConfigMap.values().stream()
                .filter(bonusConfig -> player.hasPermission(bonusConfig.getPermission()))
                .mapToDouble(BonusesConfig::getBonus)
                .max()
                .orElse(0.0);
    }

    public static double getMaxBonusMultiplier(Player p) {
        double maxBonus = 0.0;
        for (BonusesConfig permissionValues : ArmazemMain.getPlugin().getBonusesType().values()) {
            if (p.hasPermission(permissionValues.getPermission())) {
                maxBonus = Math.max(maxBonus, permissionValues.getBonus());
            }
        }
        return maxBonus;
    }


}
