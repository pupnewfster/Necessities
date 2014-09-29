package com.crossge.necessities.Economy;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import com.crossge.necessities.RankManager.Rank;
import com.crossge.necessities.RankManager.RankManager;

public class CmdPrices {
	private File configFilePrices = new File("plugins/Necessities/Economy", "prices.yml");
   	private static ArrayList<String> co = new ArrayList<String>();
   	RankPrices rp = new RankPrices();
   	RankManager rm = new RankManager();
   	Formatter form = new Formatter();
   	
	public boolean canBuy(String cmd, String rank) {
		YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
		rank = rank.toUpperCase();
		cmd = cmd.toUpperCase();
	   	if(!configPrices.contains("commands." + cmd))
	   		return false;
		String price = configPrices.getString("commands." + cmd);
		Rank cmdRank = rm.getRank(form.capFirst(price.split(" ")[0]));
		Rank r = rm.getRank(form.capFirst(rank));
		if(rm.hasRank(r, cmdRank))
			return true;
		return false;
	}
	
	public boolean realCommand(String cmd) {
		YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
		cmd = cmd.toUpperCase();
		return configPrices.contains("commands." + cmd);
	}
	
	public String cost(String cmd) {
		YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
		cmd = cmd.toUpperCase();
	   	if(!configPrices.contains("commands." + cmd))
	   		return null;
	   	String price = configPrices.getString("commands." + cmd);
		return price.split(" ")[1];
	}
	
	public double getCost(String cmd) {
		cmd = cmd.toUpperCase();
		String costPerUnit = cost(cmd);
		if(costPerUnit == null)
			return -1.00;
		double cost = Double.parseDouble(costPerUnit);
		return cost;
	}
	
	public void upList() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Retrieving all command prices.");
		YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
		co.clear();
		for(String key : configPrices.getKeys(true))
			if(key.startsWith("commands") && !key.equals("commands"))
				co.add(key.replaceFirst("commands.", "") + " " + configPrices.getString(key));
		ordList();
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Command prices retrieved.");
	}
	
	private void ordList() {
		ArrayList<String> temp = new ArrayList<String>();
		for(Rank r : rm.getOrder())
			for(String cmd : co)
				if(r.getName().toUpperCase().equals(cmd.split(" ")[1]))
					temp.add(cmd);
		co = temp;
	}
	
	public void addCommand(String rank, String cmd, String price) {
		YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
		rank = rank.toUpperCase();
		cmd = cmd.toUpperCase();
		configPrices.set("commands" + cmd, rank + " " + price);
	   	try {
	   		configPrices.save(configFilePrices);
		} catch (Exception e){}
	   	upList();
	}
	
	public void removeCommand(String cmd) {
		YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
		cmd = cmd.toUpperCase();
	   	if(configPrices.contains("commands." + cmd))
	   		configPrices.set("commands." + cmd, null);
	   	try {
	   		configPrices.save(configFilePrices);
		} catch (Exception e) {}
	   	upList();
	}
	
	public int priceListPages() {
		int rounder = 0;
		if (co.size() % 10 != 0)
			rounder = 1;
		return (co.size() / 10) + rounder;
	}
	
	public String priceLists(int page, int time) {
		page *= 10;
		if (co.size() < time + page + 1 || time == 10)
			return null;
		return co.get(page + time);
	}
}