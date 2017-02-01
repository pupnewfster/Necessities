package com.crossge.necessities.Commands.Economy;

import com.crossge.necessities.Economy.Materials;
import com.crossge.necessities.Economy.Prices;
import com.crossge.necessities.Necessities;
import com.crossge.necessities.Utils;
import com.crossge.necessities.Variables;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CmdPriceList implements EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
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
        Prices pr = Necessities.getInstance().getPrices();
        int totalPages = pr.priceListPages();
        if (page > totalPages) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Input a number from 1 to " + Integer.toString(totalPages));
            return true;
        }
        sender.sendMessage(ChatColor.GOLD + "Price List Page [" + Integer.toString(page) + "/" + Integer.toString(totalPages) + "]");
        page = page - 1;
        price = pr.priceLists(page, time);
        Materials mat = Necessities.getInstance().getMaterials();
        while (price != null) {
            price = formL(Utils.capFirst(mat.getName(mat.findItem(price.split(" ")[0]))), price.split(" ")[1], price.split(" ")[2], Integer.toString((page * 10) + time + 1) + ".");
            sender.sendMessage(price);
            time++;
            price = pr.priceLists(page, time);
        }
        return true;
    }

    private String formL(String item, String buy, String sell, String numb) {
        item = Necessities.getInstance().getMaterials().pluralize(item, 2);
        String selling = "  sell price: ", buying = "  buy price: ";
        if (!numb.equalsIgnoreCase("10."))
            numb += " ";
        numb += " ";
        if (buy.trim().equalsIgnoreCase("null")) {//shouldn't ever happen now anyways
            buying = "";
            buy = "";
        } else
            buy = "$" + Utils.addCommas(buy);
        if (sell.trim().equalsIgnoreCase("null")) {
            selling = "";
            sell = "";
        } else
            sell = "$" + Utils.addCommas(sell);
        Variables var = Necessities.getInstance().getVar();
        return ChatColor.GOLD + numb + var.getCatalog() + item + buying + var.getMoney() + buy + var.getCatalog() + selling + var.getMoney() + sell;
    }
}