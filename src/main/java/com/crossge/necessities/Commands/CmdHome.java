package com.crossge.necessities.Commands;

import com.crossge.necessities.GetUUID;
import com.crossge.necessities.Necessities;
import com.crossge.necessities.RankManager.User;
import com.crossge.necessities.RankManager.UserManager;
import com.crossge.necessities.SafeLocation;
import com.crossge.necessities.Variables;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdHome implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            UserManager um = Necessities.getInstance().getUM();
            SafeLocation safe = Necessities.getInstance().getSafeLocations();
            User u = um.getUser(p.getUniqueId());
            String homes = u.getHomes();
            int homeCount = u.homeCount();
            if (homeCount == 0 || homes.equals("")) {
                p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have any homes.");
                return true;
            }
            if (p.getBedSpawnLocation() != null)
                homes += ", bed";
            if (args.length == 0) {
                if (u.hasHome("home")) {
                    p.sendMessage(var.getMessages() + "Teleporting home...");
                    u.teleport(safe.getSafe(u.getHome("home")));
                    return true;
                }
                p.sendMessage(var.getMessages() + "Homes: " + ChatColor.WHITE + homes);
                return true;
            }
            if (args[0].contains(":") && p.hasPermission("Necessities.homeothers")) {
                if (args[0].equals(":")) {
                    p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a player.");
                    return true;
                }
                String[] info = args[0].replaceAll("&", "").replaceAll("\\.", "").split(":");
                String targetName = info[0];
                GetUUID get = Necessities.getInstance().getUUID();
                UUID uuid = get.getID(targetName);
                if (uuid == null) {
                    uuid = get.getOfflineID(targetName);
                    if (uuid == null) {
                        sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That player has not joined the server. If the player is offline, please use the full and most recent name.");
                        return true;
                    }
                }
                User us = um.getUser(uuid);
                homes = us.getHomes();
                if (info.length == 1 || !us.hasHome(info[1])) {
                    p.sendMessage(var.getMessages() + "Homes: " + ChatColor.WHITE + homes);
                    return true;
                }
                p.teleport(safe.getSafe(us.getHome(info[1])));
                p.sendMessage(var.getMessages() + "Teleported to their home.");
                return true;
            }
            String name = args[0].replaceAll("&", "").replaceAll("\\.", "").replaceAll(":", "");
            if (!u.hasHome(name)) {
                if (name.equalsIgnoreCase("bed"))
                    if (p.getBedSpawnLocation() != null) {
                        u.teleport(safe.getSafe(p.getBedSpawnLocation()));
                        p.sendMessage(var.getMessages() + "Teleporting home...");
                        return true;
                    }
                p.sendMessage(var.getMessages() + "Homes: " + ChatColor.WHITE + homes);
                return true;
            }
            p.sendMessage(var.getMessages() + "Teleporting home...");
            u.teleport(safe.getSafe(u.getHome(name)));
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot goto any homes.");
        return true;
    }
}