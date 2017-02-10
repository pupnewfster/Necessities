package gg.galaxygaming.necessities.Economy;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class Economy {//TODO add OpenAnalytics
    private final HashMap<UUID, Double> loadedBals = new HashMap<>();
    private Properties properties;
    private String dbURL;
    private int type;

    public void init() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Loading Economy...");
        YamlConfiguration config = Necessities.getInstance().getConfig();
        this.dbURL = "jdbc:mysql://" + config.getString("Economy.DBHost") + "/" + config.getString("Economy.DBName");
        this.properties = new Properties();
        this.properties.setProperty("user", config.getString("Economy.DBUser"));
        this.properties.setProperty("password", config.getString("Economy.DBPassword"));
        this.properties.setProperty("useSSL", "false");
        this.properties.setProperty("autoReconnect", "true");
        this.type = config.getInt("Economy.currencyType");
        Bukkit.getOnlinePlayers().forEach(p -> addPlayerIfNotExists(p.getUniqueId()));
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Economy loaded.");
    }

    public boolean addPlayerIfNotExists(UUID uuid) { //TODO let the table have default values
        YamlConfiguration config = Necessities.getInstance().getConfig();
        double startBal = config.getDouble("Economy.initialMoney");
        boolean added = false;
        try {
            Connection conn = DriverManager.getConnection(this.dbURL, this.properties);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM economy WHERE uuid = '" + uuid + "' AND currencyType=" + this.type);
            if (!rs.next()) {
                stmt.execute("INSERT INTO economy (uuid, currencyType, balance) VALUES ('" + uuid + "'," + this.type + "," + startBal + ")");
                added = true;
            } else if (!loadedBals.containsKey(uuid)) //Loads it into memory for temporary faster lookup
                this.loadedBals.put(uuid, rs.getDouble("balance"));
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception ignored) {
        }
        return added;
    }

    public boolean doesPlayerExist(UUID uuid) {
        if (loadedBals.containsKey(uuid))
            return true;
        boolean exists = false;
        try {
            Connection conn = DriverManager.getConnection(this.dbURL, this.properties);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM economy WHERE uuid = '" + uuid + "' AND currencyType=" + this.type);
            if (rs.next())
                exists = true;
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception ignored) {
        }
        return exists;
    }

    public double getBalance(UUID uuid) {
        if (uuid == null)
            return 0.0;
        if (this.loadedBals.containsKey(uuid))
            return this.loadedBals.get(uuid);
        double bal = 0.0;
        try {
            Connection conn = DriverManager.getConnection(this.dbURL, this.properties);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT balance FROM economy WHERE uuid = '" + uuid + "' AND currencyType=" + this.type);
            if (rs.next())
                bal = rs.getDouble("balance");
            else
                bal = -1.0;
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception ignored) {
        }
        return bal;
    }

    public void unloadAccount(UUID uuid) {
        this.loadedBals.remove(uuid);
    }

    public void removeMoney(UUID uuid, double amount) {
        if (this.loadedBals.containsKey(uuid))
            this.loadedBals.put(uuid, this.loadedBals.get(uuid) - amount);
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Connection conn = DriverManager.getConnection(dbURL, properties);
                    Statement stmt = conn.createStatement();
                    stmt.execute("UPDATE economy SET balance = balance - " + amount + " WHERE uuid = '" + uuid + "' AND currencyType=" + type);
                    stmt.close();
                    conn.close();
                } catch (Exception ignored) {
                }
            }
        }.runTaskAsynchronously(Necessities.getInstance());
    }

    public void addMoney(UUID uuid, double amount) {
        if (this.loadedBals.containsKey(uuid))
            this.loadedBals.put(uuid, this.loadedBals.get(uuid) + amount);
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Connection conn = DriverManager.getConnection(dbURL, properties);
                    Statement stmt = conn.createStatement();
                    stmt.execute("UPDATE economy SET balance = balance + " + amount + " WHERE uuid = '" + uuid + "' AND currencyType=" + type);
                    stmt.close();
                    conn.close();
                } catch (Exception ignored) {
                }
            }
        }.runTaskAsynchronously(Necessities.getInstance());
    }

    public void setBalance(UUID uuid, double amount) {
        if (this.loadedBals.containsKey(uuid))
            this.loadedBals.put(uuid, amount);
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Connection conn = DriverManager.getConnection(dbURL, properties);
                    Statement stmt = conn.createStatement();
                    stmt.execute("UPDATE economy SET balance = " + amount + " WHERE uuid = '" + uuid + "' AND currencyType=" + type);
                    stmt.close();
                    conn.close();
                } catch (Exception ignored) {
                }
            }
        }.runTaskAsynchronously(Necessities.getInstance());
    }

    public List<String> getBalTop(int page) {
        List<String> balTop = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(this.dbURL, this.properties);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT uuid,balance FROM economy WHERE currencyType=" + this.type + " ORDER BY balance DESC LIMIT " + (page - 1) * 10 + ",10");
            while (rs.next())
                balTop.add(rs.getString("uuid") + " " + rs.getDouble("balance"));
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception ignored) {
        }
        return balTop;
    }

    public int baltopPages() {
        int size = playerCount();
        return size % 10 != 0 ? size / 10 + 1 : size / 10;
    }

    public static String format(double balance) {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        return config.getString("Economy.prefix") + Utils.addCommas(Utils.roundTwoDecimals(balance)) + config.getString("Economy.suffix");
    }

    public int playerCount() {
        int amount = 0;
        try {
            Connection conn = DriverManager.getConnection(this.dbURL, this.properties);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM economy WHERE currencyType=" + this.type);
            if (rs.next())
                amount = rs.getInt(1);
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception ignored) {
        }
        return amount;
    }
}