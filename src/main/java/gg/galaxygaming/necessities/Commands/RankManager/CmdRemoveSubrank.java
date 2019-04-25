package gg.galaxygaming.necessities.Commands.RankManager;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.RankManager;
import gg.galaxygaming.necessities.Variables;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;

public class CmdRemoveSubrank implements RankCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length == 0) {
            sender.sendMessage(
                  var.getEr() + "Error: " + var.getErMsg() + "Format requires you to enter a subrank to delete.");
            return true;
        }
        String subrank = args[0];
        RankManager rm = Necessities.getRM();
        if (rm.validSubrank(subrank)) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That subrank does not exist.");
            return true;
        }
        rm.removeSubrank(rm.getSub(subrank));
        sender.sendMessage(var.getObj() + subrank + var.getMessages() + " deleted and removed from list of subranks.");
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        //TODO: TabComplete
        return Collections.emptyList();
    }
}