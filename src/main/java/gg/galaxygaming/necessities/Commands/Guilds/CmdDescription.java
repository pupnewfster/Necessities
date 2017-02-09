package gg.galaxygaming.necessities.Commands.Guilds;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdDescription implements GuildCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.description")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild description.");
                return true;
            }
            User u = Necessities.getInstance().getUM().getUser(p.getUniqueId());
            if (u.getGuild() == null || u.getGuild().getRank(p.getUniqueId()) == null || u.getGuild().getRank(p.getUniqueId()).equalsIgnoreCase("member")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a mod or higher in your guild to change its description.");
                return true;
            }
            String description = "";
            for (String arg : args)
                description += arg + " ";
            description = ChatColor.translateAlternateColorCodes('&', description.trim());
            u.getGuild().setDescription(description);
            sender.sendMessage(var.getMessages() + "Set guild description to: " + ChatColor.WHITE + description);
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a player to change your guild's description.");
        return true;
    }
}