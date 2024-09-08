package net.minebr.armazem.command;

import net.minebr.armazem.ArmazemMain;
import net.minebr.armazem.command.impl.armazemSettings;
import net.minebr.armazem.registery.CommandRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ArmazemCommand extends CommandRegistry {

    private armazemSettings armazemSettings;

    public ArmazemCommand(ArmazemMain main) {
        super(main);

        this.armazemSettings = new armazemSettings();
        main.getCommand("armazem").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
        if (s instanceof Player || s instanceof ConsoleCommandSender) {
            if (!s.hasPermission("armaze.use")) {
                s.sendMessage("§cVocê precisa ser Gerente ou superior para poder usar este comando!");
                return false;
            }
            armazemSettings.execute(s, args);
            return false;
        }
        return false;
    }
}
