package com.crossge.necessities.Commands;

import com.crossge.necessities.Economy.Formatter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdExp extends Cmd {
    Formatter form = new Formatter();

    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                p.sendMessage(var.getObj() + p.getName() + var.getMessages() + " has " + var.getObj() + form.addCommas(p.getTotalExperience()) + var.getMessages() +
                        " (level " + var.getObj() + p.getLevel() + var.getMessages() + ") and needs " + var.getObj() + p.getExpToLevel() + var.getMessages() +
                        " more xp to level up.");
                return true;
            }
        }
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a player to view xp of.");
            return true;
        }
        if (args.length == 1) {
            UUID uuid = get.getID(args[0]);
            if (uuid == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
                return true;
            }
            Player p = Bukkit.getPlayer(uuid);
            sender.sendMessage(var.getObj() + p.getName() + var.getMessages() + " has " + var.getObj() + form.addCommas(p.getTotalExperience()) + var.getMessages() +
                    " (level " + var.getObj() + p.getLevel() + var.getMessages() + ") and needs " + var.getObj() + p.getExpToLevel() + var.getMessages() +
                    " more xp to level up.");
            return true;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("show")) {
            UUID uuid = get.getID(args[1]);
            if (uuid == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
                return true;
            }
            Player p = Bukkit.getPlayer(uuid);
            sender.sendMessage(var.getObj() + p.getName() + var.getMessages() + " has " + var.getObj() + form.addCommas(p.getTotalExperience()) + var.getMessages() +
                    " (level " + var.getObj() + p.getLevel() + var.getMessages() + ") and needs " + var.getObj() + p.getExpToLevel() + var.getMessages() +
                    " more xp to level up.");
            return true;
        }
        if (args.length == 3) {
            UUID uuid = get.getID(args[1]);
            if (uuid == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
                return true;
            }
            if (args[0].equalsIgnoreCase("set")) {
                if (args[2].contains("L")) {
                    String lvl = args[2].replace("L", "").trim();
                    if (!form.isLegal(lvl) || Integer.parseInt(lvl) < 0) {
                        sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid amount of xp.");
                        return true;
                    }
                    Bukkit.getPlayer(uuid).giveExpLevels(-Bukkit.getPlayer(uuid).getLevel());
                    Bukkit.getPlayer(uuid).giveExp(-Bukkit.getPlayer(uuid).getTotalExperience());
                    Bukkit.getPlayer(uuid).giveExpLevels(Integer.parseInt(lvl));
                } else {
                    if (!form.isLegal(args[2]) || Integer.parseInt(args[2]) < 0) {
                        sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid amount of xp.");
                        return true;
                    }
                    Bukkit.getPlayer(uuid).giveExpLevels(-Bukkit.getPlayer(uuid).getLevel());
                    Bukkit.getPlayer(uuid).giveExp(-Bukkit.getPlayer(uuid).getTotalExperience());
                    Bukkit.getPlayer(uuid).giveExp(Integer.parseInt(args[2]));
                }
                sender.sendMessage(var.getObj() + Bukkit.getPlayer(uuid).getName() + var.getMessages() + " now has " + var.getObj() +
                        Bukkit.getPlayer(uuid).getTotalExperience() + var.getMessages() + " exp.");
                return true;
            }
            if (args[0].equalsIgnoreCase("give")) {
                if (args[2].contains("L")) {
                    String lvl = args[2].replace("L", "").trim();
                    if (!form.isLegal(lvl)) {
                        sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid amount of xp.");
                        return true;
                    }
                    if (Bukkit.getPlayer(uuid).getLevel() + Integer.parseInt(lvl) < 0) {
                        sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid amount of xp.");
                        return true;
                    }
                    Bukkit.getPlayer(uuid).giveExpLevels(Integer.parseInt(lvl));
                } else {
                    if (!form.isLegal(args[2])) {
                        sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid amount of xp.");
                        return true;
                    }
                    if (Bukkit.getPlayer(uuid).getTotalExperience() + Integer.parseInt(args[2]) < 0) {
                        sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid amount of xp.");
                        return true;
                    }
                    Bukkit.getPlayer(uuid).giveExp(Integer.parseInt(args[2]));
                }
                sender.sendMessage(var.getObj() + Bukkit.getPlayer(uuid).getName() + var.getMessages() + " now has " + var.getObj() +
                        Bukkit.getPlayer(uuid).getTotalExperience() + var.getMessages() + " exp.");
            }
        }
        return true;
    }
}