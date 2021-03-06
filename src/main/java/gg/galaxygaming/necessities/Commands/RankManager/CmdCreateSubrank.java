package gg.galaxygaming.necessities.Commands.RankManager;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.RankManager;
import gg.galaxygaming.necessities.Variables;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;

public class CmdCreateSubrank implements RankCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                  + "Format requires you to enter a name for the subrank you are creating.");
            return true;
        }
        RankManager rm = Necessities.getRM();
        String subrank = args[0];
        if (!rm.validSubrank(subrank)) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That subrank already exists.");
            return true;
        }
        rm.addSubrank(subrank);
        sender.sendMessage(var.getObj() + subrank + var.getMessages() + " created and added to list of subranks.");
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        //TODO: TabComplete
        return Collections.emptyList();
    }
}