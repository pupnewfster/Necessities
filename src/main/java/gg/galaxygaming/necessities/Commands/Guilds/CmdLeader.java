package gg.galaxygaming.necessities.Commands.Guilds;

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

public class CmdLeader implements GuildCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.leader")) {
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild leader.");
                return true;
            }
            UserManager um = Necessities.getUM();
            User u = um.getUser(p.getUniqueId());
            if (!p.hasPermission("Necessities.guilds.admin") && (u.getGuild() == null
                  || u.getGuild().getRank(p.getUniqueId()) == null ||
                  !u.getGuild().getRank(p.getUniqueId()).equalsIgnoreCase("leader"))) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "You must be a leader to make transfer your power to another.");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "You must enter a player to make leader in your guild.");
                return true;
            }
            UUID uuid = Utils.getID(args[0]);
            if (uuid == null) {
                uuid = Utils.getOfflineID(args[0]);
            }
            if (uuid == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "That player has not joined the server. If the player is offline, please use the full and most recent name.");
                return true;
            }
            User them = um.getUser(uuid);
            if (them.getGuild() == null || !u.getGuild().equals(them.getGuild())) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "They are not a member of your guild.");
                return true;
            }
            u.getGuild().setLeader(uuid);
            sender.sendMessage(
                  var.getMessages() + "Successfully transferred leadership to " + var.getObj() + them.getName() + var
                        .getMessages() + '.');
        } else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                  + "You must be a player to transfer leadership to someone in your guild.");
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        //TODO: TabComplete
        return Collections.emptyList();
    }
}