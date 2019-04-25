package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdImp implements Cmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        if (args.length == 0) {
            Variables var = Necessities.getVar();
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Must enter a message.");
            return true;
        }
        UUID uuid = Utils.getID(args[0]);
        Player p = null;
        if (uuid != null) {
            p = Bukkit.getPlayer(uuid);
        }
        if (args.length > 1 && p != null) {
            StringBuilder messageBuilder = new StringBuilder();
            for (String a : args) {
                messageBuilder.append(' ').append(a);
            }
            String message = ChatColor
                  .translateAlternateColorCodes('&', messageBuilder.toString().replaceFirst(args[0], "").trim());
            while (message.startsWith("/")) {
                message = message.replaceFirst("/", "");
            }
            p.chat(message);
            return true;
        }
        StringBuilder message = new StringBuilder();
        for (String a : args) {
            message.append(' ').append(a);
        }
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message.toString().trim()));
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}