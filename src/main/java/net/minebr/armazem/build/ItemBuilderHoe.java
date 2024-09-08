package net.minebr.armazem.build;

import de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.val;
import net.minebr.armazem.ArmazemMain;
import net.minebr.armazem.api.builder.ItemBuilder;
import net.minebr.armazem.settings.HoeConfig;
import net.minebr.armazem.utils.NumberFormat;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.stream.Collectors;

public class ItemBuilderHoe {

    public static void hoePlayer(Player p, String enxada) {
        HoeConfig hoeConfig = ArmazemMain.getPlugin().getHoesType().get(enxada);

        if (hoeConfig == null) {
            p.sendMessage("§cEnxada não encontrada: " + enxada);
            return;
        }

        ItemStack item = (new ItemBuilder()).setType(hoeConfig.getMaterial()).setName(hoeConfig.getName())
                .setLore(hoeConfig.getLore().stream().map(line -> line.replace("&", "§")
                        .replace("{efficiency}", NumberFormat.numberFormat(hoeConfig.getEnchants().get("efficiency")))
                        .replace("{fortune}", NumberFormat.decimalFormat(hoeConfig.getEnchants().get("fortune")))
                        .replace("{durability}", "∞")
                        .replace("{laser}", NumberFormat.decimalFormat(hoeConfig.getEnchants().get("laser")))
                        .replace("{explosive}", NumberFormat.decimalFormat(hoeConfig.getEnchants().get("explosive")))
                ).collect(Collectors.toList())).addEnchant(Enchantment.DIG_SPEED, hoeConfig.getEnchants().get("efficiency"))
                .addEnchant(Enchantment.LOOT_BONUS_BLOCKS, hoeConfig.getEnchants().get("fortune")).addGlow()
                .build();


        ItemMeta meta = item.getItemMeta();
        meta.spigot().setUnbreakable(true);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("ENXADA", enxada);
        nbtItem.setInteger("efficiency", hoeConfig.getEnchants().get("efficiency"));
        nbtItem.setInteger("fortune", hoeConfig.getEnchants().get("fortune"));
        nbtItem.setInteger("laser", hoeConfig.getEnchants().get("laser"));
        nbtItem.setInteger("explosive", hoeConfig.getEnchants().get("explosive"));


        p.getInventory().addItem(new ItemStack(nbtItem.getItem()));
    }
}
