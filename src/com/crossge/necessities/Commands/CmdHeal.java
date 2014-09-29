package com.crossge.necessities.Commands;

import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class CmdHeal extends Cmd {	
	public boolean commandUse(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 0) {
				p.setHealth(20);
				p.setFoodLevel(20);
				for(PotionEffect potion : p.getActivePotionEffects())
					p.removePotionEffect(potion.getType());
				p.setSaturation(5);
				p.sendMessage(var.getMessages() + "You have been healed.");
				return true;
			}
		} else if(args.length == 0) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot be healed.");
				return true;
		}
		UUID uuid = get.getID(args[0]);
		if(uuid == null) {
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
			return true;
		}
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(!p.hasPermission("Necessities.healOthers"))
	    		uuid = p.getUniqueId();
		}
		Player target = sender.getServer().getPlayer(uuid);
		target.setHealth(20);
		target.setFoodLevel(20);
		for(PotionEffect potion : target.getActivePotionEffects())
			target.removePotionEffect(potion.getType());
		target.setSaturation(5);
		target.sendMessage(var.getMessages() + "You have been healed.");
		sender.sendMessage(var.getMessages() + "Healed player: " + var.getObj() + target.getDisplayName());
		return true;
	}
}