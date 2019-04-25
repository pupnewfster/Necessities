package gg.galaxygaming.necessities.Commands.Guilds;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.RankManager.UserManager;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdInvite implements GuildCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.invite")) {
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild invite.");
                return true;
            }
            UserManager um = Necessities.getUM();
            User u = um.getUser(p.getUniqueId());
            if (u.getGuild() == null || u.getGuild().getRank(p.getUniqueId()) == null || u.getGuild()
                  .getRank(p.getUniqueId()).equalsIgnoreCase("member")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "You must be a mod or higher in your guild to invite members.");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "You must enter a player to invite to your guild.");
                return true;
            }
            UUID uuid = Utils.getID(args[0]);
            if (uuid == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
                return true;
            }
            User them = um.getUser(uuid);
            if (u.getGuild().equals(them.getGuild())) {
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "They are already a member of your guild.");
                return true;
            }
            if (u.getGuild().isInvited(uuid)) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "This player has already been invited to join your guild.");
                return true;
            }
            u.getGuild().invite(uuid);
            Bukkit.getPlayer(uuid).sendMessage(
                  var.getMessages() + "You have been invited to join " + var.getObj() + u.getGuild().getName());
            Bukkit.getPlayer(uuid).sendMessage(
                  var.getMessages() + "Type /guild join " + var.getObj() + u.getGuild().getName() + var.getMessages()
                        + " to join.");
            sender.sendMessage(
                  var.getMessages() + "Successfully invited " + var.getObj() + them.getName() + var.getMessages()
                        + " to your guild.");
        } else {
            sender.sendMessage(
                  var.getEr() + "Error: " + var.getErMsg() + "You must be a player to invite someone to a guild.");
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        //TODO: TabComplete
        return Collections.emptyList();
    }
}