package gg.galaxygaming.necessities.Economy;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.util.List;

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
        return Necessities.getEconomy().doesPlayerExist(Utils.getOfflineID(s));
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return Necessities.getEconomy().doesPlayerExist(offlinePlayer.getUniqueId());
    }

    @Override
    public double getBalance(String s) {
        return Necessities.getEconomy().getBalance(Utils.getOfflineID(s));
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return Necessities.getEconomy().getBalance(offlinePlayer.getUniqueId());
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
        Necessities.getEconomy().removeMoney(Utils.getOfflineID(s), v);
        return new EconomyResponse(v, getBalance(s), EconomyResponse.ResponseType.SUCCESS, "no implemented response yet");//TODO: Maybe?
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        Necessities.getEconomy().removeMoney(offlinePlayer.getUniqueId(), v);
        return new EconomyResponse(v, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, "no implemented response yet");//TODO: Maybe?
    }

    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        Necessities.getEconomy().addMoney(Utils.getOfflineID(s), v);
        return new EconomyResponse(v, getBalance(s), EconomyResponse.ResponseType.SUCCESS, "no implemented response yet");//TODO: Maybe?
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        Necessities.getEconomy().addMoney(offlinePlayer.getUniqueId(), v);
        return new EconomyResponse(v, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, "no implemented response yet");//TODO: Maybe?
    }

    @Override
    public boolean createPlayerAccount(String s) {
        return Necessities.getEconomy().addPlayerIfNotExists(Utils.getOfflineID(s));
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return Necessities.getEconomy().addPlayerIfNotExists(offlinePlayer.getUniqueId());
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