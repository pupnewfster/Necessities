package gg.galaxygaming.necessities.Economy;

import gg.galaxygaming.necessities.Material.Material;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Prices {
    private final File configFilePrices = new File("plugins/Necessities/Economy", "prices.yml");
    private final HashMap<String, Double> sellPrices = new HashMap<>();
    private final HashMap<String, Double> buyPrices = new HashMap<>();
    private final HashMap<String, Boolean> price = new HashMap<>();
    private final ArrayList<String> priceOrder = new ArrayList<>();
    //TODO should price be replaced with just checking sellPrices and buyPrices instead of duplicating the data. Probably

    /**
     * Gets the buy or sell price of an item.
     * @param direction Either buy or sell depending on which price is being looked up.
     * @param itemName  The name of the item to look for the price of.
     * @return The price or null if the item cannot be bought or sold.
     */
    public String cost(String direction, String itemName) {
        itemName = itemName.toUpperCase().replaceAll("_", "");
        if (direction.equals("sell") && sellPrices.containsKey(itemName))
            return Double.toString(sellPrices.get(itemName));
        else if (direction.equals("buy") && buyPrices.containsKey(itemName))
            return Double.toString(buyPrices.get(itemName));
        return null;
    }

    /**
     * Gets the total buy or sell price of a specified amount of the given item.
     * @param direction Either buy or sell depending on which price is being looked up.
     * @param itemName  The name of the item to look for the price of.
     * @param amount    The number of items to multiply the price by.
     * @return The price of the items or null if the item cannot be bought or sold.
     */
    public double getPrice(String direction, String itemName, int amount) {
        String costPerUnit = cost(direction, itemName);
        return costPerUnit == null ? -1.00 : Double.parseDouble(costPerUnit) * amount;
    }

    /**
     * Sets the buy/sell price of a given item.
     * @param direction Either buy or sell depending on which price is being set.
     * @param itemName  The item name to set the price of.
     * @param price     The price to set the item at.
     */
    public void setPrice(String direction, String itemName, String price) {
        YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
        itemName = itemName.toUpperCase().replaceAll("_", "");
        if (Material.fromString(itemName) == null)
            return;
        boolean isSell = false;
        if (direction.equals("sell"))
            isSell = true;
        if (price.equalsIgnoreCase("null")) {
            if (isSell)
                sellPrices.remove(itemName);
            else
                buyPrices.remove(itemName);
            boolean hasPrice = this.price.getOrDefault(itemName, false);
            if (hasPrice)
                this.price.put(itemName, false);
            else {
                this.price.remove(itemName);
                this.priceOrder.remove(itemName);
            }
            configPrices.set(direction + '.' + itemName, null);
        } else {
            if (isSell) {
                sellPrices.put(itemName, Double.parseDouble(price));
                if (buyPrices.containsKey(itemName)) {
                    this.price.put(itemName, true);
                } else
                    this.price.put(itemName, false);
            } else {
                buyPrices.put(itemName, Double.parseDouble(price));
                if (sellPrices.containsKey(itemName)) {
                    this.price.put(itemName, true);
                } else
                    this.price.put(itemName, false);
            }
            if (!priceOrder.contains(itemName)) {
                priceOrder.add(itemName);
                Collections.sort(priceOrder);
            }
            configPrices.set(direction + '.' + itemName, Double.parseDouble(price));
        }
        try {
            configPrices.save(configFilePrices);
        } catch (Exception ignored) {
        }
    }

    /**
     * Initialize and load the prices.
     */
    public void init() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Retrieving item prices.");
        YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
        for (String key : configPrices.getKeys(true))
            if (key.startsWith("buy") && !key.equals("buy")) {
                String tempKey = key.replaceFirst("buy.", "");
                buyPrices.put(tempKey, configPrices.getDouble(key));
                if (price.containsKey(tempKey))
                    price.put(tempKey, true);
                else {
                    price.put(tempKey, false);
                    priceOrder.add(tempKey);
                }
            } else if (key.startsWith("sell") && !key.equals("sell")) {
                String tempKey = key.replaceFirst("sell.", "");
                sellPrices.put(tempKey, configPrices.getDouble(key));
                if (price.containsKey(tempKey))
                    price.put(tempKey, true);
                else {
                    price.put(tempKey, false);
                    priceOrder.add(tempKey);
                }
            }
        Collections.sort(priceOrder);
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Item prices retrieved.");
    }

    /**
     * Gets the number of pages to the price list.
     * @return The number of pages of the price list.
     */
    public int priceListPages() {
        return priceOrder.size() % 10 != 0 ? priceOrder.size() / 10 + 1 : priceOrder.size() / 10;
    }

    /**
     * Retrieves the message that corresponds the the specified page and row.
     * @param page The page number to retrieve.
     * @param time The row number to retrieve.
     * @return Gets the message at the specific page and row.
     */
    public String priceLists(int page, int time) {
        page *= 10;
        if (priceOrder.size() < time + page + 1 || time == 10)
            return null;
        String item = priceOrder.get(page + time);
        double buy = buyPrices.getOrDefault(item, -1.0), sell = sellPrices.getOrDefault(item, -1.0);
        return item + ' ' + (buy == -1 ? "null" : Double.toString(buy)) + ' ' + (sell == -1 ? "null" : Double.toString(sell));
    }
}