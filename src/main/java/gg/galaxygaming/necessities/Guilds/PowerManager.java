package gg.galaxygaming.necessities.Guilds;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class PowerManager {
    private final HashMap<Player, BukkitRunnable> players = new HashMap<>();

    public void initiate() {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds"))
            Bukkit.getOnlinePlayers().forEach(this::addPlayer);
    }

    public void unload() {
        if (!players.isEmpty())
            Bukkit.getOnlinePlayers().forEach(this::removePlayer);
    }

    public void addPlayer(Player p) {
        if (!players.keySet().contains(p)) {
            final User u = Necessities.getUM().getUser(p.getUniqueId());
            players.put(p, new BukkitRunnable() {
                @Override
                public void run() {
                    u.addPower();
                }
            });
            players.get(p).runTaskTimerAsynchronously(Necessities.getInstance(), 20 * 60, 20 * 60);//60 seconds or one minute
        }
    }

    public void removePlayer(Player p) {
        players.get(p).cancel();
        players.remove(p);
    }
}