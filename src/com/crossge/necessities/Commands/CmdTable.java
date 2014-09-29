package com.crossge.necessities.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdTable extends Cmd {
	public boolean commandUse(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			((Player) sender).openEnchanting(null, true);
			sender.sendMessage(var.getMessages() + "Enchanting table opened.");
		}
		else
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You can not open an enchanting table because you are not a player.");
		return true;
	}
}