package com.crossge.necessities.Commands.Economy;

import com.crossge.necessities.Economy.Materials;
import com.crossge.necessities.Necessities;
import com.crossge.necessities.Utils;
import com.crossge.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class CmdTradeItems implements EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            if (args.length < 5) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you to enter the player you wish to trade with the item you wish to trade an how much the item " +
                        "trading for and the amount of that item.");
                return true;
            }
            Player player = (Player) sender;
            UUID uuid = Necessities.getInstance().getUUID().getID(args[0]);
            if (uuid == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
                return true;
            }
            Player target = Bukkit.getPlayer(uuid);
            String pname = player.getName();
            String offerToPname = target.getName();
            if (!Utils.legalInt(args[2]) || !Utils.legalInt(args[4])) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid amount.");
                return true;
            }
            String amountGetting = args[2];
            String itemGetting;
            String amountOffering = args[4];
            String itemOffering;
            short dataGet = 0;
            short dataOff = 0;
            String tempGet;
            tempGet = args[1].replaceAll(":", " ");
            itemGetting = tempGet.split(" ")[0];
            String tempOff;
            tempOff = args[3].replaceAll(":", " ");
            itemOffering = tempGet.split(" ")[0];
            Materials mat = Necessities.getInstance().getMaterials();
            if (Utils.legalInt(itemGetting)) {
                itemGetting = mat.idToName(Integer.parseInt(itemGetting));
                try {
                    dataGet = Short.parseShort(tempGet.split(" ")[1]);
                } catch (Exception ignored) {
                }
            }
            if (Utils.legalInt(itemOffering)) {
                itemOffering = mat.idToName(Integer.parseInt(itemOffering));
                try {
                    dataOff = Short.parseShort(tempOff.split(" ")[1]);
                } catch (Exception ignored) {
                }
            }
            PlayerInventory theirInventory = target.getInventory();
            PlayerInventory yourInventory = player.getInventory();
            itemOffering = mat.findItem(itemOffering);
            itemGetting = mat.findItem(itemGetting);
            if (itemOffering == null || itemGetting == null) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That item does not exist");
                return true;
            }
            if (!yourInventory.contains(Material.matchMaterial(itemOffering), Integer.parseInt(amountOffering))) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have that much " + mat.pluralize(mat.getName(itemOffering), Integer.parseInt(amountOffering)));
                return true;
            }
            if (!theirInventory.contains(Material.matchMaterial(itemGetting), Integer.parseInt(amountGetting))) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "They do not have that much " + mat.pluralize(mat.getName(itemGetting), Integer.parseInt(amountGetting)));
                return true;
            }
            Necessities.getInstance().getTrades().createTrade(offerToPname + " " + pname + " " + itemGetting + ":" + Short.toString(dataGet) + " " + amountGetting + " " + itemOffering + ":" + Short.toString(dataOff) + " " + amountOffering);
            itemGetting = mat.pluralize(mat.getName(itemGetting), Integer.parseInt(amountOffering));
            itemOffering = mat.pluralize(mat.getName(itemOffering), Integer.parseInt(amountGetting));
            player.sendMessage(var.getMessages() + "You have offered a trade to " + var.getObj() + offerToPname);
            target.sendMessage(var.getObj() + pname + " has offered to trade you " + var.getObj() + amountGetting + var.getMessages() + " of " + var.getObj() + itemGetting + var.getMessages() + " for " +
                    var.getObj() + amountOffering + var.getMessages() + " of " + var.getObj() + itemOffering);
            target.sendMessage(var.getMessages() + "Type /taccept " + var.getObj() + pname + var.getMessages() + " or /tdeny " + var.getObj() + pname + var.getMessages() + " to accept or deny their trade request");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You don't have an inventory. Please log in to trade.");
        return true;
    }
}