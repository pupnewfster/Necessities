package com.crossge.necessities.Commands.Guilds;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.crossge.necessities.Guilds.Guild;
import com.crossge.necessities.RankManager.User;

public class CmdNeutral extends GuildCmd {
	public boolean commandUse(CommandSender sender, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(!p.hasPermission("Necessities.guilds.neutral")) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild neutral.");
				return true;
			}
			if(args.length == 0) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Must enter a name for the guild you wish to become neutral with.");
				return true;
			}
			Guild g = gm.getGuild(args[0]);
			if(g == null) {
				UUID uuid = get.getID(args[0]);
		    	if(uuid == null) {
		    		uuid = get.getOfflineID(args[0]);
		    		if(uuid == null) {
		    			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That player has not joined the server. If the player is offline, please use the full and most recent name.");
			    		return true;
		    		}
		    	}
		    	if(um.getUser(uuid) != null)
		    		g = um.getUser(uuid).getGuild();
			}
			if(g == null) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That guild does not exists.");
				return true;
			}
			User u = um.getUser(p.getUniqueId());
			if(u.getGuild() == null) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are not in a guild.");
				return true;
			}
			if(g.equals(u.getGuild()) || u.getGuild().isNeutral(g)) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are already neutral with that guild.");
				return true;
			}
			if(g.isInvitedAlly(u.getGuild()) || u.getGuild().isInvitedAlly(g)) {
				g.removeAllyInvite(u.getGuild());
				u.getGuild().removeAllyInvite(g);
			}
			if(u.getGuild().isEnemy(g)) {
				if(g.isInvitedNeutral(u.getGuild())) {
					g.setNeutral(u.getGuild());
					u.getGuild().setNeutral(g);
					g.sendMods(var.getMessages() + "You are now neutral with " + var.getObj() + u.getGuild().getName());
					u.getGuild().sendMods(var.getMessages() + "You are now neutral with " + var.getObj() + g.getName());
					return true;
				}
				u.getGuild().addNeutralInvite(g);
				g.sendMods(var.getMessages() + "You have been invited to be neutral with " + var.getObj() + u.getGuild().getName());
				sender.sendMessage(var.getMessages() + "Successfully sent neutral request to " + var.getObj() + g.getName());
				return true;
			}
			g.setNeutral(u.getGuild());
			u.getGuild().setNeutral(g);
			g.sendMods(var.getMessages() + "You are now neutral with " + var.getObj() + u.getGuild().getName());
			u.getGuild().sendMods(var.getMessages() + "You are now neutral with " + var.getObj() + g.getName());
		} else
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be in a guild to be able to become neutral.");
		return true;
	}
}