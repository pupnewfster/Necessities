package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdTpDim implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length < 7) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you enter a player a dimension to send them to an x coordinate a y coordinate a z coordinate and a yaw and pitch.");
            return true;
        }
        UUID uuid = Utils.getID(args[0]);
        if (uuid == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
            return true;
        }
        Player target = Bukkit.getPlayer(uuid);
        World dim = sender.getServer().getWorld(args[1]);
        if (dim == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid dimension.");
            return true;
        }
        if (!Utils.legalInt(args[2])) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid x coordinate.");
            return true;
        }
        if (!Utils.legalInt(args[3])) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid y coordinate.");
            return true;
        }
        if (!Utils.legalInt(args[4])) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid z coordinate.");
            return true;
        }
        if (!Utils.legalInt(args[5])) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid yaw.");
            return true;
        }
        if (!Utils.legalInt(args[6])) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid pitch.");
            return true;
        }
        target.teleport(Necessities.getSafeLocations().getSafe(new Location(dim, Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]),
                Integer.parseInt(args[6]))));
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}