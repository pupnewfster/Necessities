package com.crossge.necessities.Commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CmdBoots extends Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            ItemStack hand = p.getItemInHand();
            PlayerInventory inv = p.getInventory();
            ItemStack boots = inv.getBoots();
            if (hand.getType() != Material.AIR) {
                inv.setBoots(hand);
                inv.setItemInHand(boots);
                p.sendMessage(var.getMessages() + "Boots equipped.");
            } else {
                if (boots != null)
                    inv.setItemInHand(boots);
                inv.setBoots(new ItemStack(Material.AIR));
                p.sendMessage(var.getMessages() + "Boots removed.");
            }
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have armor.");
        return true;
    }
}