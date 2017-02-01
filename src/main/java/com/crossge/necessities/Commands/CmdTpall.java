package com.crossge.necessities.Commands;

import com.crossge.necessities.Necessities;
import com.crossge.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdTpall implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            for (Player t : Bukkit.getOnlinePlayers())
                if (p.hasPermission("Necessities.seehidden") || !Necessities.getInstance().getHide().isHidden(t)) {
                    t.teleport(Necessities.getInstance().getSafeLocations().getSafe(p.getLocation()));
                    t.sendMessage(var.getMessages() + "Teleporting...");
                }
            p.sendMessage(var.getMessages() + "Teleporting all players...");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a player to teleport everyone to yourself.");
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}