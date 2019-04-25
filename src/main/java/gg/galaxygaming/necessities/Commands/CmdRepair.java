package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class CmdRepair implements Cmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0 || args[0].equalsIgnoreCase("hand")) {
                ItemStack hand = player.getInventory().getItemInMainHand();
                if (hand == null || hand.getData().getItemType().equals(Material.AIR)) {
                    player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are not holding an item.");
                    return true;
                }
                ItemMeta itemMeta = hand.getItemMeta();
                if (itemMeta instanceof Damageable) {
                    ((Damageable) itemMeta).setDamage(0);
                    hand.setItemMeta(itemMeta);
                    player.sendMessage(var.getMessages() + "Repaired item in hand.");
                } else {
                    player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You cannot repair that item.");
                }
            } else if (args[0].equalsIgnoreCase("all")) {
                for (ItemStack is : player.getInventory()) {
                    if (is != null) {
                        ItemMeta itemMeta = is.getItemMeta();
                        if (itemMeta instanceof Damageable) {
                            ((Damageable) itemMeta).setDamage(0);
                            is.setItemMeta(itemMeta);
                        }
                    }
                }
                player.sendMessage(var.getMessages() + "Repaired all items.");
            }
        } else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You can do not have any items.");
        }
        return true;
    }
}