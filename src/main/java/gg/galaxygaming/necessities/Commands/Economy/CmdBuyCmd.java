package gg.galaxygaming.necessities.Commands.Economy;

import gg.galaxygaming.necessities.Economy.CmdPrices;
import gg.galaxygaming.necessities.Economy.Economy;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.UserManager;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdBuyCmd implements EconomyCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length != 1) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a command to buy.");
            return true;
        }
        if (args.length == 1) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                  + "This command is temporarily disabled as we work on transitioning to per use paying.");
            return true;
        }
        UserManager um = Necessities.getUM();
        CmdPrices cmdp = Necessities.getCommandPrices();
        if (sender instanceof Player) {
            Economy eco = Necessities.getEconomy();
            Player player = (Player) sender;
            String cmd = Utils.capFirst(args[0]);
            double cost = cmdp.getPrice(cmd);
            String rank = um.getUser(player.getUniqueId()).getRank().getName().toUpperCase();
            if (eco.getBalance(player.getUniqueId()) < cost) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Not enough money.");
                return true;
            }
            if (!cmdp.realCommand(cmd)) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Unknown or unbuyable command.");
                return true;
            }
            if (!cmdp.canBuy(cmd, rank)) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "You do not have the rank required to buy this command.");
                return true;
            }
            Command com = player.getServer().getPluginCommand(cmd);
            if (com == null) {
                player.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "The command " + var.getObj() + cmd + var.getMessages()
                            + " is a nonexistent or built in command. Contact an admin if it exists.");
                return true;
            }
            String permNode = com.getPermission();
            if (permNode == null || permNode.contains("mcmmo")) {
                permNode = "necessities." + cmd.toLowerCase();
            }
            if (player.hasPermission(permNode)) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Already have command");
                return true;
            }
            eco.removeMoney(player.getUniqueId(), cost);
            um.updateUserPerms(player.getUniqueId(), permNode, false);
            Bukkit.broadcastMessage(
                  var.getMessages() + player.getName() + " bought the command " + var.getObj() + cmd.toLowerCase());
        } else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                  + "You cannot buy a command you are already have all commands.");
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        //TODO: TabComplete
        return Collections.emptyList();
    }
}