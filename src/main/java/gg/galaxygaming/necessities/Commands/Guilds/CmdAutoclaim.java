package gg.galaxygaming.necessities.Commands.Guilds;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdAutoclaim implements GuildCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.autoclaim")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild autoclaim.");
                return true;
            }
            User u = Necessities.getUM().getUser(p.getUniqueId());
            sender.sendMessage(var.getMessages() + (u.isClaiming() ? "No longer automatically claiming land." : "Now automatically claiming land."));
            u.setClaiming(!u.isClaiming());
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be logged in to claim land.");
        return true;
    }
}