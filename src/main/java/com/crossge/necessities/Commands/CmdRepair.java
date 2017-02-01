package com.crossge.necessities.Commands;

import com.crossge.necessities.Necessities;
import com.crossge.necessities.Variables;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdRepair implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            short dur = 0;
            if (args.length == 0 || args[0].equalsIgnoreCase("hand")) {
                ItemStack hand = player.getInventory().getItemInMainHand();
                if (hand == null || hand.getData().getItemType().equals(Material.AIR)) {
                    player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are not holding an item.");
                    return true;
                }
                if (hand.getData().getItemType().equals(Material.ANVIL) || !Necessities.getInstance().getMaterials().isTool(hand)) {
                    player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You cannot repair that item.");
                    return true;
                }
                hand.setDurability(dur);
                player.sendMessage(var.getMessages() + "Repaired item in hand.");
            } else if (args[0].equalsIgnoreCase("all")) {
                for (ItemStack is : player.getInventory())
                    if (is != null && Necessities.getInstance().getMaterials().isTool(is) && !is.getData().getItemType().equals(Material.ANVIL))
                        is.setDurability(dur);
                player.sendMessage(var.getMessages() + "Repaired all items.");
            }
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You can do not have any items.");
        return true;
    }
}