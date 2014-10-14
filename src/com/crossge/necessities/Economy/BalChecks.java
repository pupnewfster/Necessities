package com.crossge.necessities.Economy;

import com.crossge.necessities.GetUUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

public class BalChecks {
    private static HashMap<UUID, Double> balances = new HashMap<UUID, Double>();
    Formatter form = new Formatter();
    GetUUID get = new GetUUID();
    private File configFileUsers = new File("plugins/Necessities/RankManager", "users.yml");

    public void updateB() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Retrieving all balances.");
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        for (String key : configUsers.getKeys(false))
            if (key != null && !key.equals("null") && !key.equals(""))//TODO: Is this needed or did I fix the null even having ability to show up
                balances.put(UUID.fromString(key), configUsers.getDouble(key + ".balance"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Retrieved all balances.");
    }

    public String bal(UUID uuid) {
        if (!balances.containsKey(uuid))
            return null;
        return form.roundTwoDecimals(balances.get(uuid));
    }

    public String balTop(int page, int time) {//TODO: Make baltop more efficient
        if (balances.size() < time + page + 1 || time == 10)
            return null;//Check before hand because this means you dont have to sort if it not valid
        ArrayList<Double> balsort = new ArrayList<Double>();
        for (double doub : balances.values())
            balsort.add(doub);
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
        int rounder = 0;
        if (balances.size() % 10 != 0)
            rounder = 1;
        return (balances.size() / 10) + rounder;
    }

    public String players() {
        return Integer.toString(balances.size());
    }

    public boolean doesPlayerExist(UUID uuid) {
        return balances.containsKey(uuid);
    }

    public void addPlayerToList(UUID uuid) {
        setMoney(uuid, "500.0");
    }

    public void setMoney(UUID uuid, String amount) {
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        if (!form.isLegal(amount))
            return;
        balances.put(uuid, Double.parseDouble(amount));
        configUsers.set(uuid.toString() + ".balance", Double.parseDouble(amount));
        try {
            configUsers.save(configFileUsers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeMoney(UUID uuid, double amount) {
        setMoney(uuid, Double.toString(Double.parseDouble(bal(uuid)) - amount));
    }

    public void addMoney(UUID uuid, double amount) {
        setMoney(uuid, Double.toString(Double.parseDouble(bal(uuid)) + amount));
    }
}