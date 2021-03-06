package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Janet.JanetSlack;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdRequestMod implements Cmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        String reason = "";
        if (args.length > 0) {
            StringBuilder reasonBuilder = new StringBuilder();
            for (String arg : args) {
                reasonBuilder.append(arg).append(' ');
            }
            reason = reasonBuilder.toString().trim();
        }
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User u = Necessities.getUM().getUser(p.getUniqueId());
            if (System.currentTimeMillis() - u.getLastRequest() < 300000
                  && System.currentTimeMillis() - u.getLastRequest() > 0) {
                p.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "You requested a mod to recently, please wait a little while and try again.");
                return true;
            }
            JanetSlack slack = Necessities.getSlack();
            if (args.length > 0) {
                slack.sendMessage(p.getName() + " requested a mod with the reason: " + reason);
                Bukkit.broadcast(var.getMessages() + "To Slack - " + ChatColor.WHITE + ChatColor
                            .translateAlternateColorCodes('&', p.getName() + " requested a mod with the reason: " + reason),
                      "Necessities.slack");
            } else {
                slack.sendMessage(p.getName() + " requested a mod.");
                Bukkit.broadcast(var.getMessages() + "To Slack - " + ChatColor.WHITE + ChatColor
                      .translateAlternateColorCodes('&', p.getName() + " requested a mod."), "Necessities.slack");
            }
            p.sendMessage(var.getMessages() + "Request successfully sent.");
            u.setLastRequest(System.currentTimeMillis());
        } else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not need a moderator.");
        }
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}