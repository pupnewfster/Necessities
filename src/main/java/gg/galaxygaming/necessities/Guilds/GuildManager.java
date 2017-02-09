package gg.galaxygaming.necessities.Guilds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GuildManager {
    private final HashMap<String, Guild> guilds = new HashMap<>();
    private final File configFileProtected = new File("plugins/Necessities/Guilds", "Protected.yml");
    private final File configFileGuilds = new File("plugins/Necessities/Guilds", "guilds.yml");

    public void initiate() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Loading all guilds...");
        YamlConfiguration configGuilds = YamlConfiguration.loadConfiguration(configFileGuilds);
        if (configGuilds.contains("guilds"))
            for (String guild : configGuilds.getStringList("guilds"))
                if (guild != null)
                    guilds.put(guild.toLowerCase(), new Guild(guild));
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "All guilds successfully loaded.");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void createGuild(String name, UUID uuid) {
        YamlConfiguration configGuilds = YamlConfiguration.loadConfiguration(configFileGuilds);
        File fileGuild = new File("plugins/Necessities/Guilds", name + ".yml");
        YamlConfiguration guild = YamlConfiguration.loadConfiguration(fileGuild);
        List<String> guildList = configGuilds.getStringList("guilds");
        guildList.add(name);
        if (guildList.contains(""))
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

    @SuppressWarnings("ResultOfMethodCallIgnored")
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

    public Guild getGuild(String name) {
        return guilds.get(name.toLowerCase());
    }

    public Guild chunkOwner(Chunk c) {
        for (Guild g : guilds.values())
            if (g.claimed(c))
                return g;
        return null;
    }

    public void disband(Guild g) {
        String name = g.getName();
        for (String key : guilds.keySet())
            if (guilds.get(key).isAlly(g) || guilds.get(key).isEnemy(g))
                guilds.get(key).setNeutral(g);
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

    public String getPrefix(String rank) {
        return rank.equalsIgnoreCase("leader") ? "#" : rank.equalsIgnoreCase("mod") ? "*" : "";
    }

    public HashMap<String, Guild> getGuilds() {
        return guilds;
    }
}