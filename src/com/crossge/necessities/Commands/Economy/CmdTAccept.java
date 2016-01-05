package com.crossge.necessities.Commands.Economy;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class CmdTAccept extends EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (args.length != 1) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter the player you want to accept the trade of.");
                return true;
            }
            Player player = (Player) sender;
            UUID uuid = get.getID(args[0]);
            if (uuid == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
                return true;
            }
            Player target = Bukkit.getPlayer(uuid);
            String pname = player.getName(), offerpname = target.getName();
            if (tr.hasTrade(pname, offerpname)) {
                String info = tr.acceptTrade(pname, offerpname);
                String amount = info.split(" ")[1], price = info.split(" ")[2], toWhom = info.split(" ")[3];
                short data = 0;
                String temp = info.split(" ")[0].replaceAll(":", " ");
                String item = temp.split(" ")[0];
                if (form.isLegal(price))
                    price = form.roundTwoDecimals(Double.parseDouble(price));
                if (form.isLegal(item)) {
                    item = mat.idToName(Integer.parseInt(item));
                    try {
                        data = Short.parseShort(temp.split(" ")[1]);
                    } catch (Exception e) { }
                }
                if (!toWhom.equalsIgnoreCase(pname) && !toWhom.equalsIgnoreCase(offerpname)) {
                    short dataoff = 0;
                    String temp2 = price.replaceAll(":", " ");
                    String itemoffering = temp.split(" ")[0];
                    if (form.isLegal(itemoffering))
                        try {
                            dataoff = Short.parseShort(temp2.split(" ")[1]);
                        } catch (Exception e) { }
                    PlayerInventory thereinventory = target.getInventory();
                    PlayerInventory yourinventory = player.getInventory();
                    ItemStack itemstack = new ItemStack(Material.matchMaterial(mat.findItem(item)), Integer.parseInt(amount), data);
                    ItemStack is = new ItemStack(Material.matchMaterial(mat.findItem(itemoffering)), Integer.parseInt(toWhom), dataoff);
                    if (!yourinventory.contains(itemstack)) {
                        player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have that much " + mat.pluralize(item, Integer.parseInt(amount)));
                        return true;
                    }
                    if (!thereinventory.contains(is)) {
                        player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "They do not have that much " + mat.pluralize(itemoffering, Integer.parseInt(toWhom)));
                        return true;
                    }
                    yourinventory.addItem(is);
                    yourinventory.removeItem(itemstack);
                    thereinventory.addItem(itemstack);
                    thereinventory.removeItem(is);
                }
                if (toWhom.equalsIgnoreCase(pname)) {
                    if (Double.parseDouble(balc.bal(target.getUniqueId())) - Double.parseDouble(price) < 0) {
                        player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "They do not have " + var.getMoney() + "$" + form.addCommas(price));
                        return true;
                    }
                    PlayerInventory thereinventory = target.getInventory();
                    PlayerInventory yourinventory = player.getInventory();
                    ItemStack itemstack = new ItemStack(Material.matchMaterial(mat.findItem(item)), Integer.parseInt(amount), data);
                    if (!yourinventory.contains(itemstack)) {
                        player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have that much " + mat.pluralize(item, Integer.parseInt(amount)));
                        return true;
                    }
                    balc.removeMoney(target.getUniqueId(), Double.parseDouble(price));
                    balc.addMoney(player.getUniqueId(), Double.parseDouble(price));
                    thereinventory.addItem(itemstack);
                    yourinventory.removeItem(itemstack);
                }
                if (toWhom.equalsIgnoreCase(offerpname)) {
                    if (Double.parseDouble(balc.bal(player.getUniqueId())) - Double.parseDouble(price) < 0) {
                        player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have " + var.getMoney() + "$" + form.addCommas(price));
                        return true;
                    }
                    PlayerInventory thereinventory = target.getInventory();
                    PlayerInventory yourinventory = player.getInventory();
                    ItemStack itemstack = new ItemStack(Material.matchMaterial(mat.findItem(item)), Integer.parseInt(amount), data);
                    if (!thereinventory.contains(itemstack)) {
                        player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "They do not have that much " + mat.pluralize(item, Integer.parseInt(amount)));
                        return true;
                    }
                    balc.removeMoney(player.getUniqueId(), Double.parseDouble(price));
                    balc.addMoney(target.getUniqueId(), Double.parseDouble(price));
                    yourinventory.addItem(itemstack);
                    thereinventory.removeItem(itemstack);
                }
                player.sendMessage(var.getMessages() + "You have accepted the trade from " + var.getObj() + offerpname);
                target.sendMessage(var.getMessages() + "Your trade to " + var.getObj() + pname + var.getMessages() + " has been accepted");
            } else
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have a trade offer from " + offerpname);
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You don't have an inventory. Please log in to trade.");
        return true;
    }
}