package gg.galaxygaming.necessities.RankManager;

import gg.galaxygaming.necessities.Hats.Hat;
import gg.galaxygaming.necessities.Necessities;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class UserManager {
    private final File configFileUsers = new File("plugins/Necessities/RankManager", "users.yml");
    private final HashMap<UUID, User> players = new HashMap<>();

    void readUsers() {
        Bukkit.getOnlinePlayers().forEach(this::parseUser);
    }

    private void parseUser(final Player p) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), () -> forceParseUser(p));
    }

    /**
     * Force adds a user.
     * @param p The player to add as a user.
     */
    public void forceParseUser(Player p) {
        players.put(p.getUniqueId(), new User(p));
        players.get(p.getUniqueId()).givePerms();
    }

    /**
     * Gets the uuid to user mapping.
     * @return The uuid to user mapping.
     */
    public HashMap<UUID, User> getUsers() {
        return players;
    }

    /**
     * Removes a given user.
     * @param uuid The uuid of the user.
     */
    public void removeUser(UUID uuid) {
        players.remove(uuid);
    }

    /**
     * Gets the user with the specified uuid.
     * @param uuid The uuid of the user.
     * @return The user with the given uuid.
     */
    public User getUser(UUID uuid) {
        return !players.containsKey(uuid) ? new User(uuid) : players.get(uuid);
    }

    /**
     * Unload the user manager.
     */
    public void unload() {
        for (Map.Entry<UUID, User> uuidUserEntry : players.entrySet()) {
            User u = uuidUserEntry.getValue();
            Hat h = u.getHat();
            if (h != null)
                h.despawn();
            u.updateTimePlayed();
            u.removePerms();
        }
    }

    void addRankPerm(Rank r, String node) {
        RankManager rm = Necessities.getRM();
        for (Map.Entry<UUID, User> uuidUserEntry : players.entrySet())
            if (rm.hasRank(uuidUserEntry.getValue().getRank(), r))
                uuidUserEntry.getValue().addPerm(node);
    }

    void delRankPerm(Rank r, String node) {
        RankManager rm = Necessities.getRM();
        for (Map.Entry<UUID, User> uuidUserEntry : players.entrySet())
            if (rm.hasRank(uuidUserEntry.getValue().getRank(), r))
                uuidUserEntry.getValue().removePerm(node);
    }

    void refreshRankPerm(Rank r) {
        RankManager rm = Necessities.getRM();
        for (Map.Entry<UUID, User> uuidUserEntry : players.entrySet())
            if (rm.hasRank(uuidUserEntry.getValue().getRank(), r))
                uuidUserEntry.getValue().refreshPerms();
    }

    /**
     * Adds the specified player.
     * @param player The player to add.
     */
    public void addUser(Player player) {
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        RankManager rm = Necessities.getRM();
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

    /**
     * Updates a user's rank.
     * @param u The user to update the rank of.
     * @param r The rank to set the user to.
     */
    public void updateUserRank(User u, Rank r) {
        if (r == null)
            return;
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        configUsers.set(u.getUUID().toString() + ".rank", r.getName());
        try {
            configUsers.save(configFileUsers);
        } catch (Exception ignored) {
        }
        u.updateRank(r);
    }

    /**
     * Updates a user's permissions.
     * @param uuid       The uuid of the user to update.
     * @param permission The permission to add or remove.
     * @param remove     True to remove the permission, false otherwise.
     */
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

    /**
     * Updates a user's subranks.
     * @param uuid   The uuid of the user to update.
     * @param name   The name of the subrank to add or remove.
     * @param remove True to remove the subrank, false otherwise.
     */
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