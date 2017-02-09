package gg.galaxygaming.necessities.Commands.Economy;

import gg.galaxygaming.necessities.Economy.Economy;
import gg.galaxygaming.necessities.Economy.RankPrices;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.command.CommandSender;

public class CmdSetRankPrice implements EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length < 2) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you enter a rank and a price to sell that rank for.");
            return true;
        }
        String rankName = Utils.capFirst(args[0]);
        RankPrices rp = Necessities.getRankPrices();
        if (!Utils.legalDouble(args[1])) {
            if (args[1].equalsIgnoreCase("null")) {
                rp.rCost(rankName);
                sender.sendMessage(var.getObj() + rankName + var.getMessages() + " can no longer be bought.");
                return true;
            }
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Must enter a price to sell rank for.");
            return true;
        }
        String cost = Utils.roundTwoDecimals(Double.parseDouble(args[1]));
        rp.setCost(rankName, cost);
        sender.sendMessage(var.getMessages() + "Added " + var.getObj() + rankName + var.getMessages() + " at the price of " + var.getMoney() + Economy.format(Double.parseDouble(cost)));
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}