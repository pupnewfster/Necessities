package com.crossge.necessities.Commands.Economy;

import com.crossge.necessities.Economy.BalChecks;
import com.crossge.necessities.Economy.CmdPrices;
import com.crossge.necessities.Necessities;
import com.crossge.necessities.RankManager.UserManager;
import com.crossge.necessities.Utils;
import com.crossge.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdBuyCmd implements EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (args.length != 1) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a command to buy.");
            return true;
        }
        if (args.length == 1) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "This command is temporarily disabled as we work on transitioning to per use paying.");
            return true;
        }
        UserManager um = Necessities.getInstance().getUM();
        CmdPrices cmdp = Necessities.getInstance().getCommandPrices();
        if (sender instanceof Player) {
            BalChecks balc = Necessities.getInstance().getBalChecks();
            Player player = (Player) sender;
            String cmd = Utils.capFirst(args[0]);
            double cost = cmdp.getCost(cmd);
            String rank = um.getUser(player.getUniqueId()).getRank().getName().toUpperCase();
            if (Double.parseDouble(balc.bal(player.getUniqueId())) < cost) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Not enough money.");
                return true;
            }
            if (!cmdp.realCommand(cmd)) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Unknown or unbuyable command.");
                return true;
            }
            if (!cmdp.canBuy(cmd, rank)) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have the rank required to buy this command.");
                return true;
            }
            Command com = player.getServer().getPluginCommand(cmd);
            if (com == null) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The command " + var.getObj() + cmd + var.getMessages() + " is a nonexistent or built in command. Contact an admin if it exists.");
                return true;
            }
            String permNode = com.getPermission();
            if (permNode == null || permNode.contains("mcmmo"))
                permNode = "necessities." + cmd.toLowerCase();
            if (player.hasPermission(permNode)) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Already have command");
                return true;
            }
            balc.removeMoney(player.getUniqueId(), cost);
            um.updateUserPerms(player.getUniqueId(), permNode, false);
            Bukkit.broadcastMessage(var.getMessages() + player.getName() + " bought the command " + var.getObj() + cmd.toLowerCase());
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You cannot buy a command you are already have all commands.");
        return true;
    }
}