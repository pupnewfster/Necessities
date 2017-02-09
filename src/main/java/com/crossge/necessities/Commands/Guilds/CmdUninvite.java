package com.crossge.necessities.Commands.Guilds;

import com.crossge.necessities.Necessities;
import com.crossge.necessities.RankManager.User;
import com.crossge.necessities.RankManager.UserManager;
import com.crossge.necessities.Utils;
import com.crossge.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdUninvite implements GuildCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.uninvite")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild uninvite.");
                return true;
            }
            UserManager um = Necessities.getInstance().getUM();
            User u = um.getUser(p.getUniqueId());
            if (u.getGuild() == null || u.getGuild().getRank(p.getUniqueId()) == null || u.getGuild().getRank(p.getUniqueId()).equalsIgnoreCase("member")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a mod or higher in your guild to uninvite members.");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a player to uninvite from your guild.");
                return true;
            }
            UUID uuid = Utils.getID(args[0]);
            if (uuid == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
                return true;
            }
            User them = um.getUser(uuid);
            if (!u.getGuild().isInvited(uuid)) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "This player is not invited to join your guild.");
                return true;
            }
            u.getGuild().uninvite(uuid);
            Bukkit.getPlayer(uuid).sendMessage(var.getMessages() + "Your invitation to join " + var.getObj() + u.getGuild().getName() + var.getMessages() + " has been revoked.");
            sender.sendMessage(var.getMessages() + "Successfully uninvited " + var.getObj() + them.getName() + var.getMessages() + " to your guild.");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a player to uninvite someone from a guild.");
        return true;
    }
}