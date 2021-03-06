package gg.galaxygaming.necessities.Commands.RankManager;

import gg.galaxygaming.necessities.Necessities;
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

public class CmdDemote implements RankCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length != 1) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a user to demote.");
            return true;
        }
        UUID uuid = Utils.getID(args[0]);
        Player target;
        if (uuid == null) {
            uuid = Utils.getOfflineID(args[0]);
            if (uuid == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "That player has not joined the server. If the player is offline, please use the full and most recent name.");
                return true;
            }
            target = Bukkit.getOfflinePlayer(uuid).getPlayer();
        } else {
            target = Bukkit.getPlayer(uuid);
        }
        UserManager um = Necessities.getUM();
        User u = um.getUser(uuid);
        if (u.getRank().getPrevious() == null) {
            sender.sendMessage(
                  var.getEr() + "Error: " + var.getErMsg() + target.getName() + " is already the lowest rank.");
            return true;
        }
        String name = "Console";
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("Necessities.rankmanager.setranksame") && Necessities.getRM()
                  .hasRank(um.getUser(player.getUniqueId()).getRank(), u.getRank())) {
                player.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "You may not demote people a higher or equal rank.");
                return true;
            }
            name = player.getName();
        }
        String cOld = u.getRank().getColor();
        um.updateUserRank(u, u.getRank().getPrevious());
        String c = u.getRank().getColor();
        Bukkit.broadcastMessage(
              var.getDemote() + name + " demoted " + cOld + Utils.nameFromString(uuid.toString()) + var.getDemote()
                    + " to " + c + u.getRank().getName() + var.getDemote() + '.');
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        //TODO: TabComplete
        return Collections.emptyList();
    }
}