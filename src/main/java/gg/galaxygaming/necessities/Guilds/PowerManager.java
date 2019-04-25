package gg.galaxygaming.necessities.Guilds;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PowerManager {

    private final Map<Player, BukkitRunnable> players = new HashMap<>();

    /**
     * Initiates the power manager and add all online players to it.
     */
    public void initiate() {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds")) {
            Bukkit.getOnlinePlayers().forEach(this::addPlayer);
        }
    }

    /**
     * Unloads all the players from the power manager.
     */
    public void unload() {
        if (!players.isEmpty()) {
            Bukkit.getOnlinePlayers().forEach(this::removePlayer);
        }
    }

    /**
     * Adds the specified player to the power manager.
     *
     * @param p The player to add.
     */
    public void addPlayer(Player p) {
        if (!players.keySet().contains(p)) {
            final User u = Necessities.getUM().getUser(p.getUniqueId());
            players.put(p, new BukkitRunnable() {
                @Override
                public void run() {
                    u.addPower();
                }
            });
            players.get(p)
                  .runTaskTimerAsynchronously(Necessities.getInstance(), 20 * 60, 20 * 60);//60 seconds or one minute
        }
    }

    /**
     * Removes the specified player from the power manager.
     *
     * @param p The player to remove.
     */
    public void removePlayer(Player p) {
        players.get(p).cancel();
        players.remove(p);
    }
}