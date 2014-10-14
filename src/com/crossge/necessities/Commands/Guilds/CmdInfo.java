package com.crossge.necessities.Commands.Guilds;

import com.crossge.necessities.Economy.Formatter;
import com.crossge.necessities.Guilds.Guild;
import com.crossge.necessities.RankManager.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdInfo extends GuildCmd {
    Formatter form = new Formatter();

    public boolean commandUse(CommandSender sender, String[] args) {
        Guild g = null;
        if (args.length != 0) {
            g = gm.getGuild(args[0]);
            if (g == null) {
                UUID uuid = get.getID(args[0]);
                if (uuid == null) {
                    uuid = get.getOfflineID(args[0]);
                    if (uuid == null) {
                        sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That player has not joined the server. If the player is offline, please use the full and most recent name.");
                        return true;
                    }
                }
                if (um.getUser(uuid) != null)
                    g = um.getUser(uuid).getGuild();
            }
        }
        Guild other = null;
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.info")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild who.");
                return true;
            }
            User u = um.getUser(p.getUniqueId());
            if (g == null && u.getGuild() == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid guild.");
                return true;
            }
            if (g == null)
                g = u.getGuild();
            other = u.getGuild();
        }
        if (g == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid guild.");
            return true;
        }
        String name = g.getName();
        if (other != null)
            sender.sendMessage(var.getMessages() + parant(name.length() / 2) + ".[ " + g.relation(other) + name + var.getMessages() + " ]." + parant(name.length() / 2));
        else
            sender.sendMessage(var.getMessages() + parant(name.length() / 2) + ".[ " + var.getNeutral() + name + var.getMessages() + " ]." + parant(name.length() / 2));
        sender.sendMessage(var.getMessages() + "Description: " + var.getGuildMsgs() + g.getDescription());
        if (g.isPermanent())
            sender.sendMessage(var.getMessages() + "This guild is permanent - remaining even with no players.");
        sender.sendMessage(var.getMessages() + "Land / Power / Maxpower: " + var.getGuildMsgs() + g.getLand() + "/" + form.roundTwoDecimals(g.getPower()) + "/" +
                g.getMaxPower() + ".00");
        if (!g.getAllies().isEmpty()) {
            String allies = "";
            for (String ally : g.getAllies())
                if (other == null)
                    allies += var.getNeutral() + ally + ", ";
                else
                    allies += other.relation(gm.getGuild(ally)) + ally + ", ";
            sender.sendMessage(var.getMessages() + "Allies: " + allies.substring(0, allies.length() - 2));
        }
        if (!g.getEnemies().isEmpty()) {
            String enemies = "";
            for (String enemy : g.getEnemies())
                if (other == null)
                    enemies += var.getNeutral() + enemy + ", ";
                else
                    enemies += other.relation(gm.getGuild(enemy)) + enemy + ", ";
            sender.sendMessage(var.getMessages() + "Enemies: " + enemies.substring(0, enemies.length() - 2));
        }
        sender.sendMessage(var.getMessages() + "Members online: " + var.getNeutral() + g.onlineMembers());
        sender.sendMessage(var.getMessages() + "Members offline: " + var.getNeutral() + g.offlineMember());
        return true;
    }

    private String parant(int nameLength) {
        String p = "";
        for (int i = 0; i < 22 - nameLength; i++)
            p += "_";
        return p;
    }
}