package com.crossge.necessities.Commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CmdPants extends Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            PlayerInventory inv = p.getInventory();
            ItemStack hand = inv.getItemInMainHand();
            ItemStack pants = inv.getLeggings();
            if (hand.getType() != Material.AIR) {
                inv.setLeggings(hand);
                inv.setItemInMainHand(pants);
                p.sendMessage(var.getMessages() + "Pants equipped.");
            } else {
                if (pants != null)
                    inv.setItemInMainHand(pants);
                inv.setLeggings(new ItemStack(Material.AIR));
                p.sendMessage(var.getMessages() + "Pants removed.");
            }
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have armor.");
        return true;
    }
}