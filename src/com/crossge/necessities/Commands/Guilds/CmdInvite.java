package com.crossge.necessities.Commands.Guilds;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.crossge.necessities.RankManager.User;

public class CmdInvite extends GuildCmd {
	public boolean commandUse(CommandSender sender, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(!p.hasPermission("Necessities.guilds.invite")) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild invite.");
				return true;
			}
			User u = um.getUser(p.getUniqueId());
			if(u.getGuild() == null || u.getGuild().getRank(p.getUniqueId()) == null || u.getGuild().getRank(p.getUniqueId()).equalsIgnoreCase("member")) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a mod or higher in your guild to invite members.");
				return true;
			}
			if(args.length == 0) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a player to invite to your guild.");
				return true;
			}
			UUID uuid = get.getID(args[0]);
	    	if(uuid == null) {
	    		sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
		    	return true;
	    	}
			User them = um.getUser(uuid);
			if(u.getGuild().equals(them.getGuild())) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "They are already a member of your guild.");
				return true;
			}
			if(u.getGuild().isInvited(uuid)) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "This player has already been invited to join your guild.");
				return true;
			}
			u.getGuild().invite(uuid);
			Bukkit.getPlayer(uuid).sendMessage(var.getMessages() + "You have been invited to join " + var.getObj() + u.getGuild().getName());
			Bukkit.getPlayer(uuid).sendMessage(var.getMessages() + "Type /guild join " + var.getObj() + u.getGuild().getName() + var.getMessages() + " to join.");
			sender.sendMessage(var.getMessages() + "Successfully invited " + var.getObj() + them.getName() + var.getMessages() + " to your guild.");
		} else
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a player to invite someone to a guild.");
		return true;
	}
}