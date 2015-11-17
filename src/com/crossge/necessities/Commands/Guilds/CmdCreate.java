package com.crossge.necessities.Commands.Guilds;

import com.crossge.necessities.Economy.BalChecks;
import com.crossge.necessities.Guilds.Guild;
import com.crossge.necessities.RankManager.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdCreate extends GuildCmd {
    BalChecks balc = new BalChecks();

    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.create")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild create.");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Must enter a name for the guild you wish to create.");
                return true;
            }
            Guild g = gm.getGuild(args[0]);
            if (g != null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That guild already exists.");
                return true;
            }
            User u = um.getUser(p.getUniqueId());
            if (u.getGuild() != null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are already in a guild.");
                return true;
            }
            if (balc.balance(u.getUUID()) < 800) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have $800 to spend on creating a guild.");
                return true;
            }
            gm.createGuild(args[0], p.getUniqueId());
            g = gm.getGuild(args[0]);
            u.joinGuild(g);
            g.setLeader(p.getUniqueId());
            balc.removeMoney(u.getUUID(), 800);
            sender.sendMessage(var.getMessages() + "Successfully created guild " + var.getObj() + args[0] + var.getMessages() + ".");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a player to create a guild.");
        return true;
    }
}