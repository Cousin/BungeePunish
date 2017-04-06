package xyz.betanyan.caribbeanbans.punishment;

import com.google.common.base.Strings;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class KickPunishment extends Punishment {

    public KickPunishment(UUID uuid, String reason, String punisher, boolean silent) {
        super("KICK", uuid, 0, reason, punisher, silent);
    }

    @Override
    public void punish() {

        ProxiedPlayer player = getPlugin().getProxy().getPlayer(getUuid());

        if (player != null) {

            StringBuilder cancelReason = new StringBuilder("&8&m" + Strings.repeat("-", 20) + "\n");
            cancelReason.append("&cYou have been kicked from this network!\n");
            cancelReason.append("&cKicked By: &7").append(getPunisher()).append("\n");
            cancelReason.append("&cReason: &7").append(getReason()).append("\n");
            cancelReason.append("&8&m").append(Strings.repeat("-", 20));

            player.disconnect(TextComponent.fromLegacyText(
                    ChatColor.translateAlternateColorCodes('&', cancelReason.toString())
            ));

        }

    }

    @Override
    public void pardon() {

    }
}
