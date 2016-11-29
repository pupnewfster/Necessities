package com.crossge.necessities.Commands.Guilds;

import com.crossge.necessities.Guilds.Guild;
import com.crossge.necessities.Necessities;
import com.crossge.necessities.RankManager.User;
import com.crossge.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdFlag implements GuildCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Guild g = null;
        Guild other = null;
        if (args.length != 0)
            g = Necessities.getInstance().getGM().getGuild(args[0]);
        String bool = "";
        String opper = "";
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.guilds.flag")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild flag.");
                return true;
            }
            User u = Necessities.getInstance().getUM().getUser(p.getUniqueId());
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
            sender.sendMessage(var.getMessages() + (g.isPermanent() ? "This guild is permanent - remaining even with no players." : "This guild is not permanent."));
            sender.sendMessage(var.getMessages() + (g.getPower() == -1 ? "This guild has infinite power." : "This guild does not have infinite power."));
            sender.sendMessage(var.getMessages() + (g.canPVP() ? "PVP is enabled in this guild's territory." : "PVP is disabled in this guild's territory."));
            sender.sendMessage(var.getMessages() + (g.canExplode() ? "Explosions are enabled in this guild's territory." : "Explosions are disabled in this guild's territory."));
            sender.sendMessage(var.getMessages() + (g.canHostileSpawn() ? "Hostile mobs can spawn in this guild's territory." : "Hostile mobs cannot spawn in this guild's territory."));
            sender.sendMessage(var.getMessages() + (g.allowInteract() ? "Players may interact in this guild's territory." : "Players may not interact in this guild's territory."));
            return true;
        }
        boolean b = bool.trim().equalsIgnoreCase("true");
        if (opper.equalsIgnoreCase("permanent")) {
            g.setPermanent(b);
            sender.sendMessage(var.getMessages() + (b ? "This guild is now permanent." : "This guild is no longer permanent."));
        } else if (opper.equalsIgnoreCase("infinite")) {
            if (g.getPower() != -1 && b || !b && g.getPower() == -1)
                g.toggleInfinite();
            sender.sendMessage(var.getMessages() + (b ? "This guild now has infinite power." : "This guild no longer has infinite power."));
        } else if (opper.equalsIgnoreCase("pvp")) {
            g.setPVP(b);
            sender.sendMessage(var.getMessages() + (b ? "PVP is now enabled in this guild's territory." : "PVP is now disabled in this guild's territory."));
        } else if (opper.equalsIgnoreCase("explosions")) {
            g.setExplode(b);
            sender.sendMessage(var.getMessages() + (b ? "Explosions are now enabled in this guild's territory." : "Explosions are now disabled in this guild's territory."));
        } else if (opper.equalsIgnoreCase("interact")) {
            g.setInteract(b);
            sender.sendMessage(var.getMessages() + (b ? "Players can now interact with doors in this guild's territory." : "Players can no longer interact with doors in this guild's territory."));
        } else if (opper.equalsIgnoreCase("hostilespawn")) {
            g.setHostileSpawn(b);
            sender.sendMessage(var.getMessages() + (b ? "Hostile mobs can now spawn in this guild's territory." : "Hostile mobs no longer can spawn in this guild's territory."));
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