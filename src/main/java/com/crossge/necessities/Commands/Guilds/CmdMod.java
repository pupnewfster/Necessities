package com.crossge.necessities.Commands.Guilds;

import com.crossge.necessities.Necessities;
import com.crossge.necessities.RankManager.User;
import com.crossge.necessities.RankManager.UserManager;
import com.crossge.necessities.Utils;
import com.crossge.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdMod implements GuildCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.mod")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild mod.");
                return true;
            }
            UserManager um = Necessities.getInstance().getUM();
            User u = um.getUser(p.getUniqueId());
            if (!p.hasPermission("Necessities.guilds.admin") && (u.getGuild() == null || u.getGuild().getRank(p.getUniqueId()) == null ||
                    !u.getGuild().getRank(p.getUniqueId()).equalsIgnoreCase("leader"))) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a leader to make people mods or to demod them.");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a player to promote to mod in your guild.");
                return true;
            }
            UUID uuid = Utils.getID(args[0]);
            if (uuid == null)
                uuid = Utils.getOfflineID(args[0]);
            if (uuid == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That player has not joined the server. If the player is offline, please use the full and most recent name.");
                return true;
            }
            User them = um.getUser(uuid);
            if (them.getGuild() == null || !u.getGuild().equals(them.getGuild())) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "They are not a member of your guild.");
                return true;
            }
            if (u.getGuild().getRank(uuid).equalsIgnoreCase("mod")) {
                u.getGuild().removeMod(uuid);
                sender.sendMessage(var.getMessages() + "Successfully demodded " + var.getObj() + them.getName() + var.getMessages() + ".");
            } else {
                u.getGuild().addMod(uuid);
                sender.sendMessage(var.getMessages() + "Successfully modded " + var.getObj() + them.getName() + var.getMessages() + ".");
            }
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a player to kick someone out of a guild.");
        return true;
    }
}