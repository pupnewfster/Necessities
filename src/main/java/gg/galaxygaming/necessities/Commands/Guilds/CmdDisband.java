package gg.galaxygaming.necessities.Commands.Guilds;

import gg.galaxygaming.necessities.Guilds.Guild;
import gg.galaxygaming.necessities.Guilds.GuildManager;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdDisband implements GuildCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        GuildManager gm = Necessities.getGM();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.disband")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild disband.");
                return true;
            }
            User u = Necessities.getUM().getUser(p.getUniqueId());
            if (!p.hasPermission("Necessities.guilds.admin") && (u.getGuild() == null || u.getGuild().getRank(p.getUniqueId()) == null ||
                    !u.getGuild().getRank(p.getUniqueId()).equalsIgnoreCase("leader"))) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be the leader to disband your guild.");
                return true;
            }
            if (u.getGuild() == null && args.length == 0) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are not in a  guild.");
                return true;
            }
            if (args.length > 0) {
                Guild g = gm.getGuild(args[0]);
                if (g == null) {
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid guild.");
                    return true;
                }
                if (g.isPermanent()) {
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That guild is permanent and you cannot disband it.");
                    return true;
                }
                gm.disband(g);
                sender.sendMessage(var.getMessages() + "Successfully disbanded " + var.getObj() + g.getName());
                return true;
            }
            if (u.getGuild() == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are not in a  guild.");
                return true;
            }
            if (u.getGuild().isPermanent()) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Your guild is permanent and you cannot disband it.");
                return true;
            }
            gm.disband(u.getGuild());
            sender.sendMessage(var.getMessages() + "Successfully disbanded your guild.");
        } else {
            if (args.length == 0) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid guild.");
                return true;
            }
            Guild g = gm.getGuild(args[0]);
            if (g == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid guild.");
                return true;
            }
            if (g.isPermanent()) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That guild is permanent and you cannot disband it.");
                return true;
            }
            gm.disband(g);
            sender.sendMessage(var.getMessages() + "Successfully disbanded " + var.getObj() + g.getName());
        }
        return true;
    }
}