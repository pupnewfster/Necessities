package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdAfk implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User u = Necessities.getUM().getUser(p.getUniqueId());
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