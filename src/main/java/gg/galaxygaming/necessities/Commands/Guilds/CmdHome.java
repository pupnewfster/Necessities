package gg.galaxygaming.necessities.Commands.Guilds;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdHome implements GuildCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.home")) {
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild home.");
                return true;
            }
            User u = Necessities.getUM().getUser(p.getUniqueId());
            if (u.getGuild() == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "You must be a member of a guild to go to your guilds home.");
                return true;
            }
            if (u.getGuild().getHome() == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Your guild does not have a home set.");
                return true;
            }
            sender.sendMessage(var.getMessages() + "Teleporting to guild home...");
            u.teleport(Necessities.getSafeLocations().getSafe(u.getGuild().getHome()));
        } else {
            sender.sendMessage(
                  var.getEr() + "Error: " + var.getErMsg() + "You must be a player to go to your guilds home.");
        }
        return true;
    }
}