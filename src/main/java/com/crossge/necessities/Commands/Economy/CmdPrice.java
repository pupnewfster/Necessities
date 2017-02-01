package com.crossge.necessities.Commands.Economy;

import com.crossge.necessities.Economy.Materials;
import com.crossge.necessities.Necessities;
import com.crossge.necessities.Utils;
import com.crossge.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdPrice implements EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you enter the item you want to know the price of and whether to buy or sell.");
            return true;
        }
        String itemName = args[0];
        String oper = args[0];
        if (sender instanceof Player) {
            if (args.length == 1)
                itemName = ((Player) sender).getInventory().getItemInMainHand().getType().name();
        } else if (args.length < 2) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you enter the item you want to know the price of and whether to buy or sell.");
            return true;
        }
        if (args.length > 1)
            oper = args[1];
        Materials mat = Necessities.getInstance().getMaterials();
        if (Utils.legalInt(itemName))
            itemName = mat.idToName(Integer.parseInt(itemName));
        itemName = mat.findItem(itemName);
        if (itemName == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That item does not exist");
            return true;
        }
        String file;
        if (oper.equalsIgnoreCase("buy") || oper.equalsIgnoreCase("sell"))
            file = oper.toLowerCase();
        else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Input either buy or sell");
            return true;
        }
        String cost = Necessities.getInstance().getPrices().cost(file, itemName);
        itemName = Utils.capFirst(mat.getName(itemName));
        if (cost == null || cost.equalsIgnoreCase("null")) {
            if (oper.equalsIgnoreCase("buy"))
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + mat.pluralize(itemName, 2) + " cannot be bought from the server");
            else
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + mat.pluralize(itemName, 2) + " cannot be sold to the server");
            return true;
        }
        if (oper.equalsIgnoreCase("buy"))
            sender.sendMessage(var.getObj() + mat.pluralize(itemName, 2) + var.getMessages() + " can be bought for " + var.getMoney() + "$" + Utils.addCommas(cost));
        else
            sender.sendMessage(var.getObj() + mat.pluralize(itemName, 2) + var.getMessages() + " can be sold for " + var.getMoney() + "$" + Utils.addCommas(cost));
        return true;
    }
}