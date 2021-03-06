package gg.galaxygaming.necessities.Commands.WorldManager;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Variables;
import gg.galaxygaming.necessities.WorldManager.WorldManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdUnloadWorld implements WorldCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a world name to unload.");
            return true;
        }
        WorldManager wm = Necessities.getWM();
        if (wm.worldUnloaded(args[0])) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That world is not loaded.");
            return true;
        }
        if (!wm.worldExists(args[0])) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That world does not exist.");
            return true;
        }
        String name = Bukkit.getWorld(args[0]).getName();
        wm.unloadWorld(args[0]);
        sender.sendMessage(var.getMessages() + "Unloaded " + var.getObj() + name + var.getMessages() + '.');
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