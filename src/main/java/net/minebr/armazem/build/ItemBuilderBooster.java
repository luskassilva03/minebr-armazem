package net.minebr.armazem.build;

import de.tr7zw.changeme.nbtapi.NBTItem;
import net.minebr.armazem.ArmazemMain;
import net.minebr.armazem.api.builder.ItemBuilder;
import net.minebr.armazem.api.builder.SkullBuilder;
import net.minebr.armazem.settings.BoosterConfig;
import net.minebr.armazem.utils.NumberFormat;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Collectors;

public class ItemBuilderBooster {

    public static void createBoosterItem(Player player, String boosterKey) {
        BoosterConfig boosterObject = ArmazemMain.getPlugin().getBoostersType().get(boosterKey);

        if (boosterObject == null) {
            player.sendMessage("§cBooster não encontrado: " + boosterKey);
            return;
        }

        ItemBuilder itemBuilder = new ItemBuilder();
        SkullBuilder skullBuilder = new SkullBuilder();

        if (boosterObject.isUseSkull() && !boosterObject.getSkullUrl().isEmpty()) {
            skullBuilder.setTexture(boosterObject.getSkullUrl());
        } else {
            itemBuilder.setType(boosterObject.getMaterial());
        }

        ItemStack item = itemBuilder
                .setName(boosterObject.getName().replace("&", "§"))
                .setLore(boosterObject.getLore().stream()
                        .map(line -> line.replace("&", "§")
                                .replace("{timer}", NumberFormat.numberFormat(boosterObject.getTime()))
                                .replace("{multiply}", NumberFormat.decimalFormat(boosterObject.getMultiply())))
                        .collect(Collectors.toList()))
                .addGlow()
                .build();

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("BOOSTERARMAZEM", boosterKey);
        nbtItem.setInteger("time", boosterObject.getTime());
        nbtItem.setDouble("multiply", boosterObject.getMultiply());

        player.getInventory().addItem(nbtItem.getItem());
    }
}
