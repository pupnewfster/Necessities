package gg.galaxygaming.necessities.Commands.RankManager;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.bukkit.command.CommandSender;

public class CmdAddPermissionUser implements RankCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length != 2) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                  + "Format requires a user and a permission node to add for that user.");
            return true;
        }
        UUID uuid = Utils.getID(args[0]);
        if (uuid == null) {
            uuid = Utils.getOfflineID(args[0]);
        }
        if (uuid == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                  + "That player has not joined the server. If the player is offline, please use the full and most recent name.");
            return true;
        }
        String node = args[1];
        Necessities.getUM().updateUserPerms(uuid, node, false);
        sender.sendMessage(
              var.getMessages() + "Added " + var.getObj() + node + var.getMessages() + " to " + var.getObj() + Utils
                    .ownerShip(Utils.nameFromString(uuid.toString())) + var.getMessages() + " permissions.");
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        //TODO: TabComplete
        return Collections.emptyList();
    }
}