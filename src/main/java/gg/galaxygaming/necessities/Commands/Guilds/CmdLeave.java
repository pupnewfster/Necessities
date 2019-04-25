package gg.galaxygaming.necessities.Commands.Guilds;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdLeave implements GuildCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.leave")) {
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild leave.");
                return true;
            }
            User u = Necessities.getUM().getUser(p.getUniqueId());
            if (u.getGuild() == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are not in a guild.");
                return true;
            }
            String name = u.getGuild().getName();
            u.getGuild().kick(p.getUniqueId());
            sender.sendMessage(
                  var.getMessages() + "Successfully left " + var.getObj() + name + var.getMessages() + '.');
        } else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a player to leave a guild.");
        }
        return true;
    }
}