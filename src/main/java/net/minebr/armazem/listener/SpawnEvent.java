package net.minebr.armazem.listener;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import net.minebr.armazem.ArmazemMain;
import net.minebr.armazem.functions.AddDropsToArmazem;
import net.minebr.armazem.registery.ListenerRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnEvent extends ListenerRegistry {

    private final AddDropsToArmazem addDropsToArmazem;

    public SpawnEvent(ArmazemMain main) {
        super(main);
        this.addDropsToArmazem = new AddDropsToArmazem();
    }

    @EventHandler
    public void grow(final ItemSpawnEvent e) {

        Item item = e.getEntity();
        if (item == null || !item.getItemStack().getType().equals(Material.CACTUS) || !item.getLocation().getWorld().getName().equalsIgnoreCase("Terrenos")) {
            return;
        }

        Plot plot = new PlotAPI().getPlot(item.getLocation());
        if (plot == null || !plot.hasOwner() || !main.getPlantationsType().containsKey(item.getItemStack().getType().toString())) {
            return;
        }

        // Verificar se o item é um cacto que nasceu automaticamente
        if (item.getItemStack().getType().equals(Material.CACTUS)) {
            item.remove();

            Player player = Bukkit.getPlayer(plot.getOwners().stream().findFirst().orElse(null));
            if (player == null) {
                return;
            }

            new BukkitRunnable() {
                public void run() {
                    try {
                        addDropsToArmazem.addDropsToArmazem(main, player, new ItemStack(Material.CACTUS));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }.runTaskAsynchronously(main);
        }
    }
}
