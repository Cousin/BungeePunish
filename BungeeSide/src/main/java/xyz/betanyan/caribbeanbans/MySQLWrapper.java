package xyz.betanyan.caribbeanbans;

import com.zaxxer.hikari.HikariDataSource;
import xyz.betanyan.caribbeanbans.punishment.BanPunishment;
import xyz.betanyan.caribbeanbans.punishment.MutePunishment;
import xyz.betanyan.caribbeanbans.punishment.Punishment;
import xyz.betanyan.caribbeanbans.punishment.WarnPunishment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MySQLWrapper {

    private String host, database, username, password;
    private int port;

    private HikariDataSource hikari;

    public MySQLWrapper(String host, String database, String username, String password, int port) {

        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        this.port = port;

        hikari = new HikariDataSource();
        hikari.setMaximumPoolSize(10);
        hikari.setMaxLifetime(1800000);
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", host);
        hikari.addDataSourceProperty("port", port);
        hikari.addDataSourceProperty("databaseName", database);
        hikari.addDataSourceProperty("user", username);
        hikari.addDataSourceProperty("password", password);

    }

    public void pardonPunishment(String table, Punishment punishment) {

        String update = "UPDATE " + table + " SET `pardoned` = 1 WHERE `uuid` = ? AND `type` = ? AND `punisher` = ?;";

        Connection connection = null;
        try {
            connection = hikari.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PreparedStatement p = null;

        try {
            p = connection.prepareStatement(update);

            p.setString(1, punishment.getUuid().toString());
            p.setString(2, punishment.getType());
            p.setString(3, punishment.getPunisher());

            p.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if(p != null) {
                try {
                    p.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void insertPunishment(String table, Punishment punishment) {

        String update = "INSERT INTO " + table + " VALUES(?, ?, ?, ?, ?, ?, ?);";

        Connection connection = null;
        try {
            connection = hikari.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PreparedStatement p = null;

        try {
            p = connection.prepareStatement(update);

            String[] split = punishment.toString().split("=\\|=");

            p.setObject(1, null);

            for (int i=2;i<split.length + 2;i++) {
                p.setObject(i, split[i - 2]);
            }

            p.setBoolean(split.length + 2, false);

            p.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if(p != null) {
                try {
                    p.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public List<Punishment> loadPunishments(String table) {

        String update = "SELECT * FROM " + table + ";";

        Connection connection = null;
        try {
            connection = hikari.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PreparedStatement p = null;

        try {

            p = connection.prepareStatement(update);

            ResultSet resultSet = p.executeQuery();

            List<Punishment> punishments = new ArrayList<>();

            while (resultSet.next()) {

                String type = resultSet.getString(2);

                long time = resultSet.getLong(3);

                UUID uuid = UUID.fromString(resultSet.getString(4));
                String reason = resultSet.getString(5);
                String punisher = resultSet.getString(6);
                boolean pardonded = resultSet.getBoolean(7);

                if (!pardonded && System.currentTimeMillis() <= time) {

                    switch (type.toUpperCase()) {
                        case "BAN":
                            punishments.add(new BanPunishment(uuid, time, reason, punisher, false));
                            break;
                        case "MUTE":
                            punishments.add(new MutePunishment(uuid, time, reason, punisher, false));
                            break;
                        case "WARN":
                            punishments.add(new WarnPunishment(uuid, time, reason, punisher, false));
                            break;
                    }

                }

            }

            return punishments;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {

            if (p != null) {
                try {
                    p.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void generateTable(String table) {

        Connection connection = null;
        try {
            connection = hikari.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String update = String.format("CREATE TABLE IF NOT EXISTS %s " +
                "(id int(11) NOT NULL AUTO_INCREMENT," +
                "type varchar(32) NOT NULL," +
                "time bigint(18) NOT NULL," +
                "uuid varchar(80) NOT NULL," +
                "reason text NOT NULL," +
                "punisher varchar(32) NOT NULL," +
                "pardoned tinyint(1) NOT NULL, PRIMARY KEY (`id`));", table);

        PreparedStatement p = null;

        try {

            p = connection.prepareStatement(update);
            p.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if(p != null) {
                try {
                    p.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    public String getHost() {
        return host;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }

    public HikariDataSource getHikari() {
        return hikari;
    }

}
