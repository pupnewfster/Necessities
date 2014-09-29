package com.crossge.necessities.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CmdEnderChest extends Cmd {
	public boolean commandUse(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			Inventory ender = p.getEnderChest();
			if(args.length == 0) {
				p.sendMessage(var.getMessages() + "Enderchest opened.");
			} else {
				UUID uuid = get.getID(args[0]);
		    	if(uuid == null) {
		    		sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
			    	return true;
		    	}
		    	if(!p.hasPermission("Necessities.enderchestOthers"))
		    		uuid = p.getUniqueId();
		    	ender = Bukkit.getPlayer(uuid).getEnderChest();
		    	p.sendMessage(var.getObj() + plural(Bukkit.getPlayer(uuid).getName()) + var.getMessages() + " enchest opened.");
			}
			p.openInventory(ender);
        } else
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have an inventory.");
		return true;
	}
	
	private String plural(String message) {
		if(message.endsWith("s"))
			return message + "'";
		return message + "'s";
	}
	
	public List<String> tabComplete(CommandSender sender, String[] args) {
    	List<String> complete = new ArrayList<String>();
    	String search = "";
    	if(args.length == 1)
    		search = args[0];
    	if(sender instanceof Player)
        	for(Player p : Bukkit.getOnlinePlayers())
        		if(p.getName().startsWith(search) && ((Player) sender).canSee(p))
        			complete.add(p.getName());
		return complete;
    }
}