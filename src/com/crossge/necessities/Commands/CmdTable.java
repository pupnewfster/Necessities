package com.crossge.necessities.Commands;

import com.crossge.necessities.Necessities;
import com.crossge.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdTable implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            ((Player) sender).openEnchanting(null, true);
            sender.sendMessage(var.getMessages() + "Enchanting table opened.");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You can not open an enchanting table because you are not a player.");
        return true;
    }
}