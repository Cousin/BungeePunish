package xyz.betanyan.carribeanbans.builders;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
    ItemBuilder Util I made that I use in a lot of my projects.
 */
public class ItemBuilder {

    private ItemStack item;

    public ItemBuilder(Material m) {

        item = new ItemStack(m);

    }

    public ItemBuilder(Material m, short data) {

        item = new ItemStack(m, 1, data);

    }

    public ItemBuilder(Material m, int amount, short data) {

        item = new ItemStack(m, amount, data);

    }

    public ItemBuilder(ItemStack m) {

        item = new ItemStack(m);

    }

    public ItemBuilder(Material m, int amount) {

        item = new ItemStack(m, amount);

    }

    public ItemBuilder name(String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setItemMeta(meta);

        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment) {
        item.addUnsafeEnchantment(enchantment, 1);

        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        item.addUnsafeEnchantment(enchantment, level);

        return this;
    }

    public ItemBuilder setLore(String text) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', text)));
        item.setItemMeta(meta);

        return this;
    }

    public ItemBuilder addLore(String text) {
        ItemMeta meta = item.getItemMeta();
        List<String> newLore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        newLore.add(ChatColor.translateAlternateColorCodes('&', text));
        meta.setLore(newLore);
        item.setItemMeta(meta);

        return this;
    }

    public ItemStack build() {
        return item;
    }
}