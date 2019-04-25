package gg.galaxygaming.necessities.Commands.Economy;

import gg.galaxygaming.necessities.Economy.Economy;
import gg.galaxygaming.necessities.Economy.RankPrices;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.RankManager;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.RankManager.UserManager;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdBuyRank implements EconomyCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length != 1) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                  + "You must enter a valid rank to buy. View the buyable ranks with /rankprices");
            return true;
        }
        if (sender instanceof Player) {
            Economy eco = Necessities.getEconomy();
            Player player = (Player) sender;
            String rankName = Utils.capFirst(args[0]);
            RankPrices rp = Necessities.getRankPrices();
            double cost = rp.getPrice(rankName);
            if (eco.getBalance(player.getUniqueId()) < cost) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Not enough money.");
                return true;
            }
            if (!rp.rankBuyable(rankName)) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Unknown or unbuyable rank.");
                return true;
            }
            UserManager um = Necessities.getUM();
            RankManager rm = Necessities.getRM();
            String curRank = um.getUser(player.getUniqueId()).getRank().getName();
            if (rm.getRank(rankName).getNext().equals(rm.getRank(curRank))) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "You may not skip ranks or buy ranks you have already gotten.");
                return true;
            }
            eco.removeMoney(player.getUniqueId(), cost);
            User u = um.getUser(player.getUniqueId());
            String cOld = u.getRank().getColor();
            um.updateUserRank(u, rm.getRank(Utils.capFirst(rankName)));
            String c = u.getRank().getColor();
            Bukkit.broadcastMessage(
                  var.getMessages() + cOld + player.getName() + var.getMessages() + " bought the rank " + c + rankName
                        .toLowerCase());
        } else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console may not buy ranks.");
        }
        return true;
    }
}