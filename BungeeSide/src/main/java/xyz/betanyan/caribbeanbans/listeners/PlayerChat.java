package xyz.betanyan.caribbeanbans.listeners;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import xyz.betanyan.caribbeanbans.CaribbeanBans;
import xyz.betanyan.caribbeanbans.punishment.Punishment;

import java.util.Date;
import java.util.UUID;

public class PlayerChat implements Listener {

    @EventHandler
    public void onPlayerChat(ChatEvent event) {

        if (!event.isCommand()) {

            Connection senderConnection = event.getSender();
            if (senderConnection instanceof ProxiedPlayer) {

                ProxiedPlayer sender = (ProxiedPlayer) senderConnection;

                UUID uuid = sender.getUniqueId();

                Punishment punishment = CaribbeanBans.getPlugin().getPunishmentManager()
                        .getByUUID(uuid, "MUTE");

                if (punishment != null) {

                    if (punishment.getTime() == 0) {
                        sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
                                "&cYou were muted by &7" + punishment.getPunisher() + " &cfor &7\"" + punishment.getReason() + "\" " +
                                        ".&cThis mute will last forever")));
                        event.setCancelled(true);
                    } else {

                        if (System.currentTimeMillis() > punishment.getTime()) {
                            CaribbeanBans.getPlugin().getPunishmentManager().removePunishment(punishment);
                        } else {
                            sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
                                    "&cYou were muted by &7" + punishment.getPunisher() + " &cfor &7\"" +
                                            punishment.getReason() + "\". &cThis mute will last until &7" +
                                            punishment.getFormattedDate())));
                            event.setCancelled(true);
                        }

                    }

                }

            }

        }

    }

}
