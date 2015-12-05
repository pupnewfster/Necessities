package com.crossge.necessities.Economy;

import com.crossge.necessities.GetUUID;
import com.crossge.necessities.Necessities;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BalChecks implements Economy {
    private File configFileUsers = new File("plugins/Necessities/RankManager", "users.yml");
    private static HashMap<UUID, Double> balances = new HashMap<UUID, Double>();
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
        if (!balances.containsKey(uuid))
            return null;
        return form.roundTwoDecimals(balances.get(uuid));
    }

    public double balance(UUID uuid) {
        return uuid == null ? 0.0 : balances.get(uuid);
    }

    public String balTop(int page, int time) {//TODO: Make baltop more efficient
        if (balances.size() < time + page + 1 || time == 10)
            return null;//Check before hand because this means you dont have to sort if it not valid
        ArrayList<Double> balsort = new ArrayList<Double>();
        for (double doub : balances.values())
            balsort.add(doub);
        if (balsort.size() <= page*10 + time)
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
            e.printStackTrace();
        }
    }

    public void removeMoney(UUID uuid, double amount) {
        setMoney(uuid, Double.toString(Double.parseDouble(bal(uuid)) - amount));
    }

    public void addMoney(UUID uuid, double amount) {
        setMoney(uuid, Double.toString(Double.parseDouble(bal(uuid)) + amount));
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "GGEconomy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double v) {
        return form.roundTwoDecimals(v);
    }

    @Override
    public String currencyNamePlural() {
        return "gz";
    }

    @Override
    public String currencyNameSingular() {
        return "gz";
    }

    @Override
    public boolean hasAccount(String s) {
        return doesPlayerExist(get.getOfflineID(s));
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return doesPlayerExist(offlinePlayer.getUniqueId());
    }

    @Override
    public double getBalance(String s) {
        return balance(get.getOfflineID(s));
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return balance(offlinePlayer.getUniqueId());
    }

    @Override
    public boolean has(String s, double v) {
        return getBalance(s) - v >= 0;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        return getBalance(offlinePlayer) - v >= 0;
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        removeMoney(get.getOfflineID(s), v);
        return new EconomyResponse(v, getBalance(s), EconomyResponse.ResponseType.SUCCESS, "no implemented response yet");//TODO: Maybe?
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        removeMoney(offlinePlayer.getUniqueId(), v);
        return new EconomyResponse(v, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, "no implemented response yet");//TODO: Maybe?
    }

    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        addMoney(get.getOfflineID(s), v);
        return new EconomyResponse(v, getBalance(s), EconomyResponse.ResponseType.SUCCESS, "no implemented response yet");//TODO: Maybe?
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        addMoney(offlinePlayer.getUniqueId(), v);
        return new EconomyResponse(v, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, "no implemented response yet");//TODO: Maybe?
    }

    @Override
    public boolean createPlayerAccount(String s) {
        UUID uuid = get.getOfflineID(s);
        if (!doesPlayerExist(uuid)) {
            addPlayerToList(uuid);
            return true;
        } else
            return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        UUID uuid = offlinePlayer.getUniqueId();
        if (!doesPlayerExist(uuid)) {
            addPlayerToList(uuid);
            return true;
        } else
            return false;
    }
//UNUSED METHODS BELOW
    @Override
    public boolean hasAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }

    @Override
    public double getBalance(String s, String s1) {
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        return 0;
    }

    @Override
    public boolean has(String s, String s1, double v) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        return false;
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }
}