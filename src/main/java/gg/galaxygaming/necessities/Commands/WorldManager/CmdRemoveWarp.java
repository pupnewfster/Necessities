package gg.galaxygaming.necessities.Commands.WorldManager;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Variables;
import gg.galaxygaming.necessities.WorldManager.WarpManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdRemoveWarp implements WorldCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length == 0) {
            sender.sendMessage(
                  var.getEr() + "Error: " + var.getErMsg() + "You must enter a name for the warp you wish to remove.");
            return true;
        }
        WarpManager warps = Necessities.getWarps();
        if (!warps.isWarp(args[0])) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "There is no warp by that name.");
            return true;
        }
        warps.remove(args[0]);
        sender.sendMessage(
              var.getMessages() + "Removed the warp named " + var.getObj() + args[0] + var.getMessages() + '.');
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length != 1 || !(sender instanceof Player)) {
            return Collections.emptyList();
        }
        List<String> complete = new ArrayList<>();
        String search = args[0];
        WarpManager warps = Necessities.getWarps();
        for (String warp : warps.getAllWarps()) {
            if (warp.startsWith(search)) {
                complete.add(warp);
            }
        }
        return complete;
    }
}