package com.crossge.necessities.Commands;

import java.io.File;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CmdSetspawn extends Cmd {
	private File configFile = new File("plugins/Necessities", "config.yml");
	
	public boolean commandUse(CommandSender sender, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
			config.set("Spawn.world", p.getWorld().getName());
			config.set("Spawn.x", Double.toString(p.getLocation().getX()));
			config.set("Spawn.y", Double.toString(p.getLocation().getY()));
			config.set("Spawn.z", Double.toString(p.getLocation().getZ()));
			config.set("Spawn.yaw", Float.toString(p.getLocation().getYaw()));
			config.set("Spawn.pitch", Float.toString(p.getLocation().getPitch()));
			try {
				config.save(configFile);
			} catch (Exception e){}
			p.sendMessage(var.getMessages() + "Spawn set.");
		} else
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot set the spawn.");
		return true;
	}
}