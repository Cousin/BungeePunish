package xyz.betanyan.carribeanbans.builders;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/*
    InventoryBuilder Util I made that I use in a lot of my projects.
 */
public class InventoryBuilder {

    private Inventory inventory;

    private int slots;

    public InventoryBuilder(String title) {
        if (title.length() >= 32) title = title.substring(0, title.length() - 32);
        inventory = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', title));
    }

    public InventoryBuilder(String title, int slots) {
        if (title.length() >= 32) title = title.substring(0, title.length() - 32);
        inventory = Bukkit.createInventory(null, getSlotsDivisibleByNine(slots), ChatColor.translateAlternateColorCodes('&', title));
    }

    public InventoryBuilder(String title, int slots, InventoryHolder holder) {
        if (title.length() >= 32) title = title.substring(0, title.length() - 32);
        inventory = Bukkit.createInventory(holder, getSlotsDivisibleByNine(slots), ChatColor.translateAlternateColorCodes('&', title));
    }

    public InventoryBuilder(String title, InventoryType type) {
        if (title.length() >= 32) title = title.substring(0, title.length() - 32);
        inventory = Bukkit.createInventory(null, type, ChatColor.translateAlternateColorCodes('&', (title)));
    }

    public InventoryBuilder(String title, InventoryType type, InventoryHolder holder) {
        if (title.length() >= 32) title = title.substring(0, title.length() - 32);
        inventory = Bukkit.createInventory(holder, type, ChatColor.translateAlternateColorCodes('&', (title)));
    }

    public InventoryBuilder(int size) {
        inventory = Bukkit.createInventory(null, size);
    }

    public InventoryBuilder addItem(ItemStack... items) {
        inventory.addItem(items);
        return this;
    }

    public InventoryBuilder addItem(Material... materials) {
        Arrays.stream(materials).forEach(material ->
                inventory.addItem(new ItemStack(material))
        );
        return this;
    }

    public InventoryBuilder removeItem(ItemStack... items) {
        inventory.removeItem(items);
        return this;
    }

    public InventoryBuilder removeItem(int slot) {
        inventory.clear(slot);
        return this;
    }

    public InventoryBuilder removeAll(ItemStack item) {
        inventory.remove(item);
        return this;
    }

    public InventoryBuilder removeAll(Material material) {
        inventory.remove(material);
        return this;
    }

    public InventoryBuilder setItem(int slot, ItemStack item) {
        inventory.setItem(slot, item);
        return this;
    }

    public InventoryBuilder setItem(int slot, Material material) {
        inventory.setItem(slot, new ItemStack(material));
        return this;
    }

    public InventoryBuilder setItem(ItemStack item, int... slots) {
        for (int slot : slots) {
            inventory.setItem(slot, item);
        }
        return this;
    }

    public InventoryBuilder clear() {
        inventory.clear();
        return this;
    }

    public InventoryBuilder setContents(ItemStack[] contents) {
        inventory.setContents(contents);
        return this;
    }

    public boolean contains(ItemStack item) {
        return inventory.contains(item);
    }

    public boolean contains(Material item) {
        return inventory.contains(item);
    }

    public Inventory build() {
        return inventory;
    }

    public int getSlots() {
        return slots;
    }

    private int getSlotsDivisibleByNine(int slots) {
        if (slots <= 9) {
            this.slots = 9;
            return 9;
        } else if (slots <= 18) {
            this.slots = 18;
            return 18;
        } else if (slots <= 27) {
            this.slots = 27;
            return 27;
        } else if (slots <= 36) {
            this.slots = 36;
            return 36;
        } else if (slots <= 45) {
            this.slots = 45;
            return 45;
        } else if (slots <= 54) {
            this.slots = 54;
            return 54;
        } else {
            this.slots = 54;
            return 54;
        }
    }


}
