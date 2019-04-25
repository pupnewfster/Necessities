package gg.galaxygaming.necessities.Commands.Guilds;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.RankManager.UserManager;
import gg.galaxygaming.necessities.Variables;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CmdChat implements GuildCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User u = Necessities.getUM().getUser(p.getUniqueId());
            if (!p.hasPermission("Necessities.guilds.chat")) {
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "You have not have permission to use /guild chat.");
                return true;
            }
            if (u.getGuild() == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are not part of a guild.");
                return true;
            }
            String message = "";
            if (args.length > 0) {
                StringBuilder messageBuilder = new StringBuilder();
                for (String arg : args) {
                    messageBuilder.append(arg).append(' ');
                }
                message = messageBuilder.toString().trim();
            }
            if (args.length > 0) {
                sendGuild(p.getUniqueId(), message);
            } else {
                p.sendMessage(
                      var.getMessages() + (!u.guildChat() ? "You are now sending messages only to guild members."
                            : "You are no longer sending messages to guild members."));
                u.toggleGuildChat();
            }
        } else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot toggle guild chat.");
        }
        return true;
    }

    private void sendGuild(UUID uuid, String message) {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        Player player = Bukkit.getPlayer(uuid);
        UserManager um = Necessities.getUM();
        User sender = um.getUser(uuid);
        String send = ChatColor.translateAlternateColorCodes('&', config.getString("Necessities.ChatFormat"));
        send = Necessities.getVar().getMessages() + "To Guild - " + ChatColor.WHITE + send;
        send = send.replaceAll("\\{PREFIX} ", "");
        send = send.replaceAll("\\{WORLD} ", "");
        send = send.replaceAll("\\{GUILD} ", "");
        send = send.replaceAll("\\{TITLE} ", "");
        send = send.replaceAll("\\{RANK}",
              ChatColor.translateAlternateColorCodes('&', um.getUser(uuid).getRank().getTitle()));
        send = send.replaceAll("\\{NAME}", player.getDisplayName());
        send = send.replaceAll("\\{MESSAGE}", "");
        if (player.hasPermission("Necessities.colorchat")) {
            message = ChatColor.translateAlternateColorCodes('&',
                  player.hasPermission("Necessities.magicchat") ? message : message.replaceAll("&k", ""));
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            User u = um.getUser(p.getUniqueId());
            if (u.getGuild() != null && sender.getGuild() == u.getGuild()) {
                p.sendMessage(send + message);
            }
        }
        Bukkit.getConsoleSender().sendMessage(send + message);
    }
}