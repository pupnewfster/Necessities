package gg.galaxygaming.necessities.Commands.Economy;

import gg.galaxygaming.necessities.Economy.Economy;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
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
            UUID uuid = Utils.getID(args[0]);
            Player target = null;
            if (uuid == null) {
                uuid = Utils.getOfflineID(args[0]);
                if (uuid == null || !Bukkit.getOfflinePlayer(uuid).hasPlayedBefore()) {
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That player does not exist or has not joined the server. If the player is offline, please use the full and most recent name.");
                    return true;
                }
                targetsName = Bukkit.getOfflinePlayer(uuid).getName();
            } else
                target = Bukkit.getPlayer(uuid);
            if (target != null)
                targetsName = target.getName();
            Economy eco = Necessities.getInstance().getEconomy();
            if (!eco.doesPlayerExist(uuid)) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Please enter a valid player to send money to.");
                return true;
            }
            double intBal = eco.getBalance(player.getUniqueId());
            double payAmount = Math.abs(Double.parseDouble(args[1]));
            if (intBal < payAmount) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You don't have: " + var.getMoney() + Economy.format(payAmount));
                return true;
            }
            payAmount = Double.parseDouble(Utils.roundTwoDecimals(payAmount));
            eco.removeMoney(player.getUniqueId(), payAmount);
            eco.addMoney(uuid, payAmount);
            player.sendMessage(var.getMessages() + "You paid " + var.getObj() + targetsName + var.getMoney() + " " + Economy.format(payAmount));
            if (target != null)
                target.sendMessage(var.getMessages() + "You received " + var.getMoney() + Economy.format(payAmount) + var.getMessages() + " from " + var.getObj() +
                        player.getName() + var.getMessages() + ".");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be logged in to use this command or use cce");
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}