package com.crossge.necessities.Commands.RankManager;

import java.util.UUID;
import org.bukkit.command.CommandSender;

public class CmdAddSubrankUser extends RankCmd {	
	public boolean commandUse(CommandSender sender, String[] args) {
		if (args.length != 2) {
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires a user and a subrank to add for that user.");
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
    	String subrank = args[1];
    	if(rm.validSubrank(subrank)) {
    		sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That subrank does not exist");
    		return true;
    	}
    	subrank = rm.getSub(subrank);
		um.updateUserSubrank(uuid, subrank, false);
		sender.sendMessage(var.getMessages() + "Added " + var.getObj() + subrank + var.getMessages() + " to " + var.getObj() +
				plural(get.nameFromString(uuid.toString())) + var.getMessages() + " subranks.");
		return true;
	}
	
	private String plural(String name) {
		if(name.endsWith("s"))
			return name + "'";
		return name + "'s";
	}
}