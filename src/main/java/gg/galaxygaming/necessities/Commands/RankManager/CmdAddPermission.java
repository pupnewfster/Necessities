package gg.galaxygaming.necessities.Commands.RankManager;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.Rank;
import gg.galaxygaming.necessities.RankManager.RankManager;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;

public class CmdAddPermission implements RankCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length != 2) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                  + "Format requires a rank and a permission node to add to that rank.");
            return true;
        }
        RankManager rm = Necessities.getRM();
        Rank r = rm.getRank(Utils.capFirst(args[0]));
        if (r == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That is not a valid rank.");
            return true;
        }
        String node = args[1];
        rm.updateRankPerms(r, node, false);
        sender.sendMessage(
              var.getMessages() + "Added " + var.getObj() + node + var.getMessages() + " to " + var.getObj() + Utils
                    .ownerShip(r.getName()) + var.getMessages() + " permissions.");
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        //TODO: TabComplete
        return Collections.emptyList();
    }
}