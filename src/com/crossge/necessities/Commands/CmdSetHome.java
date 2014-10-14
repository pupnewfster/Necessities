package com.crossge.necessities.Commands;

import com.crossge.necessities.RankManager.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdSetHome extends Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User u = um.getUser(p.getUniqueId());
            int maxHomes = u.getRank().getMaxHomes();
            int curhomes = u.homeCount();
            String name = "home";
            if (args.length > 0)
                name = args[0].replaceAll("&", "").replaceAll("\\.", "").replaceAll(":", "");
            if (curhomes + 1 > maxHomes && !u.hasHome(name) && maxHomes != -1) {
                p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You have reached your max number of homes for this rank.");
                return true;
            }
            u.addHome(p.getLocation(), name);
            p.sendMessage(var.getMessages() + "Home set.");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot set any homes.");
        return true;
    }
}