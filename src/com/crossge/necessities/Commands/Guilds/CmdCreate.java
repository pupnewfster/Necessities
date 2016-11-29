package com.crossge.necessities.Commands.Guilds;

import com.crossge.necessities.Economy.BalChecks;
import com.crossge.necessities.Guilds.Guild;
import com.crossge.necessities.Guilds.GuildManager;
import com.crossge.necessities.Necessities;
import com.crossge.necessities.RankManager.User;
import com.crossge.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdCreate implements GuildCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
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
            GuildManager gm = Necessities.getInstance().getGM();
            Guild g = gm.getGuild(args[0]);
            if (g != null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That guild already exists.");
                return true;
            }
            User u = Necessities.getInstance().getUM().getUser(p.getUniqueId());
            if (u.getGuild() != null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are already in a guild.");
                return true;
            }
            BalChecks balc = Necessities.getInstance().getBalChecks();
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