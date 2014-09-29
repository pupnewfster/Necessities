package com.crossge.necessities.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.crossge.necessities.Economy.Formatter;

public class CmdTpall extends Cmd {
	Formatter form = new Formatter();
	CmdHide hide = new CmdHide();
	
	public boolean commandUse(CommandSender sender, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			for(Player t : Bukkit.getOnlinePlayers())
				if(p.hasPermission("Necessities.seehidden") || !hide.isHidden(t)) {
					t.teleport(safe.getSafe(p.getLocation()));
					t.sendMessage(var.getMessages() + "Teleporting...");
				}
			p.sendMessage(var.getMessages() + "Teleporting all players...");
		} else
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a player to teleport everyone to yourself.");
		return true;
	}
}