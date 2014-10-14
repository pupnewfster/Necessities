package com.crossge.necessities.Commands.Economy;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdEco extends EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (args.length > 3 || args.length == 0 || args.length == 1) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you enter a player and the type of edit you want to do.");
            sender.sendMessage(var.getMessages() + "Valid edits are: reset, give, take, and set.");
            return true;
        }
        String targetsname = "";
        UUID uuid = get.getID(args[1]);
        Player target = null;
        if (uuid == null) {
            uuid = get.getOfflineID(args[1]);
            if (uuid == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That player has not joined the server. If the player is offline, please use the full and most recent name.");
                return true;
            }
            targetsname = Bukkit.getOfflinePlayer(uuid).getName();
        } else
            target = sender.getServer().getPlayer(uuid);
        if (target != null)
            targetsname = target.getName();
        if (!balc.doesPlayerExist(uuid)) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Please enter a valid player to change the balance of.");
            return true;
        }
        if (args[0].equalsIgnoreCase("reset")) {
            balc.setMoney(uuid, "0");
            sender.sendMessage(var.getMessages() + "Your successfully reset the balance of " + var.getObj() + targetsname + var.getMessages() + ".");
            return true;
        }
        if (args.length == 3) {
            if (!form.isLegal(args[2])) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid amount of money to modify the player with.");
                return true;
            }
            double amount = Double.parseDouble(args[2]);
            String balance = balc.bal(uuid);
            double intbal = Double.parseDouble(balance);
            amount = Double.parseDouble(form.roundTwoDecimals(amount));
            String setamount = form.roundTwoDecimals(amount);
            if (args[0].equalsIgnoreCase("give")) {
                balc.addMoney(uuid, amount);
                sender.sendMessage(var.getMessages() + "Your successfully gave " + var.getMoney() + " $" + form.addCommas(setamount) + var.getMessages() + " to " +
                        var.getObj() + targetsname + var.getMessages() + ".");
            } else if (args[0].equalsIgnoreCase("take") && intbal - amount >= 0) {
                balc.removeMoney(uuid, amount);
                sender.sendMessage(var.getMessages() + "Your successfully took " + var.getMoney() + " $" + form.addCommas(setamount) + var.getMessages() + " from " +
                        var.getObj() + targetsname + var.getMessages() + ".");
            } else if (args[0].equalsIgnoreCase("set")) {
                balc.setMoney(uuid, setamount);
                sender.sendMessage(var.getMessages() + "Your successfully set the balance of " + var.getObj() + targetsname + var.getMessages() + " to " +
                        var.getMoney() + "$" + form.addCommas(form.roundTwoDecimals(amount)));
            }
        }
        return true;
    }
}