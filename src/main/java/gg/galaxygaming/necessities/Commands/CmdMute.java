package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdMute implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a player to mute.");
            return true;
        }
        UUID uuid = Utils.getID(args[0]);
        if (uuid == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
            return true;
        }
        User u = Necessities.getInstance().getUM().getUser(uuid);
        String name = Necessities.getInstance().getConsole().getName().replaceAll(":", "");
        if (sender instanceof Player)
            name = ((Player) sender).getDisplayName();
        Bukkit.broadcastMessage(var.getObj() + name + var.getMessages() + (!u.isMuted() ? " muted " : " unmuted ") + var.getObj() + u.getPlayer().getDisplayName() + var.getMessages() + ".");
        u.getPlayer().sendMessage(var.getDemote() + "You have been " + var.getObj() + (!u.isMuted() ? "muted" : "unmuted") + var.getMessages() + ".");
        u.setMuted(!u.isMuted());
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}