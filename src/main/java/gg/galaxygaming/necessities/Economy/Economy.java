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

    /**
     * Initializes the economy.
     */
    public void init() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Loading Economy...");
        YamlConfiguration config = Necessities.getInstance().getConfig();
        this.dbURL = "jdbc:mysql://" + config.getString("Economy.DBHost") + '/' + config.getString("Economy.DBName");
        this.properties = new Properties();
        this.properties.setProperty("user", config.getString("Economy.DBUser"));
        this.properties.setProperty("password", config.getString("Economy.DBPassword"));
        this.properties.setProperty("useSSL", "false");
        this.properties.setProperty("autoReconnect", "true");
        this.properties.setProperty("useLegacyDatetimeCode", "false");
        this.properties.setProperty("serverTimezone", "EST");
        this.type = config.getInt("Economy.currencyType");
        Bukkit.getOnlinePlayers().forEach(p -> addPlayerIfNotExists(p.getUniqueId()));
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Economy loaded.");
    }

    /**
     * Adds a player with the specified UUID if they are not already in the table.
     * @param uuid The uuid of the player to check.
     * @return True if the player was added to the table, false otherwise.
     */
    public boolean addPlayerIfNotExists(UUID uuid) {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        double startBal = config.getDouble("Economy.initialMoney");
        boolean added = false;
        try {
            Connection conn = DriverManager.getConnection(this.dbURL, this.properties);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM economy WHERE uuid = '" + uuid + "' AND currencyType=" + this.type);
            if (!rs.next()) {
                stmt.execute("INSERT INTO economy (uuid, currencyType, balance) VALUES ('" + uuid + "'," + this.type + ',' + startBal + ')');
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

    /**
     * Checks if the player with the specified UUID is in the table.
     * @param uuid The uuid of the player to check.
     * @return True if the player exists in the table, false otherwise.
     */
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

    /**
     * Retrieves a player's balance.
     * @param uuid The uuid of the player to retrieve the balance of.
     * @return The balance of the specified player.
     */
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
            bal = rs.next() ? rs.getDouble("balance") : -1.0;
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception ignored) {
        }
        return bal;
    }

    /**
     * Removes an account from active memory.
     * @param uuid The uuid of the account to unload from memory.
     */
    public void unloadAccount(UUID uuid) {
        this.loadedBals.remove(uuid);
    }

    /**
     * Removes a specified amount of money from the given player.
     * @param uuid   The uuid of the player to remove the money from.
     * @param amount The amount of money to remove.
     */
    public void removeMoney(UUID uuid, double amount) {
        if (this.loadedBals.containsKey(uuid))
            this.loadedBals.put(uuid, this.loadedBals.get(uuid) - amount);
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Connection conn = DriverManager.getConnection(dbURL, properties);
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate("UPDATE economy SET balance = balance - " + amount + " WHERE uuid = '" + uuid + "' AND currencyType=" + type);
                    stmt.close();
                    conn.close();
                } catch (Exception ignored) {
                }
            }
        }.runTaskAsynchronously(Necessities.getInstance());
    }

    /**
     * Adds a specified amount of money from the given player.
     * @param uuid   The uuid of the player to add the money to.
     * @param amount The amount of money to add.
     */
    public void addMoney(UUID uuid, double amount) {
        if (this.loadedBals.containsKey(uuid))
            this.loadedBals.put(uuid, this.loadedBals.get(uuid) + amount);
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Connection conn = DriverManager.getConnection(dbURL, properties);
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate("UPDATE economy SET balance = balance + " + amount + " WHERE uuid = '" + uuid + "' AND currencyType=" + type);
                    stmt.close();
                    conn.close();
                } catch (Exception ignored) {
                }
            }
        }.runTaskAsynchronously(Necessities.getInstance());
    }

    /**
     * Sets a player's balanced to the given amount.
     * @param uuid   The uuid of the player to change the balance of.
     * @param amount The amount to set the player's balance to.
     */
    public void setBalance(UUID uuid, double amount) {
        if (this.loadedBals.containsKey(uuid))
            this.loadedBals.put(uuid, amount);
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Connection conn = DriverManager.getConnection(dbURL, properties);
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate("UPDATE economy SET balance = " + amount + " WHERE uuid = '" + uuid + "' AND currencyType=" + type);
                    stmt.close();
                    conn.close();
                } catch (Exception ignored) {
                }
            }
        }.runTaskAsynchronously(Necessities.getInstance());
    }

    /**
     * Retrieve a page of baltop.
     * @param page The page number to retrieve.
     * @return The specified page of baltop.
     */
    public List<String> getBalTop(int page) {
        List<String> balTop = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(this.dbURL, this.properties);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT uuid,balance FROM economy WHERE currencyType=" + this.type + " ORDER BY balance DESC LIMIT " + (page - 1) * 10 + ",10");
            while (rs.next())
                balTop.add(rs.getString("uuid") + ' ' + rs.getDouble("balance"));
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception ignored) {
        }
        return balTop;
    }

    /**
     * Gets the number of baltop pages there are.
     * @return The number of pages in baltop.
     */
    public int baltopPages() {
        int size = playerCount();
        return size % 10 != 0 ? size / 10 + 1 : size / 10;
    }

    /**
     * Formats a given balance based on the config.
     * @param balance The balance to format.
     * @return A formatted string of the given balance.
     */
    public static String format(double balance) {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        return config.getString("Economy.prefix") + Utils.addCommas(Utils.roundTwoDecimals(balance)) + config.getString("Economy.suffix");
    }

    /**
     * Gets the number of players who has joined the server.
     * @return The number of players who has joined the server.
     */
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