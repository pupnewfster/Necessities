package gg.galaxygaming.necessities.Commands.RankManager;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.RankManager;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CmdSubranks implements RankCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        RankManager rm = Necessities.getRM();
        if (rm.getSubranks().isEmpty()) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "No subranks are set.");
        } else {
            StringBuilder subranksBuilder = new StringBuilder(
                  var.getMessages() + "Available subranks are: " + ChatColor.WHITE);
            for (String subrank : rm.getSubranks()) {
                subranksBuilder.append(subrank).append(var.getMessages()).append(", ").append(ChatColor.WHITE);
            }
            String subranks = subranksBuilder.toString();
            sender.sendMessage(subranks.substring(0, subranks.length() - 2).trim());
        }
        return true;
    }
}