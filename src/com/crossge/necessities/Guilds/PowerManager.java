package com.crossge.necessities.Guilds;

import com.crossge.necessities.Necessities;
import com.crossge.necessities.RankManager.User;
import com.crossge.necessities.RankManager.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;

public class PowerManager {
    private static ArrayList<Player> players = new ArrayList<Player>();
    UserManager um = new UserManager();

    public void initiate() {
        for (Player p : Bukkit.getOnlinePlayers())
            addPlayer(p);
    }

    public void addPlayer(Player p) {
        if (!players.contains(p)) {
            players.add(p);
            addPower(p);
        }
    }

    public void removePlayer(Player p) {
        players.remove(p);
    }

    private void addPower(final Player p) {
        if (!players.contains(p))
            return;
        final User u = um.getUser(p.getUniqueId());
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
            @Override
            public void run() {
                u.addPower();
                addPower(p);
            }
        }, 20 * 60);//60 seconds or one minute
    }
}