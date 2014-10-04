package com.crossge.necessities.Commands.Guilds;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.crossge.necessities.Economy.Formatter;
import com.crossge.necessities.RankManager.User;

public class CmdPower extends GuildCmd {
	Formatter form = new Formatter();
	
	public boolean commandUse(CommandSender sender, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(!p.hasPermission("Necessities.guilds.power")) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild power.");
				return true;
			}
			if(args.length == 0) {
				User u = um.getUser(p.getUniqueId());
				sender.sendMessage(var.getGuildCol() + "You " + var.getMessages() + " - Power / Maxpower: " + var.getGuildMsgs() + form.roundTwoDecimals(u.getPower())
						+ " / 10.0");
				return true;
			}
		}
		if(args.length == 0) {
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a player to view the power of.");
			return true;
		}
		UUID uuid = get.getID(args[0]);
    	if(uuid == null) {
    		uuid = get.getOfflineID(args[0]);
    		if(uuid == null) {
    			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That player has not joined the server. If the player is offline, please use the full and most recent name.");
	    		return true;
    		}
    	}
    	User u = um.getUser(uuid);
    	if(u.getGuild() == null) {
    		sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That player does not have a guild.");
			return true;
    	}
    	if(!(sender instanceof Player))
    		sender.sendMessage(var.getNeutral() + u.getName() + var.getMessages() + " - Power / Maxpower: " + var.getGuildMsgs() + form.roundTwoDecimals(u.getPower())
    				+ " / 10.0");
    	else
    		sender.sendMessage(u.getGuild().relation(um.getUser(((Player) sender).getUniqueId()).getGuild()) + u.getName() + var.getMessages() +
    				" - Power / Maxpower: " + var.getGuildMsgs() + form.roundTwoDecimals(u.getPower()) + " / 10.00");
		return true;
	}
}