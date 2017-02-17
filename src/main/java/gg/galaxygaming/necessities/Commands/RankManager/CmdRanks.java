package gg.galaxygaming.necessities.Commands.RankManager;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.Rank;
import gg.galaxygaming.necessities.RankManager.RankManager;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CmdRanks implements RankCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        RankManager rm = Necessities.getRM();
        if (rm.getOrder().isEmpty())
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "No ranks are set.");
        else {
            StringBuilder ranksBuilder = new StringBuilder(var.getMessages() + "Available ranks are: " + ChatColor.WHITE);
            for (Rank r : rm.getOrder())
                ranksBuilder.append(r.getName()).append(var.getMessages()).append(", ").append(ChatColor.WHITE);
            String ranks = ranksBuilder.toString();
            ranks = ranks.substring(0, ranks.length() - 2).trim();
            sender.sendMessage(ranks);
        }
        return true;
    }
}