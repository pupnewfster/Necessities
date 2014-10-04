package com.crossge.necessities.Commands;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.crossge.necessities.RankManager.User;

public class CmdHide extends Cmd {
	private File configFileLogOut = new File("plugins/Necessities", "logoutmessages.yml");
	private File configFileLogIn = new File("plugins/Necessities", "loginmessages.yml");
	private static ArrayList<UUID> hidden = new ArrayList<UUID>();
	
	public boolean commandUse(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			User u = um.getUser(p.getUniqueId());
			if(hidden.contains(p.getUniqueId())) {
				unhidePlayer(p);
				YamlConfiguration configLogIn = YamlConfiguration.loadConfiguration(configFileLogIn);
				Bukkit.broadcastMessage((ChatColor.GREEN + " + " + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&',
							configLogIn.getString(p.getUniqueId().toString()).replaceAll("\\{NAME\\}", p.getDisplayName()).replaceAll("\\{RANK\\}",
									um.getUser(p.getUniqueId()).getRank().getTitle()))).replaceAll(ChatColor.RESET + "", ChatColor.YELLOW + ""));
				hidden.remove(p.getUniqueId());
				p.sendMessage(var.getMessages() + "You are now visible.");
				Bukkit.broadcast(var.getMessages() + "To Ops - " + var.getObj() + p.getDisplayName() + var.getMessages() + " - is now " +  ChatColor.DARK_GRAY +
						"visible" + var.getMessages() + ".", "Necessities.opBroadcast");
				if(u.opChat()) {
					u.toggleOpChat();
					p.sendMessage(var.getMessages() + "You are no longer sending messages to ops.");
				}
			} else {
				hidePlayer(p);
				YamlConfiguration configLogOut = YamlConfiguration.loadConfiguration(configFileLogOut);
				Bukkit.broadcastMessage((ChatColor.RED + " - " + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&',
							configLogOut.getString(p.getUniqueId().toString()).replaceAll("\\{NAME\\}", p.getDisplayName()).replaceAll("\\{RANK\\}",
									um.getUser(p.getUniqueId()).getRank().getTitle()))).replaceAll(ChatColor.RESET + "", ChatColor.YELLOW + ""));
				hidden.add(p.getUniqueId());
				p.sendMessage(var.getMessages() + "You are now hidden.");
				Bukkit.broadcast(var.getMessages() + "To Ops - " + var.getObj() + p.getDisplayName() + var.getMessages() + " - is now " + ChatColor.WHITE +
						"invisible" + var.getMessages() + ".", "Necessities.opBroadcast");
				if(!u.opChat()) {
					u.toggleOpChat();
					p.sendMessage(var.getMessages() + "You are now sending messages only to ops.");
				}
			}
		} else
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot hide.");
		return true;
	}
	
	public void removeP(UUID uuid) {
		if(hidden.contains(uuid)) {
			hidden.remove(uuid);
			unhidePlayer(Bukkit.getPlayer(uuid));
		}
	}
	
	public boolean isHidden(Player p) {
		return hidden.contains(p.getUniqueId());
	}
	
	public void playerJoined(Player p) {
		if(!p.hasPermission("Necessities.seehidden"))
			for(UUID uuid : hidden)
				p.hidePlayer(Bukkit.getPlayer(uuid));
	}
	
	public void playerLeft(Player p) {
		for(UUID uuid : hidden)
			p.showPlayer(Bukkit.getPlayer(uuid));
	}
	
	private void hidePlayer(Player p) {
		for(Player x : Bukkit.getOnlinePlayers())
			if(!x.equals(p) && x.canSee(p) && !x.hasPermission("Necessities.seehidden"))
				x.hidePlayer(p);
	}
	
	private void unhidePlayer(Player p) {
		for(Player x : Bukkit.getOnlinePlayers())
			if(!x.equals(p) && !x.canSee(p))
				x.showPlayer(p);
	}
}