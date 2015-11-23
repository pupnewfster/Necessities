package com.crossge.necessities.Commands;

import com.crossge.necessities.Economy.BalChecks;
import com.crossge.necessities.Economy.Formatter;
import com.crossge.necessities.RankManager.User;
import com.crossge.necessities.RankManager.UserManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public class CmdNick extends Cmd {
    private File configFile = new File("plugins/Necessities", "config.yml");
    UserManager um = new UserManager();
    BalChecks balc = new BalChecks();
    Formatter form = new Formatter();

    public boolean commandUse(CommandSender sender, String[] args) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        boolean free = !(sender instanceof Player);
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
                UUID uuid = get.getID(args[0]);
                if (ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', args[0] + "&r")).length() > 24) {
                    p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Nicks have a maximum of 24 characters.");
                    return true;
                }
                if (config.contains("Necessities.Economy") && config.getBoolean("Necessities.Economy") && !free) {
                    if (balc.balance(p.getUniqueId()) < 2000) {
                        p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must have $2000 to change your nick.");
                        return true;
                    }
                    balc.removeMoney(p.getUniqueId(), 2000);
                    p.sendMessage(var.getMoney() + "$2000.00" + var.getMessages() + " was removed from your acount.");
                }
                if (uuid == null) {
                    User u = um.getUser(p.getUniqueId());
                    u.setNick("~" + args[0] + "&r");
                    p.setDisplayName("~" + ChatColor.translateAlternateColorCodes('&', args[0] + "&r"));
                    p.sendMessage(var.getMessages() + "Nickname set to " + p.getDisplayName());
                    return true;
                }
                Player target = sender.getServer().getPlayer(uuid);
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
        UUID uuid = get.getID(args[0]);
        if (uuid == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
            return true;
        }
        Player target = sender.getServer().getPlayer(uuid);
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
            if (ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', args[1] + "&r")).length() > 24) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Nicks have a maximum of 24 characters.");
                return true;
            }
            if (config.contains("Necessities.Economy") && config.getBoolean("Necessities.Economy") && !free) {
                if (balc.balance(target.getUniqueId()) < 2000) {
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must have $2000 to change your nick.");
                    return true;
                }
                balc.removeMoney(target.getUniqueId(), 2000);
                target.sendMessage(var.getMoney() + "$2000.00" + var.getMessages() + " was removed from your acount.");
            }
            u.setNick("~" + args[1] + "&r");
            target.setDisplayName("~" + ChatColor.translateAlternateColorCodes('&', args[1] + "&r"));
            target.sendMessage(var.getMessages() + "Nickname set to " + target.getDisplayName());
            sender.sendMessage(var.getMessages() + "Nickname for " + var.getObj() + target.getName() + var.getMessages() + " set to " + target.getDisplayName());
        }
        return true;
    }
}