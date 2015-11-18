package com.crossge.necessities.Commands.Guilds;

import com.crossge.necessities.Guilds.Guild;
import com.crossge.necessities.RankManager.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdFlag extends GuildCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Guild g = null;
        Guild other = null;
        if (args.length != 0)
            g = gm.getGuild(args[0]);
        String bool = "";
        String opper = "";
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.flag")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild flag.");
                return true;
            }
            User u = um.getUser(p.getUniqueId());
            if (args.length == 0) {
                g = u.getGuild();
                other = u.getGuild();
            }
            if (args.length == 2)
                if (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false")) {
                    g = u.getGuild();
                    other = u.getGuild();
                    opper = args[0];
                    bool = args[1];
                }
            if (args.length > 2)
                if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false")) {
                    opper = args[1];
                    bool = args[2];
                }
            if (g == null && u.getGuild() == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid guild.");
                return true;
            }
            if (g == null) {
                g = u.getGuild();
                other = u.getGuild();
            }
        }
        if (g == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid guild.");
            return true;
        }
        if (bool.equals("")) {
            String name = "Flags for " + g.getName();
            if (other != null)
                sender.sendMessage(var.getMessages() + parant(name.length() / 2) + ".[ " + var.getWild() + "Flags for " + g.relation(other) + g.getName() +
                        var.getMessages() + " ]." + parant(name.length() / 2));
            else
                sender.sendMessage(var.getMessages() + parant(name.length() / 2) + ".[ " + var.getWild() + "Flags for " + var.getNeutral() + g.getName() +
                        var.getMessages() + " ]." + parant(name.length() / 2));
            if (g.isPermanent())
                sender.sendMessage(var.getMessages() + "This guild is permanent - remaining even with no players.");
            else
                sender.sendMessage(var.getMessages() + "This guild is not permanent.");
            if (g.getPower() == -1)
                sender.sendMessage(var.getMessages() + "This guild has infinite power.");
            else
                sender.sendMessage(var.getMessages() + "This guild does not have infinite power.");
            if (g.canPVP())
                sender.sendMessage(var.getMessages() + "PVP is enabled in this guild's territory.");
            else
                sender.sendMessage(var.getMessages() + "PVP is disabled in this guild's territory.");
            if (g.canExplode())
                sender.sendMessage(var.getMessages() + "Explosions are enabled in this guild's territory.");
            else
                sender.sendMessage(var.getMessages() + "Explosions are disabled in this guild's territory.");
            if (g.canHostileSpawn())
                sender.sendMessage(var.getMessages() + "Hostile mobs can spawn in this guild's territory.");
            else
                sender.sendMessage(var.getMessages() + "Hostile mobs cannot spawn in this guild's territory.");
            if (g.allowInteract())
                sender.sendMessage(var.getMessages() + "Players may interact in this guild's territory.");
            else
                sender.sendMessage(var.getMessages() + "Players may not interact in this guild's territory.");
        }
        if (opper.equalsIgnoreCase("permanent")) {
            g.setPermanent(Boolean.parseBoolean(bool));
            if (bool.equalsIgnoreCase("true"))
                sender.sendMessage(var.getMessages() + "This guild is now permanent.");
            else
                sender.sendMessage(var.getMessages() + "This guild is no longer permanent.");
        } else if (opper.equalsIgnoreCase("infinite")) {
            g.toggleInfinite();
            if (bool.equalsIgnoreCase("true"))
                sender.sendMessage(var.getMessages() + "This guild now has infinite power.");
            else
                sender.sendMessage(var.getMessages() + "This guild no longer has infinite power.");
        } else if (opper.equalsIgnoreCase("pvp")) {
            g.setPVP(Boolean.parseBoolean(bool));
            if (bool.equalsIgnoreCase("true"))
                sender.sendMessage(var.getMessages() + "PVP is now enabled in this guild's territory.");
            else
                sender.sendMessage(var.getMessages() + "PVP is now disabled in this guild's territory.");
        } else if (opper.equalsIgnoreCase("explosions")) {
            g.setExplode(Boolean.parseBoolean(bool));
            if (bool.equalsIgnoreCase("true"))
                sender.sendMessage(var.getMessages() + "Explosions are now enabled in this guild's territory.");
            else
                sender.sendMessage(var.getMessages() + "Explosions are now disabled in this guild's territory.");
        } else if (opper.equalsIgnoreCase("interact")) {
            g.setInteract(Boolean.parseBoolean(bool));
            if (bool.equalsIgnoreCase("true"))
                sender.sendMessage(var.getMessages() + "Players can now interact with doors in this guild's territory.");
            else
                sender.sendMessage(var.getMessages() + "Players can no longer interact with doors in this guild's territory.");
        } else if (opper.equalsIgnoreCase("hostilespawn")) {
            g.setHostileSpawn(Boolean.parseBoolean(bool));
            if (bool.equalsIgnoreCase("true"))
                sender.sendMessage(var.getMessages() + "Hostile mobs can now spawn in this guild's territory.");
            else
                sender.sendMessage(var.getMessages() + "Hostile mobs no longer can spawn in this guild's territory.");
        }
        return true;
    }

    private String parant(int nameLength) {
        String p = "";
        for (int i = 0; i < 23 - nameLength; i++)
            p += "_";
        return p;
    }
}