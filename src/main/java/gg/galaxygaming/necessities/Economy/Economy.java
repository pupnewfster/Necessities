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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;

public class Economy {//TODO add OpenAnalytics
    private final HashMap<UUID, Double> loadedBals = new HashMap<>();
    private ArrayList<String> balTop = new ArrayList<>();
    private Properties properties;
    private boolean regenBalTop = true;
    private String table;
    private String dbURL;

    public void init() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Loading Economy...");
        YamlConfiguration config = Necessities.getInstance().getConfig();
        this.dbURL = "jdbc:mysql://" + config.getString("Economy.DBHost") + "/" + config.getString("Economy.DBName");
        this.properties = new Properties();
        this.properties.setProperty("user", config.getString("Economy.DBUser"));
        this.properties.setProperty("password", config.getString("Economy.DBPassword"));
        this.properties.setProperty("useSSL", "false");
        this.properties.setProperty("autoReconnect", "true");
        this.table = "currency_" + config.getString("Economy.metaName");
        Bukkit.getOnlinePlayers().forEach(p -> addPlayerIfNotExists(p.getUniqueId()));
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Economy loaded.");
    }

    public boolean addPlayerIfNotExists(UUID uuid) {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        double startBal = config.getDouble("Economy.initialMoney");
        boolean added = false;
        try {
            Connection conn = DriverManager.getConnection(this.dbURL, this.properties);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + this.table + " WHERE uuid = '" + uuid + "'");
            if (!rs.next()) {
                stmt.execute("INSERT INTO " + this.table + " (uuid, balance) VALUES ('" + uuid + "'," + startBal + ")");
                this.regenBalTop = true;
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
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + this.table + " WHERE uuid = '" + uuid + "'");
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
            ResultSet rs = stmt.executeQuery("SELECT balance FROM " + this.table + " WHERE uuid = '" + uuid + "'");
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
                    stmt.execute("UPDATE " + table + " SET balance = balance - " + amount + " WHERE uuid = '" + uuid + "'");
                    stmt.close();
                    conn.close();
                    regenBalTop = true;
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
                    stmt.execute("UPDATE " + table + " SET balance = balance + " + amount + " WHERE uuid = '" + uuid + "'");
                    stmt.close();
                    conn.close();
                    regenBalTop = true;
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
                    stmt.execute("UPDATE " + table + " SET balance = " + amount + " WHERE uuid = '" + uuid + "'");
                    stmt.close();
                    conn.close();
                    regenBalTop = true;
                } catch (Exception ignored) {
                }
            }
        }.runTaskAsynchronously(Necessities.getInstance());
    }

    private void updateBalTop() { //Should this just be cached and only update the cache if things change
        if (!this.regenBalTop)
            return;
        this.regenBalTop = false;
        this.balTop = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(this.dbURL, this.properties);
            Statement stmt = conn.createStatement();//TODO can we change default order of table to just be balance desc then we can just get from it??
            ResultSet rs = stmt.executeQuery("SELECT uuid,balance FROM " + this.table + " ORDER BY balance DESC");
            while (rs.next())
                this.balTop.add(rs.getString("uuid") + " " + rs.getDouble("balance"));
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception ignored) {
        }
    }

    public String balTop(int page, int time) {
        page *= 10;
        return (this.balTop.size() < time + page + 1 || time == 10) ? null : this.balTop.get(page + time);
    }

    public int baltopPages() {
        updateBalTop(); //make sure the bal top is up to date
        return this.balTop.size() % 10 != 0 ? (this.balTop.size() / 10) + 1 : (this.balTop.size() / 10);
    }

    public static String format(double balance) {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        return config.getString("Economy.prefix") + Utils.addCommas(Utils.roundTwoDecimals(balance)) + config.getString("Economy.suffix");
    }

    public String players() {
        return Integer.toString(this.balTop.size());
    }
}