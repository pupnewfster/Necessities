package com.crossge.necessities.Commands.Guilds;

import com.crossge.necessities.RankManager.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdKick extends GuildCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.kick")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild kick.");
                return true;
            }
            User u = um.getUser(p.getUniqueId());
            if (u.getGuild() == null || u.getGuild().getRank(p.getUniqueId()) == null || u.getGuild().getRank(p.getUniqueId()).equalsIgnoreCase("member")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a mod or higher in your guild to kick members.");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a player to kick out of your guild.");
                return true;
            }
            UUID uuid = get.getID(args[0]);
            if (uuid == null) {
                uuid = get.getOfflineID(args[0]);
                if (uuid == null) {
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That player has not joined the server. If the player is offline, please use the full and most recent name.");
                    return true;
                }
            }
            User them = um.getUser(uuid);
            if (them.getGuild() == null || !u.getGuild().equals(them.getGuild())) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "They are not a member of your guild.");
                return true;
            }
            if (them.getGuild().getRank(uuid) == null || them.getGuild().getRank(uuid).equalsIgnoreCase("leader")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You may not kick the leader of your guild.");
                return true;
            }
            if (them.getGuild().getRank(uuid).equalsIgnoreCase("mod") && u.getGuild().getRank(p.getUniqueId()).equalsIgnoreCase("mod")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be the leader of the guild to kick mods.");
                return true;
            }
            u.getGuild().kick(uuid);
            sender.sendMessage(var.getMessages() + "Successfully kicked " + var.getObj() + them.getName() + var.getMessages() + " from your guild.");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a player to kick someone out of a guild.");
        return true;
    }
}