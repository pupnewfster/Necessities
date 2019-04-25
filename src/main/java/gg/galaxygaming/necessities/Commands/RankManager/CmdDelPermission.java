package gg.galaxygaming.necessities.Commands.RankManager;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.Rank;
import gg.galaxygaming.necessities.RankManager.RankManager;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.command.CommandSender;

public class CmdDelPermission implements RankCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length != 2) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                  + "Format requires a rank and a permission node to remove from that rank.");
            return true;
        }
        RankManager rm = Necessities.getRM();
        Rank r = rm.getRank(args[0]);
        if (r == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That is not a valid rank.");
            return true;
        }
        String node = args[1];
        rm.updateRankPerms(r, node, true);
        sender.sendMessage(
              var.getMessages() + "Removed " + var.getObj() + node + var.getMessages() + " from " + var.getObj() + Utils
                    .ownerShip(r.getName()) + var.getMessages() + " permissions.");
        return true;
    }
}