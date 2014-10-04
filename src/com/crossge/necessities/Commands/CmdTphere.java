package com.crossge.necessities.Commands;

import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.crossge.necessities.Economy.Formatter;

public class CmdTphere extends Cmd {
	Formatter form = new Formatter();
	CmdHide hide = new CmdHide();
	
	public boolean commandUse(CommandSender sender, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you enter a player to summon.");
			return true;
		}
		if(sender instanceof Player) {
			Player p = (Player) sender;
			UUID uuid = get.getID(args[0]);
			if(uuid == null) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
				return true;
			}
			Player target = sender.getServer().getPlayer(uuid);
			if(!p.hasPermission("Necessities.seehidden") && hide.isHidden(target)) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
				return true;
			}
			target.teleport(safe.getSafe(p.getLocation()));
			p.sendMessage(var.getMessages() + "Teleporting...");
			target.sendMessage(var.getMessages() + "Teleporting...");
		} else
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a player to teleport someone to yourself.");
		return true;
	}
}