package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CmdChest implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            PlayerInventory inv = p.getInventory();
            ItemStack hand = inv.getItemInMainHand();
            ItemStack chestplate = inv.getChestplate();
            if (hand.getType() != Material.AIR) {
                inv.setChestplate(hand);
                inv.setItemInMainHand(chestplate);
                p.sendMessage(var.getMessages() + "Chestplate equipped.");
            } else {
                if (chestplate != null)
                    inv.setItemInMainHand(chestplate);
                inv.setChestplate(new ItemStack(Material.AIR));
                p.sendMessage(var.getMessages() + "Chestplate removed.");
            }
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have armor.");
        return true;
    }
}