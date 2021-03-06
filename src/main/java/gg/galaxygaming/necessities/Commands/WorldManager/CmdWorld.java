package gg.galaxygaming.necessities.Commands.WorldManager;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdWorld implements WorldCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a world name.");
            return true;
        }
        if (sender instanceof Player) {
            String wName = args[args.length > 1 ? 1 : 0];
            World dim = sender.getServer().getWorld(wName);
            if (dim == null) {
                for (World w : Bukkit.getWorlds()) {
                    if (w.getName().startsWith(wName)) {
                        dim = w;
                        break;
                    } else if (w.getName().contains(wName)) {
                        dim = w;
                    }
                }
            }
            if (dim == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid world.");
                return true;
            }
            if (args.length > 1) {
                UUID uuid = Utils.getID(args[0]);
                if (uuid == null) {
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
                    return true;
                }
                Bukkit.getPlayer(uuid).teleport(dim.getSpawnLocation());
                sender.sendMessage(
                      var.getMessages() + "Teleported " + var.getObj() + Bukkit.getPlayer(uuid).getName() + var
                            .getMessages() + " to " + var.getObj() + dim.getName() + var.getMessages() + '.');
                return true;
            }
            ((Player) sender).teleport(dim.getSpawnLocation());
            sender.sendMessage(
                  var.getMessages() + "Teleported to " + var.getObj() + dim.getName() + var.getMessages() + '.');
        } else {
            if (args.length > 1) {
                String wName = args[1];
                World dim = sender.getServer().getWorld(wName);
                if (dim == null) {
                    for (World w : Bukkit.getWorlds()) {
                        if (w.getName().startsWith(wName)) {
                            dim = w;
                            break;
                        } else if (w.getName().contains(wName)) {
                            dim = w;
                        }
                    }
                }
                if (dim == null) { //Should never be possible but just in case
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid dimension.");
                    return true;
                }
                UUID uuid = Utils.getID(args[0]);
                if (uuid == null) {
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
                    return true;
                }
                Bukkit.getPlayer(uuid).teleport(dim.getSpawnLocation());
                sender.sendMessage(
                      var.getMessages() + "Teleported " + var.getObj() + Bukkit.getPlayer(uuid).getName() + var
                            .getMessages() + " to " + var.getObj() + dim.getName() + var.getMessages() + '.');
                return true;
            }
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                  + "You can not teleport to other worlds because you are not a player.");
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length != 1 || !(sender instanceof Player)) {
            return Collections.emptyList();
        }
        List<World> worlds = sender.getServer().getWorlds();
        List<String> complete = new ArrayList<>();
        String search = args[0];
        for (World world : worlds) {
            String name = world.getName();
            if (name.startsWith(search)) {
                complete.add(name);
            }
        }
        return complete;
    }
}