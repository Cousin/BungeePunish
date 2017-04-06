package xyz.betanyan.caribbeanbans.punishment;

import com.google.common.base.Strings;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class BanPunishment extends Punishment {

    public BanPunishment(UUID uuid, long time, String reason, String punisher, boolean silent) {
        super("BAN", uuid, time, reason, punisher, silent);
    }

    @Override
    public void punish() {

        getPlugin().getWrapper().insertPunishment("punishments", this);

        ProxiedPlayer player = getPlugin().getProxy().getPlayer(getUuid());

        if (player != null) {

            StringBuilder cancelReason = new StringBuilder("&8&m" + Strings.repeat("-", 20) + "\n");
            cancelReason.append("&cYou have been suspended from this network!\n");
            cancelReason.append("&cBanned By: &7").append(getPunisher()).append("\n");
            cancelReason.append("&cReason: &7").append(getReason()).append("\n");
            cancelReason.append("&cExpires: &7").append(getTime() == 0 ? "Never" : getTimeLeft()).append("\n");
            cancelReason.append("&8&m").append(Strings.repeat("-", 20));

            player.disconnect(TextComponent.fromLegacyText(
                    ChatColor.translateAlternateColorCodes('&', cancelReason.toString())
            ));

        }

    }

    @Override
    public void pardon() {

        getPlugin().getWrapper().pardonPunishment("punishments", this);
        getPlugin().getPunishmentManager().removePunishment(this);

    }

}
