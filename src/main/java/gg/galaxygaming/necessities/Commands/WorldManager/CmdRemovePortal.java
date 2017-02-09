package gg.galaxygaming.necessities.Commands.WorldManager;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Variables;
import gg.galaxygaming.necessities.WorldManager.PortalManager;
import org.bukkit.command.CommandSender;

public class CmdRemovePortal implements WorldCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a portal to remove.");
            return true;
        }
        PortalManager pm = Necessities.getPM();
        if (!pm.exists(args[0])) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That portal does not exist.");
            return true;
        }
        pm.remove(args[0]);
        sender.sendMessage(var.getMessages() + "Removed portal named " + var.getObj() + args[0] + var.getMessages() + ".");
        return true;
    }
}