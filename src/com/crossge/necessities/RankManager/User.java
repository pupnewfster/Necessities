package com.crossge.necessities.RankManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.BlockIterator;

import com.crossge.necessities.Necessities;
import com.crossge.necessities.ScoreBoards;
import com.crossge.necessities.Variables;
import com.crossge.necessities.Economy.BalChecks;
import com.crossge.necessities.Economy.Formatter;
import com.crossge.necessities.Economy.Materials;
import com.crossge.necessities.Guilds.Guild;
import com.crossge.necessities.Guilds.GuildManager;

public class User {
	private File configFileUsers = new File("plugins/Necessities/RankManager", "users.yml");
	private File configFileSubranks = new File("plugins/Necessities/RankManager", "subranks.yml");
	private HashMap<String, Location> homes = new HashMap<String, Location>();
	private ArrayList<String> permissions = new ArrayList<String>();
	private ArrayList<String> subranks = new ArrayList<String>();
	private ArrayList<UUID> ignored = new ArrayList<UUID>();
	private PermissionAttachment attachment;
	private boolean teleporting = false;
	private boolean jailed = false;
	private boolean opChat = false;
	private boolean afk = false;
	private boolean isbacking = false;
	private boolean god = false;
	private boolean muted = false;
	private boolean autoClaiming = false;
	private double power = 0.0;
	private Guild guild;
	private long lastAction = 0;
	private int pastTotal = 0;
	private long login = 0;
	private String lastContact;
	private Player bukkitPlayer;
	private Location lastPos;
	private Location right;
	private Location left;
	private UUID userUUID;
	private String nick;
	private Rank rank;
	
	public User(Player p) {
		YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
		RankManager rm = new RankManager();
		this.bukkitPlayer = p;
		this.right = p.getLocation();
		this.left = p.getLocation();
		this.userUUID = this.bukkitPlayer.getUniqueId();
		if(configUsers.contains(getUUID().toString() + ".rank"))
			this.rank = rm.getRank(configUsers.getString(getUUID().toString() + ".rank"));
		if(configUsers.contains(getUUID().toString() + ".nick"))
			this.nick = ChatColor.translateAlternateColorCodes('&', configUsers.getString(getUUID().toString() + ".nick"));
		if(this.nick != null && !this.nick.startsWith("~")) {
			this.nick = "~" + this.nick;
			updateListName();
		}
		if(configUsers.contains(getUUID().toString() + ".jailed"))
			this.jailed = configUsers.getBoolean(getUUID().toString() + ".jailed");
		if(configUsers.contains(getUUID().toString() + ".afk"))
			this.afk = configUsers.getBoolean(getUUID().toString() + ".afk");
		if(configUsers.contains(getUUID().toString() + ".muted"))
			this.muted = configUsers.getBoolean(getUUID().toString() + ".muted");
		if(configUsers.contains(getUUID().toString() + ".power"))
			this.power = configUsers.getDouble(getUUID().toString() + ".power");
		if(configUsers.contains(getUUID().toString() + ".guild")) {
			GuildManager gm = new GuildManager();
			this.guild = gm.getGuild(configUsers.getString(getUUID().toString() + ".guild"));
		}
		if(configUsers.contains(getUUID().toString() + ".timePlayed"))
			this.pastTotal = configUsers.getInt(getUUID().toString() + ".timePlayed");
		this.login = System.currentTimeMillis();
		readHomes();
		readIgnored();
	}
	
