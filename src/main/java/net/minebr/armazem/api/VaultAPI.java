package net.minebr.armazem.api;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public final class VaultAPI {
    public static Economy econ = null;

    public static boolean hook() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null)
            return false;
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;
        econ = (Economy)rsp.getProvider();
        return (econ != null);
    }
}

