package gg.galaxygaming.necessities.Commands.RankManager;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.Rank;
import gg.galaxygaming.necessities.RankManager.RankManager;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;

public class CmdDelSubrank implements RankCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length != 2) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                  + "Format requires a rank and a subrank to remove from that rank.");
            return true;
        }
        RankManager rm = Necessities.getRM();
        Rank r = rm.getRank(args[0]);
        if (r == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That is not a valid rank.");
            return true;
        }
        String subrank = args[1];
        if (rm.validSubrank(subrank)) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That subrank does not exist");
            return true;
        }
        subrank = rm.getSub(subrank);
        rm.updateRankSubrank(r, subrank, true);
        sender.sendMessage(
              var.getMessages() + "Removed " + var.getObj() + subrank + var.getMessages() + " from " + var.getObj()
                    + Utils.ownerShip(r.getName()) + var.getMessages() + " subranks.");
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        //TODO: TabComplete
        return Collections.emptyList();
    }
}