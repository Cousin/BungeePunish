package xyz.betanyan.caribbeanbans.punishment;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class MutePunishment extends Punishment {

    public MutePunishment(UUID uuid, long time, String reason, String punisher, boolean silent) {
        super("MUTE", uuid, time, reason, punisher, silent);
    }

    @Override
    public void punish() {

        getPlugin().getWrapper().insertPunishment("punishments", this);

        ProxiedPlayer player = getPlugin().getProxy().getPlayer(getUuid());

        if (player != null) {

            if (getTime() != 0) {
                player.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
                        "&cYou have been muted by &7" + getPunisher() + " &cfor &7\"" + getReason() + "\". &cThis mute will last until &7" +
                                getFormattedDate())));
            } else {
                player.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
                        "&cYou were muted by &7" + getPunisher() + " &cfor &7\"" + getReason() + "\" " +
                                ".&cThis mute will last forever")));
            }

        }

    }

    @Override
    public void pardon() {

        getPlugin().getWrapper().pardonPunishment("punishments", this);
        getPlugin().getPunishmentManager().removePunishment(this);

        ProxiedPlayer player = getPlugin().getProxy().getPlayer(getUuid());
        if (player != null) {
            player.sendMessage(TextComponent.fromLegacyText(ChatColor.GREEN + "You were un-muted by " + ChatColor.YELLOW + getPunisher()));
        }

    }

}
