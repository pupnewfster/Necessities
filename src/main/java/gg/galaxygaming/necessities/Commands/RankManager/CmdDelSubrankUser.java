package gg.galaxygaming.necessities.Commands.RankManager;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.RankManager;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class CmdDelSubrankUser implements RankCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length != 2) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires a user and a subrank to remove from that user.");
            return true;
        }
        UUID uuid = Utils.getID(args[0]);
        if (uuid == null)
            uuid = Utils.getOfflineID(args[0]);
        if (uuid == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That player has not joined the server. If the player is offline, please use the full and most recent name.");
            return true;
        }
        RankManager rm = Necessities.getRM();
        String subrank = args[1];
        if (rm.validSubrank(subrank)) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That subrank does not exist");
            return true;
        }
        subrank = rm.getSub(subrank);
        Necessities.getUM().updateUserSubrank(uuid, subrank, true);
        sender.sendMessage(var.getMessages() + "Removed " + var.getObj() + subrank + var.getMessages() + " from " + var.getObj() + Utils.ownerShip(Utils.nameFromString(uuid.toString())) + var.getMessages() + " subranks.");
        return true;
    }
}