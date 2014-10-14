package com.crossge.necessities.Commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CmdHat extends Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            ItemStack hand = p.getItemInHand();
            PlayerInventory inv = p.getInventory();
            ItemStack hat = inv.getHelmet();
            if (hand.getType() != Material.AIR) {
                inv.setHelmet(hand);
                inv.setItemInHand(hat);
                p.sendMessage(var.getMessages() + "Hat equiped.");
            } else {
                if (hat != null)
                    inv.setItemInHand(hat);
                inv.setHelmet(new ItemStack(Material.AIR));
                p.sendMessage(var.getMessages() + "Hat removed.");
            }
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have armor.");
        return true;
    }
}