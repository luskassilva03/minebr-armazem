package net.minebr.armazem.listener;

import de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.val;
import net.minebr.armazem.ArmazemMain;
import net.minebr.armazem.model.PlayerManager;
import net.minebr.armazem.registery.ListenerRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Interact extends ListenerRegistry {
    public Interact(ArmazemMain main) {
        super(main);
    }

    @EventHandler
    void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack itemInHand = p.getInventory().getItemInHand();

        if (itemInHand == null || !itemInHand.hasItemMeta() || !itemInHand.getItemMeta().hasDisplayName())
            return;

        NBTItem nbtItem = new NBTItem(itemInHand);

        // Handle booster interaction
        if (nbtItem.hasTag("BOOSTERARMAZEM")) {
            e.setCancelled(true);
            String boosterKey = nbtItem.getString("BOOSTERARMAZEM");
            val booster = ArmazemMain.getPlugin().getBoostersType().get(boosterKey);

            if (booster == null) {
                p.sendMessage("§cBooster não encontrado: " + boosterKey);
                return;
            }

            PlayerManager playerManager = ArmazemMain.getPlugin().getMainDataManager().USERS.getCached(p.getName());
            if (playerManager == null) {
                p.sendMessage("§cDados do jogador não encontrados.");
                return;
            }

            // Check if the booster name is valid
            if (boosterKey == null || booster.getTime() <= 0 || booster.getMultiply() <= 0) {
                p.sendMessage("§cDados inválidos para o booster: " + boosterKey);
                return;
            }

            // Check if the player already has an active booster
            if (playerManager.getBoosters().containsKey(boosterKey)) {
                p.sendMessage("§cVocê já tem um booster ativo com a chave: " + boosterKey);
                return;
            }

            p.sendMessage("§eBooster key: " + boosterKey);
            p.sendMessage("§eBooster: " + booster.toString());
            p.sendMessage("§ePlayerObject: " + playerManager.toString());

            playerManager.addBooster(boosterKey, booster.getMultiply(), booster.getTime());

            // Remove the item from the player's hand
            p.getInventory().setItemInHand(null);
            p.sendMessage("§aVocê ativou o booster: " + boosterKey);
        }
    }
}
