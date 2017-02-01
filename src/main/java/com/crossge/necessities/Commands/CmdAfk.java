package com.crossge.necessities.Commands;

import com.crossge.necessities.Necessities;
import com.crossge.necessities.RankManager.User;
import com.crossge.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdAfk implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User u = Necessities.getInstance().getUM().getUser(p.getUniqueId());
            p.sendMessage(var.getMessages() + (!u.isAfk() ? "You are now afk." : "You are no longer afk."));
            u.setAfk(!u.isAfk());
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot go afk.");
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}