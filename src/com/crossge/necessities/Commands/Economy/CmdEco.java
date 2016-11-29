package com.crossge.necessities.Commands.Economy;

import com.crossge.necessities.Economy.BalChecks;
import com.crossge.necessities.GetUUID;
import com.crossge.necessities.Necessities;
import com.crossge.necessities.Utils;
import com.crossge.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdEco implements EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (args.length < 2) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you enter a player and the type of edit you want to do.");
            sender.sendMessage(var.getMessages() + "Valid edits are: reset, give, take, and set.");
            return true;
        }
        String targetsName = "";
        GetUUID get = Necessities.getInstance().getUUID();
        UUID uuid = get.getID(args[1]);
        Player target = null;
        if (uuid == null) {
            uuid = get.getOfflineID(args[1]);
            if (uuid == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That player has not joined the server. If the player is offline, please use the full and most recent name.");
                return true;
            }
            targetsName = Bukkit.getOfflinePlayer(uuid).getName();
        } else
            target = Bukkit.getPlayer(uuid);
        if (target != null)
            targetsName = target.getName();
        BalChecks balc = Necessities.getInstance().getBalChecks();
        if (!balc.doesPlayerExist(uuid)) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Please enter a valid player to change the balance of.");
            return true;
        }
        if (args[0].equalsIgnoreCase("reset")) {
            balc.setMoney(uuid, "0");
            sender.sendMessage(var.getMessages() + "Your successfully reset the balance of " + var.getObj() + targetsName + var.getMessages() + ".");
            return true;
        }
        if (args.length > 2) {
            if (!Utils.legalDouble(args[2])) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid amount of money to modify the player with.");
                return true;
            }
            double amount = Double.parseDouble(args[2]);
            String balance = balc.bal(uuid);
            double intBal = Double.parseDouble(balance);
            amount = Double.parseDouble(Utils.roundTwoDecimals(amount));
            String setAmount = Utils.roundTwoDecimals(amount);
            if (args[0].equalsIgnoreCase("give")) {
                balc.addMoney(uuid, amount);
                sender.sendMessage(var.getMessages() + "Your successfully gave " + var.getMoney() + " $" + Utils.addCommas(setAmount) + var.getMessages() + " to " + var.getObj() + targetsName +
                        var.getMessages() + ".");
            } else if (args[0].equalsIgnoreCase("take") && intBal - amount >= 0) {
                balc.removeMoney(uuid, amount);
                sender.sendMessage(var.getMessages() + "Your successfully took " + var.getMoney() + " $" + Utils.addCommas(setAmount) + var.getMessages() + " from " + var.getObj() + targetsName +
                        var.getMessages() + ".");
            } else if (args[0].equalsIgnoreCase("set")) {
                balc.setMoney(uuid, setAmount);
                sender.sendMessage(var.getMessages() + "Your successfully set the balance of " + var.getObj() + targetsName + var.getMessages() + " to " + var.getMoney() + "$" +
                        Utils.addCommas(Utils.roundTwoDecimals(amount)));
            }
        }
        return true;
    }
}