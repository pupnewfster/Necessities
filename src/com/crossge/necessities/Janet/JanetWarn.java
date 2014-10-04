package com.crossge.necessities.Janet;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class JanetWarn {
	private File configFile = new File("plugins/Necessities", "config.yml");
	private YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
	private static HashMap<UUID,Integer> warnCount = new HashMap<UUID,Integer>();
	private int warns = config.getInt("Necessities.warns");
	private static String world = "";
	private static String title = ChatColor.DARK_RED + "[" + ChatColor.AQUA + "Manager" + ChatColor.DARK_RED + "] ";
	private static String name = ChatColor.GOLD + "Janet" + ChatColor.DARK_RED + ": " + ChatColor.WHITE;
	private static String JanetName = "";
	private String JanetLogName = "Janet: ";
	JanetLog log = new JanetLog();

	public void initiate() {
		String temp = Bukkit.getServerName();
		if(temp.contains(" "))
			temp = "world";
		world = ChatColor.WHITE + temp + " ";
		JanetName = world + title + name;
	}
	
	public void removePlayer(UUID uuid) {
		warnCount.remove(uuid);
	}
	
	public void warn(UUID uuid, String reason, String warner) {
		if(!warnCount.containsKey(uuid))
			warnCount.put(uuid, 1);
		else
			warnCount.put(uuid, warnCount.get(uuid) + 1);
		String warning = "";
		if(warnCount.get(uuid) == warns) {
			if(reason.equals("Language"))
				warning = language(uuid);
			else if(reason.equals("ChatSpam"))
				warning = chatSpam(uuid);
			else if(reason.equals("CmdSpam"))
				warning = cmdSpam(uuid);
			else if(reason.equals("Adds"))
				warning = advertising(uuid);
			else
				warning = other(uuid, reason);
		} else {
			if(reason.equals("Language"))
				warning = langMsg(uuid);
			else if(reason.equals("ChatSpam"))
				warning = chatMsg(uuid);
			else if(reason.equals("CmdSpam"))
				warning = cmdMsg(uuid);
			else if(reason.equals("Adds"))
				warning = addsMsg(uuid);
			else
				warning = warnMessage(uuid, reason, warner);
			timesLeft(uuid);
		}
		log.log(JanetLogName + warning);
	}
	
	private void timesLeft(UUID uuid) {
		String left = Integer.toString(warns - warnCount.get(uuid));
		String plural = "times";
		if(warns - warnCount.get(uuid) == 1)
			plural = "time";
		Bukkit.getPlayer(uuid).sendMessage(JanetName + "Do it " + left + " more " + plural + " and you will be kicked.");
	}
	
	private String langMsg(UUID uuid) {
		Bukkit.getPlayer(uuid).sendMessage(JanetName + ChatColor.DARK_RED + "Warning, " + ChatColor.WHITE + "Do not swear please.");
		broadcast(JanetName + getName(uuid) + " was warned for using bad language.", uuid);
		return getName(uuid) + " was warned for using bad language.";
	}
	
	private String chatMsg(UUID uuid) {
		Bukkit.getPlayer(uuid).sendMessage(JanetName + ChatColor.DARK_RED + "Warning, " + ChatColor.WHITE + "Do not spam the chat please.");
		broadcast(JanetName + getName(uuid) + " was warned for spamming the chat.", uuid);
		return getName(uuid) + " was warned for spamming the chat.";
	}
	
	private String cmdMsg(UUID uuid) {
		Bukkit.getPlayer(uuid).sendMessage(JanetName + ChatColor.DARK_RED + "Warning, " + ChatColor.WHITE + "Do not spam commands please.");
		broadcast(JanetName + getName(uuid) + " was warned for spamming commands.", uuid);
		return getName(uuid) + " was warned for spamming commands.";
	}
	
	private String addsMsg(UUID uuid) {
		Bukkit.getPlayer(uuid).sendMessage(JanetName + ChatColor.DARK_RED + "Warning, " + ChatColor.WHITE + "Do not advertise other servers please.");
		broadcast(JanetName + getName(uuid) + " was warned for advertising other servers.", uuid);
		return getName(uuid) + " was warned for advertising other servers.";
	}
	
	private String warnMessage(UUID uuid, String reason, String warner) {
		reason = ChatColor.translateAlternateColorCodes('&', reason);
		Bukkit.getPlayer(uuid).sendMessage(JanetName + ChatColor.DARK_RED + "Warning, " + ChatColor.WHITE + "Do not " + reason + ".");
		broadcast(JanetName + getName(uuid) + " was warned by " + warner + " for " + reason + ".", uuid);
		return getName(uuid) + " was warned by " + warner + " for " + reason + ".";
	}
	
	private String other(UUID uuid, String reason) {
		String pname = getName(uuid);
		Bukkit.broadcastMessage(JanetName + pname + " was kicked for " + reason + ".");
		Bukkit.getPlayer(uuid).kickPlayer(ChatColor.WHITE + "You were kicked for " + reason);
		return pname + " was kicked for " + reason + ".";
	}
	
	private String chatSpam(UUID uuid) {
		String pname = getName(uuid);
		Bukkit.broadcastMessage(JanetName + pname + " was kicked for spamming the chat.");
		Bukkit.getPlayer(uuid).kickPlayer(ChatColor.WHITE + "Don't spam the chat!");
		return pname + " was kicked for spamming the chat.";
	}
	
	private String cmdSpam(UUID uuid) {
		String pname = getName(uuid);
		Bukkit.broadcastMessage(JanetName + pname + " was kicked for spamming commands.");
		Bukkit.getPlayer(uuid).kickPlayer(ChatColor.WHITE + "Don't spam commands!");
		return pname + " was kicked for spamming commands.";
	}
	
	private String language(UUID uuid) {
		String pname = getName(uuid);
		Bukkit.broadcastMessage(JanetName + pname + " was kicked for using bad language.");
		Bukkit.getPlayer(uuid).kickPlayer(ChatColor.WHITE + "Watch your language!");		
		return pname + " was kicked for using bad language.";
	}
	
	private String advertising(UUID uuid) {
		String pname = getName(uuid);
		Bukkit.broadcastMessage(JanetName + pname + " was kicked for advertising.");
		Bukkit.getPlayer(uuid).kickPlayer(ChatColor.WHITE + "Do not advertise other servers!");
		return pname + " was kicked for advertising.";
	}
	
	private String getName(UUID uuid) {
		return Bukkit.getPlayer(uuid).getName();
	}
	
	private void broadcast(String message, UUID uuid) {
		Bukkit.getConsoleSender().sendMessage(message);
		for(Player p : Bukkit.getOnlinePlayers())
			if(p.getUniqueId() != uuid)
				p.sendMessage(message);
	}
}