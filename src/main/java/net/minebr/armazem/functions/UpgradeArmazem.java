package net.minebr.armazem.functions;

import net.minebr.armazem.ArmazemMain;
import net.minebr.armazem.api.VaultAPI;
import net.minebr.armazem.model.PlayerManager;
import net.minebr.armazem.utils.NumberFormat;
import net.minebr.armazem.utils.SendTitle;
import org.bukkit.entity.Player;

import java.util.Objects;

public class UpgradeArmazem {

    public static void upgradeArmazem(Player p, PlayerManager playerManager, double costUpgradeCoins, double costUpgradeTokens, double novoValor) {

        if (VaultAPI.econ.getBalance(p.getPlayer()) >= costUpgradeCoins && Objects.requireNonNull(ArmazemMain.getRankupAPI()).getFragmentos(p) >= costUpgradeTokens) {
            playerManager.setArmazemLevel(playerManager.getArmazemLevel() + 1);
            VaultAPI.econ.withdrawPlayer(p, costUpgradeCoins);
            ArmazemMain.getRankupAPI().getPlayer(p.getName()).setFragmentos(ArmazemMain.getRankupAPI().getPlayer(p).getFragmentos() - costUpgradeTokens);
            p.sendMessage("§aSucesso! você evoluiu a capacidade do armazém para §f" + NumberFormat.decimalFormat(novoValor));
            SendTitle.enviarTitulo(p, "§6☆ §6§lARMAZÉM §6☆", "§fEvolução realizada, novo nível §8[" + NumberFormat.numberFormat(playerManager.getArmazemLevel()) + "]", 0, 0, 0);
        } else {
            p.sendMessage("");
            p.sendMessage("§cOps! parece que você não tem saldo o suficiente para dar upgrade!");
            p.sendMessage("§cRequisitos:");
            p.sendMessage("    §8↳ §cCoin(s): " + NumberFormat.decimalFormat(VaultAPI.econ.getBalance(p.getName())) + "/" + NumberFormat.decimalFormat(costUpgradeCoins));
            p.sendMessage("    §8↳ §cTokens(s): " + NumberFormat.decimalFormat(Objects.requireNonNull(ArmazemMain.getRankupAPI()).getFragmentos(p)) + "/" + NumberFormat.decimalFormat(costUpgradeTokens));
            p.sendMessage("");
        }
    }
}
