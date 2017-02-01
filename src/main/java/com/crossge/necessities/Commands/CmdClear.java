package com.crossge.necessities.Commands;

import com.crossge.necessities.Economy.Materials;
import com.crossge.necessities.Necessities;
import com.crossge.necessities.Utils;
import com.crossge.necessities.Variables;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdClear implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                p.getInventory().clear();
                p.sendMessage(var.getMessages() + "Inventory cleared.");
                return true;
            }
            String itemName = args[0];
            Materials mat = Necessities.getInstance().getMaterials();
            if (Utils.legalInt(itemName))
                itemName = mat.idToName(Integer.parseInt(itemName));
            itemName = mat.findItem(itemName);
            if (itemName == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That item does not exist.");
                return true;
            }
            if (args.length == 1) {
                if (!p.getInventory().contains(new ItemStack(Material.getMaterial(itemName), 1))) {
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have any of that item.");
                    return true;
                }
                int amount = 0;
                for (ItemStack i : p.getInventory().getContents()) {
                    if (i != null && i.getType().equals(Material.getMaterial(itemName)))
                        amount += i.getAmount();
                }
                p.getInventory().remove(new ItemStack(Material.getMaterial(itemName)));
                p.sendMessage(var.getMessages() + "Removed " + var.getObj() + amount + " " + mat.pluralize(Utils.capFirst(mat.getName(itemName)), amount) + var.getMessages() + " from your inventory.");
                return true;
            }
            if (!Utils.legalInt(args[1])) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Illegal amount the amount must be numeric.");
                return true;
            }
            int amount = Integer.parseInt(args[1]);
            if (args.length == 2) {
                if (!p.getInventory().contains(new ItemStack(Material.getMaterial(itemName), amount))) {
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have " + var.getObj() + amount + var.getMessages() + " of that item.");
                    return true;
                }
                p.getInventory().remove(new ItemStack(Material.getMaterial(itemName), amount));
                p.sendMessage(var.getMessages() + "Removed " + var.getObj() + amount + " " + mat.pluralize(Utils.capFirst(mat.getName(itemName)), amount) + var.getMessages() + " from your inventory.");
                return true;
            }
            short data = 0;
            if (Utils.legalInt(args[2])) //Really is a short
                data = Short.parseShort(args[2]);
            if (!p.getInventory().contains(new ItemStack(Material.getMaterial(itemName), amount, data))) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have " + var.getObj() + amount + var.getMessages() + " of that item.");
                return true;
            }
            p.getInventory().remove(new ItemStack(Material.getMaterial(itemName), amount, data));
            p.sendMessage(var.getMessages() + "Removed " + var.getObj() + amount + " " + mat.pluralize(Utils.capFirst(mat.getName(itemName)), amount) + var.getMessages() + " from your inventory.");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be logged in to use this command.");
        return true;
    }
}