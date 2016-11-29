package com.crossge.necessities.Commands;

import com.crossge.necessities.Necessities;
import com.crossge.necessities.Variables;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdWeather implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a world name and weather.");
            return true;
        }
        World dim = null;
        boolean thundering = false;
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length > 1) {
                dim = sender.getServer().getWorld(args[0]);
                if (args[1].equalsIgnoreCase("sun") || args[1].equalsIgnoreCase("clear") || args[1].equalsIgnoreCase("nice"))
                    thundering = false;
                if (args[1].equalsIgnoreCase("storm") || args[1].equalsIgnoreCase("stormy") || args[1].equalsIgnoreCase("bad") || args[1].equalsIgnoreCase("rain"))
                    thundering = true;
            } else {
                if (args[0].equalsIgnoreCase("sun") || args[0].equalsIgnoreCase("clear") || args[0].equalsIgnoreCase("nice"))
                    thundering = false;
                if (args[0].equalsIgnoreCase("storm") || args[0].equalsIgnoreCase("stormy") || args[0].equalsIgnoreCase("bad") || args[0].equalsIgnoreCase("rain"))
                    thundering = true;
            }
            if (dim == null)
                dim = p.getWorld();
        } else {
            if (args.length == 1) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a world name and weather.");
                return true;
            }
            dim = sender.getServer().getWorld(args[0]);
            if (args[1].equalsIgnoreCase("sun") || args[1].equalsIgnoreCase("clear") || args[1].equalsIgnoreCase("nice"))
                thundering = false;
            if (args[1].equalsIgnoreCase("storm") || args[1].equalsIgnoreCase("stormy") || args[1].equalsIgnoreCase("bad") || args[1].equalsIgnoreCase("rain"))
                thundering = true;
            if (dim == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid world.");
                return true;
            }
        }
        if (!thundering)
            dim.setWeatherDuration(1);
        else
            dim.setThundering(true);
        sender.sendMessage(var.getMessages() + "Set weather in " + var.getObj() + dim.getName() + var.getMessages() + " to " + (thundering ? "stormy" : "clear") + ".");
        return true;
    }
}