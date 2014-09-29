package com.crossge.necessities.Commands.Guilds;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.crossge.necessities.RankManager.User;

public class CmdAutoclaim extends GuildCmd {
	public boolean commandUse(CommandSender sender, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(!p.hasPermission("Necessities.guilds.autoclaim")) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild autoclaim.");
				return true;
			}
			User u = um.getUser(p.getUniqueId());
			if(u.isClaiming()) {
				u.setClaiming(false);
				sender.sendMessage(var.getMessages() + "No longer automatically claiming land.");
			} else {
				u.setClaiming(true);
				sender.sendMessage(var.getMessages() + "Now automatically claiming land.");
			}
		} else
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be logged in to claim land.");
		return true;
	}
}