package com.crossge.necessities.Commands;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class CmdInvsee extends Cmd {
	private static HashMap<PlayerInventory, Player> openInvs = new HashMap<PlayerInventory, Player>();
	
	public Player getFromInv(PlayerInventory pinv) {
		return openInvs.get(pinv);
	}
	
	public void closeInv(PlayerInventory pinv) {
		openInvs.remove(pinv);
	}
	
	public boolean commandUse(CommandSender sender, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Must enter a player to view inventory of.");
			return true;
		}
		if (sender instanceof Player) {
			Player p = (Player) sender;
			UUID uuid = get.getID(args[0]);
		    if(uuid == null) {
		    	sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
			   	return true;
		    }
		    Player target = Bukkit.getPlayer(uuid);
		    if(target.equals(p)) {
		    	sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Must enter a player other than yourself.");
				return true;
		    }
		    PlayerInventory inv = target.getInventory();
		    openInvs.put(inv, target);
		    p.sendMessage(var.getObj() + plural(target.getName()) + var.getMessages() + " inventory opened.");
			p.openInventory(inv);
        } else
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have an inventory.");
		return true;
	}
	
	private String plural(String message) {
		if(message.endsWith("s"))
			return message + "'";
		return message + "'s";
	}
}