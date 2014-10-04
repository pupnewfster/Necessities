package com.crossge.necessities.WorldManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

public class WarpManager {
	private File configFileWarps = new File("plugins/Necessities/WorldManager", "warps.yml");
	private static HashMap<String,Warp> warps = new HashMap<String,Warp>();
	private static HashMap<String,String> lowerNames = new HashMap<String,String>();
	
	public void initiate() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Loading warps...");
		YamlConfiguration configWarps = YamlConfiguration.loadConfiguration(configFileWarps);
		for(String warp : configWarps.getKeys(false)) {
			warps.put(warp, new Warp(warp));
			lowerNames.put(warp.toLowerCase(), warp);
		}
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "All warps loaded.");
	}
	
	public boolean isWarp(String name) {
		return lowerNames.containsKey(name.toLowerCase());
	}
	
	public Warp getWarp(String name) {
		if(!isWarp(name))
			return null;
		return warps.get(lowerNames.get(name.toLowerCase()));
	}
	
	public String getWarps() {
		ArrayList<String> ws = new ArrayList<String>();
		for(String w : warps.keySet())
			ws.add(w);
		Collections.sort(ws);
		String wrps = "";
		for(String w : ws)
			wrps += w + ", ";
		if(wrps.equals(""))
			return "";
		return wrps.trim().substring(0, wrps.length() - 2);
	}
	
	public void remove(String name) {
		YamlConfiguration configWarps = YamlConfiguration.loadConfiguration(configFileWarps);
		configWarps.set(name, null);
		try {
			configWarps.save(configFileWarps);
		} catch (Exception e) {}
		warps.remove(name);
		lowerNames.remove(name.toLowerCase());
	}
	
	public void create(String name, Location loc) {
		YamlConfiguration configWarps = YamlConfiguration.loadConfiguration(configFileWarps);
		configWarps.set(name + ".world", loc.getWorld().getName());
		configWarps.set(name + ".x", loc.getX());
		configWarps.set(name + ".y", loc.getY());
		configWarps.set(name + ".z", loc.getZ());
		configWarps.set(name + ".yaw", loc.getYaw());
		configWarps.set(name + ".pitch", loc.getPitch());
		try {
			configWarps.save(configFileWarps);
		} catch (Exception e) {}
		warps.put(name, new Warp(name));
		lowerNames.put(name.toLowerCase(), name);
	}
}