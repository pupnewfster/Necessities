package gg.galaxygaming.necessities.Commands.RankManager;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.Rank;
import gg.galaxygaming.necessities.RankManager.RankManager;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CmdRanks implements RankCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        RankManager rm = Necessities.getInstance().getRM();
        if (rm.getOrder().isEmpty())
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "No ranks are set.");
        else {
            String ranks = var.getMessages() + "Available ranks are: " + ChatColor.WHITE;
            for (Rank r : rm.getOrder())
                ranks += r.getName() + var.getMessages() + ", " + ChatColor.WHITE;
            ranks = ranks.substring(0, ranks.length() - 2).trim();
            sender.sendMessage(ranks);
        }
        return true;
    }
}