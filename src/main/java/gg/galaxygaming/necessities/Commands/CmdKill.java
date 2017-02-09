package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdKill implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a player to kill.");
            return true;
        }
        UUID uuid = Utils.getID(args[0]);
        if (uuid == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
            return true;
        }
        Player target = Bukkit.getServer().getPlayer(uuid);
        target.setHealth(0);
        String reason = "";
        for (int i = 1; i < args.length; i++)
            reason += args[i] + " ";
        reason = ChatColor.translateAlternateColorCodes('&', reason.trim());
        if (reason.equals(""))
            reason = "no reason.";
        Bukkit.broadcastMessage(var.getMessages() + target.getDisplayName() + var.getMessages() + " was killed for " + reason);
        return true;
    }
}