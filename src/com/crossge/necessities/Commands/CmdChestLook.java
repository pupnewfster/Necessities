package com.crossge.necessities.Commands;

import com.crossge.necessities.RankManager.User;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

public class CmdChestLook extends Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User u = um.getUser(p.getUniqueId());
            Location l = u.getLookingAt();
            if (l == null) {
                p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Block out of range.");
                return true;
            }
            if (l.getBlock() != null && (l.getBlock().getType().equals(Material.CHEST) || l.getBlock().getType().equals(Material.TRAPPED_CHEST))) {
                p.openInventory(((InventoryHolder) l.getBlock().getState()).getInventory());
                u.setOpenInv(((InventoryHolder) l.getBlock().getState()).getInventory());
                return true;
            }
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That block is not a chest.");
            return true;
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You cannot open chests.");
        return true;
    }
}