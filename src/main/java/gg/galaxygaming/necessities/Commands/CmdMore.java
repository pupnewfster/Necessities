package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdMore implements Cmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack hand = player.getInventory().getItemInMainHand();
            if (hand.getType() == Material.AIR) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are not holding an item.");
            } else {
                hand.setAmount(64);
            }
        } else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You can do not have any items.");
        }
        return true;
    }
}