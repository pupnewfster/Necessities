package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Console;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.RankManager.UserManager;
import gg.galaxygaming.necessities.Variables;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdReply implements Cmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a reply to send.");
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
            if (self.getLastC() == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not messaged anyone yet.");
                return true;
            }
            if (self.getLastC().equals("Console")) {
                StringBuilder messageBuilder = new StringBuilder();
                for (String arg : args) {
                    messageBuilder.append(arg).append(' ');
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
            User u = um.getUser(UUID.fromString(self.getLastC()));
            Player t = Bukkit.getPlayer(u.getUUID());
            if (t == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That player is not online.");
                return true;
            }
            if (u.isIgnoring(p.getUniqueId())) {
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "That user is ignoring you, so you cannot reply.");
                return true;
            }
            if (self.isIgnoring(u.getUUID())) {
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "You are ignoring that user, so cannot message them.");
                return true;
            }
            StringBuilder messageBuilder = new StringBuilder();
            for (String arg : args) {
                messageBuilder.append(arg).append(' ');
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
            if (console.getLastContact() == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not messaged anyone yet.");
                return true;
            }
            User u = um.getUser(console.getLastContact());
            Player t = Bukkit.getPlayer(u.getUUID());
            StringBuilder messageBuilder = new StringBuilder();
            for (String arg : args) {
                messageBuilder.append(arg).append(' ');
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
}