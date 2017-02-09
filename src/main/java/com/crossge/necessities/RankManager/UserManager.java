package com.crossge.necessities.RankManager;

import com.crossge.necessities.Hats.Hat;
import com.crossge.necessities.Necessities;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class UserManager {
    private final File configFileUsers = new File("plugins/Necessities/RankManager", "users.yml");
    private final HashMap<UUID, User> players = new HashMap<>();

    public void readUsers() {
        Bukkit.getOnlinePlayers().forEach(this::parseUser);
    }

    private void parseUser(final Player p) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), () -> forceParseUser(p));
    }

    public void forceParseUser(Player p) {
        players.put(p.getUniqueId(), new User(p));
        players.get(p.getUniqueId()).givePerms();
    }

    public HashMap<UUID, User> getUsers() {
        return players;
    }

    public void removeUser(UUID uuid) {
        players.remove(uuid);
    }

    public User getUser(UUID uuid) {
        return !players.containsKey(uuid) ? new User(uuid) : players.get(uuid);
    }

    public void unload() {
        for (UUID uuid : players.keySet()) {
            User u = players.get(uuid);
            Hat h = u.getHat();
            if (h != null)
                h.despawn();
            u.updateTimePlayed();
            u.removePerms();
        }
    }

    void addRankPerm(Rank r, String node) {
        RankManager rm = Necessities.getInstance().getRM();
        for (UUID uuid : players.keySet())
            if (rm.hasRank(players.get(uuid).getRank(), r))
                players.get(uuid).addPerm(node);
    }

    void delRankPerm(Rank r, String node) {
        RankManager rm = Necessities.getInstance().getRM();
        for (UUID uuid : players.keySet())
            if (rm.hasRank(players.get(uuid).getRank(), r))
                players.get(uuid).removePerm(node);
    }

    void refreshRankPerm(Rank r) {
        RankManager rm = Necessities.getInstance().getRM();
        for (UUID uuid : players.keySet())
            if (rm.hasRank(players.get(uuid).getRank(), r))
                players.get(uuid).refreshPerms();
    }

    public void addUser(Player player) {
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        RankManager rm = Necessities.getInstance().getRM();
        if (configUsers.contains(player.getUniqueId().toString())) {
            if (!configUsers.contains(player.getUniqueId().toString() + ".rank"))
                configUsers.set(player.getUniqueId().toString() + ".rank", rm.getRank(0).getName());
            if (!configUsers.contains(player.getUniqueId().toString() + ".permissions"))
                configUsers.set(player.getUniqueId().toString() + ".permissions", Collections.singletonList(""));
            if (!configUsers.contains(player.getUniqueId().toString() + ".subranks"))
                configUsers.set(player.getUniqueId().toString() + ".subranks", Collections.singletonList(""));
            if (!configUsers.contains(player.getUniqueId().toString() + ".power"))
                configUsers.set(player.getUniqueId().toString() + ".power", 0);
        } else {
            configUsers.set(player.getUniqueId().toString() + ".rank", rm.getRank(0).getName());
            configUsers.set(player.getUniqueId().toString() + ".permissions", Collections.singletonList(""));
            configUsers.set(player.getUniqueId().toString() + ".subranks", Collections.singletonList(""));
            configUsers.set(player.getUniqueId().toString() + ".power", 0);
        }
        try {
            configUsers.save(configFileUsers);
        } catch (Exception ignored) {
        }
    }

    public void updateUserRank(User u, UUID uuid, Rank r) {
        if (r == null)
            return;
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        configUsers.set(uuid.toString() + ".rank", r.getName());
        try {
            configUsers.save(configFileUsers);
        } catch (Exception ignored) {
        }
        u.updateRank(r);
    }

    public void updateUserPerms(UUID uuid, String permission, boolean remove) {
        if (permission.equals(""))
            return;
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        List<String> perms = configUsers.getStringList(uuid.toString() + ".permissions");
        if (perms.contains(""))
            perms.remove("");
        if (remove) {
            perms.remove(permission);
            if (perms.isEmpty())
                perms.add("");
            configUsers.set(uuid.toString() + ".permissions", perms);
            if (players.containsKey(uuid))
                getUser(uuid).removePerm(permission);

        } else {
            perms.add(permission);
            configUsers.set(uuid.toString() + ".permissions", perms);
            if (players.containsKey(uuid))
                getUser(uuid).addPerm(permission);
        }
        try {
            configUsers.save(configFileUsers);
        } catch (Exception ignored) {
        }
    }

    public void updateUserSubrank(UUID uuid, String name, boolean remove) {
        if (name.equals(""))
            return;
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        List<String> subranks = configUsers.getStringList(uuid.toString() + ".subranks");
        if (subranks.contains(""))
            subranks.remove("");
        if (remove)
            subranks.remove(name);
        else
            subranks.add(name);
        if (subranks.isEmpty())
            subranks.add("");
        configUsers.set(uuid.toString() + ".subranks", subranks);
        try {
            configUsers.save(configFileUsers);
        } catch (Exception ignored) {
        }
        if (players.containsKey(uuid))
            getUser(uuid).refreshPerms();
    }
}