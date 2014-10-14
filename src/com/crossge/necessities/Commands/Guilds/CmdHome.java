package com.crossge.necessities.Commands.Guilds;

import com.crossge.necessities.RankManager.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdHome extends GuildCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.home")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild home.");
                return true;
            }
            User u = um.getUser(p.getUniqueId());
            if (u.getGuild() == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a member of a guild to go to your guilds home.");
                return true;
            }
            if (u.getGuild().getHome() == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Your guild does not have a home set.");
                return true;
            }
            sender.sendMessage(var.getMessages() + "Teleporting to guild home.");
            u.teleport(safe.getSafe(u.getGuild().getHome()));
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a player to go to your guilds home.");
        return true;
    }
}