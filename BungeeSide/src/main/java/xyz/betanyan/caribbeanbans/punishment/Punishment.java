package xyz.betanyan.caribbeanbans.punishment;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import xyz.betanyan.caribbeanbans.CaribbeanBans;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public abstract class Punishment {

    private String type;
    private UUID uuid;
    private String reason;
    private String punisher;

    private String formattedDate;

    private boolean silent;

    private long time;

    public Punishment(String type, UUID uuid, long time, String reason, String punisher, boolean silent) {
        this.type = type;
        this.uuid = uuid;
        this.time = time;
        this.reason = reason;
        this.punisher = punisher;
        this.silent = silent;

        this.formattedDate = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a").format(new Date(getTime()));

        getPlugin().getPunishmentManager().addPunishment(this);
    }

    public String getType() {
        return type;
    }

    public String getReason() {
        return reason;
    }

    public UUID getUuid() {
        return uuid;
    }

    public long getTime() {
        return time;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public String getPunisher() {
        return punisher;
    }

    public boolean isSilent() {
        return silent;
    }

    public void broadcastPunish() {

        getUsername(getUuid(), (username, throwable) -> getPlugin().getProxy().broadcast(TextComponent.fromLegacyText(
                ChatColor.translateAlternateColorCodes('&', getPlugin().getConfig().getString("prefix") +
                        String.format(" %s has been %s by %s.", username, getPastTense(), getPunisher()))
        )));

    }

    public void broadcastPardon() {

        getUsername(getUuid(), (username, throwable) -> getPlugin().getProxy().broadcast(TextComponent.fromLegacyText(
                ChatColor.translateAlternateColorCodes('&', getPlugin().getConfig().getString("prefix") +
                        String.format(" %s has been %s by %s.", username, "un-" + getPastTense(), getPunisher()))
        )));

    }

    /*
        Easy way for formatting past tense message
     */
    private String getPastTense() {

        switch (getType()) {
            case "BAN":
                return "banned";
            case "MUTE":
                return "muted";
            case "WARN":
                return "warned";
            case "KICK":
                return "kicked";
            default:
                return "punished";
        }

    }

    /*
        For when needing to convert UUID to username.
     */
    public void getUsername(UUID uuid, Callback<String> callback) {

        getPlugin().getProxy().getScheduler().runAsync(getPlugin(), () -> {

            URL mojangAPI = null;
            try {
                mojangAPI = new URL(
                        String.format("https://api.mojang.com/user/profiles/%s/names", uuid.toString().replace("-", "")));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new java.io.InputStreamReader(
                        mojangAPI.openStream()
                ));
            } catch (IOException e) {
                e.printStackTrace();
            }

            String results = "";
            String tmp;

            try {
                while ((tmp = reader.readLine()) != null) {
                    results = results.concat(tmp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // because JSON libraries are for lames
            String[] nameSplit = results.split("\"name\":\"");
            String username = nameSplit[nameSplit.length - 1].split("\"")[0];

            callback.done(username, new Throwable());

        });
    }

    /*
        Returns formatted time left for ban messages.
     */
    public String getTimeLeft() {

        long millis = time - System.currentTimeMillis();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = millis / daysInMilli;
        millis = millis % daysInMilli;

        long elapsedHours = millis / hoursInMilli;
        millis = millis % hoursInMilli;

        long elapsedMinutes = millis / minutesInMilli;

        if (elapsedHours < 1) {
            return String.format(
                    "%d minutes",
                    elapsedMinutes);
        }

        return String.format(
                "%d days %d hours",
                elapsedDays, elapsedHours);
    }

    /*
        Easy access to plugin instance within punishment
     */
    public CaribbeanBans getPlugin() {
        return CaribbeanBans.getPlugin();
    }

    public abstract void punish();
    public abstract void pardon();

    @Override
    public String toString() {
        return type + "=|=" + time + "=|=" + uuid + "=|=" + reason + "=|=" + punisher;
    }

    /*
        Load Punishment object from String, which is sent from Spigot side.
     */
    public static Punishment fromString(String str) {

        String[] split = str.split("=\\|=");

        long time = Long.parseLong(split[1]);
        String identifier = split[2];

        String reason = split[3];

        String punisher = split[4];

        boolean silent = Boolean.parseBoolean(split[5]);

        if (identifier.length() <= 16) {

            URL mojangAPI = null;
            try {
                mojangAPI = new URL(
                        String.format("https://api.mojang.com/users/profiles/minecraft/%s", identifier));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new java.io.InputStreamReader(
                        mojangAPI.openStream()
                ));
            } catch (IOException e) {
                e.printStackTrace();
            }

            String results = "";
            String tmp;

            try {
                while ((tmp = reader.readLine()) != null) {
                    results = results.concat(tmp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // add dashes to uuid
            identifier = results.split("\\{\"id\":\"")[1].split("\",\"name\":")[0]
                .replaceFirst("([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5" );

        }

        UUID uuid = UUID.fromString(identifier);

        switch (split[0].toUpperCase()) {
            case "BAN":
                return new BanPunishment(uuid, time, reason, punisher, silent);
            case "MUTE":
                return new MutePunishment(uuid, time, reason, punisher, silent);
            case "WARN":
                return new WarnPunishment(uuid, time, reason, punisher, silent);
            case "KICK":
                return new KickPunishment(uuid, reason, punisher, silent);
            default:
                return null;
        }
    }

}
