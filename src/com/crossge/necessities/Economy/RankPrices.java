package com.crossge.necessities.Economy;

import com.crossge.necessities.Necessities;
import com.crossge.necessities.RankManager.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class RankPrices {
    private final File configFilePrices = new File("plugins/Necessities/Economy", "prices.yml");
    private final HashMap<String, Double> rankPrices = new HashMap<>();

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

    private String cost(String rankName) {
        rankName = rankName.toUpperCase();
        return !rankPrices.containsKey(rankName) ? null : Double.toString(rankPrices.get(rankName));
    }

    public double getCost(String rankName) {
        rankName = rankName.toUpperCase();
        String costPerUnit = cost(rankName);
        return costPerUnit == null ? -1.00 : Double.parseDouble(costPerUnit);
    }

    public void rCost(String rankName) {
        YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
        rankName = rankName.toUpperCase();
        rankPrices.remove(rankName + " " + cost(rankName));
        configPrices.set("ranks." + rankName, null);
        try {
            configPrices.save(configFilePrices);
        } catch (Exception ignored) {
        }
    }

    public void setCost(String rankName, String amount) {
        YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
        rankName = rankName.toUpperCase();
        rankPrices.put(rankName, Double.parseDouble(amount));
        configPrices.set("ranks." + rankName, Double.parseDouble(amount));
        try {
            configPrices.save(configFilePrices);
        } catch (Exception ignored) {
        }
    }

    public int priceListPages() {
        return rankPrices.size() % 10 != 0 ? (rankPrices.size() / 10) + 1 : (rankPrices.size() / 10);
    }

    public String priceLists(int page, int time) {
        page *= 10;
        if (rankPrices.size() < time + page + 1 || time == 10)
            return null;
        Rank r = Necessities.getInstance().getRM().getRank(page + time + 1);
        return r == null ? null : r.getName() + " " + rankPrices.get(r.getName().toUpperCase());
    }
}