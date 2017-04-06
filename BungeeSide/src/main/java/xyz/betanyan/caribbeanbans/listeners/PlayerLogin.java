package xyz.betanyan.caribbeanbans.listeners;

import com.google.common.base.Strings;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import xyz.betanyan.caribbeanbans.CaribbeanBans;
import xyz.betanyan.caribbeanbans.punishment.Punishment;

import java.util.UUID;

public class PlayerLogin implements Listener {

    @EventHandler
    public void onPlayerLogin(LoginEvent event) {

        UUID uuid = event.getConnection().getUniqueId();
        CaribbeanBans plugin = CaribbeanBans.getPlugin();

        Punishment punishment = plugin.getPunishmentManager()
                .getByUUID(uuid, "BAN");

        boolean denied = false;
        String expires = "";

        if (punishment != null) {

            if (punishment.getTime() == 0) {

                denied = true;
                expires = "Never";

            } else {

                if (System.currentTimeMillis() > punishment.getTime()) {

                    plugin.getPunishmentManager().removePunishment(punishment);

                } else {

                    denied = true;
                    expires = punishment.getTimeLeft();

                }

            }

        }

        if (denied) {
            event.setCancelled(true);

            StringBuilder cancelReason = new StringBuilder("&8&m" + Strings.repeat("-", 20) + "\n");
            cancelReason.append("&cYou have been suspended from this network!\n");
            cancelReason.append("&cBanned By: &7").append(punishment.getPunisher()).append("\n");
            cancelReason.append("&cReason: &7").append(punishment.getReason()).append("\n");
            cancelReason.append("&cExpires: &7").append(expires).append("\n");
            cancelReason.append("&8&m").append(Strings.repeat("-", 20));

            event.setCancelReason(ChatColor.translateAlternateColorCodes('&', cancelReason.toString()));
        }

    }

}
