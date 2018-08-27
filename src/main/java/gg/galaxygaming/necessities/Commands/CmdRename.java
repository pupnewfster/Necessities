package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CmdRename implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
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
            StringBuilder nameBuilder = new StringBuilder();
            for (String arg : args)
                nameBuilder.append(arg).append(' ');
            String name = ChatColor.translateAlternateColorCodes('&', nameBuilder.toString()).trim();
            if (ChatColor.stripColor(name).trim().equals("")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a name to rename your item to.");
                return true;
            }
            handMeta.setDisplayName(name);
            hand.setItemMeta(handMeta);
            player.sendMessage(var.getMessages() + "Successfully renamed your " + var.getObj() + gg.galaxygaming.necessities.Economy.Material.fromBukkit(hand.getType()).getFriendlyName() +
                    var.getMessages() + " to " + var.getObj() + name);
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You can not rename your items because you do not have any.");
        return true;
    }
}