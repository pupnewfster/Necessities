package gg.galaxygaming.necessities.RankManager;

import gg.galaxygaming.necessities.Necessities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;

import java.io.File;
import java.util.*;

public class RankManager {
    private final File configFileRanks = new File("plugins/Necessities/RankManager", "ranks.yml");
    private final File configFileSubranks = new File("plugins/Necessities/RankManager", "subranks.yml");
    private final HashMap<String, String> subranks = new HashMap<>();
    private final HashMap<String, Rank> ranks = new HashMap<>();
    private final ArrayList<String> names = new ArrayList<>();
    private final ArrayList<Rank> order = new ArrayList<>();

    /**
     * Initializes the rank manager.
     */
    public void readRanks() {
        YamlConfiguration configRanks = YamlConfiguration.loadConfiguration(configFileRanks), configSubranks = YamlConfiguration.loadConfiguration(configFileSubranks);
        for (String rank : configRanks.getKeys(false)) {
            if (configRanks.contains(rank + ".previousRank")) {
                if (names.contains(configRanks.getString(rank + ".previousRank")))
                    names.add(names.indexOf(configRanks.getString(rank + ".previousRank")) + 1, rank);
                else {
                    String previous = configRanks.getString(rank + ".previousRank");
                    while (configRanks.contains(previous + ".previousRank")) {
                        previous = configRanks.getString(previous + ".previousRank");
                        if (names.contains(previous)) {
                            names.add(names.indexOf(previous) + 1, rank);
                            break;
                        }
                    }
                }
            } else if (!names.contains(rank))
                names.add(0, rank);
        }
        for (String name : names) {
            ranks.put(name, new Rank(name));
            order.add(ranks.get(name));
        }
        for (String subrank : configSubranks.getKeys(true))
            if (!subrank.equals("") && !configSubranks.getStringList(subrank).isEmpty())//If is an actual subrank not just base node in tree of a subrank
                subranks.put(subrank.toLowerCase(), subrank);
        Necessities.getUM().readUsers();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), () -> {
            Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Retrieving all permissions.");
            updatePerms();
            Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "All permissions retrieved.");
        });
    }

    private void updatePerms() {
        ArrayList<String> p = new ArrayList<>();
        for (Permission perm : Bukkit.getPluginManager().getPermissions()) {
            if (perm.getName().equals("*"))
                continue;
            for (String t : perm.getChildren().keySet())
                if (!p.contains(t))
                    p.add(t);
            if (!p.contains(perm.getName())) {
                perm.addParent("*", true);
                p.add(perm.getName());
            }
        }
    }

    /**
     * Reloads the permissions.
     */
    public void reloadPermissions() {
        UserManager um = Necessities.getUM();
        for (Rank r : getOrder()) {
            r.refreshPerms();
            um.refreshRankPerm(r);
        }
    }

    /**
     * Checks if the given name is a valid subrank.
     * @param subrank The name to check.
     * @return True if the name is a valid subrank, false otherwise.
     */
    public boolean validSubrank(String subrank) {
        return !subranks.containsKey(subrank.toLowerCase());
    }

    /**
     * Gets the proper name of the given subrank.
     * @param subrank The subrank to get the proper name of.
     * @return The proper name of the given subrank.
     */
    public String getSub(String subrank) {
        return subranks.get(subrank.toLowerCase());
    }

    /**
     * Gets the list of ranks in their order of lowest to highest.
     * @return The list of ranks in their order of lowest to highest.
     */
    public ArrayList<Rank> getOrder() {
        return order;
    }

    /**
     * Gets the list of subranks.
     * @return The list of subranks.
     */
    public Collection<String> getSubranks() {
        return subranks.values();
    }

    /**
     * Gets the rank at the specified index in the order list.
     * @param index The index in the order list.
     * @return The rank at the specified index.
     */
    public Rank getRank(int index) {
        return order.size() - 1 < index ? null : order.get(index);
    }

    /**
     * Gets the rank based off of the given name.
     * @param name The name of the rank to get.
     * @return The rank based on the given rank name.
     */
    public Rank getRank(String name) {
        return ranks.get(name);
    }

    /**
     * Checks if the given rank has the specified rank.
     * @param rank  The rank to check if it has the other rank.
     * @param check The rank to check.
     * @return True if the given rank has the specified rank, false otherwise.
     */
    public boolean hasRank(Rank rank, Rank check) {
        return order.contains(check) && order.contains(rank) && order.indexOf(rank) - order.indexOf(check) >= 0;
    }

    /**
     * Update the permissions of a given rank.
     * @param r          The rank to update the permissions of.
     * @param permission The permission to add or remove.
     * @param remove     True to remove the permission, false otherwise.
     */
    public void updateRankPerms(Rank r, String permission, boolean remove) {
        if (permission.equals(""))
            return;
        UserManager um = Necessities.getUM();
        YamlConfiguration configRanks = YamlConfiguration.loadConfiguration(configFileRanks);
        List<String> perms = configRanks.getStringList(r.getName() + ".permissions");
        perms.remove("");
        if (remove) {
            perms.remove(permission);
            if (perms.isEmpty())
                perms.add("");
            configRanks.set(r.getName() + ".permissions", perms);
            r.removePerm(permission);
            um.delRankPerm(r, permission);
        } else {
            perms.add(permission);
            configRanks.set(r.getName() + ".permissions", perms);
            r.addPerm(permission);
            um.addRankPerm(r, permission);
        }
        try {
            configRanks.save(configFileRanks);
        } catch (Exception ignored) {
        }
    }

    /**
     * Update the permissions of a given subrank.
     * @param subrank    The subrank to update the permissions of.
     * @param permission The permission to add or remove.
     * @param remove     True to remove the permission, false otherwise.
     */
    public void updateSubPerms(String subrank, String permission, boolean remove) {
        if (permission.equals(""))
            return;
        YamlConfiguration configSubranks = YamlConfiguration.loadConfiguration(configFileSubranks);
        YamlConfiguration configRanks = YamlConfiguration.loadConfiguration(configFileRanks);
        UserManager um = Necessities.getUM();
        List<String> perms = configSubranks.getStringList(subrank);
        perms.remove("");
        if (remove) {
            perms.remove(permission);
            if (perms.isEmpty())
                perms.add("");
            configSubranks.set(subrank, perms);
        } else {
            perms.add(permission);
            configSubranks.set(subrank, perms);
        }
        try {
            configSubranks.save(configFileSubranks);
        } catch (Exception ignored) {
        }
        for (Rank r : order)
            if (configRanks.contains(r.getName()) && configRanks.getStringList(r.getName() + ".subranks").contains(subrank)) {
                r.refreshPerms();
                um.refreshRankPerm(r);
            }
    }

    /**
     * Update the subranks of a given rank.
     * @param r      The rank to update the subranks of.
     * @param name   The name of the subrank to add or remove.
     * @param remove True to remove the subrank, false otherwise.
     */
    public void updateRankSubrank(Rank r, String name, boolean remove) {
        if (name.equals(""))
            return;
        YamlConfiguration configRanks = YamlConfiguration.loadConfiguration(configFileRanks);
        List<String> subranks = configRanks.getStringList(r.getName() + ".subranks");
        subranks.remove("");
        if (remove)
            subranks.remove(name);
        else
            subranks.add(name);
        if (subranks.isEmpty())
            subranks.add("");
        configRanks.set(r.getName() + ".subranks", subranks);
        try {
            configRanks.save(configFileRanks);
        } catch (Exception ignored) {
        }
        r.refreshPerms();
        Necessities.getUM().refreshRankPerm(r);
    }

    /**
     * Adds a rank with the specified name with the given previous and next ranks.
     * @param name     The name of the rank to create.
     * @param previous The rank that goes before this new rank.
     * @param next     The rank that goes after this new rank.
     */
    public void addRank(String name, Rank previous, Rank next) {
        if (name.equals(""))
            return;
        YamlConfiguration configRanks = YamlConfiguration.loadConfiguration(configFileRanks);
        configRanks.set(name, Collections.singletonList(""));
        configRanks.set(name + ".permissions", Collections.singletonList(""));
        configRanks.set(name + ".subranks", Collections.singletonList(""));
        configRanks.set(name + ".rankTitle", '[' + name + ']');
        if (previous != null)
            configRanks.set(name + ".previousRank", previous.getName());
        else {
            configRanks.set(name + ".maxHomes", 1);
            configRanks.set(name + ".teleportDelay", 3);
        }
        try {
            configRanks.save(configFileRanks);
        } catch (Exception ignored) {
        }
        if (previous == null) {
            ranks.put(name, new Rank(name));
            if (next != null)
                next.setPrevious(ranks.get(name));
            order.add(0, ranks.get(name));
        } else {
            ranks.put(name, new Rank(name));
            previous.setNext(ranks.get(name));
            if (next != null)
                next.setPrevious(ranks.get(name));
            order.add(order.indexOf(previous) + 1, ranks.get(name));
        }
    }

    /**
     * Removes the specified rank.
     * @param rank The rank to remove.
     */
    public void removeRank(Rank rank) {
        UserManager um = Necessities.getUM();
        Rank previous = rank.getPrevious();
        Rank next = rank.getNext();
        for (User u : um.getUsers().values())
            if (u.getRank().equals(rank)) {
                if (next != null)
                    u.setRank(next);
                else if (previous != null)
                    u.setRank(previous);
            }
        order.remove(rank);
        ranks.remove(rank.getName());
        if (previous != null && next != null) {
            next.setPrevious(previous);
            previous.setNext(next);
        } else if (previous == null && next != null)
            next.setPrevious(null);
        else if (previous != null)
            previous.setNext(null);
        YamlConfiguration configRanks = YamlConfiguration.loadConfiguration(configFileRanks);
        configRanks.set(rank.getName(), null);
        try {
            configRanks.save(configFileRanks);
        } catch (Exception ignored) {
        }
        if (next != null)
            um.refreshRankPerm(next);
        else if (previous != null)
            um.refreshRankPerm(previous);
    }

    /**
     * Adds a subrank with the given name.
     * @param name The name of the subrank to add.
     */
    public void addSubrank(String name) {
        if (name.equals(""))
            return;
        YamlConfiguration configSubranks = YamlConfiguration.loadConfiguration(configFileSubranks);
        configSubranks.set(name, Collections.singletonList(""));
        subranks.put(name.toLowerCase(), name);
        try {
            configSubranks.save(configFileSubranks);
        } catch (Exception ignored) {
        }
    }

    /**
     * Removes the subrank with the specified name.
     * @param name The name of the subrank to remove.
     */
    public void removeSubrank(String name) {
        if (name.equals(""))
            return;
        UserManager um = Necessities.getUM();
        um.getUsers().values().forEach(u -> um.updateUserSubrank(u.getUUID(), name, true));
        order.forEach(r -> updateRankSubrank(r, name, true));
        YamlConfiguration configSubranks = YamlConfiguration.loadConfiguration(configFileSubranks);
        configSubranks.set(name, null);
        subranks.remove(name.toLowerCase());
        try {
            configSubranks.save(configFileSubranks);
        } catch (Exception ignored) {
        }
    }

    /**
     * Sets the default subranks if they do not currently exist.
     */
    public void setSubranks() {
        if (!configFileSubranks.exists())
            try {
                configFileSubranks.createNewFile();
                YamlConfiguration configSubranks = YamlConfiguration.loadConfiguration(configFileSubranks);
                configSubranks.set("Necessities.Guest", Arrays.asList("bukkit.broadcast.user", "bukkit.command.plugins", "bukkit.command.version"));
                configSubranks.set("Necessities.Novice", Arrays.asList("Necessities.afk", "Necessities.me"));
                configSubranks.set("Necessities.Survivalist", Arrays.asList("Necessities.boots", "Necessities.chest", "Necessities.pants", "Necessities.hat"));
                configSubranks.set("Necessities.Expert", Arrays.asList("Necessities.rename", "Necessities.colorchat", "Necessities.title", "Necessities.bracket"));
                configSubranks.set("Necessities.Veteran", Arrays.asList("Necessities.nick", "Necessities.loginmessage", "Necessities.logoutmessage"));
                configSubranks.set("Necessities.Master", Collections.singletonList("Necessities.warn"));
                configSubranks.set("Necessities.Moderator", Arrays.asList("bukkit.command.ban", "bukkit.command.ban.player", "bukkit.command.banlist",
                        "bukkit.command.whitelist.list", "bukkit.command.ban.list", "Necessities.table", "Necessities.spamchat", "Necessities.spamcommands",
                        "bukkit.command.kick", "bukkit.command.unban", "bukkit.command.unban.player", "Necessities.economy.setSign", "Necessities.spy",
                        "Necessities.tpdim", "Necessities.hide", "Necessities.loginmessage", "Necessities.logoutmessage", "Necessities.opBroadcast",
                        "Necessities.seehidden", "Necessities.weather", "Necessities.time", "Necessities.repair", "Necessities.ext", "Necessities.enderchest",
                        "Necessities.invsee", "Necessities.top", "Necessities.jail", "Necessities.mute", "Necessities.homeothers", "Necessities.unignoreable",
                        "Necessities.ban", "Necessities.unban", "Necessities.jump", "Necessities.tp", "Necessities.tppos", "Necessities.tphere",
                        "Necessities.keepxp", "Necessities.fly", "Necessities.enderchestOthers"));
                configSubranks.set("Necessities.Operator", Arrays.asList("Necessities.bracket", "Necessities.bracketOthers", "Necessities.titleOthers",
                        "Necessities.nickOthers", "Necessities.heal", "Necessities.feed", "Necessities.killall", "bukkit.command.clear", "Necessities.janetai",
                        "Necessities.caps", "Necessities.language", "bukkit.command.ban.ip", "bukkit.command.unban.ip", "Necessities.god", "Necessities.more",
                        "Necessities.antiKick", "Necessities.speed", "Necessities.spawnmob", "Necessities.tpall", "Necessities.setjail", "Necessities.unjailable",
                        "Necessities.exp", "Necessities.skull", "Necessities.spawner", "Necessities.unbanip", "Necessities.ignoreGameMode", "Necessities.give",
                        "Necessities.item", "Necessities.clear", "Necessities.banip", "Necessities.guilds.flag", "Necessities.guilds.admin",
                        "Necessities.keepitems", "Necessities.nopvpLoss"));
                configSubranks.set("Necessities.Manager", Arrays.asList("*", "-Necessities.rankmanager.setranksame", "minecraft.command.*", "bukkit.broadcast.*",
                        "-minecraft.command.deop", "bukkit.command.whitelist.*", "bukkit.command.gamerule", "-minecraft.command.op", "-bukkit.command.op"));
                configSubranks.set("Necessities.Admin", Arrays.asList("*", "minecraft.command.gamerule", "bukkit.command.gamerule", "bukkit.command.whitelist.*",
                        "bukkit.broadcast.*"));
                configSubranks.save(configFileSubranks);
            } catch (Exception ignored) {
            }
    }

    /**
     * Sets the default ranks if they do not currently exist.
     */
    public void setRanks() {
        if (!configFileRanks.exists())
            try {
                configFileRanks.createNewFile();
                YamlConfiguration configRanks = YamlConfiguration.loadConfiguration(configFileRanks);
                configRanks.set("Guest.permissions", Collections.singletonList(""));
                configRanks.set("Guest.subranks", Collections.singletonList("Necessities.Guest"));
                configRanks.set("Guest.rankTitle", "&4[&8Guest&4]&7");
                configRanks.set("Guest.maxHomes", 1);
                configRanks.set("Guest.teleportDelay", 3);
                configRanks.set("Novice.permissions", Collections.singletonList(""));
                configRanks.set("Novice.subranks", Collections.singletonList("Necessities.Novice"));
                configRanks.set("Novice.rankTitle", "&4[&3Novice&4]&9");
                configRanks.set("Novice.previousRank", "Guest");
                configRanks.set("Survivalist.permissions", Collections.singletonList(""));
                configRanks.set("Survivalist.subranks", Collections.singletonList("Necessities.Survivalist"));
                configRanks.set("Survivalist.rankTitle", "&4[&1Survivalist&4]&2");
                configRanks.set("Survivalist.previousRank", "Novice");
                configRanks.set("Expert.permissions", Collections.singletonList(""));
                configRanks.set("Expert.subranks", Collections.singletonList("Necessities.Expert"));
                configRanks.set("Expert.rankTitle", "&4[&0Expert&4]&f");
                configRanks.set("Expert.previousRank", "Survivalist");
                configRanks.set("Veteran.permissions", Collections.singletonList(""));
                configRanks.set("Veteran.subranks", Collections.singletonList("Necessities.Veteran"));
                configRanks.set("Veteran.rankTitle", "&4[&5Veteran&4]&d");
                configRanks.set("Veteran.previousRank", "Expert");
                configRanks.set("Master.permissions", Collections.singletonList(""));
                configRanks.set("Master.subranks", Collections.singletonList("Necessities.Master"));
                configRanks.set("Master.rankTitle", "&4[&6Master&4]&e");
                configRanks.set("Master.previousRank", "Veteran");
                configRanks.set("Moderator.permissions", Collections.singletonList(""));
                configRanks.set("Moderator.subranks", Collections.singletonList("Necessities.Moderator"));
                configRanks.set("Moderator.rankTitle", "&4[&2Mod&4]&a");
                configRanks.set("Moderator.previousRank", "Master");
                configRanks.set("Operator.permissions", Collections.singletonList(""));
                configRanks.set("Operator.subranks", Collections.singletonList("Necessities.Operator"));
                configRanks.set("Operator.rankTitle", "&4[&1Op&4]&b");
                configRanks.set("Operator.previousRank", "Moderator");
                configRanks.set("Manager.permissions", Collections.singletonList(""));
                configRanks.set("Manager.subranks", Collections.singletonList("Necessities.Manager"));
                configRanks.set("Manager.rankTitle", "&4[&bManager&4]&6");
                configRanks.set("Manager.previousRank", "Operator");
                configRanks.save(configFileRanks);
            } catch (Exception ignored) {
            }
    }
}