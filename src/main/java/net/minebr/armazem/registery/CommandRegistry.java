package net.minebr.armazem.registery;

import net.minebr.armazem.ArmazemMain;
import org.bukkit.command.CommandExecutor;

public abstract class CommandRegistry implements CommandExecutor {

    protected ArmazemMain main;

    public CommandRegistry(ArmazemMain main) {
        this.main = main;
    }
}
