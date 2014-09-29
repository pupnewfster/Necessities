package com.crossge.necessities.WorldManager;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

public class Warp {
	private File configFileWarps = new File("plugins/Necessities/WorldManager", "warps.yml");
	private Location loc = null;
	private String name = "";
	
	public Warp(String name) {
		YamlConfiguration configWarps = YamlConfiguration.loadConfiguration(configFileWarps);
		this.name = name;
		if(configWarps.contains(this.name))	
			loc = new Location(Bukkit.getWorld(configWarps.getString(this.name + ".world")), Double.parseDouble(configWarps.getString(this.name + ".x")),
					Double.parseDouble(configWarps.getString(this.name + ".y")), Double.parseDouble(configWarps.getString(this.name + ".z")),
					Float.parseFloat(configWarps.getString(this.name + ".yaw")), Float.parseFloat(configWarps.getString(this.name + ".pitch")));
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean hasDestination() {
		return loc != null;
	}
	
	public Location getDestination() {
		return loc;
	}
}