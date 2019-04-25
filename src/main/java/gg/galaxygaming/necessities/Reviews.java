package gg.galaxygaming.necessities;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class Reviews {

    private final File configFileReviews = new File("plugins/Necessities/Creative", "reviews.yml");
    private final Map<String, String> reviews = new HashMap<>();

    void init() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Retrieving stored review requests.");
        YamlConfiguration configReviews = YamlConfiguration.loadConfiguration(configFileReviews);
        //What other information is needed
        for (String key : configReviews.getKeys(false)) {
            if (key != null) {
                String name = Utils.nameFromString(key);
                if (name != null) {
                    reviews.put(name, configReviews.getString(key));
                }
            }
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Stored review requests retrieved.");
    }

    /**
     * Gets the number of pages to the reviews list.
     *
     * @return The number of pages of the reviews list.
     */
    public int reviewPages() {
        return reviews.size() % 10 != 0 ? reviews.size() / 10 + 1 : reviews.size() / 10;
    }

    /**
     * Retrieves the message that corresponds the the specified page and row.
     *
     * @param page The page number to retrieve.
     * @param time The row number to retrieve.
     * @return Gets the message at the specific page and row.
     */
    public String reviewLists(int page, int time) {
        page *= 10;
        if (reviews.size() < time + page + 1 || time == 10) {
            return null;
        }
        String name = reviews.keySet().toArray(new String[0])[page + time];
        return name + " " + reviews.get(name);
    }
}