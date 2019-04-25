package gg.galaxygaming.necessities.Commands.RankManager;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.Rank;
import gg.galaxygaming.necessities.RankManager.RankManager;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.RankManager.UserManager;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdSetrank implements RankCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length != 2) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                  + "Format requires you enter a user and a rank to set the user's rank to.");
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
        UserManager um = Necessities.getUM();
        RankManager rm = Necessities.getRM();
        User u = um.getUser(uuid);
        Rank r = rm.getRank(Utils.capFirst(args[1]));
        if (r == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That rank does not exist");
            return true;
        }
        String name = "Console";
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("Necessities.rankmanager.setranksame") && (
                  rm.getOrder().indexOf(um.getUser(player.getUniqueId()).getRank()) - rm.getOrder().indexOf(u.getRank())
                        <= 0 ||
                        rm.getOrder().indexOf(um.getUser(player.getUniqueId()).getRank()) - rm.getOrder().indexOf(r)
                              <= 0)) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "You may not change the rank of someone higher than you.");
                return true;
            }
            name = player.getName();
        }
        String cOld = u.getRank().getColor();
        um.updateUserRank(u, r);
        String c = u.getRank().getColor();
        Bukkit.broadcastMessage(
              var.getMessages() + name + " set " + cOld + Utils.ownerShip(Utils.nameFromString(uuid.toString())) + var
                    .getMessages() + " rank to " + c + u.getRank().getName() + var.getMessages() + '.');
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        //TODO: TabComplete
        return Collections.emptyList();
    }
}