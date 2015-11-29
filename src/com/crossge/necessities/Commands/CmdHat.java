package com.crossge.necessities.Commands;
import com.crossge.necessities.Hats.*;
import com.crossge.necessities.RankManager.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdHat extends Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User u = um.getUser(p.getUniqueId());
            if (args.length == 0) {
                u.setHat(null);
                return true;
            }
            HatType type = HatType.valueOf(args[0]);
            if (type == null) {
                p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid hat type.");
                //TODO: Display valid types
                return true;
            }
            Hat h = Hat.fromType(type, p.getLocation());
            if (h == null) {
                p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid hat type.");
                //TODO: Display valid types
                return true;
            }
            u.setHat(h);
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be a player to wear a hat.");
        return true;
    }
}