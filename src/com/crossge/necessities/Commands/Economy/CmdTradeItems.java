package com.crossge.necessities.Commands.Economy;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class CmdTradeItems extends EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (args.length < 5) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you to enter the player you wish to trade with the item you wish to trade an how much the item " +
                        "trading for and the amount of that item.");
                return true;
            }
            Player player = (Player) sender;
            UUID uuid = get.getID(args[0]);
            if (uuid == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
                return true;
            }
            Player target = Bukkit.getPlayer(uuid);
            String pname = player.getName();
            String offertopname = target.getName();
            if (!form.isLegal(args[2]) || !form.isLegal(args[4])) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid amount.");
                return true;
            }
            String amountgetting = args[2];
            String itemgetting;
            String amountoffering = args[4];
            String itemoffering;
            short dataget = 0;
            short dataoff = 0;
            String tempget;
            tempget = args[1].replaceAll(":", " ");
            itemgetting = tempget.split(" ")[0];
            String tempoff;
            tempoff = args[3].replaceAll(":", " ");
            itemoffering = tempget.split(" ")[0];
            if (form.isLegal(itemgetting)) {
                itemgetting = mat.idToName(Integer.parseInt(itemgetting));
                try {
                    dataget = Short.parseShort(tempget.split(" ")[1]);
                } catch (Exception e) {
                }
            }
            if (form.isLegal(itemoffering)) {
                itemoffering = mat.idToName(Integer.parseInt(itemoffering));
                try {
                    dataoff = Short.parseShort(tempoff.split(" ")[1]);
                } catch (Exception e) {
                }
            }
            PlayerInventory thereinventory = target.getInventory();
            PlayerInventory yourinventory = player.getInventory();
            itemoffering = mat.findItem(itemoffering);
            itemgetting = mat.findItem(itemgetting);
            if (itemoffering == null || itemgetting == null) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That item does not exist");
                return true;
            }
            if (!yourinventory.contains(Material.matchMaterial(itemoffering), Integer.parseInt(amountoffering))) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have that much " + mat.pluralize(mat.getName(itemoffering), Integer.parseInt(amountoffering)));
                return true;
            }
            if (!thereinventory.contains(Material.matchMaterial(itemgetting), Integer.parseInt(amountgetting))) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "They do not have that much " + mat.pluralize(mat.getName(itemgetting), Integer.parseInt(amountgetting)));
                return true;
            }
            tr.createTrade(offertopname + " " + pname + " " + itemgetting + ":" + Short.toString(dataget) + " " + amountgetting + " " + itemoffering + ":" + Short.toString(dataoff) + " " + amountoffering);
            itemgetting = mat.pluralize(mat.getName(itemgetting), Integer.parseInt(amountoffering));
            itemoffering = mat.pluralize(mat.getName(itemoffering), Integer.parseInt(amountgetting));
            player.sendMessage(var.getMessages() + "You have offered a trade to " + var.getObj() + offertopname);
            target.sendMessage(var.getObj() + pname + " has offered to trade you " + var.getObj() + amountgetting + var.getMessages() + " of " + var.getObj() + itemgetting + var.getMessages() + " for " +
                    var.getObj() + amountoffering + var.getMessages() + " of " + var.getObj() + itemoffering);
            target.sendMessage(var.getMessages() + "Type /taccept " + var.getObj() + pname + var.getMessages() + " or /tdeny " + var.getObj() + pname + var.getMessages() + " to accept or deny their trade request");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You don't have an inventory. Please log in to trade.");
        return true;
    }
}