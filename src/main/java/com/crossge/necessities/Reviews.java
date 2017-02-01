package com.crossge.necessities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class Reviews {
    private final File configFileReviews = new File("plugins/Necessities/Creative", "reviews.yml");
    private final HashMap<String, String> reviews = new HashMap<>();

    void parseList() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Retrieving stored review requests.");
        YamlConfiguration configReviews = YamlConfiguration.loadConfiguration(configFileReviews);
        //What other information is needed
        GetUUID get = new GetUUID();
        for (String key : configReviews.getKeys(false))
            if (key != null) {
                String name = get.nameFromString(key);
                if (name != null)
                    reviews.put(name, configReviews.getString(key));
            }
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Stored review requests retrieved.");
    }

    public int priceListPages() {
        return reviews.size() % 10 != 0 ? (reviews.size() / 10) + 1 : (reviews.size() / 10);
    }

    public String priceLists(int page, int time) {
        page *= 10;
        return (reviews.size() < time + page + 1 || time == 10) ? null : reviews.keySet().toArray()[page + time] + " " + reviews.get(reviews.keySet().toArray()[page + time]);
    }
}