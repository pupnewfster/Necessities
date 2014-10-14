package com.crossge.necessities.Commands;

import com.crossge.necessities.RankManager.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdBack extends Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            User u = um.getUser(player.getUniqueId());
            if (u.getLastPos() == null) {//This is now highly unlikely to appear
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not teleported anywhere.");
                return true;
            }
            u.setBacking(true);
            u.teleport(safe.getSafe(u.getLastPos()));
            player.sendMessage(var.getMessages() + "Returning to previous location.");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not gone anywere.");
        return true;
    }
}