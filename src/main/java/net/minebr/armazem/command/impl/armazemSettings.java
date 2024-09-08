package net.minebr.armazem.command.impl;

import net.minebr.armazem.ArmazemMain;
import net.minebr.armazem.command.utils.ISubCommand;
import net.minebr.armazem.inventories.ArmazemInventory;
import net.minebr.armazem.settings.menus.ArmazemMenuConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class armazemSettings implements ISubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("armaze.use")) {
            sender.sendMessage("§cVocê não tem permissão para usar este comando.");
            return;
        }

        if (args.length == 0) {
            if (sender instanceof Player) {
                sender.sendMessage("§aVocê abriu o seu armazem");

                ArmazemMenuConfig menuConfig = ArmazemMain.getPlugin().getArmazemMenuConfig().get("upgrade");
                ArmazemInventory invNew = new ArmazemInventory(ArmazemMain.getPlugin());
                invNew.openInventory(((Player) sender).getPlayer());
            } else {
                sender.sendMessage("§cApenas jogadores podem usar este comando sem argumentos.");
            }
            return;
        }

        if (args.length == 1) {
            String playerName = args[0];
            Player targetPlayer = Bukkit.getPlayerExact(playerName);
            if (targetPlayer == null || !targetPlayer.isOnline()) {
                sender.sendMessage("§cO jogador " + playerName + " não está online.");
            } else {
                sender.sendMessage("§aVocê abriu o armazem de " + playerName);

                ArmazemMenuConfig menuConfig = ArmazemMain.getPlugin().getArmazemMenuConfig().get("upgrade");
                ArmazemInventory invNew = new ArmazemInventory(ArmazemMain.getPlugin());
                invNew.openInventory(targetPlayer);
            }
            return;
        }

        sender.sendMessage("§cUso incorreto do comando. Use /armazem ou /armazem (player)");
    }
}
