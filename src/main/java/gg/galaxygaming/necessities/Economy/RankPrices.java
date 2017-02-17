package gg.galaxygaming.necessities.Economy;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class RankPrices {
    private final File configFilePrices = new File("plugins/Necessities/Economy", "prices.yml");
    private final HashMap<String, Double> rankPrices = new HashMap<>();

    /**
     * Initialize and load the rank prices.
     */
    public void initiate() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Retrieving all rank prices.");
        YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
        for (String key : configPrices.getKeys(true))
            if (key.startsWith("ranks") && !key.equals("ranks"))
                rankPrices.put(key.replaceFirst("ranks.", ""), configPrices.getDouble(key));
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "All rank prices retrieved.");
    }

    /**
     * Checks if a given rank is able to be bought.
     * @param rankName The name of the rank to check.
     * @return True if the rank can be sold, false otherwise.
     */
    public boolean rankBuyable(String rankName) {
        return rankPrices.containsKey(rankName.toUpperCase());
    }

    private String cost(String rankName) {
        rankName = rankName.toUpperCase();
        return !rankPrices.containsKey(rankName) ? null : Double.toString(rankPrices.get(rankName));
    }

    /**
     * Gets the price of the specified rank.
     * @param rankName The rank to check the price of.
     * @return The price of the rank or null if it cannot be bought.
     */
    public double getPrice(String rankName) {
        String costPerUnit = cost(rankName.toUpperCase());
        return costPerUnit == null ? -1.00 : Double.parseDouble(costPerUnit);
    }

    /**
     * Removes the specified rank from being able to be sold.
     * @param rankName The rank to remove from being able to be sold.
     */
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

    /**
     * Sets a price for the specified rank.
     * @param rankName The rank to set a buy price for.
     * @param price    The price to set the rank at.
     */
    public void setPrice(String rankName, String price) {
        YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
        rankName = rankName.toUpperCase();
        rankPrices.put(rankName, Double.parseDouble(price));
        configPrices.set("ranks." + rankName, Double.parseDouble(price));
        try {
            configPrices.save(configFilePrices);
        } catch (Exception ignored) {
        }
    }

    /**
     * Gets the number of pages to the price list.
     * @return The number of pages of the price list.
     */
    public int priceListPages() {
        return rankPrices.size() % 10 != 0 ? (rankPrices.size() / 10) + 1 : (rankPrices.size() / 10);
    }

    /**
     * Retrieves the message that corresponds the the specified page and row.
     * @param page The page number to retrieve.
     * @param time The row number to retrieve.
     * @return Gets the message at the specific page and row.
     */
    public String priceLists(int page, int time) {
        page *= 10;
        if (rankPrices.size() < time + page + 1 || time == 10)
            return null;
        Rank r = Necessities.getRM().getRank(page + time + 1);
        return r == null ? null : r.getName() + " " + rankPrices.get(r.getName().toUpperCase());
    }
}