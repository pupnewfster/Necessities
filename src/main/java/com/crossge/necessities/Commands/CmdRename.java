package com.crossge.necessities.Commands;

import com.crossge.necessities.Necessities;
import com.crossge.necessities.Variables;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CmdRename implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a name to rename your item to.");
            return true;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack hand = player.getInventory().getItemInMainHand();
            if (hand == null || hand.getType().equals(Material.AIR)) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are not holding an item.");
                return true;
            }
            ItemMeta handMeta = hand.getItemMeta();
            String name = "";
            for (String arg : args)
                name += arg + " ";
            name = name.trim();
            name = ChatColor.translateAlternateColorCodes('&', name).trim();
            if (ChatColor.stripColor(name).trim().equals("")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a name to rename your item to.");
                return true;
            }
            handMeta.setDisplayName(name);
            hand.setItemMeta(handMeta);
            player.sendMessage(var.getMessages() + "Successfully renamed your " + var.getObj() + Necessities.getInstance().getMaterials().pluralize(hand.getType().name(), 1) + var.getMessages() + " to " +
                    var.getObj() + name);
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You can not rename your items because you do not have any.");
        return true;
    }
}