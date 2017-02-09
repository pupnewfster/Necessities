package gg.galaxygaming.necessities.Economy;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.Rank;
import gg.galaxygaming.necessities.RankManager.RankManager;
import gg.galaxygaming.necessities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;

public class CmdPrices {
    private final File configFilePrices = new File("plugins/Necessities/Economy", "prices.yml");
    private ArrayList<String> co = new ArrayList<>();

    public boolean canBuy(String cmd, String rank) {
        YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
        rank = rank.toUpperCase();
        cmd = cmd.toUpperCase();
        if (!configPrices.contains("commands." + cmd))
            return false;
        String price = configPrices.getString("commands." + cmd);
        RankManager rm = Necessities.getRM();
        return rm.hasRank(rm.getRank(Utils.capFirst(rank)), rm.getRank(Utils.capFirst(price.split(" ")[0])));
    }

    public boolean realCommand(String cmd) {
        return YamlConfiguration.loadConfiguration(configFilePrices).contains("commands." + cmd.toUpperCase());
    }

    private String cost(String cmd) {
        YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
        cmd = cmd.toUpperCase();
        return !configPrices.contains("commands." + cmd) ? null : configPrices.getString("commands." + cmd).split(" ")[1];
    }

    public double getCost(String cmd) {
        String costPerUnit = cost(cmd.toUpperCase());
        return costPerUnit == null ? -1.00 : Double.parseDouble(costPerUnit);
    }

    public void upList() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Retrieving all command prices.");
        YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
        co.clear();
        for (String key : configPrices.getKeys(true))
            if (key.startsWith("commands") && !key.equals("commands"))
                co.add(key.replaceFirst("commands.", "") + " " + configPrices.getString(key));
        ordList();
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Command prices retrieved.");
    }

    private void ordList() {
        ArrayList<String> temp = new ArrayList<>();
        for (Rank r : Necessities.getRM().getOrder())
            for (String cmd : co)
                if (r.getName().toUpperCase().equals(cmd.split(" ")[1]))
                    temp.add(cmd);
        co = temp;
    }

    public void addCommand(String rank, String cmd, String price) {
        YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
        rank = rank.toUpperCase();
        cmd = cmd.toUpperCase();
        configPrices.set("commands" + cmd, rank + " " + price);
        try {
            configPrices.save(configFilePrices);
        } catch (Exception ignored) {
        }
        upList();
    }

    public void removeCommand(String cmd) {
        YamlConfiguration configPrices = YamlConfiguration.loadConfiguration(configFilePrices);
        cmd = cmd.toUpperCase();
        if (configPrices.contains("commands." + cmd))
            configPrices.set("commands." + cmd, null);
        try {
            configPrices.save(configFilePrices);
        } catch (Exception ignored) {
        }
        upList();
    }

    public int priceListPages() {
        return co.size() % 10 != 0 ? (co.size() / 10) + 1 : (co.size() / 10);
    }

    public String priceLists(int page, int time) {
        page *= 10;
        if (co.size() < time + page + 1 || time == 10)
            return null;
        return co.get(page + time);
    }
}