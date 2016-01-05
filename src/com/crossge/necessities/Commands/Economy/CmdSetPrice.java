package com.crossge.necessities.Commands.Economy;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdSetPrice extends EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you enter the item and price for it and whether buying or selling price.");
            return true;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 2) {
                String itemName = player.getItemInHand().getType().name();
                if (!form.isLegal(args[0]) && !args[0].equalsIgnoreCase("null")) {
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a the price you want to set the item at.");
                    return true;
                }
                itemName = mat.findItem(itemName);
                if (itemName == null) {
                    player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That item does not exist");
                    return true;
                }
                String file;
                if (args[1].equalsIgnoreCase("buy") || args[1].equalsIgnoreCase("sell"))
                    file = args[1].toLowerCase();
                else {
                    player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Input either sell or buy");
                    return true;
                }
                if (args[0].equalsIgnoreCase("null")) {
                    pr.setCost(file, itemName, args[0]);
                    itemName = mat.pluralize(mat.getName(itemName), 2);
                    player.sendMessage(var.getObj() + itemName + var.getMessages() + " can no longer be " + (args[1].equalsIgnoreCase("buy") ? "bought" : "sold"));
                } else {
                    pr.setCost(file, itemName, form.roundTwoDecimals(Double.parseDouble(args[0])));
                    itemName = mat.pluralize(mat.getName(itemName), 2);
                    player.sendMessage(var.getObj() + form.ownerShip(itemName) + var.getMessages() + " " + (args[1].equalsIgnoreCase("buy") ? "buy" : "sell") + " price was set to " + var.getMoney() + "$" +
                            form.addCommas(form.roundTwoDecimals(Double.parseDouble(args[0]))));
                }
                return true;
            }
        }
        if (args.length < 3) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you enter the item and price for it and whether buying or selling price.");
            return true;
        }
        String itemName = args[0];
        if (form.isLegal(itemName))
            itemName = mat.idToName(Integer.parseInt(itemName));
        if (!form.isLegal(args[1]) && !args[1].equalsIgnoreCase("null")) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a the price you want to set the item at.");
            return true;
        }
        itemName = mat.findItem(itemName);
        if (itemName == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That item does not exist");
            return true;
        }
        String file;
        if (args[2].equalsIgnoreCase("buy") || args[2].equalsIgnoreCase("sell"))
            file = args[2].toLowerCase();
        else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Input either sell or buy");
            return true;
        }
        if (args[1].equalsIgnoreCase("null")) {
            pr.setCost(file, itemName, args[1]);
            itemName = mat.pluralize(mat.getName(itemName), 2);
            sender.sendMessage(var.getObj() + itemName + var.getMessages() + " can no longer be " + (args[2].equalsIgnoreCase("buy") ? "bought" : "sold"));
        } else {
            pr.setCost(file, itemName, form.roundTwoDecimals(Double.parseDouble(args[1])));
            itemName = mat.pluralize(mat.getName(itemName), 2);
            sender.sendMessage(var.getObj() + form.ownerShip(itemName) + var.getMessages() + " " + (args[2].equalsIgnoreCase("buy") ? "buy" : "sell") + " price was set to " + var.getMoney() + "$" +
                    form.addCommas(form.roundTwoDecimals(Double.parseDouble(args[1]))));
        }
        return true;
    }
}