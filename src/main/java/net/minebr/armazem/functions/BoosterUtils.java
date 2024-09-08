package net.minebr.armazem.functions;

import net.minebr.armazem.ArmazemMain;
import net.minebr.armazem.model.PlayerManager;
import net.minebr.armazem.model.PlayerManager.BoosterInfo;
import org.bukkit.entity.Player;

public class BoosterUtils {

    public static String getBoosterInfo(Player player) {
        PlayerManager playerManager = ArmazemMain.getPlugin().getMainDataManager().USERS.getCached(player.getName());
        BoosterInfo booster = playerManager.getActiveBooster();
        String boosterType = playerManager.getActiveBoosterType();

        if (booster != null) {
            return String.format("Booster: %s, Multiply: %s, Time: %d", boosterType, booster.getMultiply(), booster.getTime());
        } else {
            return "No active booster.";
        }
    }
}
