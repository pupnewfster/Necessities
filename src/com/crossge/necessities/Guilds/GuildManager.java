package com.crossge.necessities.Guilds;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.YamlConfiguration;

public class GuildManager {
	private static HashMap<String, Guild> guilds = new HashMap<String, Guild>();
	private File configFileProtected = new File("plugins/Necessities/Guilds", "Protected.yml");
	private File configFileGuilds = new File("plugins/Necessities/Guilds", "guilds.yml");
	
	public void initiate() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Loading all guillds...");
		YamlConfiguration configGuilds = YamlConfiguration.loadConfiguration(configFileGuilds);
		if(configGuilds.contains("guilds"))
			for(String guild : configGuilds.getStringList("guilds"))
				guilds.put(guild.toLowerCase(), new Guild(guild));
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "All guilds successfully loaded.");
	}
	
	public void createFiles() {
		if(!configFileProtected.exists())
			try {
				configFileProtected.createNewFile();
				YamlConfiguration configProtected = YamlConfiguration.loadConfiguration(configFileProtected);
				configProtected.set("power", -1);
				configProtected.set("description", "Server claimed unclaimable zone.");
				configProtected.set("leader", "Janet");
				configProtected.set("mods", Arrays.asList(""));
				configProtected.set("members", Arrays.asList(""));
				configProtected.set("allies", Arrays.asList(""));
				configProtected.set("enemies", Arrays.asList(""));
				configProtected.set("flag.permanent", true);
				configProtected.set("flag.pvp", false);
				configProtected.set("flag.explosions", false);
				configProtected.set("claims", Arrays.asList(""));
				configProtected.save(configFileProtected);
			} catch (Exception e){}
		if(!configFileGuilds.exists())
			try {
				configFileGuilds.createNewFile();
				YamlConfiguration configGuilds = YamlConfiguration.loadConfiguration(configFileGuilds);
				configGuilds.set("guilds", Arrays.asList("protected"));
				configGuilds.save(configFileGuilds);
			} catch (Exception e){}
	}
	
	public void createGuild(String name, UUID uuid) {
		YamlConfiguration configGuilds = YamlConfiguration.loadConfiguration(configFileGuilds);
		File fileGuild = new File("plugins/Necessities/Guilds", name + ".yml");
		YamlConfiguration guild = YamlConfiguration.loadConfiguration(fileGuild);
		List<String> guildList = configGuilds.getStringList("guilds");
		guildList.add(name);
		if(guildList.contains(""))
			guildList.remove("");
		configGuilds.set("guilds", guildList);
		try {
			configGuilds.save(configFileGuilds);
			if(!fileGuild.exists())
				fileGuild.createNewFile();
		} catch (Exception e){}
		guild.set("power", 0);
		guild.set("description", "Default description.");
		guild.set("leader", uuid.toString());
		guild.set("mods", Arrays.asList(""));
		guild.set("members", Arrays.asList(""));
		guild.set("allies", Arrays.asList(""));
		guild.set("enemies", Arrays.asList(""));
		guild.set("flag.permanent", false);
		guild.set("flag.pvp", true);
		guild.set("flag.explosions", true);
		guild.set("claims", Arrays.asList(""));
		try {
			guild.save(fileGuild);
		} catch (Exception e){}
		guilds.put(name.toLowerCase(), new Guild(name));
	}
	
	public Guild getGuild(String name) {
		return guilds.get(name.toLowerCase());
	}
	
	public Guild chunkOwner(Chunk c) {
		for(Guild g : guilds.values())
			if(g.claimed(c))
				return g;
		return null;
	}
	
	public void disband(Guild g) {
		String name = g.getName();
		for(String key : guilds.keySet())
			if(guilds.get(key).isAlly(g) || guilds.get(key).isEnemy(g))
				guilds.get(key).setNeutral(g);
		g.disband();
		guilds.remove(name.toLowerCase());
		YamlConfiguration configGuilds = YamlConfiguration.loadConfiguration(configFileGuilds);
		List<String> guildList = configGuilds.getStringList("guilds");
		guildList.remove(name);
		if(guildList.isEmpty())
			guildList.add("");
		configGuilds.set("guilds", guildList);
		try {
			configGuilds.save(configFileGuilds);
		} catch(Exception e) {}
	}
	
	public String getPrefix(String rank) {
		if(rank.equalsIgnoreCase("leader"))
			return "#";
		if(rank.equalsIgnoreCase("mod"))
			return "*";
		return "";
	}

	public HashMap<String,Guild> getGuilds() {
		return guilds;
	}
}