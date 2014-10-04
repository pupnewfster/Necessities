package com.crossge.necessities.RankManager;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import com.crossge.necessities.GetUUID;
import com.crossge.necessities.Necessities;

public class UserManager {
	private File configFileUsers = new File("plugins/Necessities/RankManager", "users.yml");
	private static HashMap<UUID, User> players = new HashMap<UUID, User>();
	RankManager rm = new RankManager();
	GetUUID get = new GetUUID();
	
	public void readUsers() {
		for(Player p : Bukkit.getOnlinePlayers())
			parseUser(p);
	}
	
	public void parseUser(final Player p) {
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	    scheduler.scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
	        @Override
	        public void run() {
	        	players.put(p.getUniqueId(), new User(p));
	    		players.get(p.getUniqueId()).givePerms();
	        }
	    });
	}
	
	public HashMap<UUID, User> getUsers() {
		return players;
	}
	
	public void removeUser(UUID uuid) {
		players.remove(uuid);
	}
	
	public User getUser(UUID uuid) {
		if(!players.containsKey(uuid))
			return new User(uuid);
		return players.get(uuid);
	}
	
	public void unload() {
		for(UUID uuid : players.keySet())
			players.get(uuid).removePerms();
	}
	
	public void addRankPerm(Rank r, String node) {
		if(players == null)
			return;
		for(UUID uuid : players.keySet()) {
			if(rm.hasRank(players.get(uuid).getRank(), r))
				players.get(uuid).addPerm(node);
		}
	}
	
	public void delRankPerm(Rank r, String node) {
		if(players == null)
			return;
		for(UUID uuid : players.keySet()) {
			if(rm.hasRank(players.get(uuid).getRank(), r))
				players.get(uuid).removePerm(node);
		}
	}
	
	public void refreshRankPerm(Rank r) {
		if(players == null)
			return;
		for(UUID uuid : players.keySet()) {
			if(rm.hasRank(players.get(uuid).getRank(), r))
				players.get(uuid).refreshPerms();
		}
	}
	
	public void addUser(Player player) {
		YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
		if(configUsers.contains(player.getUniqueId().toString()))
			return;
		configUsers.set(player.getUniqueId().toString() + ".rank", "Guest");
		configUsers.set(player.getUniqueId().toString() + ".permissions", Arrays.asList(""));
		configUsers.set(player.getUniqueId().toString() + ".subranks", Arrays.asList(""));
		configUsers.set(player.getUniqueId().toString() + ".balance", 0.0);
		configUsers.set(player.getUniqueId().toString() + ".power", 0);
		try {
			configUsers.save(configFileUsers);
		} catch (Exception e){}
	}
	
	public void updateUserRank(User u, UUID uuid, Rank r) {
		if(r == null)
			return;
		YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
		configUsers.set(uuid.toString() + ".rank", r.getName());
		try {
			configUsers.save(configFileUsers);
		} catch (Exception e){}
		u.updateRank(r);
	}
	
	public void updateUserPerms(UUID uuid, String permission, boolean remove) {
		if(permission == "")
			return;
		YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
		List<String> perms = configUsers.getStringList(uuid.toString() + ".permissions");
		if(perms.contains(""))
			perms.remove("");
		if(remove) {
			perms.remove(permission);
			if(perms.isEmpty())
				perms.add("");
			configUsers.set(uuid.toString() + ".permissions", perms);
			if(players.containsKey(uuid))
				getUser(uuid).removePerm(permission);
			
		} else {
			perms.add(permission);
			configUsers.set(uuid.toString() + ".permissions", perms);
			if(players.containsKey(uuid))
				getUser(uuid).addPerm(permission);
		}
		try {
			configUsers.save(configFileUsers);
		} catch (Exception e){}
	}
	
	public void updateUserSubrank(UUID uuid, String name, boolean remove) {
		if(name == "")
			return;
		YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
		List<String> subranks = configUsers.getStringList(uuid.toString() + ".subranks");
		if(subranks.contains(""))
			subranks.remove("");
		if(remove)
			subranks.remove(name);
		else
			subranks.add(name);
		if(subranks.isEmpty())
			subranks.add("");
		configUsers.set(uuid.toString() + ".subranks", subranks);
		try {
			configUsers.save(configFileUsers);
		} catch (Exception e){}
		if(players.containsKey(uuid))
			getUser(uuid).refreshPerms();
	}
}