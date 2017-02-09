package com.crossge.necessities.Commands;

import com.crossge.necessities.Necessities;
import com.crossge.necessities.Utils;
import com.crossge.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdFeed implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                p.setFoodLevel(20);
                p.setSaturation(5);
                p.sendMessage(var.getMessages() + "You have been satisfied.");
                return true;
            }
        } else if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot be fed.");
            return true;
        }
        UUID uuid = Utils.getID(args[0]);
        if (uuid == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
            return true;
        }
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.feedOthers"))
                uuid = p.getUniqueId();
        }
        Player target = Bukkit.getPlayer(uuid);
        target.setFoodLevel(20);
        target.setSaturation(5);
        target.sendMessage(var.getMessages() + "You have been satisfied.");
        sender.sendMessage(var.getMessages() + "Satisfied player: " + var.getObj() + target.getDisplayName());
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}