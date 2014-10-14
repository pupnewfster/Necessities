package com.crossge.necessities.Commands;

import com.crossge.necessities.Economy.Formatter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdTpDim extends Cmd {
    Formatter form = new Formatter();

    public boolean commandUse(CommandSender sender, String[] args) {
        if (args.length != 7) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you enter a player " +
                    "a dimension to send them to an x coordinate a y coordinate a z coordinate and a yaw and pitch.");
            return true;
        }
        UUID uuid = get.getID(args[0]);
        if (uuid == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
            return true;
        }
        Player target = sender.getServer().getPlayer(uuid);
        World dim = sender.getServer().getWorld(args[1]);
        if (dim == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid dimension.");
            return true;
        }
        if (!form.isLegal(args[2])) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid x coordinate.");
            return true;
        }
        if (!form.isLegal(args[3])) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid y coordinate.");
            return true;
        }
        if (!form.isLegal(args[4])) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid z coordinate.");
            return true;
        }
        if (!form.isLegal(args[5])) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid yaw.");
            return true;
        }
        if (!form.isLegal(args[6])) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid pitch.");
            return true;
        }
        int x = Integer.parseInt(args[2]);
        int y = Integer.parseInt(args[3]);
        int z = Integer.parseInt(args[4]);
        int yaw = Integer.parseInt(args[5]);
        int pitch = Integer.parseInt(args[6]);
        Location loc = new Location(dim, x, y, z, yaw, pitch);
        target.teleport(safe.getSafe(loc));
        return true;
    }
}