package gg.galaxygaming.necessities.Commands.Economy;

import gg.galaxygaming.necessities.Economy.Economy;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
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
        UUID uuid = Utils.getID(args[1]);
        Player target = null;
        if (uuid == null) {
            uuid = Utils.getOfflineID(args[1]);
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
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Please enter a valid player to change the balance of.");
            return true;
        }
        if (args[0].equalsIgnoreCase("reset")) {
            eco.setBalance(uuid, 0);
            sender.sendMessage(var.getMessages() + "Your successfully reset the balance of " + var.getObj() + targetsName + var.getMessages() + ".");
            return true;
        }
        if (args.length > 2) {
            if (!Utils.legalDouble(args[2])) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid amount of money to modify the player with.");
                return true;
            }
            double amount = Double.parseDouble(args[2]);
            double intBal = eco.getBalance(uuid);
            amount = Double.parseDouble(Utils.roundTwoDecimals(amount));
            if (args[0].equalsIgnoreCase("give")) {
                eco.addMoney(uuid, amount);
                sender.sendMessage(var.getMessages() + "Your successfully gave " + var.getMoney() + " " + Economy.format(amount) + var.getMessages() + " to " + var.getObj() + targetsName +
                        var.getMessages() + ".");
            } else if (args[0].equalsIgnoreCase("take") && intBal - amount >= 0) {
                eco.removeMoney(uuid, amount);
                sender.sendMessage(var.getMessages() + "Your successfully took " + var.getMoney() + " " + Economy.format(amount) + var.getMessages() + " from " + var.getObj() + targetsName +
                        var.getMessages() + ".");
            } else if (args[0].equalsIgnoreCase("set")) {
                eco.setBalance(uuid, amount);
                sender.sendMessage(var.getMessages() + "Your successfully set the balance of " + var.getObj() + targetsName + var.getMessages() + " to " + var.getMoney() + Economy.format(amount));
            }
        }
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}