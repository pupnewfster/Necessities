package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class CmdTps implements Cmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        sender.sendMessage(Utils.getTPS());
        int mb = 1024 * 1024;
        Variables var = Necessities.getVar();
        Runtime runtime = Runtime.getRuntime();
        sender.sendMessage(
              var.getMessages() + "Used Memory: " + var.getObj() + (runtime.totalMemory() - runtime.freeMemory()) / mb
                    + " mb / " + runtime.maxMemory() / mb + " mb" + var.getMessages() + '.');
        for (World w : Bukkit.getWorlds()) {
            sender.sendMessage(
                  var.getMessages() + "World: " + var.getObj() + w.getName() + var.getMessages() + ", Entities Loaded: "
                        + var.getObj() + w.getEntities().size() + var.getMessages() +
                        ", Chunks Loaded: " + var.getObj() + w.getLoadedChunks().length);
        }
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}