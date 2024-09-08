package net.minebr.armazem.api.builder;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SkullBuilder extends Builder<ItemStack> {
    private ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short)3);

    private SkullMeta meta = (SkullMeta)this.item.getItemMeta();

    public SkullBuilder setOwner(String owner) {
        this.meta.setOwner(owner);
        return this;
    }

    public SkullBuilder(ItemStack item) {
        if (item.getType() != Material.SKULL_ITEM)
            return;
        this.item = item;
        this.meta = (SkullMeta)item.getItemMeta();
    }

    public SkullBuilder setType(Material material) {
        this.item.setType(material);
        return this;
    }

    public SkullBuilder setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public SkullBuilder setName(String name) {
        this.meta.setDisplayName(name);
        return this;
    }

    public SkullBuilder setLore(List<String> lore) {
        this.meta.setLore(lore);
        return this;
    }

    public SkullBuilder setLore(String... lore) {
        List<String> list = Arrays.asList(lore);
        this.meta.setLore(list);
        return this;
    }

    public SkullBuilder addEnchant(Enchantment enc, int level) {
        this.meta.addEnchant(enc, level, true);
        return this;
    }

    public SkullBuilder setData(byte b) {
        this.item.setDurability((short)b);
        return this;
    }

    public SkullBuilder addGlow() {
        this.meta.addEnchant(Enchantment.DURABILITY, 1, true);
        this.meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
        return this;
    }

    public ItemStack build() {
        this.item.setItemMeta((ItemMeta)this.meta);
        return this.item;
    }

    public SkullBuilder setTexture(String texture) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", new Object[] { texture }).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        try {
            Field profileField = this.meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(this.meta, profile);
        } catch (NoSuchFieldException|IllegalArgumentException|IllegalAccessException exception) {
            exception.printStackTrace();
        }
        return this;
    }

    /*public static ItemStack setSkull(String url) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        if (url == null || url.isEmpty())
            return skull;
        SkullMeta skullMeta = (SkullMeta)skull.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", new Object[] { url }).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException|SecurityException e) {
            e.printStackTrace();
        }
        profileField.setAccessible(true);
        try {
            profileField.set(skullMeta, profile);
        } catch (IllegalArgumentException|IllegalAccessException e) {
            e.printStackTrace();
        }
        skull.setItemMeta((ItemMeta)skullMeta);
        return skull;
    }*/

    public SkullBuilder() {}
}

