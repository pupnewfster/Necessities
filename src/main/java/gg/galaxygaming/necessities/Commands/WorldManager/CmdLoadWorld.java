package gg.galaxygaming.necessities.Commands.WorldManager;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Variables;
import gg.galaxygaming.necessities.WorldManager.WorldManager;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class CmdLoadWorld implements WorldCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a world name to load.");
            return true;
        }
        WorldManager wm = Necessities.getWM();
        if (wm.worldExists(args[0])) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That world is all ready loaded.");
            return true;
        }
        if (!wm.worldUnloaded(args[0])) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That world does not exist.");
            return true;
        }
        wm.loadWorld(args[0]);
        World w = Bukkit.getWorld(args[0]);
        if (w == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "There was and error loading the world.");
        } else {
            sender.sendMessage(
                  var.getMessages() + "Loaded world " + var.getObj() + w.getName() + var.getMessages() + '.');
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        //TODO: TabComplete
        return Collections.emptyList();
    }
}