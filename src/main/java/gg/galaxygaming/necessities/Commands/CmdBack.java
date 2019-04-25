package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdBack implements Cmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            User u = Necessities.getUM().getUser(player.getUniqueId());
            if (u.getLastPos() == null) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not teleported anywhere.");
                return true;
            }
            u.teleport(Necessities.getSafeLocations().getSafe(u.getLastPos()));
            player.sendMessage(var.getMessages() + "Returning to previous location.");
        } else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not gone anywhere.");
        }
        return true;
    }
}