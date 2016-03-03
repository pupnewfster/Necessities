package com.crossge.necessities.Commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdMore extends Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack hand = player.getInventory().getItemInMainHand();
            if (hand.getType() == Material.AIR)
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are not holding an item.");
            else
                hand.setAmount(64);
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You can do not have any items.");
        return true;
    }
}