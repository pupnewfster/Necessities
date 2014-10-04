package com.crossge.necessities.Commands;

import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import com.crossge.necessities.RankManager.User;

public class CmdGod extends Cmd {	
	public boolean commandUse(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 0) {
				User u = um.getUser(p.getUniqueId());
				if(u.godmode()) {
					p.sendMessage(var.getMessages() + "God mode " + var.getObj() + "disabled" + var.getMessages() + ".");
				} else {
					p.setHealth(20);
					p.setFoodLevel(20);
					for(PotionEffect potion : p.getActivePotionEffects())
						p.removePotionEffect(potion.getType());
					p.sendMessage(var.getMessages() + "God mode " + var.getObj() + "enabled" + var.getMessages() + ".");
				}
				u.setGod(!u.godmode());
				return true;
			}
		} else if(args.length == 0) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot become a god.");
				return true;
		}
		UUID uuid = get.getID(args[0]);
		if(uuid == null) {
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
			return true;
		}
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(!p.hasPermission("Necessities.godOthers"))
	    		uuid = p.getUniqueId();
		}
		Player target = sender.getServer().getPlayer(uuid);
		User u = um.getUser(uuid);
		if(u.godmode()) {
			target.sendMessage(var.getMessages() + "God mode " + var.getObj() + "disabled" + var.getMessages() + ".");
			sender.sendMessage(var.getMessages() + "God mode " + var.getObj() + "disabled" + var.getMessages() + " for " + var.getObj() + target.getDisplayName());
		} else {
			target.setHealth(20);
			target.setFoodLevel(20);
			for(PotionEffect potion : target.getActivePotionEffects())
				target.removePotionEffect(potion.getType());
			target.sendMessage(var.getMessages() + "God mode " + var.getObj() + "enabled" + var.getMessages() + ".");
			sender.sendMessage(var.getMessages() + "God mode " + var.getObj() + "enabled" + var.getMessages() + " for " + var.getObj() + target.getDisplayName());
		}
		u.setGod(!u.godmode());
		return true;
	}
}