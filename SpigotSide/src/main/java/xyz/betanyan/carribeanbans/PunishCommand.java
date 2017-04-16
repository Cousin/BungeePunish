package xyz.betanyan.carribeanbans;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.betanyan.carribeanbans.builders.InventoryBuilder;
import xyz.betanyan.carribeanbans.builders.ItemBuilder;

public class PunishCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;
            
            if (player.hasPermission("punish.use")) {

                if (args.length == 0) {

                    player.sendMessage(ChatColor.RED + "Correct syntax: /punish <name>");

                } else {

                    String toPunish = args[0];

                    if (toPunish.length() <= 16) {

                        InventoryBuilder builder = new InventoryBuilder("&ePunish: &a" + toPunish, 27);
                        builder.setItem(10, new ItemBuilder(Material.DIAMOND_SWORD).name("&aBan").build());
                        builder.setItem(12, new ItemBuilder(Material.IRON_SWORD).name("&aMute").build());
                        builder.setItem(14, new ItemBuilder(Material.GOLDEN_APPLE).name("&aWarn").build());
                        builder.setItem(16, new ItemBuilder(Material.IRON_BOOTS).name("&aKick").build());

                        player.openInventory(builder.build());

                    } else {
                        sender.sendMessage(ChatColor.RED + "The username " + args[0] + " is too long!");
                    }

                }
                
            } else {
                sender.sendMessage(ChatColor.RED + "No permission.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Only players can punish.");
        }

        return false;
    }
}
