package com.crossge.necessities.Commands.Economy;

import com.crossge.necessities.Economy.RankPrices;
import com.crossge.necessities.Necessities;
import com.crossge.necessities.RankManager.RankManager;
import com.crossge.necessities.Utils;
import com.crossge.necessities.Variables;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdRankPrices implements EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        int page = 0;
        if (args.length > 1) {
            if (!Utils.legalInt(args[0])) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid command price page.");
                return true;
            }
            page = Integer.parseInt(args[0]);
        }
        if (args.length == 0 || page == 0)
            page = 1;
        int time = 0;
        String price;
        RankPrices rp = Necessities.getInstance().getRankPrices();
        int totalPages = rp.priceListPages();
        if (page > totalPages) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Input a number from 1 to " + Integer.toString(totalPages));
            return true;
        }
        sender.sendMessage(ChatColor.GOLD + "Rank Prices Page [" + Integer.toString(page) + "/" + Integer.toString(totalPages) + "]");
        page = page - 1;
        price = rp.priceLists(page, time);
        String rank = "CONSOLE";
        if (sender instanceof Player)
            rank = Necessities.getInstance().getUM().getUser(((Player) sender).getUniqueId()).getRank().getName();
        while (price != null) {
            price = formL(price.split(" ")[0], price.split(" ")[1], Integer.toString((page * 10) + time + 1) + ".", rank);
            sender.sendMessage(price);
            time++;
            price = rp.priceLists(page, time);
        }
        return true;
    }

    private String formL(String rank, String cost, String numb, String curRank) {
        String price;
        if (!numb.equalsIgnoreCase("10."))
            numb += " ";
        numb += " ";
        Variables var = Necessities.getInstance().getVar();
        if (curRank.equals("CONSOLE")) {
            cost = "$" + Utils.addCommas(Utils.roundTwoDecimals(Double.parseDouble(cost)));
            price = ChatColor.GOLD + numb + var.getCatalog() + rank + " can be bought for " + var.getMoney() + cost;
        } else {
            cost = "$" + Utils.addCommas(Utils.roundTwoDecimals(Double.parseDouble(cost)));
            price = ChatColor.GOLD + numb + var.getCatalog() + rank + " can be bought for " + var.getMoney() + cost;
            RankManager rm = Necessities.getInstance().getRM();
            if (rm.hasRank(rm.getRank(curRank), rm.getRank(rank)))
                price += var.getCatalog() + "   Already Acquired.";
        }
        return price;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}