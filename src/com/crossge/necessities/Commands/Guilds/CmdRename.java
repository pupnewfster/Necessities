package com.crossge.necessities.Commands.Guilds;

import com.crossge.necessities.RankManager.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdRename extends GuildCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.rename")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild renane.");
                return true;
            }
            User u = um.getUser(p.getUniqueId());
            if (u.getGuild() == null || u.getGuild().getRank(p.getUniqueId()) == null || !u.getGuild().getRank(p.getUniqueId()).equalsIgnoreCase("leader")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a leader to rename your guild.");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a new name for your guild.");
                return true;
            }
            String name = args[0];
            if (gm.getGuild(name) != null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Another guild already has that name.");
                return true;
            }
            gm.renameGuild(u.getGuild(), name);
            sender.sendMessage(var.getMessages() + "Successfully renamed guild to " + var.getObj() + name + var.getMessages() + ".");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a player to transfer leadership to someone in your guild.");
        return true;
    }
}