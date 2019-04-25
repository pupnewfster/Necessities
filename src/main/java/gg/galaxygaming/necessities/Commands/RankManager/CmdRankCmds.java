package gg.galaxygaming.necessities.Commands.RankManager;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.Rank;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CmdRankCmds implements RankCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length == 0) {
            sender.sendMessage(
                  var.getEr() + "Error: " + var.getErMsg() + "You must enter a rank to view the commands of.");
            return true;
        }
        Rank r = Necessities.getRM().getRank(Utils.capFirst(args[0]));
        if (r == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid rank.");
            return true;
        }
        sender.sendMessage(var.getMessages() + "Commands for " + var.getObj() + r.getName() + var.getMessages() + ": "
              + ChatColor.WHITE + r.getCommands());
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        //TODO: TabComplete
        return Collections.emptyList();
    }
}