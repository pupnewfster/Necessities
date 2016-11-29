package com.crossge.necessities.Commands;

import com.crossge.necessities.Economy.Materials;
import com.crossge.necessities.Necessities;
import com.crossge.necessities.Utils;
import com.crossge.necessities.Variables;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdItem implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You need to enter an item.");
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
                p.getInventory().addItem(new ItemStack(Material.getMaterial(itemName), 64));
                p.sendMessage(var.getMessages() + "Giving " + var.getObj() + "64 " + mat.pluralize(Utils.capFirst(mat.getName(itemName)), 64) + var.getMessages() + ".");
                return true;
            }
            if (!Utils.legalInt(args[1])) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Illegal amount the amount must be numeric.");
                return true;
            }
            int amount = Integer.parseInt(args[1]);
            if (args.length == 2) {

                p.getInventory().addItem(new ItemStack(Material.getMaterial(itemName), amount));
                p.sendMessage(var.getMessages() + "Giving " + var.getObj() + amount + " " + mat.pluralize(Utils.capFirst(mat.getName(itemName)), amount) + var.getMessages() + ".");
                return true;
            }
            short data = 0;
            if (Utils.legalInt(args[2])) //Really a short
                data = Short.parseShort(args[2]);
            p.getInventory().addItem(new ItemStack(Material.getMaterial(itemName), amount, data));
            p.sendMessage(var.getMessages() + "Giving " + var.getObj() + amount + " " + mat.pluralize(Utils.capFirst(mat.getName(itemName)), amount) + var.getMessages() + ".");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be logged in to use this command.");
        return true;
    }
}