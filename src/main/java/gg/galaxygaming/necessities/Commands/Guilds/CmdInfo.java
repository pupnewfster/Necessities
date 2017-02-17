package gg.galaxygaming.necessities.Commands.Guilds;

import gg.galaxygaming.necessities.Guilds.Guild;
import gg.galaxygaming.necessities.Guilds.GuildManager;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.RankManager.UserManager;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdInfo implements GuildCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Guild g = null;
        Variables var = Necessities.getVar();
        UserManager um = Necessities.getUM();
        GuildManager gm = Necessities.getGM();
        if (args.length != 0) {
            g = gm.getGuild(args[0]);
            if (g == null) {
                UUID uuid = Utils.getID(args[0]);
                if (uuid == null)
                    uuid = Utils.getOfflineID(args[0]);
                if (uuid == null) {
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That player has not joined the server. If the player is offline, please use the full and most recent name.");
                    return true;
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
        sender.sendMessage(var.getMessages() + "Land / Power / Max power: " + var.getGuildMsgs() + g.getLand() + "/" + Utils.roundTwoDecimals(g.getPower()) + "/" + g.getMaxPower() + ".00");
        if (!g.getAllies().isEmpty()) {
            StringBuilder alliesBuilder = new StringBuilder();
            for (String ally : g.getAllies())
                alliesBuilder.append(other == null ? var.getNeutral() + ally + ", " : other.relation(gm.getGuild(ally)) + ally + ", ");
            String allies = alliesBuilder.toString();
            sender.sendMessage(var.getMessages() + "Allies: " + allies.substring(0, allies.length() - 2));
        }
        if (!g.getEnemies().isEmpty()) {
            StringBuilder enemiesBuilder = new StringBuilder();
            for (String enemy : g.getEnemies())
                enemiesBuilder.append(other == null ? var.getNeutral() + enemy + ", " : other.relation(gm.getGuild(enemy)) + enemy + ", ");
            String enemies = enemiesBuilder.toString();
            sender.sendMessage(var.getMessages() + "Enemies: " + enemies.substring(0, enemies.length() - 2));
        }
        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.sendMessage(var.getMessages() + "Members online: " + var.getNeutral() + g.onlineMembers(p.hasPermission("Necessities.seehidden")));
            p.sendMessage(var.getMessages() + "Members offline: " + var.getNeutral() + g.offlineMember(p.hasPermission("Necessities.seehidden")));
        } else {
            sender.sendMessage(var.getMessages() + "Members online: " + var.getNeutral() + g.onlineMembers(true));
            sender.sendMessage(var.getMessages() + "Members offline: " + var.getNeutral() + g.offlineMember(true));
        }
        return true;
    }

    private String parant(int nameLength) {
        StringBuilder p = new StringBuilder();
        for (int i = 0; i < 22 - nameLength; i++)
            p.append("_");
        return p.toString();
    }
}