package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdSetHome implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User u = Necessities.getUM().getUser(p.getUniqueId());
            int maxHomes = u.getRank().getMaxHomes();
            int curHomes = u.homeCount();
            String name = "home";
            if (args.length > 0)
                name = args[0].replaceAll("&", "").replaceAll("\\.", "").replaceAll(":", "");
            if (curHomes + 1 > maxHomes && !u.hasHome(name) && maxHomes != -1) {
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