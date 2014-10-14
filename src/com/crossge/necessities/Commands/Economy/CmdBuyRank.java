package com.crossge.necessities.Commands.Economy;

import com.crossge.necessities.RankManager.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdBuyRank extends EconomyCmd {
    RankManager rm = new RankManager();

    public boolean commandUse(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid rank to buy.");
            return true;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String rankName = form.capFirst(args[0]);
            double cost = rp.getCost(rankName);
            double balan = Double.parseDouble(balc.bal(player.getUniqueId()));
            if (balan < cost) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Not enough money.");
                return true;
            }
            if (!rp.rankBuyable(rankName)) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Unknown or unbuyable rank.");
                return true;
            }
            String curRank = um.getUser(player.getUniqueId()).getRank().getName();
            if (rm.getRank(rankName).getNext().equals(rm.getRank(curRank))) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You may not skip ranks or buy ranks you have already gotten.");
                return true;
            }
            balc.removeMoney(player.getUniqueId(), cost);
            um.updateUserRank(um.getUser(player.getUniqueId()), player.getUniqueId(), rm.getRank(form.capFirst(rankName)));
            Bukkit.broadcastMessage(var.getMessages() + player.getName() + " bought the rank " + var.getObj() + rankName.toLowerCase());
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You cannot buy a rank you are already the highest rank.");
        return true;
    }
}