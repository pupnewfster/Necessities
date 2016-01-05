package com.crossge.necessities.Commands.Economy;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class CmdTrade extends EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (args.length != 5) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you to enter the player you wish to trade with the item you wish to trade an how much the price to " +
                        "be payed and who pays.");
                return true;
            }
            Player player = (Player) sender;
            UUID uuid = get.getID(args[0]);
            if (uuid == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
                return true;
            }
            Player target = Bukkit.getPlayer(uuid);
            String pname = player.getName(), offertopname = target.getName();
            if (!form.isLegal(args[3])) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid price.");
                return true;
            }
            if (!form.isLegal(args[2])) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid amount.");
                return true;
            }
            String price = args[3];
            String amount = args[2];
            String temp = args[1].replaceAll(":", " ");
            String item = temp.split(" ")[0];
            short data = 0;
            if (form.isLegal(item)) {
                item = mat.idToName(Integer.parseInt(item));
                try {
                    data = Short.parseShort(temp.split(" ")[1]);
                } catch (Exception e) {
                }
            }
            price = form.roundTwoDecimals(Double.parseDouble(price));
            item = mat.findItem(item);
            if (item == null) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That item does not exist");
                return true;
            }
            if (args[4].equalsIgnoreCase("theypay")) {
                if (Double.parseDouble(balc.bal(target.getUniqueId())) - Double.parseDouble(price) < 0) {
                    player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + offertopname + " does not have that much money");
                    return true;
                }
                PlayerInventory inventory = player.getInventory();
                if (!inventory.contains(Material.matchMaterial(item), Integer.parseInt(amount))) {
                    player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have that much " +
                            mat.pluralize(mat.getName(item), Integer.parseInt(amount)));
                    return true;
                }
                tr.createTrade(offertopname + " " + pname + " " + item + ":" + Short.toString(data) + " " + amount + " " + price + " " + pname);
                item = mat.pluralize(mat.getName(item), Integer.parseInt(amount));
                player.sendMessage(var.getMessages() + "You have offered a trade to " + var.getObj() + offertopname);
                target.sendMessage(var.getObj() + pname + var.getMessages() + " has offered to trade you " + var.getObj() + amount + var.getMessages() + " of " + var.getObj() + item + var.getMessages() +
                        " for " + var.getMoney() + "$" + form.addCommas(price));
                target.sendMessage(var.getMessages() + "Type /taccept or /tdeny to accept or deny their trade request");
            }
            if (args[4].equalsIgnoreCase("ipay")) {
                if (Double.parseDouble(balc.bal(player.getUniqueId())) - Double.parseDouble(price) < 0) {
                    player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have " + var.getMoney() + "$" + form.addCommas(price));
                    return true;
                }
                PlayerInventory inventory = target.getInventory();
                if (!inventory.contains(Material.matchMaterial(item), Integer.parseInt(amount))) {
                    player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "They do not have that much " + mat.pluralize(mat.getName(item), Integer.parseInt(amount)));
                    return true;
                }
                tr.createTrade(offertopname + " " + pname + " " + item + ":" + Short.toString(data) + " " + amount + " " + price + " " + offertopname);
                item = mat.pluralize(mat.getName(item), Integer.parseInt(amount));
                player.sendMessage(var.getMessages() + "You have offered a trade to " + var.getObj() + offertopname);
                target.sendMessage(var.getObj() + pname + " has offered to trade you " + var.getMoney() + "$" + form.addCommas(price) + var.getMessages() + " for " + var.getObj() + amount + var.getMessages() +
                        " of " + var.getObj() + item);
                target.sendMessage(var.getMessages() + "Type /taccept " + var.getObj() + pname + var.getMessages() + " or /tdeny " + var.getObj() + pname + var.getMessages() +
                        " to accept or deny their trade request");
            }
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You don't have an inventory. Please log in to trade.");
        return true;
    }
}