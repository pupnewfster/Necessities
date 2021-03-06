package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Console;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.RankManager.UserManager;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdMsg implements Cmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length < 2) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                  + "You must enter a player to message and a message to send.");
            return true;
        }
        Console console = Necessities.getConsole();
        UserManager um = Necessities.getUM();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User self = um.getUser(p.getUniqueId());
            if (self.isMuted()) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are muted.");
                return true;
            }
            if (args[0].equalsIgnoreCase("console")) {
                StringBuilder messageBuilder = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    messageBuilder.append(args[i]).append(' ');
                }
                String message = ChatColor.WHITE + messageBuilder.toString().trim();
                if (p.hasPermission("Necessities.colorchat")) {
                    message = ChatColor.translateAlternateColorCodes('&',
                          p.hasPermission("Necessities.magicchat") ? message : message.replaceAll("&k", ""));
                }
                self.setLastC("Console");
                console.setLastContact(self.getUUID());
                p.sendMessage(var.getMessages() + "[me -> " + console.getName().replaceAll(":", "") + "] " + message);
                Bukkit.getConsoleSender().sendMessage(
                      var.getMessages() + "[" + p.getDisplayName() + var.getMessages() + " -> me] " + message);
                return true;
            }
            UUID uuid = Utils.getID(args[0]);
            if (uuid == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
                return true;
            }
            User u = um.getUser(uuid);
            if (u.isIgnoring(p.getUniqueId())) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "That user is ignoring you, so you cannot message them.");
                return true;
            }
            if (self.isIgnoring(uuid)) {
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "You are ignoring that user, so cannot message them.");
                return true;
            }
            Player t = Bukkit.getPlayer(uuid);
            StringBuilder messageBuilder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                messageBuilder.append(args[i]).append(' ');
            }
            String message = ChatColor.WHITE + messageBuilder.toString().trim();
            if (p.hasPermission("Necessities.colorchat")) {
                message = ChatColor.translateAlternateColorCodes('&',
                      p.hasPermission("Necessities.magicchat") ? message : message.replaceAll("&k", ""));
            }
            u.setLastC(self.getUUID().toString());
            self.setLastC(u.getUUID().toString());
            p.sendMessage(var.getMessages() + "[me -> " + t.getDisplayName() + var.getMessages() + "] " + message);
            t.sendMessage(var.getMessages() + "[" + p.getDisplayName() + var.getMessages() + " -> me] " + message);
        } else {
            UUID uuid = Utils.getID(args[0]);
            if (uuid == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
                return true;
            }
            User u = um.getUser(uuid);
            Player t = Bukkit.getPlayer(uuid);
            StringBuilder messageBuilder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                messageBuilder.append(args[i]).append(' ');
            }
            String message = ChatColor
                  .translateAlternateColorCodes('&', ChatColor.WHITE + messageBuilder.toString().trim());
            u.setLastC("Console");
            console.setLastContact(u.getUUID());
            sender.sendMessage(var.getMessages() + "[me -> " + t.getDisplayName() + var.getMessages() + "] " + message);
            t.sendMessage(var.getMessages() + "[" + console.getName().replaceAll(":", "") + " -> me] " + message);
        }
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length != 1) {
            return Collections.emptyList();
        }
        return Utils.getPlayerComplete(sender, args[0]);
    }
}