package com.crossge.necessities.Commands;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdHighfive extends Cmd {
	public boolean commandUse(CommandSender sender, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a player to highfive.");
			return true;
		}
		if(sender instanceof Player && args[0].equalsIgnoreCase("Console")) {
			Player player = (Player) sender;
			Bukkit.getConsoleSender().sendMessage(var.getObj() + player.getName() + var.getMessages() + " just highfived you.");
			Bukkit.broadcastMessage(var.getMessages() + player.getName() + " just highfived " + console.getName().replaceAll(":", ""));
			return true;
		}
		UUID uuid = get.getID(args[0]);
		if(uuid == null) {
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid player.");
			return true;
		}
		Player target = sender.getServer().getPlayer(uuid);
		if (sender instanceof Player) {
			Player player = (Player) sender;
			target.sendMessage(var.getObj() + player.getName() + var.getMessages() + " just highfived you.");
			Bukkit.broadcastMessage(var.getMessages() + player.getName() + " just highfived " + target.getName());
		} else {
			target.sendMessage(var.getObj() + console.getName().replaceAll(":", "") + var.getMessages() + " just highfived you.");
			Bukkit.broadcastMessage(var.getMessages() + console.getName().replaceAll(":", "") + " just highfived " + var.getObj() + target.getName());
		}
		return true;
	}
}