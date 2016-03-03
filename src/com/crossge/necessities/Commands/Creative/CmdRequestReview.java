package com.crossge.necessities.Commands.Creative;

import com.crossge.necessities.RankManager.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdRequestReview extends CreativeCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User u = um.getUser(p.getUniqueId());
            p.sendMessage(var.getMessages() + "This command is currently Disabled.");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be in game to use this command.");
        return true;
    }
}