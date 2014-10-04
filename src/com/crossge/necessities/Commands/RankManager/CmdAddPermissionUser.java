package com.crossge.necessities.Commands.RankManager;

import java.util.UUID;
import org.bukkit.command.CommandSender;

public class CmdAddPermissionUser extends RankCmd {	
	public boolean commandUse(CommandSender sender, String[] args) {
		if (args.length != 2) {
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires a user and a permission node to add for that user.");
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
    	String node = args[1];
		um.updateUserPerms(uuid, node, false);
		sender.sendMessage(var.getMessages() + "Added " + var.getObj() + node + var.getMessages() + " to " + var.getObj() + plural(get.nameFromString(uuid.toString()))
				+ var.getMessages() + " permissions.");
		return true;
	}
	
	private String plural(String name) {
		if(name.endsWith("s"))
			return name + "'";
		return name + "'s";
	}
}