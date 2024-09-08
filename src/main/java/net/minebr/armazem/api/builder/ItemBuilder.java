package net.minebr.armazem.api.builder;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ItemBuilder extends Builder<ItemStack> {
    private ItemStack item = new ItemStack(Material.GLASS);

    private ItemMeta meta = this.item.getItemMeta();

    public ItemBuilder(ItemStack item) {
        this.item = item;
    }

    public ItemBuilder setType(Material material) {
        this.item.setType(material);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemBuilder setName(String name) {
        this.meta.setDisplayName(name);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        this.meta.setLore(lore);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        List<String> list = Arrays.asList(lore);
        this.meta.setLore(list);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enc, Integer level) {
        this.meta.addEnchant(enc, level, true);
        return this;
    }

    public ItemBuilder setData(byte b) {
        this.item.setDurability((short)b);
        return this;
    }

    public ItemBuilder addGlow() {
        this.meta.addEnchant(Enchantment.DURABILITY, 1, true);
        this.meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
        return this;
    }

    public ItemStack build() {
        this.item.setItemMeta(this.meta);
        return this.item;
    }

    public ItemBuilder() {}
}
