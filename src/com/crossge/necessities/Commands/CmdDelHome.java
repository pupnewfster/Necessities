package com.crossge.necessities.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.crossge.necessities.RankManager.User;

public class CmdDelHome extends Cmd {
	public boolean commandUse(CommandSender sender, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			User u = um.getUser(p.getUniqueId());
			String name = "home";
			if(args.length > 0) {
				if(args[0].contains(":") && p.hasPermission("Necessities.homeothers")) {
					String[] info = args[0].replaceAll("&", "").replaceAll("\\.", "").split(":");
					String targetName = info[0];
					UUID uuid = get.getID(targetName);
			    	if(uuid == null) {
			    		uuid = get.getOfflineID(targetName);
			    		if(uuid == null) {
			    			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That player has not joined the server. If the player is offline, please use the full and most recent name.");
				    		return true;
			    		}
			    	}
			    	User us = um.getUser(uuid);
			    	if(info.length == 1 || !us.hasHome(info[1])) {
			    		p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "They do not have a home under that name.");
						return true;
					}
			    	if(rm.hasRank(us.getRank(), u.getRank())) {
			    		p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You can not delete same rank or higher homes.");
						return true;
			    	}
					us.delHome(info[1]);
					p.sendMessage(var.getMessages() + "Home " + var.getObj() + info[1] + var.getMessages() + " has been removed from " + var.getObj() + us.getName()
							+ var.getMessages() + " list of homes.");
					return true;
				}
				name = args[0].replaceAll("&", "").replaceAll("\\.", "").replaceAll(":", "");
			}
			if(!u.hasHome(name)) {
				p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have a home under that name.");
				return true;
			}
			u.delHome(name);
			p.sendMessage(var.getMessages() + "Home " + var.getObj() + name + var.getMessages() + " has been removed.");
		} else
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot delete any homes.");
		return true;
	}
	
	public List<String> tabComplete(CommandSender sender, String[] args) {
    	List<String> complete = new ArrayList<String>();
    	String search = "";
    	if(args.length == 1)
    		search = args[0];
		if(sender instanceof Player) {
			User u = um.getUser(((Player) sender).getUniqueId());
    		for(String home : u.getHomes().split(", "))
    			if(home.startsWith(search))
    				complete.add(home);
		}
		return complete;
    }
}