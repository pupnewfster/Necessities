package gg.galaxygaming.necessities.Commands.Economy;

import gg.galaxygaming.necessities.Economy.Economy;
import gg.galaxygaming.necessities.Material;
import gg.galaxygaming.necessities.Economy.Prices;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CmdPriceList implements EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        int page = 0;
        if (args.length > 0) {
            if (!Utils.legalInt(args[0])) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid price page.");
                return true;
            }
            page = Integer.parseInt(args[0]);
        }
        if (args.length == 0 || page == 0)
            page = 1;
        int time = 0;
        String price;
        Prices pr = Necessities.getPrices();
        int totalPages = pr.priceListPages();
        if (page > totalPages) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Input a number from 1 to " + Integer.toString(totalPages));
            return true;
        }
        sender.sendMessage(ChatColor.GOLD + "Price List Page [" + Integer.toString(page) + '/' + Integer.toString(totalPages) + ']');
        page = page - 1;
        price = pr.priceLists(page, time);
        while (price != null) {
            price = formL(Material.fromString(price.split(" ")[0]).getFriendlyName(2), price.split(" ")[1], price.split(" ")[2], Integer.toString(page * 10 + time + 1) + '.');
            sender.sendMessage(price);
            time++;
            price = pr.priceLists(page, time);//TODO if I get all the prices for that page at once... then I can get the longest named item and try to line it up better
        }
        return true;
    }

    private String formL(String item, String buy, String sell, String numb) {
        Variables var = Necessities.getVar();
        String selling = "  " + ChatColor.GREEN + "Sell: ", buying = ChatColor.GREEN + "  Buy: ";
        if (!numb.equalsIgnoreCase("10."))
            numb += " ";
        numb += " ";
        if (buy.trim().equalsIgnoreCase("null")) {//shouldn't ever happen now anyways
            buying = "";
            buy = "";
        } else
            buy = Economy.format(Double.parseDouble(buy));
        if (sell.trim().equalsIgnoreCase("null")) {
            selling = "";
            sell = "";
        } else {
            if (!buy.equals(""))
                selling = "  |" + selling;
            sell = Economy.format(Double.parseDouble(sell));
        }
        return ChatColor.GOLD + numb + var.getCatalog() + item + buying + var.getMoney() + buy + var.getCatalog() + selling + var.getMoney() + sell;
    }
}