package com.crossge.necessities.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class Prices {
    private File configFilePrices = new File("plugins/Necessities/Economy", "prices.yml");
    private static HashMap<String, Double> sellPrices = new HashMap<>();
    private static HashMap<String, Double> buyPrices = new HashMap<>();
    private static HashMap<String, String> price = new HashMap<>();
    Materials mat = new Materials();//TODO: Ability to set price for damage values probs use a dash or something in storing datavalue

    public String cost(String direction, String itemName) {
        itemName = itemName.toUpperCase().replaceAll("_", "");
        if (direction.equals("sell") && sellPrices.containsKey(itemName))
            return Double.toString(sellPrices.get(itemName));
        if (direction.equals("buy") && buyPrices.containsKey(itemName))
            return Double.toString(buyPrices.get(itemName));
        return null;
    }

    public double getCost(String direction, String itemName, int amount) {
        String costPerUnit = cost(direction, itemName);
        return costPerUnit == null ? -1.00 : Double.parseDouble(costPerUnit) * amount;
    }

    public void setCost(String direction, String itemName, String amount) {
        YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
        itemName = itemName.toUpperCase().replaceAll("_", "");
        if (mat.findItem(itemName) == null)
            return;
        if (amount.equalsIgnoreCase("null")) {
            sellPrices.remove(itemName);
            configPrices.set(direction + "." + itemName, null);
        } else {
            sellPrices.put(itemName, Double.parseDouble(amount));
            configPrices.set(direction + "." + itemName, Double.parseDouble(amount));
        }
        try {
            configPrices.save(configFilePrices);
        } catch (Exception e) {
        }
        if (buyPrices.containsKey(itemName) && sellPrices.containsKey(itemName))
            price.put(itemName, Double.toString(buyPrices.get(itemName)) + " " + Double.toString(sellPrices.get(itemName)));
        else if (buyPrices.containsKey(itemName))
            price.put(itemName, Double.toString(buyPrices.get(itemName)) + " null");
        else if (sellPrices.containsKey(itemName))
            price.put(itemName, "null " + Double.toString(sellPrices.get(itemName)));
        else
            price.remove(itemName);
    }

    public void parseList() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Retrieving item prices.");
        YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
        for (String key : configPrices.getKeys(true))
            if (key.startsWith("buy") && !key.equals("buy")) {
                String tempkey = key.replaceFirst("buy.", "");
                buyPrices.put(tempkey, configPrices.getDouble(key));
                if (price.containsKey(tempkey))
                    price.put(tempkey, Double.toString(buyPrices.get(tempkey)) + " " + price.get(tempkey).replaceAll("null ", ""));
                else
                    price.put(tempkey, Double.toString(buyPrices.get(tempkey)) + " null");
            } else if (key.startsWith("sell") && !key.equals("sell")) {
                String tempkey = key.replaceFirst("sell.", "");
                sellPrices.put(tempkey, configPrices.getDouble(key));
                if (price.containsKey(tempkey))
                    price.put(tempkey, price.get(tempkey).replaceAll("null", "") + Double.toString(sellPrices.get(tempkey)));
                else
                    price.put(tempkey, "null " + Double.toString(sellPrices.get(tempkey)));
            }
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Item prices retrieved.");
    }

    public int priceListPages() {
        return price.size() % 10 != 0 ? (price.size() / 10) + 1 : (price.size() / 10);
    }

    public String priceLists(int page, int time) {//TODO: Make this more efficient
        page *= 10;
        return (price.size() < time + page + 1 || time == 10) ? null : price.keySet().toArray()[page + time] + " " + price.get(price.keySet().toArray()[page + time]);
    }
}