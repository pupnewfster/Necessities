package gg.galaxygaming.necessities.Commands.Economy;

import gg.galaxygaming.necessities.Economy.Economy;
import gg.galaxygaming.necessities.Economy.RankPrices;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.RankManager;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdRankPrices implements EconomyCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        int page = 0;
        if (args.length > 1) {
            if (!Utils.legalInt(args[0])) {
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid command price page.");
                return true;
            }
            page = Integer.parseInt(args[0]);
        }
        if (args.length == 0 || page == 0) {
            page = 1;
        }
        int time = 0;
        String price;
        RankPrices rp = Necessities.getRankPrices();
        int totalPages = rp.priceListPages();
        if (page > totalPages) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Input a number from 1 to " + totalPages);
            return true;
        }
        sender.sendMessage(ChatColor.GOLD + "Rank Prices Page [" + page + '/' + totalPages + ']');
        page = page - 1;
        price = rp.priceLists(page, time);
        String rank = "CONSOLE";
        if (sender instanceof Player) {
            rank = Necessities.getUM().getUser(((Player) sender).getUniqueId()).getRank().getName();
        }
        while (price != null) {
            price = formL(price.split(" ")[0], price.split(" ")[1], Integer.toString(page * 10 + time + 1) + '.', rank);
            sender.sendMessage(price);
            time++;
            price = rp.priceLists(page, time);
        }
        return true;
    }

    private String formL(String rank, String cost, String numb, String curRank) {
        String price;
        if (!numb.equalsIgnoreCase("10.")) {
            numb += " ";
        }
        numb += " ";
        Variables var = Necessities.getVar();
        if (curRank.equals("CONSOLE")) {
            cost = Economy.format(Double.parseDouble(cost));

            price = ChatColor.GOLD + numb + Necessities.getRM().getRank(rank).getColor() + rank + ChatColor.GREEN
                  + "  Buy: " + var.getMoney() + cost;
        } else {
            cost = Economy.format(Double.parseDouble(cost));
            price = ChatColor.GOLD + numb + Necessities.getRM().getRank(rank).getColor() + rank + ChatColor.GREEN
                  + "  Buy: " + var.getMoney() + cost;
            RankManager rm = Necessities.getRM();
            if (rm.hasRank(rm.getRank(curRank), rm.getRank(rank))) {
                price += var.getCatalog() + "   Already Acquired.";
            }
        }
        return price;
    }

    public boolean isPaintballEnabled() {
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        //TODO: TabComplete
        return Collections.emptyList();
    }
}