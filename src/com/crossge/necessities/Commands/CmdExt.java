package com.crossge.necessities.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdExt extends Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                p.setFireTicks(0);
                p.sendMessage(var.getMessages() + "Extinguished.");
                return true;
            }
        } else if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console is not on fire. At least it shouldn't be...");
            return true;
        }
        UUID uuid = get.getID(args[0]);
        if (uuid == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
            return true;
        }
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.extOthers"))
                uuid = p.getUniqueId();
        }
        Player target = Bukkit.getPlayer(uuid);
        target.setFireTicks(0);
        target.sendMessage(var.getMessages() + "You have been extinguished.");
        sender.sendMessage(var.getMessages() + "Extinguished player: " + var.getObj() + target.getDisplayName());
        return true;
    }
}