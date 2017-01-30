package com.crossge.necessities.Commands;

import com.crossge.necessities.Economy.BalChecks;
import com.crossge.necessities.Necessities;
import com.crossge.necessities.RankManager.User;
import com.crossge.necessities.RankManager.UserManager;
import com.crossge.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdNick implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        Variables var = Necessities.getInstance().getVar();
        boolean free = !(sender instanceof Player);
        UserManager um = Necessities.getInstance().getUM();
        BalChecks balc = Necessities.getInstance().getBalChecks();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            free = p.hasPermission("Necessities.freeCommand");
            if (args.length == 0) {
                User u = um.getUser(p.getUniqueId());
                u.setNick(null);
                p.setDisplayName(p.getName());
                p.sendMessage(var.getMessages() + "Nickname removed.");
                return true;
            } else if (args.length == 1) {
                UUID uuid = Necessities.getInstance().getUUID().getID(args[0]);
                if (ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', args[0] + "&r")).trim().length() > 16 ||
                        ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', args[0] + "&r")).trim().length() < 1) {
                    p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Nicks have a maximum of 16 characters.");
                    return true;
                }
                if (config.contains("Necessities.Economy") && config.getBoolean("Necessities.Economy") && !free && config.contains("Necessities.Creative") && !config.getBoolean("Necessities.Creative")) {
                    if (balc.balance(p.getUniqueId()) < 2000) {
                        p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must have $2000 to change your nick.");
                        return true;
                    }
                    balc.removeMoney(p.getUniqueId(), 2000);
                    p.sendMessage(var.getMoney() + "$2000.00" + var.getMessages() + " was removed from your account.");
                }
                if (uuid == null) {
                    User u = um.getUser(p.getUniqueId());
                    String nick = args[0];
                    nick = ChatColor.translateAlternateColorCodes('&', (p.hasPermission("Necessities.magicchat") ? nick : nick.replaceAll("&k", "")));
                    u.setNick("~" + nick.trim() + "&r");
                    p.setDisplayName("~" + ChatColor.translateAlternateColorCodes('&', nick + "&r").trim());
                    p.sendMessage(var.getMessages() + "Nickname set to " + p.getDisplayName());
                    return true;
                }
                Player target = Bukkit.getPlayer(uuid);
                if (!p.hasPermission("Necessities.nickOthers"))
                    target = p;
                target.setDisplayName(target.getName());
                User u = um.getUser(target.getUniqueId());
                u.setNick(null);
                target.sendMessage(var.getMessages() + "Nickname removed.");
                p.sendMessage(var.getMessages() + "Nickname for " + var.getObj() + target.getName() + var.getMessages() + " removed");
                return true;
            }
        } else if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot have a nickname.");
            return true;
        }
        UUID uuid = Necessities.getInstance().getUUID().getID(args[0]);
        if (uuid == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
            return true;
        }
        Player target = Bukkit.getPlayer(uuid);
        if (sender instanceof Player && !sender.hasPermission("Necessities.nickOthers"))
            target = ((Player) sender);
        if (args.length == 1) {
            target.setDisplayName(target.getName());
            User u = um.getUser(target.getUniqueId());
            u.setNick(null);
            target.sendMessage(var.getMessages() + "Nickname removed.");
            sender.sendMessage(var.getMessages() + "Nickname for " + var.getObj() + target.getName() + var.getMessages() + " removed");
        } else {
            User u = um.getUser(target.getUniqueId());
            if (ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', args[1] + "&r")).trim().length() > 16 ||
                    ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', args[1] + "&r")).trim().length() < 1) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Nicks have a maximum of 16 characters.");
                return true;
            }
            if (config.contains("Necessities.Economy") && config.getBoolean("Necessities.Economy") && !free && config.contains("Necessities.Creative") && !config.getBoolean("Necessities.Creative")) {
                if (balc.balance(target.getUniqueId()) < 2000) {
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must have $2000 to change your nick.");
                    return true;
                }
                balc.removeMoney(target.getUniqueId(), 2000);
                target.sendMessage(var.getMoney() + "$2000.00" + var.getMessages() + " was removed from your account.");
            }
            u.setNick("~" + args[1].trim() + "&r");
            target.setDisplayName("~" + ChatColor.translateAlternateColorCodes('&', args[1] + "&r").trim());
            target.sendMessage(var.getMessages() + "Nickname set to " + target.getDisplayName());
            sender.sendMessage(var.getMessages() + "Nickname for " + var.getObj() + target.getName() + var.getMessages() + " set to " + target.getDisplayName());
        }
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}