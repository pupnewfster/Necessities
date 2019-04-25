package gg.galaxygaming.necessities.Commands.Guilds;

import gg.galaxygaming.necessities.Guilds.Guild;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdAlly implements GuildCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.ally")) {
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild ally.");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "Must enter a name for the guild you wish to ally with.");
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
            if (g.equals(u.getGuild()) || g.isAlly(u.getGuild())) {
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "You are already allies with that guild.");
                return true;
            }
            if (g.isInvitedNeutral(u.getGuild()) || u.getGuild().isInvitedNeutral(g)) {
                g.removeNeutralInvite(u.getGuild());
                u.getGuild().removeNeutralInvite(g);
            }
            if (g.isInvitedAlly(u.getGuild())) {
                g.addAlly(u.getGuild());
                u.getGuild().addAlly(g);
                g.sendMods(var.getMessages() + "You are now allies with " + var.getObj() + u.getGuild().getName());
                u.getGuild().sendMods(var.getMessages() + "You are now allies with " + var.getObj() + g.getName());
                return true;
            }
            u.getGuild().addAllyInvite(g);
            g.sendMods(var.getMessages() + "You have been invited to be an ally of " + var.getObj() + u.getGuild()
                  .getName());
            sender.sendMessage(var.getMessages() + "Successfully sent ally request to " + var.getObj() + g.getName());
        } else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be in a guild to offer allyship.");
        }
        return true;
    }
}