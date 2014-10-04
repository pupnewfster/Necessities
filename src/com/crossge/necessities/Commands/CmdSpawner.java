package com.crossge.necessities.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.block.CreatureSpawner;

import com.crossge.necessities.RankManager.User;

public class CmdSpawner extends Cmd {
	public boolean commandUse(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			User u = um.getUser(p.getUniqueId());
			Location l = u.getLookingAt();
			if(l == null) {
				p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Block out of range.");
				return true;
			}
			if(l.getBlock().getState() instanceof CreatureSpawner) {
				CreatureSpawner spawner = (CreatureSpawner) l.getBlock().getState();
				if(args.length == 0) {
					p.sendMessage(var.getMessages() + "Spawner type is " + spawner.getSpawnedType().toString().replaceAll("_", " ").toLowerCase() + ".");
					return true;
				}
				EntityType spawnertype = getType(args[0]);
				if(spawnertype == null) {
					p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid mob type.");
					p.sendMessage(var.getMessages() + "Valid mob types: " + ChatColor.WHITE + validTypes());
					return true;
				}
				spawner.setSpawnedType(spawnertype);
				p.sendMessage(var.getMessages() + "Spawner type set to " + var.getObj() + spawner.getSpawnedType().toString().replaceAll("_", " ").toLowerCase() +
						var.getMessages() + ".");
				return true;
			}
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That block is not a spawner.");
			return true;
        } else
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You cannot change the type of spawner because you cannot look at the spawner.");
		return true;
	}
	
	private EntityType getType(String message) {
		message = message.replaceAll("_", "").toUpperCase();
		for(EntityType type : EntityType.values())
			if(type.isSpawnable()) {
				String name = type.toString();
				if(message.equals(name.replaceAll("_", "")))
					return type;
			}
		return null;
	}
	
	private String validTypes() {
		String types = "";
		for(EntityType type : EntityType.values())
			if(type.isSpawnable())
				types += type.toString().replaceAll("_", " ").toLowerCase() + ", ";
		return types.trim().substring(0, types.length() - 2);
	}
}