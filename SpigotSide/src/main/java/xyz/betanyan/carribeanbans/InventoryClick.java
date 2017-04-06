package xyz.betanyan.carribeanbans;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.betanyan.carribeanbans.builders.AnvilGUI;
import xyz.betanyan.carribeanbans.builders.InventoryBuilder;
import xyz.betanyan.carribeanbans.builders.ItemBuilder;

import java.util.concurrent.TimeUnit;

public class InventoryClick implements Listener {

    private CarribeanBans plugin;

    public InventoryClick(CarribeanBans plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        Inventory inventory = event.getInventory();
        String title = inventory.getTitle();

        if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {

            ItemStack currentItem = event.getCurrentItem();

            if (title.startsWith(ChatColor.YELLOW + "Punish: " + ChatColor.GREEN)) {

                event.setCancelled(true);

                String displayName = currentItem.getItemMeta().getDisplayName();
                if (displayName.contains("Ban")) {

                    title = title.replace("Punish", "Ban");

                } else if (displayName.contains("Mute")) {

                    title = title.replace("Punish", "Mute");

                } else if (displayName.contains("Warn")) {

                    String name = inventory.getTitle().split(ChatColor.GREEN.toString())[1];

                    Inventory silentChoose = Bukkit.createInventory(null, 9, ChatColor.RED + "Warning " + name);
                    for (int i = 0; i < 8; i++) {
                        silentChoose.setItem(i, new ItemBuilder(
                                Material.STAINED_GLASS_PANE, (short) 10).name("&c").addLore("&cWarning " + name).build());
                    }
                    silentChoose.setItem(2, new ItemBuilder(Material.REDSTONE).name("&cSilent Punishment").build());
                    silentChoose.setItem(6, new ItemBuilder(Material.INK_SACK, (short) 10).name("&aBroadcast Punishment").build());

                    player.openInventory(silentChoose);
                    return;

                } else if (displayName.contains("Kick")) {

                    String name = inventory.getTitle().split(ChatColor.GREEN.toString())[1];

                    Inventory silentChoose = Bukkit.createInventory(null, 9, ChatColor.RED + "Kicking " + name);
                    for (int i = 0; i < 8; i++) {
                        silentChoose.setItem(i, new ItemBuilder(
                                Material.STAINED_GLASS_PANE, (short) 10).name("&c").addLore("&cKicking " + name).build());
                    }
                    silentChoose.setItem(2, new ItemBuilder(Material.REDSTONE).name("&cSilent Punishment").build());
                    silentChoose.setItem(6, new ItemBuilder(Material.INK_SACK, (short) 10).name("&aBroadcast Punishment").build());

                    player.openInventory(silentChoose);
                    return;

                }

                player.openInventory(new InventoryBuilder(title, 27)
                        .setItem(0, new ItemBuilder(Material.INK_SACK, (short) 10).name("&aBroadcast Punishment").build())
                        .setItem(10, new ItemBuilder(Material.BOOK).name("&a30 minutes").build())
                        .setItem(11, new ItemBuilder(Material.BOOK).name("&a6 hours").build())
                        .setItem(12, new ItemBuilder(Material.BOOK).name("&a1 day").build())
                        .setItem(13, new ItemBuilder(Material.BOOK).name("&a7 days").build())
                        .setItem(14, new ItemBuilder(Material.BOOK).name("&a1 month").build())
                        .setItem(15, new ItemBuilder(Material.BOOK).name("&a3 months").build())
                        .setItem(16, new ItemBuilder(Material.BOOK).name("&a6 months").build())
                        .setItem(4, new ItemBuilder(Material.ENCHANTED_BOOK).name("Permanent").addLore("&eExpires once pardoned.").build())
                        .setItem(22, new ItemBuilder(Material.ENCHANTED_BOOK).name("Pardon").addLore("&eRemove Punishment").build())
                        .build());

            } else if (title.startsWith(ChatColor.RED + "Kicking ")) {

                event.setCancelled(true);

                if (event.getSlot() == 2 || event.getSlot() == 6) {

                    new AnvilGUI(plugin, player, (menu, text) -> {

                        String type = "KICK";
                        String punishName = inventory.getTitle().split("Kicking ")[1];

                        if (text.equals("Reason")) {
                            text = "Breaking the rules";
                        }

                        boolean silent = event.getSlot() == 2;

                        plugin.sendPunishment(player, String.format("%s=|=%d=|=%s=|=%s=|=%s=|=%b",
                                type.toUpperCase(), 0, punishName, text, player.getName(), silent));

                        player.sendMessage(ChatColor.GREEN + "Successfully kicked " + ChatColor.YELLOW + punishName);

                        return true;

                    }).setInputName("Reason").open();

                }

            } else if (title.startsWith(ChatColor.RED + "Warning")) {

                event.setCancelled(true);

                if (event.getSlot() == 2 || event.getSlot() == 6) {

                    new AnvilGUI(plugin, player, (menu, text) -> {

                        String type = "WARN";
                        String punishName = inventory.getTitle().split("Warning ")[1];

                        if (text.equals("Reason")) {
                            text = "Breaking the rules";
                        }

                        boolean silent = event.getSlot() == 2;

                        plugin.sendPunishment(player, String.format("%s=|=%d=|=%s=|=%s=|=%s=|=%b",
                                type.toUpperCase(), System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7), punishName, text, player.getName(), silent));

                        player.sendMessage(ChatColor.GREEN + "Successfully warned " + ChatColor.YELLOW + punishName);

                        return true;

                    }).setInputName("Reason").open();

                }

            } else if (title.startsWith(ChatColor.YELLOW + "Ban")
                    || title.startsWith(ChatColor.YELLOW + "Mute")) {

                if (event.getSlot() == 4 || (event.getSlot() >= 10 && event.getSlot() <= 16) || event.getSlot() == 22) {

                    event.setCancelled(true);

                    new AnvilGUI(plugin, player, (menu, text) -> {

                        long time = System.currentTimeMillis();
                        switch (event.getSlot()) {
                            case 10:
                                time += TimeUnit.MINUTES.toMillis(30);
                                break;
                            case 11:
                                time += TimeUnit.HOURS.toMillis(6);
                                break;
                            case 12:
                                time += TimeUnit.DAYS.toMillis(1);
                                break;
                            case 13:
                                time += TimeUnit.DAYS.toMillis(7);
                                break;
                            case 14:
                                time += TimeUnit.DAYS.toMillis(30);
                                break;
                            case 15:
                                time += TimeUnit.DAYS.toMillis(90);
                                break;
                            case 16:
                                time += TimeUnit.MINUTES.toMillis(180);
                                break;
                            case 17:
                                time += TimeUnit.MINUTES.toMillis(0);
                                break;
                            case 4:
                                time = 0;
                                break;
                            case 22:
                                time = -1;
                                break;
                        }

                        String type = ChatColor.stripColor(inventory.getTitle().split(": " + ChatColor.GREEN)[0]);
                        String punishName = inventory.getTitle().split(ChatColor.GREEN.toString())[1];

                        if (text.equals("Reason")) {
                            text = "Breaking the rules";
                        }

                        boolean silent = true;
                        if (inventory.getItem(0).getType() == Material.INK_SACK) {
                            silent = false;
                        }

                        plugin.sendPunishment(player, String.format("%s=|=%d=|=%s=|=%s=|=%s=|=%b",
                                type.toUpperCase(), time, punishName, text, player.getName(), silent));

                        player.sendMessage(ChatColor.GREEN + "Successfully punished " + ChatColor.YELLOW + punishName);

                        return true;

                    }).setInputName("Reason").open();

                } else if (event.getSlot() == 0) {

                    event.setCancelled(true);

                    Inventory inv = inventory;
                    if (currentItem.getType() == Material.INK_SACK) {
                        inv.setItem(0, new ItemBuilder(Material.REDSTONE).name("&cSilent Punishment").build());
                    } else {
                        inv.setItem(0, new ItemBuilder(Material.INK_SACK, (short) 10).name("&aBroadcast Punishment").build());
                    }

                }

            }

        }

    }

}