	public User(UUID uuid) {
		this.userUUID = uuid;
		YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
		RankManager rm = new RankManager();
		if(configUsers.contains(getUUID().toString() + ".rank"))
			this.rank = rm.getRank(configUsers.getString(getUUID().toString() + ".rank"));
		for(String subrank : configUsers.getStringList(uuid + ".subranks"))
			if(!subrank.equals(""))
				this.subranks.add(subrank);
		for(String node : configUsers.getStringList(uuid + ".permissions"))
			if(!node.equals(""))
				this.permissions.add(node);
		if(configUsers.contains(getUUID().toString() + ".nick"))
			this.nick = ChatColor.translateAlternateColorCodes('&', configUsers.getString(getUUID().toString() + ".nick"));
		if(this.nick != null && !this.nick.startsWith("~"))
			this.nick = "~" + this.nick;
		if(configUsers.contains(getUUID().toString() + ".power"))
			this.power = configUsers.getDouble(getUUID().toString() + ".power");
		if(configUsers.contains(getUUID().toString() + ".guild")) {
			GuildManager gm = new GuildManager();
			this.guild = gm.getGuild(configUsers.getString(getUUID().toString() + ".guild"));
		}
		if(configUsers.contains(getUUID().toString() + ".timePlayed"))
			this.pastTotal = configUsers.getInt(getUUID().toString() + ".timePlayed");
		readHomes();
		readIgnored();
	}
	
	public void logOut() {
		YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
		configUsers.set(getUUID().toString() + ".timePlayed", (int) (this.pastTotal + (System.currentTimeMillis() - this.login)/1000));
		try {
			configUsers.save(configFileUsers);
		} catch (Exception e){}
		ScoreBoards sb = new ScoreBoards();
		sb.delPlayer(this);
		this.bukkitPlayer = null;
	}
	
	private void readIgnored() {
		YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
		if(configUsers.contains(getUUID().toString() + ".ignored")) {
			for(String name : configUsers.getStringList(getUUID().toString() + ".ignored"))
				if(!name.equals(""))
					this.ignored.add(UUID.fromString(name));
		} else {
			configUsers.set(getUUID().toString() + ".ignored", Arrays.asList(""));
			try {
				configUsers.save(configFileUsers);
			} catch (Exception e){}
		}
	}
	
	public boolean isIgnoring(UUID uuid) {
		return this.ignored.contains(uuid);
	}
	
	public void ignore(UUID uuid) {
		if(!this.ignored.contains(uuid)) {//this should already be checked but whatevs
			this.ignored.add(uuid);
			YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
			List<String> ign = configUsers.getStringList(uuid.toString() + ".ignored");
			if(ign.contains(""))
				ign.remove("");
			ign.add(uuid.toString());
			configUsers.set(uuid.toString() + ".ignored", ign);
			try {
				configUsers.save(configFileUsers);
			} catch (Exception e){}
		}
	}
	
	public void unignore(UUID uuid) {
		if(this.ignored.contains(uuid)) {//this should already be checked but whatevs
			this.ignored.remove(uuid);
			YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
			List<String> ign = configUsers.getStringList(uuid.toString() + ".ignored");
			ign.remove(uuid.toString());
			if(ign.isEmpty())
				ign.add("");
			configUsers.set(uuid.toString() + ".ignored", ign);
			try {
				configUsers.save(configFileUsers);
			} catch (Exception e){}
		}
	}
	
	public void leaveGuild() {
		this.guild = null;
		YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
		configUsers.set(getUUID().toString() + ".guild", null);
		try {
			configUsers.save(configFileUsers);
		} catch (Exception e){}
	}
	
	public void joinGuild(Guild g) {
		this.guild = g;
		YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
		configUsers.set(getUUID().toString() + ".guild", g.getName());
		try {
			configUsers.save(configFileUsers);
		} catch (Exception e){}
	}
	
	public double getPower() {
		return this.power;
	}
	
