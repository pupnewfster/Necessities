package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.PlayerNotFoundException;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CmdSkull implements Cmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a player to get their head.");
            return true;
        }
        if (sender instanceof Player) {
            Player p = (Player) sender;
            UUID uuid = Utils.getOfflineID(args[0]);
            ItemStack skull;
            try {
                skull = Utils.getPlayerHead(uuid, args[0]);
            } catch (PlayerNotFoundException e) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "Unable to retrieve skin of entered player, falling back to no skin.");
                skull = new ItemStack(Material.PLAYER_HEAD, 1);
                ItemMeta meta = skull.getItemMeta();
                meta.setDisplayName(Utils.ownerShip(args[0]) + " Head");
                skull.setItemMeta(meta);
            }
            p.getInventory().addItem(skull);
            p.sendMessage(var.getMessages() + "You have been given 1 head of " + var.getObj() + args[0]);
        } else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have an inventory.");
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length != 1) {
            return Collections.emptyList();
        }
        return Utils.getPlayerComplete(sender, args[0]);
    }
}