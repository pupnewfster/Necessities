package com.crossge.necessities.Guilds;

import com.crossge.necessities.Necessities;
import com.crossge.necessities.RankManager.User;
import com.crossge.necessities.RankManager.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashMap;

public class PowerManager {
    private File configFile = new File("plugins/Necessities", "config.yml");
    private static HashMap<Player,BukkitRunnable> players = new HashMap<>();
    UserManager um = new UserManager();

    public void initiate() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds"))
            for (Player p : Bukkit.getOnlinePlayers())
                addPlayer(p);
    }

    public void unload() {
        if (!players.isEmpty())
            for (Player p : Bukkit.getOnlinePlayers())
                removePlayer(p);
    }

    public void addPlayer(Player p) {
        if (!players.keySet().contains(p)) {
            final User u = um.getUser(p.getUniqueId());
            players.put(p, new BukkitRunnable() {
                @Override
                public void run() {
                    u.addPower();
                }
            });
            players.get(p).runTaskTimerAsynchronously(Necessities.getInstance(), 20*60, 20 * 60);//60 seconds or one minute
        }
    }

    public void removePlayer(Player p) {
        players.get(p).cancel();
        players.remove(p);
    }
}