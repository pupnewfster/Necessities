package gg.galaxygaming.necessities.Commands.Guilds;

import gg.galaxygaming.necessities.Guilds.Guild;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.RankManager.UserManager;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdJoin implements GuildCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.join")) {
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild join.");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a guild to join.");
                return true;
            }
            UserManager um = Necessities.getUM();
            User u = um.getUser(p.getUniqueId());
            if (u.getGuild() != null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are already in a guild.");
                return true;
            }
            Guild g = Necessities.getGM().getGuild(args[0]);
            if (g == null) {
                UUID uuid = Utils.getID(args[0]);
                if (uuid == null) {
                    uuid = Utils.getOfflineID(args[0]);
                }
                if (uuid == null) {
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                          + "That player has not joined the server. If the player is offline, please use the full and most recent name.");
                    return true;
                }
                if (um.getUser(uuid) != null) {
                    g = um.getUser(uuid).getGuild();
                }
            }
            if (g == null) {//If still null....
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid guild.");
                return true;
            }
            if (!p.hasPermission("Necessities.guilds.admin") && !g.isInvited(p.getUniqueId())) {
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "You have not been invited to join that guild.");
                return true;
            }
            g.join(p.getUniqueId());
            g.sendMods(var.getObj() + p.getDisplayName() + var.getMessages() + " joined your guild.");
            sender.sendMessage(
                  var.getMessages() + "Successfully joined " + var.getObj() + g.getName() + var.getMessages() + '.');
        } else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a player to join a guild.");
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        //TODO: TabComplete
        return Collections.emptyList();
    }
}