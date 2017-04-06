package xyz.betanyan.caribbeanbans.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import xyz.betanyan.caribbeanbans.CaribbeanBans;
import xyz.betanyan.caribbeanbans.punishment.Punishment;

public class PunishmentReceive implements Listener {

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {

        if (event.getTag().equals("BungeeCord")) {

            ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
            String subChannel = in.readUTF();

            if (subChannel.equals("ADD-PUNISHMENT")) {

                String punishStr = in.readUTF();

                CaribbeanBans.getPlugin().getProxy().getScheduler().runAsync(CaribbeanBans.getPlugin(), () -> {

                    Punishment punishment = Punishment.fromString(punishStr);
                    if (punishment != null) {

                        if (punishment.getTime() == -1) {
                            punishment.pardon();
                            if (!punishment.isSilent()) {
                                punishment.broadcastPardon();
                            }
                        } else {
                            punishment.punish();
                            if (!punishment.isSilent()) {
                                punishment.broadcastPunish();
                            }
                        }

                    }

                });

            }

        }

    }

}
