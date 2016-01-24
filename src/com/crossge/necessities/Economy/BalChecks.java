package com.crossge.necessities.Economy;

import com.crossge.necessities.Formatter;
import com.crossge.necessities.GetUUID;
import com.crossge.necessities.Necessities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BalChecks {
    private File configFileUsers = new File("plugins/Necessities/RankManager", "users.yml");
    private static HashMap<UUID, Double> balances = new HashMap<>();
    Formatter form = new Formatter();
    GetUUID get = new GetUUID();

    public void updateB() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Retrieving all balances.");
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        for (String key : configUsers.getKeys(false))
            if (key != null && !key.equals("null") && !key.equals(""))//TODO: Is this needed or did I fix the null even having ability to show up
                balances.put(UUID.fromString(key), configUsers.getDouble(key + ".balance"));
        try {
            trackAllBals();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Retrieved all balances.");
    }

    public String bal(UUID uuid) {
        return !balances.containsKey(uuid) ? null : form.roundTwoDecimals(balances.get(uuid));
    }

    public double balance(UUID uuid) {
        return uuid == null ? 0.0 : balances.get(uuid);
    }

    public String balTop(int page, int time) {//TODO: Make baltop more efficient
        if (balances.size() < time + page + 1 || time == 10)
            return null;//Check before hand because this means you dont have to sort if it not valid
        ArrayList<Double> balsort = new ArrayList<>();
        for (double doub : balances.values())
            balsort.add(doub);
        if (balsort.size() <= page * 10 + time)
            return null;
        Collections.sort(balsort);
        Collections.reverse(balsort);
        page *= 10;
        int occurrence = 1;
        for (int i = 0; i < page + time; i++)
            if (balsort.get(i).equals(balsort.get(page + time)))
                occurrence++;
        String balSpot = baltopCords(balsort.get(page + time), occurrence);
        if (balSpot == null)
            return null;
        return get.nameFromString(balSpot) + " " + form.roundTwoDecimals(balances.get(UUID.fromString(balSpot)));
    }

    private String baltopCords(double money, int occurrence) {
        int counter = 1;
        for (UUID key : balances.keySet())
            if (balances.get(key) == money) {
                if (counter == occurrence)
                    return key.toString();
                counter++;
            }
        return null;
    }

    public int baltopPages() {
        return balances.size() % 10 != 0 ? (balances.size() / 10) + 1 : (balances.size() / 10);
    }

    public String players() {
        return Integer.toString(balances.size());
    }

    public boolean doesPlayerExist(UUID uuid) {
        return uuid != null && balances.containsKey(uuid);
    }

    public void addPlayerToList(UUID uuid) {
        setMoney(uuid, "500.0");
    }

    public void trackAllBals() throws IOException {
        if (!Necessities.isTracking())
            return;
        File check = new File("plugins/Necessities", "track.yml");
        if (!check.exists())
            check.createNewFile();
        YamlConfiguration configTracker = YamlConfiguration.loadConfiguration(check);
        boolean didSend = configTracker.getBoolean("economy.init", false);
        if (!didSend) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Pushing Economy data.");
            for (UUID id : balances.keySet()) {
                Double money = balances.get(id);
                Necessities.trackActionWithValue(id, "Economy", money, money);
            }
            configTracker.set("economy.init", true);
            configTracker.save(check);
        }
    }

    public void setMoney(UUID uuid, String amount) {
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        if (!form.isLegal(amount))
            return;

        double val = Double.parseDouble(amount);
        if (Necessities.isTracking()) {
            if (balances.containsKey(uuid)) {
                double old = balances.get(uuid);
                double change = val - old;
                Necessities.trackActionWithValue(uuid, "Economy", change, change);
            }
        }
        balances.put(uuid, val);
        configUsers.set(uuid.toString() + ".balance", val);
        try {
            configUsers.save(configFileUsers);
        } catch (Exception e) {
        }
    }

    public void removeMoney(UUID uuid, double amount) {
        setMoney(uuid, Double.toString(Double.parseDouble(bal(uuid)) - amount));
    }

    public void addMoney(UUID uuid, double amount) {
        setMoney(uuid, Double.toString(Double.parseDouble(bal(uuid)) + amount));
    }
}