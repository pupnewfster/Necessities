package com.crossge.necessities.Commands.Guilds;

import com.crossge.necessities.Guilds.Guild;
import com.crossge.necessities.Necessities;
import com.crossge.necessities.RankManager.User;
import com.crossge.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdUnclaim implements GuildCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.unclaim")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild unclaim.");
                return true;
            }
            User u = Necessities.getInstance().getUM().getUser(p.getUniqueId());
            if (!p.hasPermission("Necessities.guilds.admin") && (u.getGuild() == null || u.getGuild().getRank(p.getUniqueId()) == null ||
                    u.getGuild().getRank(p.getUniqueId()).equalsIgnoreCase("member"))) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a mod or higher to unclaim land for your guild.");
                return true;
            }
            Guild owner = Necessities.getInstance().getGM().chunkOwner(p.getLocation().getChunk());
            if (owner == null || (!p.hasPermission("Necessities.guilds.admin") && !owner.equals(u.getGuild()))) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Your guild does not own this chunk.");
                return true;
            }
            u.getGuild().unclaim(p.getLocation().getChunk());
            sender.sendMessage(var.getMessages() + "Successfully unclaimed the chunk you are standing in.");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be logged in to unclaim land.");
        return true;
    }
}