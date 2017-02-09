package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdKick implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a player to kick and a reason.");
            return true;
        }
        UUID uuid = Utils.getID(args[0]);
        if (uuid == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
            return true;
        }
        Player target = Bukkit.getPlayer(uuid);
        String name = Necessities.getConsole().getName().replaceAll(":", "");
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (target.hasPermission("Necessities.antiKick") && !p.hasPermission("Necessities.kickany")) {
                p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You may not kick someone who has Necessities.antiKick unless you have Necessities.kickany.");
                return true;
            }
            name = p.getName();
        }
        String reason = "";
        for (int i = 1; i < args.length; i++)
            reason += args[i] + " ";
        reason = ChatColor.translateAlternateColorCodes('&', reason.trim());
        Bukkit.broadcastMessage(var.getMessages() + name + " kicked " + var.getObj() + target.getName() + (reason.equals("") ? "" : var.getMessages() + " for " + var.getObj() + reason));
        target.kickPlayer(reason);
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}