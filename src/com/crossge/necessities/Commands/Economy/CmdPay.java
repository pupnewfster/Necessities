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

public class CmdPay implements EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 2 || args.length == 0 || !Utils.legalDouble(args[1])) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you enter a player and the amount you want to pay them.");
                return true;
            }
            String targetsName = "";
            GetUUID get = Necessities.getInstance().getUUID();
            UUID uuid = get.getID(args[0]);
            Player target = null;
            if (uuid == null) {
                uuid = get.getOfflineID(args[0]);
                if (uuid == null) {
                    player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That player has not joined the server. If the player is offline, please use the full and most recent name.");
                    return true;
                }
                targetsName = Bukkit.getOfflinePlayer(uuid).getName();
            } else
                target = Bukkit.getPlayer(uuid);
            if (target != null)
                targetsName = target.getName();
            BalChecks balc = Necessities.getInstance().getBalChecks();
            if (!balc.doesPlayerExist(uuid)) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Please enter a valid player to send money to.");
                return true;
            }
            String balance = balc.bal(player.getUniqueId());
            double intBal = Double.parseDouble(balance);
            double payAmount = Math.abs(Double.parseDouble(args[1]));
            if (intBal < payAmount) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You don't have: " + var.getMoney() + "$" + Utils.addCommas(args[1]));
                return true;
            }
            payAmount = Double.parseDouble(Utils.roundTwoDecimals(payAmount));
            balc.removeMoney(player.getUniqueId(), payAmount);
            balc.addMoney(uuid, payAmount);
            player.sendMessage(var.getMessages() + "You paid " + var.getObj() + targetsName + var.getMoney() + " $" + Utils.addCommas(Utils.roundTwoDecimals(payAmount)));
            if (target != null)
                target.sendMessage(var.getMessages() + "You received " + var.getMoney() + "$" + Utils.addCommas(Utils.roundTwoDecimals(payAmount)) + var.getMessages() + " from " + var.getObj() +
                        player.getName() + var.getMessages() + ".");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be logged in to use this command or use cce");
        return true;
    }
}