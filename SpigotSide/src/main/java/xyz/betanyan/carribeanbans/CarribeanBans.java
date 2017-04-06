package xyz.betanyan.carribeanbans;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CarribeanBans extends JavaPlugin {

    @Override
    public void onEnable() {

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getServer().getPluginManager().registerEvents(new InventoryClick(this), this);
        getCommand("punish").setExecutor(new PunishCommand());

    }

    public void sendPunishment(Player punisher, String str) {

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("ADD-PUNISHMENT");
        output.writeUTF(str);

        punisher.sendPluginMessage(this, "BungeeCord", output.toByteArray());

    }

}
