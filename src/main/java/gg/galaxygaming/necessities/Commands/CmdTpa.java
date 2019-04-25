package gg.galaxygaming.necessities.Commands;

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

public class CmdTpa implements Cmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            if (args.length == 0) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "Format requires you enter a player to send a teleport request to.");
                return true;
            }
            UUID uuid = Utils.getID(args[0]);
            if (uuid == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
                return true;
            }
            Player target = Bukkit.getPlayer(uuid);
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.seehidden") && Necessities.getHide().isHidden(target)) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
                return true;
            }
            if (p.getName().equals(target.getName())) {
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "You may not send teleport requests to yourself.");
                return true;
            }
            UserManager um = Necessities.getUM();
            User u = um.getUser(uuid);
            User self = um.getUser(p.getUniqueId());
            if (u.isIgnoring(p.getUniqueId())) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "That user is ignoring you, so you cannot send them a teleport request.");
                return true;
            }
            if (self.isIgnoring(uuid)) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "You are ignoring that user, so cannot send them a teleport request.");
                return true;
            }
            Necessities.getTPs().addRequest(uuid, p.getUniqueId().toString() + " toMe");
            target.sendMessage(var.getObj() + p.getName() + var.getMessages() + " has requested to teleport to you.");
            target.sendMessage(
                  var.getMessages() + "To teleport, type " + var.getObj() + "/tpaccept" + var.getMessages() + '.');
            target.sendMessage(
                  var.getMessages() + "To deny this request, type " + var.getObj() + "/tpdeny" + var.getMessages()
                        + '.');
            p.sendMessage(
                  var.getMessages() + "Request sent to " + var.getObj() + target.getName() + var.getMessages() + '.');
        } else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                  + "You cannot teleport so are unable to request to teleport to someone.");
        }
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        //TODO: TabComplete
        return Collections.emptyList();
    }
}