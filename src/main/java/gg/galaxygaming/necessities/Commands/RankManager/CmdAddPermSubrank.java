package gg.galaxygaming.necessities.Commands.RankManager;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.RankManager;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;

public class CmdAddPermSubrank implements RankCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length < 2) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                  + "Format requires a subrank and a permission node to add to that subrank.");
            return true;
        }
        RankManager rm = Necessities.getRM();
        if (rm.validSubrank(args[0])) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That is not a valid subrank.");
            return true;
        }
        String subrank = rm.getSub(args[0]);
        String node = args[1];
        rm.updateSubPerms(subrank, node, false);
        sender.sendMessage(
              var.getMessages() + "Added " + var.getObj() + node + var.getMessages() + " to " + var.getObj() + Utils
                    .ownerShip(Utils.capFirst(subrank)) + var.getMessages() + " permissions.");
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        //TODO: TabComplete
        return Collections.emptyList();
    }
}