package com.crossge.necessities.Economy;

import com.crossge.necessities.RankManager.Rank;
import com.crossge.necessities.RankManager.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class RankPrices {
    private static HashMap<String, Double> rankPrices = new HashMap<String, Double>();
    RankManager rm = new RankManager();
    private File configFilePrices = new File("plugins/Necessities/Economy", "prices.yml");

    public void initiate() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Retrieving all rank prices.");
        YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
        for (String key : configPrices.getKeys(true))
            if (key.startsWith("ranks") && !key.equals("ranks"))
                rankPrices.put(key.replaceFirst("ranks.", ""), configPrices.getDouble(key));
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "All rank prices retrieved.");
    }

    public boolean rankBuyable(String rankName) {
        rankName = rankName.toUpperCase();
        return rankPrices.containsKey(rankName);
    }

    public String cost(String rankName) {
        rankName = rankName.toUpperCase();
        if (!rankPrices.containsKey(rankName))
            return null;
        return Double.toString(rankPrices.get(rankName));
    }

    public double getCost(String rankName) {
        rankName = rankName.toUpperCase();
        String costPerUnit = cost(rankName);
        if (costPerUnit == null)
            return -1.00;
        return Double.parseDouble(costPerUnit);
    }

    public void rCost(String rankName) {
        YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
        rankName = rankName.toUpperCase();
        rankPrices.remove(rankName + " " + cost(rankName));
        configPrices.set("ranks." + rankName, null);
        try {
            configPrices.save(configFilePrices);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCost(String rankName, String amount) {
        YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
        rankName = rankName.toUpperCase();
        rankPrices.put(rankName, Double.parseDouble(amount));
        configPrices.set("ranks." + rankName, Double.parseDouble(amount));
        try {
            configPrices.save(configFilePrices);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int priceListPages() {
        int rounder = 0;
        if (rankPrices.size() % 10 != 0)
            rounder = 1;
        return (rankPrices.size() / 10) + rounder;
    }

    public String priceLists(int page, int time) {
        page *= 10;
        if (rankPrices.size() < time + page + 1 || time == 10)
            return null;
        Rank r = rm.getRank(page + time + 1);
        if (r == null)
            return null;
        return r.getName() + " " + rankPrices.get(r.getName().toUpperCase());
    }
}