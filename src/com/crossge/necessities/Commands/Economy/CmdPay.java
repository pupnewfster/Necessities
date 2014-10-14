package com.crossge.necessities.Commands.Economy;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdPay extends EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 2 || args.length == 0 || !form.isLegal(args[1])) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you enter a player and the amount you want to pay them.");
                return true;
            }
            String targetsname = "";
            UUID uuid = get.getID(args[0]);
            Player target = null;
            if (uuid == null) {
                uuid = get.getOfflineID(args[0]);
                if (uuid == null) {
                    player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That player has not joined the server. If the player is offline, please use the full and most recent name.");
                    return true;
                }
                targetsname = Bukkit.getOfflinePlayer(uuid).getName();
            } else
                target = sender.getServer().getPlayer(uuid);
            if (target != null)
                targetsname = target.getName();
            if (!balc.doesPlayerExist(uuid)) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Please enter a valid player to send money to.");
                return true;
            }
            String balance = balc.bal(player.getUniqueId());
            double intbal = Double.parseDouble(balance);
            double payamount = Math.abs(Double.parseDouble(args[1]));
            if (intbal < payamount) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You dont have: " + var.getMoney() + "$" + form.addCommas(args[1]));
                return true;
            }
            payamount = Double.parseDouble(form.roundTwoDecimals(payamount));
            balc.removeMoney(player.getUniqueId(), payamount);
            balc.addMoney(uuid, payamount);
            player.sendMessage(var.getMessages() + "You paid " + var.getObj() + targetsname + var.getMoney() + " $" + form.addCommas(form.roundTwoDecimals(payamount)));
            if (target != null)
                target.sendMessage(var.getMessages() + "You received " + var.getMoney() + "$" + form.addCommas(form.roundTwoDecimals(payamount)) + var.getMessages() +
                        " from " + var.getObj() + player.getName() + var.getMessages() + ".");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be logged in to use this command or use cce");
        return true;
    }
}