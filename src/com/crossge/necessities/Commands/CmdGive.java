package com.crossge.necessities.Commands;

import com.crossge.necessities.Economy.Formatter;
import com.crossge.necessities.Economy.Materials;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CmdGive extends Cmd {
    Materials mat = new Materials();
    Formatter form = new Formatter();

    public boolean commandUse(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You need to enter an item and a player to give that item to.");
            return true;
        }
        UUID uuid = get.getID(args[0]);
        if (uuid == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
            return true;
        }
        Player t = Bukkit.getPlayer(uuid);
        String itemName = args[1];
        if (form.isLegal(itemName))
            itemName = mat.idToName(Integer.parseInt(itemName));
        itemName = mat.findItem(itemName);
        if (itemName == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That item does not exist.");
            return true;
        }
        if (args.length == 2) {
            t.getInventory().addItem(new ItemStack(Material.getMaterial(itemName), 64));
            sender.sendMessage(var.getMessages() + "Giving " + var.getObj() + "64 " + mat.pluralize(form.capFirst(mat.getName(itemName)), 64) +
                    " to " + t.getDisplayName() + var.getMessages() + ".");
            return true;
        }
        if (!form.isLegal(args[2])) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Illegal amount the amount must be numeric.");
            return true;
        }
        int amount = Integer.parseInt(args[2]);
        short data = 0;
        if (args.length > 3 && form.isLegal(args[3]))
            data = Short.parseShort(args[3]);
        t.getInventory().addItem(new ItemStack(Material.getMaterial(itemName), amount, data));
        sender.sendMessage(var.getMessages() + "Giving " + var.getObj() + amount + " " + mat.pluralize(form.capFirst(mat.getName(itemName)), amount) +
                " to " + t.getDisplayName() + var.getMessages() + ".");
        return true;
    }
}