	public void teleport(final User toTpTo) {
		Variables var = new Variables();
		this.teleporting = true;
		if(this.rank.getTpDelay() == 0) {
			getPlayer().teleport(toTpTo.getPlayer());
			return;
		}
		this.bukkitPlayer.sendMessage(var.getMessages() + "Teleportation will begin in " + ChatColor.RED + this.rank.getTpDelay() + var.getMessages() +
			" seconds, don't move.");
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	    scheduler.scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
	        @Override
	        public void run() {
	        	if(toTpTo.getPlayer() != null && getPlayer() != null && isTeleporting()) {
	        		getPlayer().teleport(toTpTo.getPlayer());
	        		tpSuccess();
	        	}
	        }
	    }, 20 * this.rank.getTpDelay());
	}
	
	public void cancelTp() {
		Variables var = new Variables();
		this.teleporting = false;
		this.isbacking = false;
		getPlayer().sendMessage(var.getMessages() + "Teleportation canceled.");
	}
	
	public void tpSuccess() {
		Variables var = new Variables();
		this.teleporting = false;
		getPlayer().sendMessage(var.getMessages() + "Teleportation successful.");
	}
	
	public void teleport(final Location l) {
		final Variables var = new Variables();
		final BalChecks bal = new BalChecks();
		if(this.rank.getTpDelay() == 0) {
			if(isBacking()) {
    			if(!getPlayer().hasPermission("Necessities.freeCommand")) {
    				Formatter form = new Formatter();
    				double price = Double.parseDouble(bal.bal(this.userUUID)) * .15;
    				if(price > 1000.0)
    					price = 1000.0;
    				bal.removeMoney(this.userUUID, price);
    				this.bukkitPlayer.sendMessage(var.getMoney() + "$" + form.addCommas(form.roundTwoDecimals(price)) + var.getMessages() +
    						" was removed from your acount.");
    			}
    			setBacking(false);
    		}
    		this.bukkitPlayer.teleport(l);
    		return;
		}
		this.teleporting = true;
		this.bukkitPlayer.sendMessage(var.getMessages() + "Teleportation will begin in " + ChatColor.RED + this.rank.getTpDelay() + var.getMessages() +
			" seconds, don't move.");
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	    scheduler.scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
	        @Override
	        public void run() {
	        	if(getPlayer() != null && isTeleporting()) {
	        		if(isBacking()) {
	        			if(!getPlayer().hasPermission("Necessities.freeCommand")) {
	        				Formatter form = new Formatter();
	        				double price = Double.parseDouble(bal.bal(getUUID())) * .15;
	        				if(price > 1000.0)
	        					price = 1000.0;
	        				bal.removeMoney(getUUID(), price);
	        				getPlayer().sendMessage(var.getMoney() + "$" + form.addCommas(form.roundTwoDecimals(price)) + var.getMessages() +
	        						" was removed from your acount.");
	        			}
	        			setBacking(false);
	        		}
	        		getPlayer().teleport(l);
	        		tpSuccess();
	        	}
	        }
	    }, 20 * this.rank.getTpDelay());
	}
	
	public boolean isBacking() {
		return this.isbacking;
	}
	
	public void setBacking(boolean back) {
		this.isbacking = back;
	}
	
	public long getLastAction() {
		return this.lastAction;
	}
	
	public void setLastAction(long time) {
		this.lastAction = time;
		if(getPlayer() != null && getPlayer().hasPermission("Necessities.afk"))
			try {
				BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
				scheduler.scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
					@Override
					public void run() {
						if(!isAfk() && getPlayer() != null && (System.currentTimeMillis() - getLastAction()) / 1000.0 >= 299.9)
							setAfk(true);
					}
				}, 20 * 300);
			} catch(Exception e) {}
	}
	
	public boolean isTeleporting() {
		return this.teleporting;
	}
	
	public UUID getUUID() {
		if(this.bukkitPlayer == null)
			return this.userUUID;
		return this.bukkitPlayer.getUniqueId();
	}
	
	public String getName() {
		if(this.bukkitPlayer == null)
			return Bukkit.getOfflinePlayer(this.userUUID).getName();
		return this.bukkitPlayer.getName();
	}
	
	public Rank getRank() {
		return this.rank;
	}
	
	public String getNick() {
		return this.nick;
	}
	
	public Location getLastPos() {
		return this.lastPos;
	}
	
	public void setLastPos(Location l) {
		this.lastPos = l;
	}
	
	public boolean isJailed() {
		return this.jailed;
	}
	
	public void setJailed(boolean jail) {
		this.jailed = jail;
		YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
		configUsers.set(getUUID().toString() + ".jailed", jail);
		try {
			configUsers.save(configFileUsers);
		} catch (Exception e){}
	}
	
	public void setRank(Rank r) {
		this.rank = r;
		YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
		configUsers.set(getUUID().toString() + ".rank", r.getName());
		try {
			configUsers.save(configFileUsers);
		} catch (Exception e){}
		refreshPerms();
	}
	
	public void updateListName() {
		if(this.bukkitPlayer == null)
			return;
		if(this.nick == null) {
			if(!this.bukkitPlayer.getPlayerListName().equals(this.bukkitPlayer.getName()))
				this.bukkitPlayer.setPlayerListName(getRank().getColor() + this.bukkitPlayer.getName());
			return;
		}
		String tempNick = getRank().getColor() + this.nick.substring(0, this.nick.length() - 2);
		if(tempNick.length() <= 16)
			this.bukkitPlayer.setPlayerListName(tempNick);
	}
	
	public void setNick(String message) {
		if(message != null)
			this.nick = ChatColor.translateAlternateColorCodes('&', message);
		else
			this.nick = null;
		updateListName();
		YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
		configUsers.set(getUUID().toString() + ".nick", message);
		try {
			configUsers.save(configFileUsers);
		} catch (Exception e){}
	}
	
	public void setMuted(boolean tomute) {
		this.muted = tomute;
		YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
		configUsers.set(getUUID().toString() + ".muted", this.muted);
		try {
			configUsers.save(configFileUsers);
		} catch (Exception e){}
	}
	
	public boolean isMuted() {
		return this.muted;
	}
	
	public void setGod(boolean godmode) {
		this.god = godmode;
	}
	
	public boolean godmode() {
		return this.god;
	}
	
	public boolean isAfk() {
		return this.afk;
	}
	
	public void setAfk(boolean isafk) {
		this.afk = isafk;
		if(getPlayer() != null) {
			this.bukkitPlayer.setSleepingIgnored(this.afk);
			Variables var = new Variables();
			if(this.isAfk())
				Bukkit.broadcastMessage(var.getMe() + "*" + getRank().getColor() + getPlayer().getDisplayName() + var.getMe() + " is now AFK");
			else
				Bukkit.broadcastMessage(var.getMe() + "*" + getRank().getColor() + getPlayer().getDisplayName() + var.getMe() + " is no longer AFK");
		}
		YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
		configUsers.set(getUUID().toString() + ".afk", isafk);
		try {
			configUsers.save(configFileUsers);
		} catch (Exception e){}
	}
	
	public void setLastC(String last) {
		this.lastContact = last;
	}
	
	public String getLastC() {
		return this.lastContact;
	}
	
	public void givePerms() {
		ScoreBoards sb = new ScoreBoards();
		sb.addPlayer(this);
		YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
		YamlConfiguration configSubranks = YamlConfiguration.loadConfiguration(configFileSubranks);
		this.attachment = this.bukkitPlayer.addAttachment(Necessities.getInstance());
		for(String node : this.rank.getNodes())
			setPerm(node);
		for(String subrank : configUsers.getStringList(getUUID().toString() + ".subranks")) {
			if(!subrank.equals("") && configSubranks.contains(subrank)) {
				this.subranks.add(subrank);
				for(String node : configSubranks.getStringList(subrank))
					setPerm(node);
			}
		}
		for(String node : configUsers.getStringList(getUUID().toString() + ".permissions")) {
			if(!node.equals(""))
				this.permissions.add(node);
			setPerm(node);
		}
	}
	
	private void setPerm(String node) {
		if(node.equals("") || node.equals("-*"))
			return;
		if(node.startsWith("-"))
			this.attachment.setPermission(node.replaceFirst("-", ""), false);
		else
			this.attachment.setPermission(node, true);
	}
	
	public void updateRank(Rank r) {
		this.rank = r;
		if(this.bukkitPlayer != null)
			refreshPerms();
	}
	
	private void readHomes() {
		YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
		if(configUsers.contains(getUUID().toString() + ".homeslist"))
			for(String home : configUsers.getStringList(getUUID().toString() + ".homeslist"))
				if(!home.equals(""))
					this.homes.put(home, new Location(Bukkit.getWorld(configUsers.getString(getUUID().toString() + ".homes." + home + ".world")),
							Double.parseDouble(configUsers.getString(getUUID().toString() + ".homes." + home + ".x")),
							Double.parseDouble(configUsers.getString(getUUID().toString() + ".homes." + home + ".y")),
							Double.parseDouble(configUsers.getString(getUUID().toString() + ".homes." + home + ".z")),
							Float.parseFloat(configUsers.getString(getUUID().toString() + ".homes." + home + ".yaw")),
							Float.parseFloat(configUsers.getString(getUUID().toString() + ".homes." + home + ".pitch"))));
	}
	
	public void addHome(Location l, String name) {
		name = name.toLowerCase().trim();
		YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
		List<String> homelist = configUsers.getStringList(getUUID().toString() + ".homeslist");
		if(homelist.isEmpty()) {
			configUsers.set(getUUID().toString() + ".homeslist", Arrays.asList(name));
		} else {
			if(homelist.contains(""))
				homelist.remove("");
			if(!homelist.contains(name))
				homelist.add(name);
			configUsers.set(getUUID().toString() + ".homeslist", homelist);
		}
		configUsers.set(getUUID().toString() + ".homes." + name + ".world", l.getWorld().getName());
		configUsers.set(getUUID().toString() + ".homes." + name + ".x", Double.toString(l.getX()));
		configUsers.set(getUUID().toString() + ".homes." + name + ".y", Double.toString(l.getY()));
		configUsers.set(getUUID().toString() + ".homes." + name + ".z", Double.toString(l.getZ()));
		configUsers.set(getUUID().toString() + ".homes." + name + ".yaw", Float.toString(l.getYaw()));
		configUsers.set(getUUID().toString() + ".homes." + name + ".pitch", Float.toString(l.getPitch()));
		try {
			configUsers.save(configFileUsers);
		} catch (Exception e){}
		this.homes.put(name, l);
	}
	
	public void delHome(String name) {
		name = name.toLowerCase().trim();
		YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
		List<String> homelist = configUsers.getStringList(getUUID().toString() + ".homeslist");
		homelist.remove(name);
		if(homelist.isEmpty())
			homelist.add("");
		configUsers.set(getUUID().toString() + ".homeslist", homelist);
		configUsers.set(getUUID().toString() + ".homes." + name, null);
		try {
			configUsers.save(configFileUsers);
		} catch (Exception e){}
		this.homes.remove(name);
	}
	
	public String getHomes() {
		if(this.homes.isEmpty())
			return "";
		String homeslist = "";
		ArrayList<String> sortHomes = new ArrayList<String>();
		for(String n : this.homes.keySet())
			sortHomes.add(n);
		Collections.sort(sortHomes);
		for(String name : sortHomes)
			homeslist += name + ", ";
		return homeslist.trim().substring(0, homeslist.length() - 2);
	}
	
	public boolean hasHome(String name) {
		name = name.toLowerCase().trim();
		return this.homes.containsKey(name);
	}
	
	public Location getHome(String name) {
		return this.homes.get(name);
	}
	
	public int homeCount() {
		return this.homes.size();
	}
	
	public void refreshPerms() {
		this.subranks.clear();
		this.permissions.clear();
		removePerms();
		givePerms();
		updateListName();
	}
	
	public void removePerms() {
		for(String p : this.attachment.getPermissions().keySet())
			this.attachment.unsetPermission(p);
	}
	
	public String getSubranks() {
		String sublist = "";
		if(this.subranks.isEmpty())
			return null;
		for(String sub : this.subranks)
			sublist += sub + ", ";
		return sublist.trim().substring(0, sublist.length() - 2);
	}
	
	public String getPermissions() {
		String permlist = "";
		if(this.permissions.isEmpty())
			return null;
		for(String perm : this.permissions)
			permlist += perm + ", ";
		return permlist.trim().substring(0, permlist.length() - 2);
	}
	
	public void addPerm(String permission) {
		setPerm(permission);
	}
	
	public void removePerm(String permission) {
		this.attachment.unsetPermission(permission);
		if(this.permissions.contains(permission))
			this.permissions.remove(permission);
	}
	
	public Player getPlayer() {
		return this.bukkitPlayer;
	}
	
	public void setLeft(Location l) {
		this.left = l;
	}
	
	public void setRight(Location l) {
		this.right = l;
	}
	
	public Location getLeft() {
		return this.left;
	}
	
	public Location getRight() {
		return this.right;
	}
	
	public Guild getGuild() {
		return this.guild;
	}
	
	public boolean isClaiming() {
		return this.autoClaiming;
	}
	
	public boolean opChat() {
		return this.opChat;
	}
	
	public void toggleOpChat() {
		this.opChat = !this.opChat;
	}
	
	public void setClaiming(boolean value) {
		this.autoClaiming = value;
	}
	
	public void removePower() {
		if(this.power - 2 < -10)
			this.power = -10;
		else
			this.power -= 2;
		YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
		configUsers.set(getUUID().toString() + ".power", this.power);
		try {
			configUsers.save(configFileUsers);
		} catch (Exception e){}
		if(this.guild != null)
			this.guild.updatePower();
	}
	
	public void addPower() {
		if(this.afk)
			return;
		if(this.power + 0.03333 > 10)
			this.power = 10;
		else
			this.power += 0.03333;
		YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
		configUsers.set(getUUID().toString() + ".power", this.power);
		try {
			configUsers.save(configFileUsers);
		} catch (Exception e){}
		if(this.guild != null)
			this.guild.updatePower();
	}
	
	public Location getLookingAt() {
		if(this.bukkitPlayer == null)
			return null;
		Materials mat = new Materials();
        Iterator<Block> itr = new BlockIterator(this.bukkitPlayer, 140);
        while(itr.hasNext()) {
        	Block block = itr.next();
            if(mat.nameToId(block.getType().toString()) != 0)
            	return block.getLocation();
        }
        return null;
	}
	
	public String getTimePlayed() {
		long seconds = this.pastTotal;
		if(this.login != 0)
			seconds += (System.currentTimeMillis() - this.login) / 1000;
		long first = seconds % 31536000;
		long second = first % 2592000;
		long third = second % 604800;
		long fourth = third % 86400;
		long fifth = fourth % 3600;
		int years = (int) (seconds / 31536000);
		int months = (int) (first / 2592000);
		int weeks = (int) (second / 604800);
		int days = (int) (third / 86400);
		int hours = (int) (fourth / 3600);
		int min = (int) (fifth / 60);
		int sec = (int) (fifth % 60);
		String time = "";
		if(years != 0)
			time = Integer.toString(years) + " year" + plural(years) + " ";
		if(months != 0)
			time += Integer.toString(months) + " month" + plural(months) + " ";
		if(weeks != 0)
			time += Integer.toString(weeks) + " week" + plural(weeks) + " ";
		if(days != 0)
			time += Integer.toString(days) + " day" + plural(days) + " ";
		if(hours != 0)
			time += Integer.toString(hours) + " hour" + plural(hours) + " ";
		if(min != 0)
			time += Integer.toString(min) + " minute" + plural(min) + " ";
		if(sec != 0)
			time += Integer.toString(sec) + " second" + plural(sec) + " ";
		time = time.trim();
		if(time.equals(""))
			time = "This player has not spent any time on our server";
		return time + ".";
	}
	
	private String plural(int times) {
		if(times == 1)
			return "";
		return "s";
	}
}