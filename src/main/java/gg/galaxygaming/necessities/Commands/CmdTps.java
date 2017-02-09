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
        Variables var = Necessities.getInstance().getVar();
        Runtime runtime = Runtime.getRuntime();
        sender.sendMessage(var.getMessages() + "Max Memory: " + var.getObj() + runtime.maxMemory() / mb + var.getMessages() + " mb.");
        sender.sendMessage(var.getMessages() + "Total Memory: " + var.getObj() + runtime.totalMemory() / mb + var.getMessages() + " mb.");
        sender.sendMessage(var.getMessages() + "Free Memory: " + var.getObj() + runtime.freeMemory() / mb + var.getMessages() + " mb.");
        sender.sendMessage(var.getMessages() + "Used Memory: " + var.getObj() + (runtime.totalMemory() - runtime.freeMemory()) / mb + var.getMessages() + " mb.");
        for (World w : Bukkit.getWorlds()) {
            sender.sendMessage(var.getMessages() + "World: " + var.getObj() + w.getName());
            sender.sendMessage(var.getMessages() + "    Entities Loaded: " + var.getObj() + w.getEntities().size());
        }
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}