package com.crossge.necessities.Commands;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CmdSpawn extends Cmd {
	private File configFile = new File("plugins/Necessities", "config.yml");
	
	public boolean commandUse(CommandSender sender, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
			if(!config.contains("Spawn")) {
				p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Spawn not set.");
				return true;
			}
			World world = Bukkit.getWorld(config.getString("Spawn.world"));
			double x = Double.parseDouble(config.getString("Spawn.x"));
			double y = Double.parseDouble(config.getString("Spawn.y"));
			double z = Double.parseDouble(config.getString("Spawn.z"));
			float yaw = Float.parseFloat(config.getString("Spawn.yaw"));
			float pitch = Float.parseFloat(config.getString("Spawn.pitch"));
			p.sendMessage(var.getMessages() + "Teleporting to spawn.");
			um.getUser(p.getUniqueId()).teleport(safe.getSafe(new Location(world, x, y, z, yaw, pitch)));
		} else
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot go to the spawn.");
		return true;
	}
}