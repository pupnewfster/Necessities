package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdWarn implements Cmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length < 2) {
            sender.sendMessage(
                  var.getEr() + "Error: " + var.getErMsg() + "You must enter a player to warn and a reason.");
            return true;
        }
        UUID uuid = Utils.getID(args[0]);
        if (uuid == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
            return true;
        }
        Player target = Bukkit.getPlayer(uuid);
        String name =
              sender instanceof Player ? sender.getName() : Necessities.getConsole().getName().replaceAll(":", "");
        if (sender instanceof Player && target.hasPermission("Necessities.antiPWarn")) {
            sender.sendMessage(
                  var.getEr() + "Error: " + var.getErMsg() + "You may not warn someone who has Necessities.antiPWarn.");
            return true;
        }
        StringBuilder reasonBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            reasonBuilder.append(args[i]).append(' ');
        }
        String reason = reasonBuilder.toString().trim();
        if (sender instanceof Player && sender.hasPermission("Necessities.colorchat")) {
            reason = ChatColor.translateAlternateColorCodes('&',
                  sender.hasPermission("Necessities.magicchat") ? reason : reason.replaceAll("&k", ""));
        } else if (!(sender instanceof Player)) {
            reason = ChatColor.translateAlternateColorCodes('&', reason);
        }
        Necessities.getWarns().warn(target.getUniqueId(), reason, name);
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length != 1) {
            return Collections.emptyList();
        }
        return Utils.getPlayerComplete(sender, args[0]);
    }
}