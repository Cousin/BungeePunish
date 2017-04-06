package xyz.betanyan.caribbeanbans.punishment;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class WarnPunishment extends Punishment {

    public WarnPunishment(UUID uuid, long time, String reason, String punisher, boolean silent) {
        super("WARN", uuid, time, reason, punisher, silent);
    }

    @Override
    public void punish() {

        getPlugin().getWrapper().insertPunishment("punishments", this);

        List<Punishment> allWarns = getPlugin().getPunishmentManager().getAllByUUID(getUuid(), getType());

        int points = allWarns.size();
        int pointsToBePunished = getPlugin().getConfig().getInt("warn-points");

        ProxiedPlayer player = getPlugin().getProxy().getPlayer(getUuid());

        if (points < pointsToBePunished) {

            if (player != null) {
                player.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
                        "&cYou were warned by &7" + getPunisher() + " &cfor &7" + getReason() + ". &cThis warn expires on &7" + getFormattedDate())));
                player.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
                        "&cYou currently have &7" + points + "/" + pointsToBePunished + " warning points until you are banned.")));
            }

        } else {

            new BanPunishment(getUuid(), System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7),
                    "Reached maximum warning points", "Console", true).punish();

        }

    }

    @Override
    public void pardon() {

    }

}
