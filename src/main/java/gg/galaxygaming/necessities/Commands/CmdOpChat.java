package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.Variables;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CmdOpChat implements Cmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        String message = "";
        if (args.length > 0) {
            StringBuilder messageBuilder = new StringBuilder();
            for (String arg : args) {
                messageBuilder.append(arg).append(' ');
            }
            message = messageBuilder.toString().trim();
        }
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User u = Necessities.getUM().getUser(p.getUniqueId());
            if (args.length > 0) {
                sendOps(p.getUniqueId(), message);
            } else {
                p.sendMessage(var.getMessages() + "You are " + (!u.opChat() ? "now" : "no longer")
                      + " sending messages only to ops.");
                u.toggleOpChat();
            }
        } else {
            if (args.length == 0) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot toggle opchat.");
            } else {
                consoleToOps(message);
            }
        }
        return true;
    }

    private void sendOps(UUID uuid, String message) {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        Player player = Bukkit.getPlayer(uuid);
        String send = ChatColor.translateAlternateColorCodes('&', config.getString("Necessities.ChatFormat"));
        send = Necessities.getVar().getMessages() + "To Ops - " + ChatColor.WHITE + send;
        send = send.replaceAll("\\{PREFIX} ", "");
        send = send.replaceAll("\\{WORLD} ", "");
        send = send.replaceAll("\\{GUILD} ", "");
        send = send.replaceAll("\\{TITLE} ", "");
        send = send.replaceAll("\\{RANK}",
              ChatColor.translateAlternateColorCodes('&', Necessities.getUM().getUser(uuid).getRank().getTitle()));
        send = send.replaceAll("\\{NAME}", player.getDisplayName());
        send = send.replaceAll("\\{MESSAGE}", "");
        if (player.hasPermission("Necessities.colorchat")) {
            message = ChatColor.translateAlternateColorCodes('&',
                  player.hasPermission("Necessities.magicchat") ? message : message.replaceAll("&k", ""));
        }
        Bukkit.broadcast(send + message, "Necessities.opBroadcast");
    }

    private void consoleToOps(String message) {
        Bukkit.broadcast(
              Necessities.getVar().getMessages() + "To Ops - " + Necessities.getConsole().getName() + ChatColor.WHITE
                    + ' ' +
                    ChatColor.translateAlternateColorCodes('&', message.trim()), "Necessities.opBroadcast");
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}