package com.crossge.necessities.Commands.WorldManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.crossge.necessities.RankManager.User;

public class CmdCreatePortal extends WorldCmd {
	public boolean commandUse(CommandSender sender, String[] args) {
		if(args.length < 2) {
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you enter a name for the portal and a world that it takes you to.");
			return true;
		}
		if(sender instanceof Player) {
			if(pm.exists(args[0])) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That portal already exists.");
				return true;
			}
			User u = um.getUser(((Player) sender).getUniqueId());
			Location left = u.getLeft();
			Location right = u.getRight();
			if(!left.getWorld().equals(right.getWorld())) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Please select left and right corners of portal.");
				return true;
			}
			if(!wm.worldExists(args[1])) {
				if(args[1].startsWith("-") && warps.isWarp(args[1].replaceFirst("-", ""))) {
					pm.create(args[0], args[1], left, right);//TODO messages below
					sender.sendMessage(var.getMessages() + "Created portal " + var.getObj() + args[0] + var.getMessages() + " from " + var.getObj() +
							left.getWorld().getName() + var.getMessages() + " to the warp " + var.getObj() + args[1].replaceFirst("-", "") + var.getMessages() + ".");
					sender.sendMessage(var.getMessages() + "This portal is between " + var.getObj() + left.getBlockX() + var.getMessages() + ", " + var.getObj() +
						left.getBlockY() + var.getMessages() + ", " + var.getObj() + left.getBlockZ() + var.getMessages() + " and " + var.getObj() + right.getBlockX() +
						var.getMessages() + ", " + var.getObj() + right.getBlockY() + var.getMessages() + ", " + var.getObj() + right.getBlockZ() + var.getMessages() + ".");
					return true;
				}
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Destination world or warp is invalid.");
				return true;
			}
			pm.create(args[0], Bukkit.getWorld(args[1]).getName(), left, right);
			sender.sendMessage(var.getMessages() + "Created portal " + var.getObj() + args[0] + var.getMessages() + " from " + var.getObj() +
					left.getWorld().getName() + var.getMessages() + " and " + var.getObj() + Bukkit.getWorld(args[1]).getName() + var.getMessages() + ".");
			sender.sendMessage(var.getMessages() + "This portal is between " + var.getObj() + left.getBlockX() + var.getMessages() + ", " + var.getObj() +
				left.getBlockY() + var.getMessages() + ", " + var.getObj() + left.getBlockZ() + var.getMessages() + " and " + var.getObj() + right.getBlockX() +
				var.getMessages() + ", " + var.getObj() + right.getBlockY() + var.getMessages() + ", " + var.getObj() + right.getBlockZ() + var.getMessages() + ".");
		} else
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Login to perform this command because you have to click two locations.");
		return true;
	}
}