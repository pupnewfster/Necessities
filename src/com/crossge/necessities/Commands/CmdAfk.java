package com.crossge.necessities.Commands;

import com.crossge.necessities.RankManager.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdAfk extends Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User u = um.getUser(p.getUniqueId());
            p.sendMessage(var.getMessages() + (!u.isAfk() ? "You are now afk." : "You are no longer afk."));
            u.setAfk(!u.isAfk());
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot go afk.");
        return true;
    }
}