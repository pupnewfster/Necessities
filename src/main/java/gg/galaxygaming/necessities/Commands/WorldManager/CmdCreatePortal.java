package gg.galaxygaming.necessities.Commands.WorldManager;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.Variables;
import gg.galaxygaming.necessities.WorldManager.PortalManager;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdCreatePortal implements WorldCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length < 2) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                  + "Format requires you enter a name for the portal and a world that it takes you to.");
            return true;
        }
        PortalManager pm = Necessities.getPM();
        if (sender instanceof Player) {
            if (pm.exists(args[0])) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That portal already exists.");
                return true;
            }
            User u = Necessities.getUM().getUser(((Player) sender).getUniqueId());
            Location left = u.getLeft();
            Location right = u.getRight();
            if (!left.getWorld().equals(right.getWorld())) {
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "Please select left and right corners of portal.");
                return true;
            }
            if (!Necessities.getWM().worldExists(args[1])) {
                if (args[1].startsWith("~")) {
                    pm.create(args[0], args[1], left, right);
                    String server = args[1].replaceFirst("~", "");
                    sender.sendMessage(
                          var.getMessages() + "Created portal " + var.getObj() + args[0] + var.getMessages() + " from "
                                + var.getObj() +
                                left.getWorld().getName() + var.getMessages() + " to server " + var.getObj() + server
                                + var.getMessages() + '.');
                    sender.sendMessage(
                          var.getMessages() + "This portal is between " + var.getObj() + left.getBlockX() + var
                                .getMessages() + ", " + var.getObj() +
                                left.getBlockY() + var.getMessages() + ", " + var.getObj() + left.getBlockZ() + var
                                .getMessages() + " and the server " + var.getObj() + server + var.getMessages() + '.');
                    return true;
                } else if (args[1].startsWith("-") && Necessities.getWarps().isWarp(args[1].replaceFirst("-", ""))) {
                    pm.create(args[0], args[1], left, right);//TODO messages below
                    sender.sendMessage(
                          var.getMessages() + "Created portal " + var.getObj() + args[0] + var.getMessages() + " from "
                                + var.getObj() +
                                left.getWorld().getName() + var.getMessages() + " to the warp " + var.getObj() + args[1]
                                .replaceFirst("-", "") + var.getMessages() + '.');
                    sender.sendMessage(
                          var.getMessages() + "This portal is between " + var.getObj() + left.getBlockX() + var
                                .getMessages() + ", " + var.getObj() +
                                left.getBlockY() + var.getMessages() + ", " + var.getObj() + left.getBlockZ() + var
                                .getMessages() + " and " + var.getObj() + right.getBlockX() +
                                var.getMessages() + ", " + var.getObj() + right.getBlockY() + var.getMessages() + ", "
                                + var.getObj() + right.getBlockZ() + var.getMessages() + '.');
                    return true;
                }
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "Destination server, world, or warp is invalid.");
                return true;
            }
            pm.create(args[0], Bukkit.getWorld(args[1]).getName(), left, right);
            sender.sendMessage(
                  var.getMessages() + "Created portal " + var.getObj() + args[0] + var.getMessages() + " from " + var
                        .getObj() +
                        left.getWorld().getName() + var.getMessages() + " and " + var.getObj() + Bukkit
                        .getWorld(args[1]).getName() + var.getMessages() + '.');
            sender.sendMessage(
                  var.getMessages() + "This portal is between " + var.getObj() + left.getBlockX() + var.getMessages()
                        + ", " + var.getObj() +
                        left.getBlockY() + var.getMessages() + ", " + var.getObj() + left.getBlockZ() + var
                        .getMessages() + " and " + var.getObj() + right.getBlockX() +
                        var.getMessages() + ", " + var.getObj() + right.getBlockY() + var.getMessages() + ", " + var
                        .getObj() + right.getBlockZ() + var.getMessages() + '.');
        } else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                  + "Login to perform this command because you have to click two locations.");
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        //TODO: TabComplete
        return Collections.emptyList();
    }
}