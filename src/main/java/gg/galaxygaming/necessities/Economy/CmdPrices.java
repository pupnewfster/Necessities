package gg.galaxygaming.necessities.Economy;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.Rank;
import gg.galaxygaming.necessities.RankManager.RankManager;
import gg.galaxygaming.necessities.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class CmdPrices {

    private final File configFilePrices = new File("plugins/Necessities/Economy", "prices.yml");
    private List<String> co = new ArrayList<>();

    /**
     * Checks if the specified rank can buy the specified command.
     *
     * @param cmd The command to check.
     * @param rank The rank to check.
     * @return True if the rank can buy the command, else false.
     */
    public boolean canBuy(String cmd, String rank) {
        YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
        rank = rank.toUpperCase();
        cmd = cmd.toUpperCase();
        if (!configPrices.contains("commands." + cmd)) {
            return false;
        }
        String price = configPrices.getString("commands." + cmd);
        RankManager rm = Necessities.getRM();
        return rm.hasRank(rm.getRank(Utils.capFirst(rank)), rm.getRank(Utils.capFirst(price.split(" ")[0])));
    }

    /**
     * Checks if the given command is a command that is for sale.
     *
     * @param cmd The command to check.
     * @return True if the command is for sale, false otherwise.
     */
    public boolean realCommand(String cmd) {
        return YamlConfiguration.loadConfiguration(configFilePrices).contains("commands." + cmd.toUpperCase());
    }

    private String cost(String cmd) {
        YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
        cmd = cmd.toUpperCase();
        return !configPrices.contains("commands." + cmd) ? null
              : configPrices.getString("commands." + cmd).split(" ")[1];
    }

    /**
     * Gets the price of the specified command.
     *
     * @param cmd The command to get the price of.
     * @return The price of the command.
     */
    public double getPrice(String cmd) {
        String costPerUnit = cost(cmd.toUpperCase());
        return costPerUnit == null ? -1.00 : Double.parseDouble(costPerUnit);
    }

    /**
     * Initialize and load the prices for commands.
     */
    public void init() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Retrieving all command prices.");
        YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
        this.co.clear();
        for (String key : configPrices.getKeys(true)) {
            if (key.startsWith("commands") && !key.equals("commands")) {
                this.co.add(key.replaceFirst("commands.", "") + ' ' + configPrices.getString(key));
            }
        }
        List<String> temp = new ArrayList<>();
        for (Rank r : Necessities.getRM().getOrder()) {
            for (String cmd : this.co) {
                if (r.getName().toUpperCase().equals(cmd.split(" ")[1])) {
                    temp.add(cmd);
                }
            }
        }
        this.co = temp;
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Command prices retrieved.");
    }

    /**
     * Adds a command with a specified rank requirement at the given price.
     *
     * @param rank The rank required.
     * @param cmd The command to add a price for.
     * @param price The price the command is being sold for.
     */
    public void addCommand(String rank, String cmd, String price) {
        YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
        rank = rank.toUpperCase();
        cmd = cmd.toUpperCase();
        configPrices.set("commands" + cmd, rank + ' ' + price);
        try {
            configPrices.save(configFilePrices);
        } catch (Exception ignored) {
        }
        init();
    }

    /**
     * Removes the specified command from being able to be sold.
     *
     * @param cmd The command to remove from being sold.
     */
    public void removeCommand(String cmd) {
        YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
        cmd = cmd.toUpperCase();
        if (configPrices.contains("commands." + cmd)) {
            configPrices.set("commands." + cmd, null);
        }
        try {
            configPrices.save(configFilePrices);
        } catch (Exception ignored) {
        }
        init();
    }

    /**
     * Gets the number of pages to the price list.
     *
     * @return The number of pages of the price list.
     */
    public int priceListPages() {
        return co.size() % 10 != 0 ? co.size() / 10 + 1 : co.size() / 10;
    }

    /**
     * Retrieves the message that corresponds the the specified page and row.
     *
     * @param page The page number to retrieve.
     * @param time The row number to retrieve.
     * @return Gets the message at the specific page and row.
     */
    public String priceLists(int page, int time) {
        page *= 10;
        if (co.size() < time + page + 1 || time == 10) {
            return null;
        }
        return co.get(page + time);
    }
}