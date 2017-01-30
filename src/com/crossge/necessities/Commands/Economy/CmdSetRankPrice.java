package com.crossge.necessities.Commands.Economy;

import com.crossge.necessities.Economy.RankPrices;
import com.crossge.necessities.Necessities;
import com.crossge.necessities.Utils;
import com.crossge.necessities.Variables;
import org.bukkit.command.CommandSender;

public class CmdSetRankPrice implements EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (args.length < 2) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you enter a rank and a price to sell that rank for.");
            return true;
        }
        String rankName = Utils.capFirst(args[0]);
        RankPrices rp = Necessities.getInstance().getRankPrices();
        if (!Utils.legalDouble(args[1])) {
            if (args[1].equalsIgnoreCase("null")) {
                rp.rCost(rankName);
                sender.sendMessage(var.getObj() + rankName + var.getMessages() + " can no longer be bought.");
                return true;
            }
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Must enter a price to sell rank for.");
            return true;
        }
        String cost = args[1];
        rp.setCost(rankName, cost);
        cost = Utils.roundTwoDecimals(Double.parseDouble(cost));
        sender.sendMessage(var.getMessages() + "Added " + var.getObj() + rankName + var.getMessages() + " at the price of " + var.getMoney() + "$" + cost);
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}