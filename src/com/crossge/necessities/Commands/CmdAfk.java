package com.crossge.necessities.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.crossge.necessities.RankManager.User;

public class CmdAfk extends Cmd {	
	public boolean commandUse(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			User u = um.getUser(p.getUniqueId());
			if(!u.isAfk())
				p.sendMessage(var.getMessages() + "You are now afk.");
			else
				p.sendMessage(var.getMessages() + "You are no longer afk.");
			u.setAfk(!u.isAfk());
		} else
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot go afk.");
		return true;
	}
}