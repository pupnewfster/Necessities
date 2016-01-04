package com.crossge.necessities.Commands.Guilds;

import com.crossge.necessities.RankManager.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public class CmdChat extends GuildCmd {
    private File configFile = new File("plugins/Necessities", "config.yml");

    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User u = um.getUser(p.getUniqueId());
            if (!p.hasPermission("Necessities.guilds.chat")) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild chat.");
                return true;
            }
            if (u.getGuild() == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are not part of a guild.");
                return true;
            }
            String message = "";
            if (args.length > 0)
                for (String arg : args)
                    message += arg + " ";
            message = message.trim();
            if (args.length > 0)
                sendGuild(p.getUniqueId(), message);
            else {
                p.sendMessage(var.getMessages() + (!u.guildChat() ? "You are now sending messages only to guild members." : "You are no longer sending messages to guild members."));
                u.toggleGuildChat();
            }
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot toggle guildchat.");
        return true;
    }

    private void sendGuild(UUID uuid, String message) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        Player player = Bukkit.getPlayer(uuid);
        User sender = um.getUser(uuid);
        String send = ChatColor.translateAlternateColorCodes('&', config.getString("Necessities.ChatFormat"));
        send = var.getMessages() + "To Guild - " + ChatColor.WHITE + send;
        send = send.replaceAll("\\{WORLD\\} ", "");
        send = send.replaceAll("\\{GUILD\\} ", "");
        send = send.replaceAll("\\{TITLE\\} ", "");
        send = send.replaceAll("\\{RANK\\}", ChatColor.translateAlternateColorCodes('&', um.getUser(uuid).getRank().getTitle()));
        send = send.replaceAll("\\{NAME\\}", player.getDisplayName());
        send = send.replaceAll("\\{MESSAGE\\}", "");
        if (player.hasPermission("Necessities.colorchat"))
            message = ChatColor.translateAlternateColorCodes('&', (player.hasPermission("Necessities.magicchat") ? message : message.replaceAll("&k", "")));
        for (Player p : Bukkit.getOnlinePlayers()) {
            User u = um.getUser(p.getUniqueId());
            if (u.getGuild() != null && sender.getGuild() == u.getGuild())
                p.sendMessage(send + message);
        }
        Bukkit.getConsoleSender().sendMessage(send + message);
    }
}