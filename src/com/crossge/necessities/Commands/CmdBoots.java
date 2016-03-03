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
            PlayerInventory inv = p.getInventory();
            ItemStack hand = inv.getItemInMainHand();
            ItemStack boots = inv.getBoots();
            if (hand.getType() != Material.AIR) {
                inv.setBoots(hand);
                inv.setItemInMainHand(boots);
                p.sendMessage(var.getMessages() + "Boots equipped.");
            } else {
                if (boots != null)
                    inv.setItemInMainHand(boots);
                inv.setBoots(new ItemStack(Material.AIR));
                p.sendMessage(var.getMessages() + "Boots removed.");
            }
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have armor.");
        return true;
    }
}