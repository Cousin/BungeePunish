package xyz.betanyan.caribbeanbans;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import xyz.betanyan.caribbeanbans.listeners.PlayerChat;
import xyz.betanyan.caribbeanbans.listeners.PlayerLogin;
import xyz.betanyan.caribbeanbans.listeners.PunishmentReceive;
import xyz.betanyan.caribbeanbans.punishment.PunishmentManager;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class CaribbeanBans extends Plugin {

    private static CaribbeanBans plugin;

    private PunishmentManager punishmentManager;

    private MySQLWrapper wrapper;

    private Configuration config;

    @Override
    public void onEnable() {
        plugin = this;

        config = loadConfig();
        wrapper = new MySQLWrapper(
                config.getString("mysql.host"),
                config.getString("mysql.database"),
                config.getString("mysql.username"),
                config.getString("mysql.password"),
                config.getInt("mysql.port"));

        System.out.println("Loading all valid punishments into memory...");

        punishmentManager = new PunishmentManager();

        /*
            "Synchronously" loading MySQL async. FutureTasks or running sync were causing IllegalStateExceptions
         */
        AtomicBoolean doneLoading = new AtomicBoolean(false);

        getPlugin().getProxy().getScheduler().runAsync(this, () -> {
            wrapper.generateTable("punishments");
            wrapper.loadPunishments("punishments");
            doneLoading.set(true);
        });

        while (true) {
            if (doneLoading.get()) {
                break;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Loaded all punishments");

        registerListeners(new PlayerChat(), new PlayerLogin(), new PunishmentReceive());

    }

    private void registerListeners(Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> getProxy().getPluginManager().registerListener(this, listener));
    }

    private Configuration loadConfig() {

        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ConfigurationProvider configurationProvider = ConfigurationProvider.getProvider(YamlConfiguration.class);

        try {
            Configuration conf = configurationProvider.load(configFile);
            if (conf.get("mysql") == null) {
                conf.set("mysql.host", "localhost");
                conf.set("mysql.database", "bans");
                conf.set("mysql.username", "root");
                conf.set("mysql.password", "password");
                conf.set("mysql.port", 3306);
            }
            if (conf.get("warn-points") == null) {
                conf.set("warn-points", 5);
            }
            if (conf.get("prefix") == null){
                conf.set("prefix", "&a[Prefix]");
            }
            configurationProvider.save(conf, configFile);

            return conf;
        } catch (IOException e) {
            e.printStackTrace();
            return new Configuration(null);
        }

    }

    public Configuration getConfig() {
        return config;
    }

    public PunishmentManager getPunishmentManager() {
        return punishmentManager;
    }

    public MySQLWrapper getWrapper() {
        return wrapper;
    }

    public static CaribbeanBans getPlugin() {
        return plugin;
    }
}
