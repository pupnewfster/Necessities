package com.crossge.necessities.Commands.Economy;

import com.crossge.necessities.Economy.BalChecks;
import com.crossge.necessities.Economy.Materials;
import com.crossge.necessities.Economy.Trade;
import com.crossge.necessities.Necessities;
import com.crossge.necessities.Utils;
import com.crossge.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class CmdTAccept implements EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            if (args.length != 1) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter the player you want to accept the trade of.");
                return true;
            }
            Player player = (Player) sender;
            UUID uuid = Necessities.getInstance().getUUID().getID(args[0]);
            if (uuid == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
                return true;
            }
            Player target = Bukkit.getPlayer(uuid);
            String pname = player.getName(), offerPname = target.getName();
            Materials mat = Necessities.getInstance().getMaterials();
            Trade tr = Necessities.getInstance().getTrades();
            if (tr.hasTrade(pname, offerPname)) {
                String info = tr.acceptTrade(pname, offerPname);
                String amount = info.split(" ")[1], price = info.split(" ")[2], toWhom = info.split(" ")[3];
                short data = 0;
                String temp = info.split(" ")[0].replaceAll(":", " ");
                String item = temp.split(" ")[0];
                if (Utils.legalDouble(price))
                    price = Utils.roundTwoDecimals(Double.parseDouble(price));
                if (Utils.legalInt(item)) {
                    item = mat.idToName(Integer.parseInt(item));
                    try {
                        data = Short.parseShort(temp.split(" ")[1]);
                    } catch (Exception ignored) {
                    }
                }
                if (!toWhom.equalsIgnoreCase(pname) && !toWhom.equalsIgnoreCase(offerPname)) {
                    short dataOff = 0;
                    String temp2 = price.replaceAll(":", " ");
                    String itemOffering = temp.split(" ")[0];
                    if (Utils.legalInt(itemOffering))
                        try {
                            dataOff = Short.parseShort(temp2.split(" ")[1]);
                        } catch (Exception ignored) {
                        }
                    PlayerInventory theirInventory = target.getInventory();
                    PlayerInventory yourInventory = player.getInventory();
                    ItemStack itemstack = new ItemStack(Material.matchMaterial(mat.findItem(item)), Integer.parseInt(amount), data);
                    ItemStack is = new ItemStack(Material.matchMaterial(mat.findItem(itemOffering)), Integer.parseInt(toWhom), dataOff);
                    if (!yourInventory.contains(itemstack)) {
                        player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have that much " + mat.pluralize(item, Integer.parseInt(amount)));
                        return true;
                    }
                    if (!theirInventory.contains(is)) {
                        player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "They do not have that much " + mat.pluralize(itemOffering, Integer.parseInt(toWhom)));
                        return true;
                    }
                    yourInventory.addItem(is);
                    yourInventory.removeItem(itemstack);
                    theirInventory.addItem(itemstack);
                    theirInventory.removeItem(is);
                }
                BalChecks balc = Necessities.getInstance().getBalChecks();
                if (toWhom.equalsIgnoreCase(pname)) {
                    if (Double.parseDouble(balc.bal(target.getUniqueId())) - Double.parseDouble(price) < 0) {
                        player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "They do not have " + var.getMoney() + "$" + Utils.addCommas(price));
                        return true;
                    }
                    PlayerInventory theirInventory = target.getInventory();
                    PlayerInventory yourInventory = player.getInventory();
                    ItemStack itemstack = new ItemStack(Material.matchMaterial(mat.findItem(item)), Integer.parseInt(amount), data);
                    if (!yourInventory.contains(itemstack)) {
                        player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have that much " + mat.pluralize(item, Integer.parseInt(amount)));
                        return true;
                    }
                    balc.removeMoney(target.getUniqueId(), Double.parseDouble(price));
                    balc.addMoney(player.getUniqueId(), Double.parseDouble(price));
                    theirInventory.addItem(itemstack);
                    yourInventory.removeItem(itemstack);
                }
                if (toWhom.equalsIgnoreCase(offerPname)) {
                    if (Double.parseDouble(balc.bal(player.getUniqueId())) - Double.parseDouble(price) < 0) {
                        player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have " + var.getMoney() + "$" + Utils.addCommas(price));
                        return true;
                    }
                    PlayerInventory theirInventory = target.getInventory();
                    PlayerInventory yourInventory = player.getInventory();
                    ItemStack itemstack = new ItemStack(Material.matchMaterial(mat.findItem(item)), Integer.parseInt(amount), data);
                    if (!theirInventory.contains(itemstack)) {
                        player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "They do not have that much " + mat.pluralize(item, Integer.parseInt(amount)));
                        return true;
                    }
                    balc.removeMoney(player.getUniqueId(), Double.parseDouble(price));
                    balc.addMoney(target.getUniqueId(), Double.parseDouble(price));
                    yourInventory.addItem(itemstack);
                    theirInventory.removeItem(itemstack);
                }
                player.sendMessage(var.getMessages() + "You have accepted the trade from " + var.getObj() + offerPname);
                target.sendMessage(var.getMessages() + "Your trade to " + var.getObj() + pname + var.getMessages() + " has been accepted");
            } else
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have a trade offer from " + offerPname);
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You don't have an inventory. Please log in to trade.");
        return true;
    }
}