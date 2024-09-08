package net.minebr.armazem.command.impl;

import net.minebr.armazem.ArmazemMain;
import net.minebr.armazem.build.ItemBuilderHoe;
import net.minebr.armazem.command.utils.ISubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class giveHoe implements ISubCommand {
    @Override
    public void execute(CommandSender s, String[] a) {
        if (s.hasPermission("mining.admin")) {
            if (a.length > 3) {
                s.sendMessage("§cArgumentos maiores que o permitido, tente novamente!");
                return;
            }
            if (a.length < 3) {
                s.sendMessage("§cArgumentos menores que o permitido, tente novamente!");
                return;
            }

            String playerName = a[1];
            Player targetPlayer = Bukkit.getPlayerExact(playerName);
            if (targetPlayer == null || !targetPlayer.isOnline()) {
                s.sendMessage("§cO jogador " + playerName + " não está online.");
                return;
            }

            String hoeKey = a[2];
            if (ArmazemMain.getPlugin().getHoesType().containsKey(hoeKey)) {
                ItemBuilderHoe.hoePlayer(targetPlayer, hoeKey);
                s.sendMessage("§aVocê deu com sucesso uma enxada " + hoeKey + " para " + playerName);
                return;
            }

            s.sendMessage("§cEssa enxada não foi encontrado na hoes.yml");
            s.sendMessage("§cTente alguns desses: " + ArmazemMain.getPlugin().getHoesType().keySet());
        } else {
            s.sendMessage("§cVocê não tem permissão para usar este comando.");
        }
    }
}
