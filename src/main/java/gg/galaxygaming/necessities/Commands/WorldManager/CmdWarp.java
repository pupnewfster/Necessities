package gg.galaxygaming.necessities.Commands.WorldManager;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Variables;
import gg.galaxygaming.necessities.WorldManager.Warp;
import gg.galaxygaming.necessities.WorldManager.WarpManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdWarp implements WorldCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a name for the warp you wish to teleport to.");
            return true;
        }
        WarpManager warps = Necessities.getWarps();
        if (!warps.isWarp(args[0])) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That warp does not exists.");
            return true;
        }
        if (sender instanceof Player) {
            Warp w = warps.getWarp(args[0]);
            if (!w.hasDestination())
                return true;
            ((Player) sender).teleport(w.getDestination());
            sender.sendMessage(var.getMessages() + "Teleporting to " + var.getObj() + args[0] + var.getMessages() + ".");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a player to teleport warps.");
        return true;
    }
}