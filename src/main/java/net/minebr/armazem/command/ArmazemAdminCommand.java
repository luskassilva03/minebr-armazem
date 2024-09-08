package net.minebr.armazem.command;

import net.minebr.armazem.ArmazemMain;
import net.minebr.armazem.command.impl.giveBooster;
import net.minebr.armazem.command.impl.giveHoe;
import net.minebr.armazem.registery.CommandRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ArmazemAdminCommand extends CommandRegistry {

    private giveBooster giveBooster;
    private giveHoe giveHoe;

    public ArmazemAdminCommand(ArmazemMain main) {
        super(main);

        this.giveBooster = new giveBooster();
        this.giveHoe = new giveHoe();
        main.getCommand("armazemadmin").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (s instanceof Player || s instanceof ConsoleCommandSender) {
                if (!s.hasPermission("armazemadmin.admin")) {
                    s.sendMessage("§cVocê precisa ser Gerente ou superior para poder usar este comando!");
                    return false;
                }
                s.sendMessage("");
                s.sendMessage("§a/armazemadmin givebooster (player) (booster)");
                s.sendMessage("§a/armazemadmin givehoe (player) (hoe)");
                s.sendMessage("");
                return false;
            }
        }
        if (args.length >= 1) {
            switch (args[0].toLowerCase()) {
                case "givebooster":
                case "darbooster":
                    giveBooster.execute(s, args);
                    break;
                case "givehoe":
                case "giveenxada":
                case "darhoe":
                case "darenxada":
                    giveHoe.execute(s, args);
                    break;
                default:
                case "help":
                case "ajuda":
                    s.sendMessage("");
                    s.sendMessage("§a/armazemadmin givebooster (player) (booster)");
                    s.sendMessage("§a/armazemadmin givehoe (player) (hoe)");
                    s.sendMessage("");
                    break;
            }
        }
        return false;
    }
}
