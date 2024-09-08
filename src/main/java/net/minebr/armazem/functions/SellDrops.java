package net.minebr.armazem.functions;

import com.ystoreplugins.ypoints.api.yPointsAPI;
import net.minebr.armazem.ArmazemMain;
import net.minebr.armazem.api.VaultAPI;
import net.minebr.armazem.model.PlayerManager;
import net.minebr.armazem.utils.NumberFormat;
import net.minebr.armazem.utils.SendTitle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class SellDrops {

    public void sellDrops(Player p, ItemStack item, String drop, double amount, double costCoins, double costTokens, double costCash, double costLimit) {
        PlayerManager user = ArmazemMain.getPlugin().getMainDataManager().USERS.getCached(p.getName());
        double bonusPlayer = BonusCalculate.getMaxBonusMultiplier(p);

        sendSuccessMessages(p, drop, amount);

        if (processSale(p, user, "coin(s)", costCoins, bonusPlayer, "§2$§f")) {
            VaultAPI.econ.depositPlayer(p, costCoins);
        }
        if (processSale(p, user, "token(s)", costTokens, bonusPlayer, "§d⚙")) {
            Objects.requireNonNull(ArmazemMain.getRankupAPI()).addFragmentos(p, costTokens);
        }
        if (processSale(p, user, "cash(s)", costCash, bonusPlayer, "§6✪")) {
            yPointsAPI.set(p.getName(), costCash + yPointsAPI.getBalance(p.getName()));
        }
        if (processSale(p, user, "limite(s)", costLimit, bonusPlayer, "§b∞")) {
            // No additional action needed for costLimit
        }

        if (costCoins <= 0 && costTokens <= 0 && costCash <= 0 && costLimit <= 0) {
            p.sendMessage("  §6§l↳ §cNenhum valor de venda encontrado!");
            p.sendMessage("§eNão foram vendidos nenhum drop(s).");
            return;
        }
        user.getPlantations().put(item.getType().toString(), 0.0);
        p.sendMessage("");
    }

    private void sendSuccessMessages(Player p, String drop, double amount) {
        SendTitle.enviarTitulo(p, "§f", "§edrop(s) vendido(s) com sucesso!", 0, 0, 0);
        p.sendMessage("");
        p.sendMessage("§6§lVOCÊ FEZ UMA VENDA!");
        p.sendMessage("§7Foram vendidos §f" + NumberFormat.numberFormat(amount) + " drop(s)§7 de " + drop);
        p.sendMessage("");
    }

    private boolean processSale(Player p, PlayerManager user, String saleType, double cost, double bonusPlayer, String currencySymbol) {
        if (cost >= 0.1) {
            p.sendMessage("  §6§l↳ §f" + saleType + "§7: " + currencySymbol + NumberFormat.decimalFormat(cost) + " §a[+" + bonusPlayer + "]");
            user.getPlantations().remove(user.getPlayerId());
            return true;
        }
        return false;
    }
}
