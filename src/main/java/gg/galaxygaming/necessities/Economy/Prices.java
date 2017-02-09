package gg.galaxygaming.necessities.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class Prices {//TODO: Ability to set price for damage values probably use a dash or something in storing data value
    private final File configFilePrices = new File("plugins/Necessities/Economy", "prices.yml");
    private final HashMap<String, Double> sellPrices = new HashMap<>();
    private final HashMap<String, Double> buyPrices = new HashMap<>();
    private final HashMap<String, String> price = new HashMap<>();

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
        if (Material.fromString(itemName) == null)
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
        } catch (Exception ignored) {
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

    public int priceListPages() {
        return price.size() % 10 != 0 ? (price.size() / 10) + 1 : (price.size() / 10);
    }

    public String priceLists(int page, int time) {//TODO: Make this more efficient
        page *= 10;
        return (price.size() < time + page + 1 || time == 10) ? null : price.keySet().toArray()[page + time] + " " + price.get(price.keySet().toArray()[page + time]);
    }
}