package xyz.betanyan.caribbeanbans.punishment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PunishmentManager {

    private List<Punishment> punishments;

    public PunishmentManager(List<Punishment> punishments) {
        this.punishments = punishments;
    }

    public PunishmentManager() {
        this.punishments = new ArrayList<>();
    }

    protected void addPunishment(Punishment punishment) {

        if (!punishment.getType().equals("WARN")) {
            Punishment check = getByUUID(punishment.getUuid(), punishment.getType());

            if (check != null) {
                removePunishment(check);
            }
        }

        punishments.add(punishment);
    }

    public void removePunishment(Punishment punishment) {
        punishments.remove(punishment);
    }

    public Punishment getByUUID(UUID uuid) {
        return punishments.stream().filter(punishment -> punishment.getUuid().equals(uuid))
                .findFirst().orElse(null);
    }

    public Punishment getByUUID(UUID uuid, String type) {
        return punishments.stream().filter(punishment -> punishment.getUuid().equals(uuid) && punishment.getType().equalsIgnoreCase(type))
                .findFirst().orElse(null);
    }

    public Punishment getByUUID(UUID uuid, String type, long time) {
        return punishments.stream().filter(punishment -> punishment.getUuid().equals(uuid)
                && punishment.getType().equalsIgnoreCase(type)
                && punishment.getTime() == time)
                    .findFirst().orElse(null);
    }

    public List<Punishment> getAllByUUID(UUID uuid, String type) {
        return punishments.stream().filter(punishment -> punishment.getUuid().equals(uuid) && punishment.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

}
