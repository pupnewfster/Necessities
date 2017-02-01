package com.crossge.necessities.Commands;

import com.crossge.necessities.Necessities;
import com.crossge.necessities.RankManager.User;
import com.crossge.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdBack implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            User u = Necessities.getInstance().getUM().getUser(player.getUniqueId());
            if (u.getLastPos() == null) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not teleported anywhere.");
                return true;
            }
            u.teleport(Necessities.getInstance().getSafeLocations().getSafe(u.getLastPos()));
            player.sendMessage(var.getMessages() + "Returning to previous location.");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not gone anywhere.");
        return true;
    }
}