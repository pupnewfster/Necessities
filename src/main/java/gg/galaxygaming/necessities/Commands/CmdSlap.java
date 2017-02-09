package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdSlap implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a player to slap.");
            return true;
        }
        UUID uuid = Utils.getID(args[0]);
        if (uuid == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
            return true;
        }
        Player target = Bukkit.getPlayer(uuid);
        Location loc = target.getLocation().clone().add(0, 2500, 0);
        target.teleport(loc);
        Bukkit.broadcastMessage(var.getMessages() + target.getName() + " was slapped sky high by " + (sender instanceof Player ? sender.getName() : Necessities.getInstance().getConsole().getName().replaceAll(":", "")));
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}