package gg.galaxygaming.necessities.Guilds;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.UserManager;
import gg.galaxygaming.necessities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class GuildManager {
    private final HashMap<String, Guild> guilds = new HashMap<>();
    private final File configFileProtected = new File("plugins/Necessities/Guilds", "Protected.yml");
    private final File configFileGuilds = new File("plugins/Necessities/Guilds", "guilds.yml");

    /**
     * Initiates the guild manager, and loads all guilds from file.
     */
    public void initiate() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Loading all guilds...");
        YamlConfiguration configGuilds = YamlConfiguration.loadConfiguration(configFileGuilds);
        if (configGuilds.contains("guilds"))
            for (String guild : configGuilds.getStringList("guilds"))
                if (guild != null)
                    guilds.put(guild.toLowerCase(), new Guild(guild));
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "All guilds successfully loaded.");
    }

    /**
     * Creates the default guild files.
     */
    public void createFiles() {
        if (!configFileProtected.exists())
            try {
                configFileProtected.createNewFile();
                YamlConfiguration configProtected = YamlConfiguration.loadConfiguration(configFileProtected);
                configProtected.set("power", -1);
                configProtected.set("description", "Server claimed unclaimable zone.");
                configProtected.set("leader", "Janet");
                configProtected.set("mods", Collections.singletonList(""));
                configProtected.set("members", Collections.singletonList(""));
                configProtected.set("allies", Collections.singletonList(""));
                configProtected.set("enemies", Collections.singletonList(""));
                configProtected.set("flag.permanent", true);
                configProtected.set("flag.pvp", false);
                configProtected.set("flag.explosions", false);
                configProtected.set("flag.interact", false);
                configProtected.set("flag.hostileSpawn", false);
                configProtected.set("claims", Collections.singletonList(""));
                configProtected.save(configFileProtected);
            } catch (Exception ignored) {
            }
        if (!configFileGuilds.exists())
            try {
                configFileGuilds.createNewFile();
                YamlConfiguration configGuilds = YamlConfiguration.loadConfiguration(configFileGuilds);
                configGuilds.set("guilds", Collections.singletonList("protected"));
                configGuilds.save(configFileGuilds);
            } catch (Exception ignored) {
            }
    }

    /**
     * Creates a guild with the specified name and the leader with the given uuid.
     * @param name The name of the guild to create.
     * @param uuid The uuid of the leader of the guild to create.
     */
    public void createGuild(String name, UUID uuid) {
        YamlConfiguration configGuilds = YamlConfiguration.loadConfiguration(configFileGuilds);
        File fileGuild = new File("plugins/Necessities/Guilds", name + ".yml");
        YamlConfiguration guild = YamlConfiguration.loadConfiguration(fileGuild);
        List<String> guildList = configGuilds.getStringList("guilds");
        guildList.add(name);
        guildList.remove("");
        configGuilds.set("guilds", guildList);
        try {
            configGuilds.save(configFileGuilds);
            if (!fileGuild.exists())
                fileGuild.createNewFile();
        } catch (Exception ignored) {
        }
        guild.set("power", 0);
        guild.set("description", "Default description.");
        guild.set("leader", uuid.toString());
        guild.set("mods", Collections.singletonList(""));
        guild.set("members", Collections.singletonList(""));
        guild.set("allies", Collections.singletonList(""));
        guild.set("enemies", Collections.singletonList(""));
        guild.set("flag.permanent", false);
        guild.set("flag.pvp", true);
        guild.set("flag.explosions", true);
        guild.set("flag.interact", false);
        guild.set("flag.hostileSpawn", true);
        guild.set("claims", Collections.singletonList(""));
        try {
            guild.save(fileGuild);
        } catch (Exception ignored) {
        }
        guilds.put(name.toLowerCase(), new Guild(name));
    }

    /**
     * Renames a specified guild with the given name.
     * @param g    The guild to rename.
     * @param name The new name of the guild.
     */
    public void renameGuild(Guild g, String name) {
        String oldName = g.getName();
        File fileGuild = new File("plugins/Necessities/Guilds", g.getName() + ".yml");
        fileGuild.renameTo(new File("plugins/Necessities/Guilds", name + ".yml"));
        g.rename(name);
        YamlConfiguration configGuilds = YamlConfiguration.loadConfiguration(configFileGuilds);
        List<String> guildList = configGuilds.getStringList("guilds");
        guildList.remove(oldName);
        guildList.add(name);
        configGuilds.set("guilds", guildList);
        try {
            configGuilds.save(configFileGuilds);
        } catch (Exception ignored) {
        }
        guilds.remove(oldName.toLowerCase());
        guilds.put(name.toLowerCase(), g);
    }

    /**
     * Retrieves the guild with the specified name.
     * @param name The name of the guild to retrieve.
     * @return The guild with the specified name.
     */
    public Guild getGuild(String name) {
        return guilds.get(name.toLowerCase());
    }

    /**
     * Retrieves the guild with the specified name or guild owner.
     * @param name The name of the guild or guild owner to retrieve.
     * @return The guild with the specified name or the guild of the player with the specified name.
     */
    public Guild getGuildVerbose(String name) {
        Guild g = getGuild(name);
        if (g == null) {
            UUID uuid = Utils.getID(name);
            if (uuid == null)
                uuid = Utils.getOfflineID(name);
            if (uuid == null) {
                return null;
            }
            UserManager um = Necessities.getUM();
            if (um.getUser(uuid) != null)
                g = um.getUser(uuid).getGuild();
        }
        return g;
    }

    /**
     * Gets the owner of a given chunk if there is one.
     * @param c The chunk to look for the owner of.
     * @return The guild that owns the specified chunk, or null if there is not one.
     */
    public Guild chunkOwner(Chunk c) {
        return guilds.values().stream().filter(g -> g.claimed(c)).findFirst().orElse(null);
    }

    /**
     * Disbands the given guild.
     * @param g The guild to disband.
     */
    public void disband(Guild g) {
        String name = g.getName();
        for (Map.Entry<String, Guild> stringGuildEntry : guilds.entrySet()) {
            Guild value = stringGuildEntry.getValue();
            if (value.isAlly(g) || value.isEnemy(g))
                value.setNeutral(g);
        }
        g.disband();
        guilds.remove(name.toLowerCase());
        YamlConfiguration configGuilds = YamlConfiguration.loadConfiguration(configFileGuilds);
        List<String> guildList = configGuilds.getStringList("guilds");
        guildList.remove(name);
        if (guildList.isEmpty())
            guildList.add("");
        configGuilds.set("guilds", guildList);
        try {
            configGuilds.save(configFileGuilds);
        } catch (Exception ignored) {
        }
    }

    /**
     * Gets the guild prefix for the specified guild rank.
     * @param rank The rank either, leader, mod, or the empty string.
     * @return The prefix that corresponds to the given rank.
     */
    public String getPrefix(String rank) {
        return rank.equalsIgnoreCase("leader") ? "#" : rank.equalsIgnoreCase("mod") ? "*" : "";
    }

    /**
     * Gets the list of guilds.
     * @return The list of guilds.
     */
    public Collection<Guild> getGuilds() {
        return guilds.values();
    }
}