package com.crossge.necessities.Commands.RankManager;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.crossge.necessities.Economy.Formatter;
import com.crossge.necessities.RankManager.*;

public class CmdSetrank extends RankCmd {
	Formatter form = new Formatter();
	
	public boolean commandUse(CommandSender sender, String[] args) {
		if (args.length != 2) {
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you enter a user and a rank to set the user's rank to.");
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
    	Rank r = rm.getRank(form.capFirst(args[1]));
    	if(r == null) {
    		sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That rank does not exist");
    		return true;
    	}
    	String name = "Console";
		if (sender instanceof Player) {
			Player player = (Player) sender;
	    	if(!player.hasPermission("Necessities.rankmanager.setranksame") && rm.hasRank(um.getUser(player.getUniqueId()).getRank(), r)) {
	    		player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You may not change the rank of someone higher than you.");
	    		return true;
	    	}
	    	name = player.getName();
		}
		um.updateUserRank(u, uuid, r);
		Bukkit.broadcastMessage(var.getMessages() + name + " set " + plural(get.nameFromString(uuid.toString())) + " rank to " + u.getRank().getName() + ".");
		return true;
	}
	
	private String plural(String name) {
		if(name.endsWith("s"))
			return name + "'";
		return name + "'s";
	}
}