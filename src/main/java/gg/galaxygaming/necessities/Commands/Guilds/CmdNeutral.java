package gg.galaxygaming.necessities.Commands.Guilds;

import gg.galaxygaming.necessities.Guilds.Guild;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.Variables;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdNeutral implements GuildCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.neutral")) {
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild neutral.");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "Must enter a name for the guild you wish to become neutral with.");
                return true;
            }
            Guild g = Necessities.getGM().getGuildVerbose(args[0]);
            if (g == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "That guild does not exists, or no guild owner found with that name.");
                return true;
            }
            User u = Necessities.getUM().getUser(p.getUniqueId());
            if (u.getGuild() == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are not in a guild.");
                return true;
            }
            if (g.equals(u.getGuild()) || u.getGuild().isNeutral(g)) {
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "You are already neutral with that guild.");
                return true;
            }
            if (g.isInvitedAlly(u.getGuild()) || u.getGuild().isInvitedAlly(g)) {
                g.removeAllyInvite(u.getGuild());
                u.getGuild().removeAllyInvite(g);
            }
            if (u.getGuild().isEnemy(g)) {
                if (g.isInvitedNeutral(u.getGuild())) {
                    g.setNeutral(u.getGuild());
                    u.getGuild().setNeutral(g);
                    g.sendMods(var.getMessages() + "You are now neutral with " + var.getObj() + u.getGuild().getName());
                    u.getGuild().sendMods(var.getMessages() + "You are now neutral with " + var.getObj() + g.getName());
                    return true;
                }
                u.getGuild().addNeutralInvite(g);
                g.sendMods(var.getMessages() + "You have been invited to be neutral with " + var.getObj() + u.getGuild()
                      .getName());
                sender.sendMessage(
                      var.getMessages() + "Successfully sent neutral request to " + var.getObj() + g.getName());
                return true;
            }
            g.setNeutral(u.getGuild());
            u.getGuild().setNeutral(g);
            g.sendMods(var.getMessages() + "You are now neutral with " + var.getObj() + u.getGuild().getName());
            u.getGuild().sendMods(var.getMessages() + "You are now neutral with " + var.getObj() + g.getName());
        } else {
            sender.sendMessage(
                  var.getEr() + "Error: " + var.getErMsg() + "You must be in a guild to be able to become neutral.");
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        //TODO: TabComplete
        return Collections.emptyList();
    }
}