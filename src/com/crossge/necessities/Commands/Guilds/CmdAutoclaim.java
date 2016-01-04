package com.crossge.necessities.Commands.Guilds;

import com.crossge.necessities.RankManager.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdAutoclaim extends GuildCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.autoclaim")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild autoclaim.");
                return true;
            }
            User u = um.getUser(p.getUniqueId());
            sender.sendMessage(var.getMessages() + (u.isClaiming() ? "No longer automatically claiming land." : "Now automatically claiming land."));
            u.setClaiming(!u.isClaiming());
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be logged in to claim land.");
        return true;
    }
}