package gg.galaxygaming.necessities.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class Prices {
    private final File configFilePrices = new File("plugins/Necessities/Economy", "prices.yml");
    private final HashMap<String, Double> sellPrices = new HashMap<>();
    private final HashMap<String, Double> buyPrices = new HashMap<>();
    private final HashMap<String, String> price = new HashMap<>();

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
        if (price.equalsIgnoreCase("null")) {
            sellPrices.remove(itemName);
            configPrices.set(direction + "." + itemName, null);
        } else {
            sellPrices.put(itemName, Double.parseDouble(price));
            configPrices.set(direction + "." + itemName, Double.parseDouble(price));
        }
        try {
            configPrices.save(configFilePrices);
        } catch (Exception ignored) {
        }
        if (buyPrices.containsKey(itemName) && sellPrices.containsKey(itemName))
            this.price.put(itemName, Double.toString(buyPrices.get(itemName)) + " " + Double.toString(sellPrices.get(itemName)));
        else if (buyPrices.containsKey(itemName))
            this.price.put(itemName, Double.toString(buyPrices.get(itemName)) + " null");
        else if (sellPrices.containsKey(itemName))
            this.price.put(itemName, "null " + Double.toString(sellPrices.get(itemName)));
        else
            this.price.remove(itemName);
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
                    price.put(tempKey, Double.toString(buyPrices.get(tempKey)) + " " + price.get(tempKey).replaceAll("null ", ""));
                else
                    price.put(tempKey, Double.toString(buyPrices.get(tempKey)) + " null");
            } else if (key.startsWith("sell") && !key.equals("sell")) {
                String tempKey = key.replaceFirst("sell.", "");
                sellPrices.put(tempKey, configPrices.getDouble(key));
                if (price.containsKey(tempKey))
                    price.put(tempKey, price.get(tempKey).replaceAll("null", "") + Double.toString(sellPrices.get(tempKey)));
                else
                    price.put(tempKey, "null " + Double.toString(sellPrices.get(tempKey)));
            }
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Item prices retrieved.");
    }

    /**
     * Gets the number of pages to the price list.
     * @return The number of pages of the price list.
     */
    public int priceListPages() {
        return price.size() % 10 != 0 ? (price.size() / 10) + 1 : (price.size() / 10);
    }

    /**
     * Retrieves the message that corresponds the the specified page and row.
     * @param page The page number to retrieve.
     * @param time The row number to retrieve.
     * @return Gets the message at the specific page and row.
     */
    public String priceLists(int page, int time) {//TODO: Make this more efficient
        page *= 10;
        return (price.size() < time + page + 1 || time == 10) ? null : price.keySet().toArray()[page + time] + " " + price.get(price.keySet().toArray()[page + time]);
    }
}