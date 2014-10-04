package com.crossge.necessities.Janet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import com.crossge.necessities.*;

public class Janet {
	private File configFile = new File("plugins/Necessities", "config.yml");
	private static HashMap<UUID,Long[]> lastChat = new HashMap<UUID,Long[]>();
	private static HashMap<UUID,Long[]> lastCmd = new HashMap<UUID,Long[]>();
	private static ArrayList<String> badwords = new ArrayList<String>();
	private static ArrayList<String> goodwords = new ArrayList<String>();
	private static ArrayList<String> ips = new ArrayList<String>();
	private double chatSpam = 1.5;//seconds for 3 messages
	private double cmdSpam = 1.5;//seconds for 3 messages
	JanetWarn warns = new JanetWarn();
	Variables var = new Variables();
	JanetLog log = new JanetLog();
	
	public void initiate() {//now has its own function instead of reading them all every time Janet was re-initiated
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Janet initiating...");
		File customConfigFileCensors = new File("plugins/Necessities", "censors.yml");
		YamlConfiguration customConfigCensors = YamlConfiguration.loadConfiguration(customConfigFileCensors);
		for(String word : customConfigCensors.getStringList("badwords"))
			if(!word.equals(""))
				badwords.add(word.toUpperCase());
		for(String word : customConfigCensors.getStringList("goodwords"))
			if(!word.equals(""))
				goodwords.add(word.toUpperCase());
		for(String ip : customConfigCensors.getStringList("ips"))
			if(!ip.equals(""))
				ips.add(ip);
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	    scheduler.scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
	        @Override
	        public void run() {
	        	Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a + &4[&bManager&4]&6 Janet&e joined the game."));
	        }
	    });
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Janet initiated.");
	}
	
	public void unload() {//possibly empty the lists not sure if needed though
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c - &4[&bManager&4]&6 Janet&e Disconnected."));
	}
	
	private void removePlayer(UUID uuid) {//called when player disconnects
		warns.removePlayer(uuid);
		lastChat.remove(uuid);
		lastCmd.remove(uuid);
	}
	
	private boolean checkChatSpam(UUID uuid) {
		Long time = System.currentTimeMillis();
		if(!lastChat.containsKey(uuid)) {
			Long[] t = new Long[2];
			t[0] = time;
			lastChat.put(uuid, t);
			return false;
		}
		if(!isFull(lastChat.get(uuid))) {
			putProp(lastChat.get(uuid), time);
			return false;
		}
		Long FirstTime = lastChat.get(uuid)[0];
		if((time - FirstTime) / 1000.0 > chatSpam) {
			putProp(lastChat.get(uuid), time);
			return false;
		}
		putProp(lastChat.get(uuid), time);
		delayedWarn(uuid, "ChatSpam");
		return true;
	}
	
	private boolean checkCmdSpam(UUID uuid) {
		Long time = System.currentTimeMillis();
		if(!lastCmd.containsKey(uuid)) {
			Long[] t = new Long[2];
			t[0] = time;
			lastCmd.put(uuid, t);
			return false;
		}
		if(!isFull(lastCmd.get(uuid))) {
			putProp(lastCmd.get(uuid), time);
			return false;
		}
		Long FirstTime = lastCmd.get(uuid)[0];
		if((time - FirstTime) / 1000.0 > cmdSpam) {
			putProp(lastCmd.get(uuid), time);
			return false;
		}
		putProp(lastCmd.get(uuid), time);
		delayedWarn(uuid, "CmdSpam");
		return true;
	}
	
	private void putProp(Long[] l, Long toPut) {
		if(l[1] != null)
			l[0] = l[1];
		l[1] = toPut;
	}
	
	private boolean isFull(Long[] l) {
		return !(l[0] == null || l[1] == null);
	}
	
	private String caps(UUID uuid, String message, boolean warn) {
		if(internalCaps(message))
			return message.toLowerCase();
		return message;
	}
	
	public boolean internalCaps(String message) {
		String orig = message.replaceAll("[^A-Z]", "");
		int s = orig.length();
		message = message.replaceAll("[^a-zA-Z]", "");
		int f = message.length();
		return f * 3.0/5 <= s && f > 5;
	}
	
	private String langCheck(UUID uuid, String message, boolean warn) {
		String censored = internalLang(message);
		if(censored.equals(message))
			return message;
		if(warn)
			delayedWarn(uuid, "Language");
		return censored;
	}
	
	public String internalLang(String message) {
		String[] orig = message.replaceAll("[^a-zA-Z ]", "").toUpperCase().split(" ");
		ArrayList<String> bad = new ArrayList<String>();
		for(int i = 0; i < badwords.size(); i++) {
			ArrayList<String> s = removeSpaces(orig, badwords.get(i));
			for(int j = 0; j < s.size(); j++)
				if(s.get(j).contains(badwords.get(i)) && check(s.get(j), badwords.get(i)) && !isGood(s.get(j)))
					bad.add(s.get(j));
			for(int j = 0; j < orig.length; j++) {
				String t = removeConsec(orig[j]);
				if((orig[j].contains(badwords.get(i)) && check(orig[j], badwords.get(i)) && !isGood(orig[j])) ||
						(t.contains(badwords.get(i)) && check(t, badwords.get(i)) && !isGood(t)))
					bad.add(orig[j]);
			}
		}
		if(bad.isEmpty())
			return message;
		String[] nonCapitalized = message.split(" ");
		String censored = "";
		for(int i = 0; i < nonCapitalized.length; i++) {
			for(String word : bad)
				if(nonCapitalized[i].replaceAll("[^a-zA-Z]", "").equalsIgnoreCase(word))
					nonCapitalized[i] = stars(nonCapitalized[i]);
			censored += nonCapitalized[i] + " ";
		}
		if(censored.equals(""))
			censored = message;
		return addSpaces(bad, censored);
	}
	
	private boolean check(String msg, String bad) {
		return msg.length() * bad.length()/(bad.length() + 1.0) - bad.length() > 0.75 || msg.length() * 4.0/5 <= bad.length() ||
			msg.replaceAll(bad, "").length() == 0 || msg.replaceAll(bad, "").length() >= msg.length() * 3.0/5;
	}
	
	private boolean isGood(String msg) {
		for(int z = 0; z < goodwords.size(); z++)
			if(msg.startsWith(goodwords.get(z)))
				return true;
		return false;
	}
	
	private String addSpaces(ArrayList<String> bad, String orig) {
		String censored = "";
		String temp = orig.toUpperCase().replaceAll("[^a-zA-Z]", "");
		String t = removeConsec(temp);
		HashMap<Integer, Character> stars = new HashMap<Integer, Character>();
		String s = t;
		for(String b : bad) {
			temp = temp.replaceAll(b, stars(b));
			s = s.replaceAll(b, stars(b));
		}
		for(int i = 0; i < s.length(); i++)
			if(s.charAt(i) == '*')
				stars.put(i, t.charAt(i));
		String c = "";
		int noSpace = 0;
		for(int i = 0; i < temp.length(); i++) {
			if(stars.containsKey(noSpace) && stars.get(noSpace) == temp.charAt(i))
				c += "*";
			else
				c += temp.charAt(i);
			if(i + 1 < temp.length() && stars.containsKey(noSpace) && stars.get(noSpace) != temp.charAt(i + 1))
				noSpace++;
		}
		temp = c;
		int loc = 0;
		for(int i = 0; i < orig.length(); i++) {
			if(loc < temp.length() && temp.charAt(loc) == '*') {
				if(orig.charAt(i) == ' ')
					censored += " ";
				else
					censored += "*";
			} else
				censored += orig.charAt(i);
			if(Character.isLetter(orig.charAt(i)))
				loc++;
		}
		if(censored.equals(""))
			return orig;
		return censored;
	}
	
	private ArrayList<String> removeSpaces(String[] msgs, String word) {
		ArrayList<String> messages = new ArrayList<String>();
		String temp = "";
		String t1 = "";
		for(int i = 0; i < msgs.length; i++) {
			for(int j = i; j < msgs.length; j++) {
				if(temp.length() < word.length()) {
					temp += msgs[j];
					if(!messages.contains(temp))
						if(word.length() > 3)
							messages.add(temp);
						else if(temp.length() <= word.length())
							messages.add(temp);
				}
				if(t1.length() < word.length()) {
					t1 = removeConsec(t1 + msgs[j]);
					if(!messages.contains(t1))
						messages.add(t1);
				}
				if(t1.length() >= word.length() && temp.length() >= word.length())
					break;
			}
			temp = "";
			t1 = "";
		}
		return messages;
	}
	
	private String removeConsec(String message) {
		if(message.equals(""))
			return "";
		String temp = "" + message.charAt(0);
		for(int i = 1; i < message.length(); i++)
			if(message.charAt(i) != message.charAt(i - 1))
				temp += message.charAt(i);
		return temp;
	}
	
	private String stars(String toStar) {
		String[] split = toStar.split(" ");
		String star = "";
		for(String s : split)
			star += starNoSpaces(s) + " ";
		return star.trim();
	}
	
	private String starNoSpaces(String toStar) {
		String star = "";
		for(int i = 0; i < toStar.trim().length(); i++)
			star += "*";
		return star;
	}
	
	private String starIP(String toStar) {
		String port = "";
		if(toStar.contains(":")) {
			port = stars(toStar.split(":")[1]);
			toStar.substring(0, toStar.length() - 2 - port.length());
		}
		String[] ipPieces = toStar.trim().split("\\.");
		String star = "";
		for(int i = 0; i < ipPieces.length; i++)
			star += stars(ipPieces[i]) + ".";
		star = star.substring(0, star.length() -1);
		if(port != "")
			star += ":" + port;
		return star;
	}
	
	private String adds(UUID uuid, String message, boolean warn) {
		String censored = internalAdds(message);
		if(censored.equals(message))
			return message;
		if(warn)
			delayedWarn(uuid, "Adds");
		return censored;
	}
	
	public String internalAdds(String message) {
		String[] orig = message.split(" ");
		String temp = "";
		for(int i = 0; i < orig.length; i++) {
			if(orig[i].split("\\:").length == 0)
				continue;
			temp = orig[i].split("\\:")[0];
			if(!whitelistedIP(temp)) {
				if(validateIPAddress(temp))
					orig[i] = starIP(orig[i]);
				else if(!temp.contains("http://") && (temp.split("\\.").length == 3 || temp.split("\\.").length == 3))
					try {
						URLConnection urlCon = new URL("http://" + temp).openConnection();
						urlCon.connect();
						InputStream is = urlCon.getInputStream();
						String u = urlCon.getURL().toString().replaceFirst("http://", "");
						is.close();
						if(validateIPAddress(u))
							orig[i] = starIP(orig[i]);
					} catch (MalformedURLException e){} catch (IOException e){}
			}
		}
		String censored = "";
		for(String word : orig)
			censored += word + " ";
		return censored.trim();
	}
	
	private boolean whitelistedIP(String ip) {
		return ips.contains(ip.trim()) || Bukkit.getIp().equals(ip);
	}
	
	private boolean validateIPAddress(String ipAddress) {
		try {
			final Pattern ipAdd = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
													"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
			return ipAdd.matcher(ipAddress).matches();
		} catch(Exception e) {}
		return false;
	}
	
	public String logChat(UUID uuid, String message) {
		Player p = Bukkit.getPlayer(uuid);
		log.log(p.getName() + ": " + message);		
		boolean warn = true;
		String censored = message;
		YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
		if(config.getBoolean("Necessities.chatSpam") && !p.hasPermission("Necessities.spamchat"))
			warn = !checkChatSpam(uuid);
		if(config.getBoolean("Necessities.language") && !p.hasPermission("Necessities.language")) {
			censored = langCheck(uuid, censored, warn);
			warn = message.equals(censored);
		}		
		if(config.getBoolean("Necessities.advertise") && !p.hasPermission("Necessities.advertise")) {
			censored = adds(uuid, censored, warn);
			warn = message.equals(censored);
		}
		if(config.getBoolean("Necessities.caps") && !p.hasPermission("Necessities.caps"))
			censored = caps(uuid, censored, warn);
		return censored;
	}
	
	public String logCom(UUID uuid, String message) {
		Player p = Bukkit.getPlayer(uuid);
		String messageOrig = message;
		log.log(p.getName() + " issued server command: " + message);
		boolean warn = false;
		String censored = message.replaceFirst(message.split(" ")[0], "").trim();
		message = message.replaceFirst(message.split(" ")[0], "").trim();
		if(censored.equals(""))
			return messageOrig;
		YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
		if(config.getBoolean("Necessities.cmdSpam") && !p.hasPermission("Necessities.spamcommands"))
			warn = !checkCmdSpam(uuid);
		if(config.getBoolean("Necessities.advertise") && !p.hasPermission("Necessities.advertise")) {
			censored = adds(uuid, censored, warn);
			warn = message.equals(censored);
		}
		if(config.getBoolean("Necessities.language") && !p.hasPermission("Necessities.language")) {
			censored = langCheck(uuid, censored, warn);
			warn = message.equals(censored);
		}
		if(config.getBoolean("Necessities.caps") && !p.hasPermission("Necessities.caps"))
			censored = caps(uuid, censored, warn);
		return messageOrig.split(" ")[0] + " " + censored;
	}
	
	public void logConsole(String message) {
		if(message.startsWith("say"))
			message = "Console:" + message.replaceFirst("say", "");
		else
			message = "Console issued command: " + message;
		log.log(message);
	}
	
	public void logIn(UUID uuid) {
		log.log(" + " + playerInfo(Bukkit.getPlayer(uuid)) + " joined the game.");
	}
	
	public void logDeath(UUID uuid, String cause) {
		log.log(playerInfo(Bukkit.getPlayer(uuid)) + " " + cause);
	}
	
	public void logOut(UUID uuid) {
		removePlayer(uuid);
		log.log(" - " + playerInfo(Bukkit.getPlayer(uuid)) + " Disconnected.");
	}
	
	private String playerInfo(Player p) {
		InetSocketAddress IPAdressPlayer = p.getAddress();
        String sfullip = IPAdressPlayer.toString();
        String[] fullip = sfullip.split("/");
        String sIpandPort = fullip[1];
        String[] ipandport = sIpandPort.split(":");
        return p.getName() + " (" + ipandport[0] + " [" + p.getWorld().getName() + " " + p.getLocation().getBlockX() + "," +
			p.getLocation().getBlockY() + "," + p.getLocation().getBlockZ() + "])";
	}
	
	private void delayedWarn(final UUID uuid, final String reason) {
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	    scheduler.scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
	        @Override
	        public void run() {
	        	warns.warn(uuid, reason, "Janet");
	        }
	    });
	}
}