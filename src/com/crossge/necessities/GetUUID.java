package com.crossge.necessities;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class GetUUID {
	private File configFileUUIDs = new File("plugins/Necessities/RankManager", "users.yml");
	private static HashMap<String, UUID> uuids = new HashMap<String, UUID>();
	
	public UUID getID(String name) {
		UUID partial = null;
		boolean startsWith = false;
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			if(p.getName().equalsIgnoreCase(name))
				return p.getUniqueId();
			if(!startsWith && p.getName().toLowerCase().startsWith(name.toLowerCase())) {
				partial = p.getUniqueId();
				startsWith = true;
			}
			if(partial == null && p.getName().toLowerCase().contains(name.toLowerCase()))
				partial = p.getUniqueId();
		}
		return partial;
	}
	
	public void addUUID(UUID uuid) {
		uuids.put(Bukkit.getPlayer(uuid).getName(), uuid);
	}
	
	public UUID getOfflineID(String name) {
		if(uuids.containsKey(name))
			return uuids.get(name);
		return null;
	}
	
	public void initiate() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Retrieving all stored UUIDs.");
		YamlConfiguration configUUIDs = YamlConfiguration.loadConfiguration(configFileUUIDs);
		for(String key : configUUIDs.getKeys(false))
			uuids.put(nameFromString(key), UUID.fromString(key));
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "All stored UUIDs retrieved.");
	}
	
	public boolean hasJoined(UUID uuid) {
		return uuids.containsValue(uuid);
	}
	
	public String nameFromString(String message) {
		UUID uuid = UUID.fromString(message);
		if(Bukkit.getPlayer(uuid) == null) {
			if(Bukkit.getOfflinePlayer(uuid) == null)
				return null; //What did you do this time
			return Bukkit.getOfflinePlayer(uuid).getName(); 
		}
		return Bukkit.getPlayer(uuid).getName();
	}
}