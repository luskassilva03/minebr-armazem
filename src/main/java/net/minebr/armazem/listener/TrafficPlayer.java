package net.minebr.armazem.listener;

import net.minebr.armazem.ArmazemMain;
import net.minebr.armazem.database.datamanager.DataManager;
import net.minebr.armazem.database.util.Utils;
import net.minebr.armazem.model.PlayerManager;
import net.minebr.armazem.registery.CommandRegistry;
import net.minebr.armazem.registery.ListenerRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TrafficPlayer extends ListenerRegistry {

    public TrafficPlayer(ArmazemMain main) {
        super(main);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!main.getMainDataManager().USERS.exists(p.getName())) {
            // Criar um novo PlayerManager
            PlayerManager playerManager = new PlayerManager(p.getName());
            // Adicione o PlayerManager ao DataManager de forma assíncrona
            Utils.async(() -> main.getMainDataManager().USERS.cache(playerManager));
        }
    }

}
