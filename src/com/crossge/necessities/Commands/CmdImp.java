package com.crossge.necessities.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdImp extends Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Must enter a message.");
            return true;
        }
        UUID uuid = get.getID(args[0]);
        Player p = null;
        if (uuid != null)
            p = sender.getServer().getPlayer(uuid);
        if (args.length > 1 && p != null) {
            String message = "";
            for (String a : args)
                message = message + " " + a;
            message = ChatColor.translateAlternateColorCodes('&', message.replaceFirst(args[0], "").trim());
            if (message.startsWith("/"))
                message = message.replaceFirst("/", "");
            p.chat(message);
            return true;
        }
        String message = "";
        for (String a : args)
            message = message + " " + a;
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message.trim()));
        return true;
    }
}