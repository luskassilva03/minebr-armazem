package net.minebr.armazem.registery;

import net.minebr.armazem.ArmazemMain;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class ListenerRegistry implements Listener {

    protected ArmazemMain main;

    public ListenerRegistry(ArmazemMain main) {
        this.main = main;

        Bukkit.getPluginManager().registerEvents(this, main);
    }

}
