package com.crossge.necessities.Commands;

import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdFly extends Cmd {
	public boolean commandUse(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 0) {
				if(p.getAllowFlight()) {
					p.setAllowFlight(false);//are both needed?
					p.setFlying(false);
					p.sendMessage(var.getMessages() + "Fly " + var.getObj() + "disabled" + var.getMessages() + ".");
				} else {
					p.setAllowFlight(true);
					p.sendMessage(var.getMessages() + "Fly " + var.getObj() + "enabled" + var.getMessages() + ".");
				}
				return true;
			}
		} else if(args.length == 0) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot fly.");
				return true;
		}
		UUID uuid = get.getID(args[0]);
		if(uuid == null) {
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
			return true;
		}
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(!p.hasPermission("Necessities.flyOthers"))
	    		uuid = p.getUniqueId();
		}
		Player target = sender.getServer().getPlayer(uuid);
		if(target.getAllowFlight()) {
			target.setAllowFlight(false);
			target.setFlying(false);
			target.sendMessage(var.getMessages() + "Fly " + var.getObj() + "disabled" + var.getMessages() + ".");
			sender.sendMessage(var.getMessages() + "Fly " + var.getObj() + "disabled" + var.getMessages() + " for " + var.getObj() + target.getDisplayName());
		} else {
			target.setAllowFlight(true);
			target.sendMessage(var.getMessages() + "Fly " + var.getObj() + "enabled" + var.getMessages() + ".");
			sender.sendMessage(var.getMessages() + "Fly " + var.getObj() + "enabled" + var.getMessages() + " for " + var.getObj() + target.getDisplayName());
		}
		return true;
	}
}