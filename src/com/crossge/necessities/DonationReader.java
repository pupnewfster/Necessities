package com.crossge.necessities;

import com.crossge.necessities.RankManager.RankManager;
import com.crossge.necessities.RankManager.User;
import com.crossge.necessities.RankManager.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.UUID;

public class DonationReader {
    private static Connection conn = null;
    RankManager rm = new RankManager();
    UserManager um = new UserManager();
    Variables var = new Variables();
    GetUUID get = new GetUUID();

    public void check() {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
            @Override
            public void run() {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Properties connectionProperties = new Properties();
                    connectionProperties.put("user", "donation");
                    connectionProperties.put("password", YamlConfiguration.loadConfiguration(new File("plugins/Necessities", "config.yml")).getString("Necessities.DonationPass"));
                    connectionProperties.put("autoReconnect", "false");
                    connectionProperties.put("maxReconnects", "0");
                    conn = DriverManager.getConnection("jdbc:mysql://egservers.net:3306/donation", connectionProperties);

                    ResultSet rs = conn.prepareStatement("SELECT * FROM mctest").executeQuery();//actions
                    while (rs.next()) {
                        if (rs.getInt("server") == 6 && rs.getInt("delivered") == 0) {
                            UUID uuid = UUID.fromString(rs.getString("uuid").replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
                            if (get.hasJoined(uuid)) {
                                User u = um.getUser(uuid);
                                um.updateUserSubrank(uuid, rm.getSub("Necessities.Donator"), false);
                                Bukkit.broadcastMessage(u.getDispName() + var.getMessages() + " just donated.");
                                conn.prepareStatement("UPDATE mctest SET delivered =" + 1).executeUpdate();//actions
                            }
                        }
                    }
                    if (!conn.isClosed())
                        conn.close();
                } catch (Exception e) {
                    System.out.print(e.getCause());
                    e.printStackTrace();
                }
                check();
            }
        }, 20 * 60);//60 seconds or one minute
    }

    public void init() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
            @Override
            public void run() {
                Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Connecting to Donator Database.");
                check();
                Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Connected to Donator Database.");
            }
        });
    }

    public void disconnect() {
        try {
            if (!conn.isClosed())
                conn.close();
        } catch (Exception e) { }
    }
}