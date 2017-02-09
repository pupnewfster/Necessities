package gg.galaxygaming.necessities.Commands.Guilds;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdUnclaimAll implements GuildCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.unclaimall")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild unclaimall.");
                return true;
            }
            User u = Necessities.getUM().getUser(p.getUniqueId());
            if (u.getGuild() == null || u.getGuild().getRank(p.getUniqueId()) == null || u.getGuild().getRank(p.getUniqueId()).equalsIgnoreCase("member")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a mod or higher to unclaimall land for your guild.");
                return true;
            }
            u.getGuild().unclaimAll();
            sender.sendMessage(var.getMessages() + "Successfully unclaimed all chunks your guild owns.");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be logged in to unclaim land.");
        return true;
    }
}