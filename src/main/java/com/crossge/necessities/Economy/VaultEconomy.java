package com.crossge.necessities.Economy;

import com.crossge.necessities.Necessities;
import com.crossge.necessities.Utils;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

public class VaultEconomy implements Economy {
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
        return Utils.roundTwoDecimals(v);
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
        return Necessities.getInstance().getBalChecks().doesPlayerExist(Necessities.getInstance().getUUID().getOfflineID(s));
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return Necessities.getInstance().getBalChecks().doesPlayerExist(offlinePlayer.getUniqueId());
    }

    @Override
    public double getBalance(String s) {
        return Necessities.getInstance().getBalChecks().balance(Necessities.getInstance().getUUID().getOfflineID(s));
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return Necessities.getInstance().getBalChecks().balance(offlinePlayer.getUniqueId());
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
        Necessities.getInstance().getBalChecks().removeMoney(Necessities.getInstance().getUUID().getOfflineID(s), v);
        return new EconomyResponse(v, getBalance(s), EconomyResponse.ResponseType.SUCCESS, "no implemented response yet");//TODO: Maybe?
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        Necessities.getInstance().getBalChecks().removeMoney(offlinePlayer.getUniqueId(), v);
        return new EconomyResponse(v, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, "no implemented response yet");//TODO: Maybe?
    }

    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        Necessities.getInstance().getBalChecks().addMoney(Necessities.getInstance().getUUID().getOfflineID(s), v);
        return new EconomyResponse(v, getBalance(s), EconomyResponse.ResponseType.SUCCESS, "no implemented response yet");//TODO: Maybe?
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        Necessities.getInstance().getBalChecks().addMoney(offlinePlayer.getUniqueId(), v);
        return new EconomyResponse(v, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, "no implemented response yet");//TODO: Maybe?
    }

    @Override
    public boolean createPlayerAccount(String s) {
        UUID uuid = Necessities.getInstance().getUUID().getOfflineID(s);
        BalChecks balc = Necessities.getInstance().getBalChecks();
        if (!balc.doesPlayerExist(uuid)) {
            balc.addPlayerToList(uuid);
            return true;
        } else
            return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        UUID uuid = offlinePlayer.getUniqueId();
        BalChecks balc = Necessities.getInstance().getBalChecks();
        if (!balc.doesPlayerExist(uuid)) {
            balc.addPlayerToList(uuid);
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