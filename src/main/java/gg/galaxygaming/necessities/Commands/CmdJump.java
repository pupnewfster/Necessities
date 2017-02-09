package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdJump implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User u = Necessities.getInstance().getUM().getUser(p.getUniqueId());
            Location l = u.getLookingAt();
            if (l == null) {
                p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Block out of range.");
                return true;
            }
            if (l.getX() - p.getLocation().getX() > 0)
                l.setX(l.getX() - 1);
            else if (l.getX() - p.getLocation().getX() < 0)
                l.setX(l.getX() + 1);
            if (l.getY() - p.getLocation().getY() > 0)
                l.setY(l.getY() - 1);
            else if (l.getY() - p.getLocation().getY() < 0)
                l.setY(l.getY() + 1);
            if (l.getZ() - p.getLocation().getZ() > 0)
                l.setZ(l.getZ() - 1);
            else if (l.getZ() - p.getLocation().getZ() < 0)
                l.setZ(l.getZ() + 1);
            l.setYaw(p.getLocation().getYaw());
            l.setPitch(p.getLocation().getPitch());
            p.teleport(l);
            p.sendMessage(ChatColor.LIGHT_PURPLE + "Poof!");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You cannot jump because you do not have a body.");
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}