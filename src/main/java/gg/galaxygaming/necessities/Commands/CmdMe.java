package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdMe implements Cmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        StringBuilder msgBuilder = new StringBuilder();
        for (String s : args) {
            msgBuilder.append(s).append(' ');
        }
        String msg = msgBuilder.toString().trim();
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            User self = Necessities.getUM().getUser(((Player) sender).getUniqueId());
            if (self.isMuted()) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are muted.");
                return true;
            }
            if (self.getPlayer().hasPermission("Necessities.colorchat")) {
                msg = ChatColor.translateAlternateColorCodes('&',
                      self.getPlayer().hasPermission("Necessities.magicchat") ? msg : msg.replaceAll("&k", ""));
            }
            sendMessage(self, msg);
        } else {
            Bukkit.broadcastMessage(
                  var.getMe() + "*" + Necessities.getConsole().getName().replaceAll(":", "") + var.getMe() + msg);
        }
        return true;
    }

    private void sendMessage(User sender, String msg) {
        Variables var = Necessities.getVar();
        for (Player p : Bukkit.getOnlinePlayers()) {
            User u = Necessities.getUM().getUser(p.getUniqueId());
            if (u != null && !u.isIgnoring(sender.getUUID())) {
                p.sendMessage(
                      var.getMe() + "*" + sender.getRank().getColor() + sender.getPlayer().getDisplayName() + var
                            .getMe() + ' ' + msg);
            }
        }
        Bukkit.getConsoleSender().sendMessage(
              var.getMe() + "*" + sender.getRank().getColor() + sender.getPlayer().getDisplayName() + var.getMe() + ' '
                    + msg);
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}