package com.crossge.necessities.Commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.crossge.necessities.RankManager.Rank;
import com.crossge.necessities.RankManager.User;
import com.crossge.necessities.RankManager.UserManager;

public class CmdWho extends Cmd {
	UserManager um = new UserManager();
	CmdHide hide = new CmdHide();
	
	public boolean commandUse(CommandSender sender, String[] args) {
		if(sender instanceof Player && !((Player) sender).hasPermission("Necessities.seehidden")) {
			HashMap<Rank,String> online = new HashMap<Rank,String>();
			int numbOnline = 1;
			online.put(rm.getRank("Manager"), "Janet, ");
			if(!um.getUsers().isEmpty()) {
				for(User u : um.getUsers().values())
					if(!hide.isHidden(u.getPlayer())) {
						if(u.isAfk()) {
							if(online.containsKey(u.getRank()))
								online.put(u.getRank(), online.get(u.getRank()) + "[AFK]" + u.getPlayer().getDisplayName() + ", ");
							else
								online.put(u.getRank(), "[AFK]" + u.getPlayer().getDisplayName() + ", ");
						} else {
							if(online.containsKey(u.getRank()))
								online.put(u.getRank(), online.get(u.getRank()) + u.getPlayer().getDisplayName() + ", ");
							else
								online.put(u.getRank(), u.getPlayer().getDisplayName() + ", ");
						}
						numbOnline++;
					}
			}
			sender.sendMessage(var.getMessages() + "There " + amount(numbOnline) + " " + var.getObj() + numbOnline + var.getMessages() + " out of a maximum " +
					var.getObj() + Bukkit.getMaxPlayers() + var.getMessages() + " players online.");
			for(int i = rm.getOrder().size() - 1; i >= 0; i--) {
				Rank r = rm.getRank(i);
				if(online.containsKey(r))
					sender.sendMessage(r.getColor() + r.getName() + "s: " + ChatColor.WHITE + online.get(r).trim().substring(0, online.get(r).length() - 2));
			}
			return true;
		}
		int numbOnline = Bukkit.getOnlinePlayers().size() + 1;
		sender.sendMessage(var.getMessages() + "There " + amount(numbOnline) + " " + var.getObj() + numbOnline + var.getMessages() + " out of a maximum " +
				var.getObj() + Bukkit.getMaxPlayers() + var.getMessages() + " players online.");
		HashMap<Rank,String> online = new HashMap<Rank,String>();
		online.put(rm.getRank("Manager"), "Janet, ");
		if(!um.getUsers().isEmpty())
			for(User u : um.getUsers().values())
				if(u.isAfk() && hide.isHidden(u.getPlayer())) {
					if(online.containsKey(u.getRank()))
						online.put(u.getRank(), online.get(u.getRank()) + "[AFK][HIDDEN]" + u.getPlayer().getDisplayName() + ", ");
					else
						online.put(u.getRank(), "[AFK][HIDDEN]" + u.getPlayer().getDisplayName() + ", ");
				} else if(u.isAfk()) {
					if(online.containsKey(u.getRank()))
						online.put(u.getRank(), online.get(u.getRank()) + "[AFK]" + u.getPlayer().getDisplayName() + ", ");
					else
						online.put(u.getRank(), "[AFK]" + u.getPlayer().getDisplayName() + ", ");
				} else if(hide.isHidden(u.getPlayer())) {
					if(online.containsKey(u.getRank()))
						online.put(u.getRank(), online.get(u.getRank()) + "[HIDDEN]" + u.getPlayer().getDisplayName() + ", ");
					else
						online.put(u.getRank(), "[HIDDEN]" + u.getPlayer().getDisplayName() + ", ");
				} else {
					if(online.containsKey(u.getRank()))
						online.put(u.getRank(), online.get(u.getRank()) + u.getPlayer().getDisplayName() + ", ");
					else
						online.put(u.getRank(), u.getPlayer().getDisplayName() + ", ");
				}
		for(int i = rm.getOrder().size() - 1; i >= 0; i--) {
			Rank r = rm.getRank(i);
			if(online.containsKey(r))
				sender.sendMessage(r.getColor() + r.getName() + "s: " + ChatColor.WHITE + online.get(r).trim().substring(0, online.get(r).length() - 2));
		}
		return true;
	}
	
	private String amount(int a) {
		if(a == 1)
			return "is";
		return "are";
	}
}