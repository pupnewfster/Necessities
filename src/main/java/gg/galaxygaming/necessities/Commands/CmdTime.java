package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdTime implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a world name and time.");
            return true;
        }
        World dim = null;
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length > 2) {
                dim = sender.getServer().getWorld(args[0]);
                if (dim == null) {
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid world.");
                    return true;
                }
            }
            if (args.length == 1) {
                dim = p.getWorld();
                if (args[0].equalsIgnoreCase("day") || args[0].equalsIgnoreCase("noon"))
                    dim.setTime(6000);
                else if (args[0].equalsIgnoreCase("night") || args[0].equalsIgnoreCase("midnight"))
                    dim.setTime(18000);
                else if (args[0].equalsIgnoreCase("dawn"))
                    dim.setTime(23000);
                else if (args[0].equalsIgnoreCase("dusk"))
                    dim.setTime(13000);
                else if (Utils.legalLong(args[0]))
                    dim.setTime(Long.parseLong(args[0]));
            } else if (args.length == 2) {
                dim = p.getWorld();
                if (args[0].equalsIgnoreCase("add")) {
                    if (Utils.legalLong(args[1]))
                        dim.setTime(dim.getTime() + Long.parseLong(args[1]));
                } else if (args[0].equalsIgnoreCase("set")) {
                    if (args[1].equalsIgnoreCase("day") || args[1].equalsIgnoreCase("noon"))
                        dim.setTime(6000);
                    else if (args[1].equalsIgnoreCase("night") || args[1].equalsIgnoreCase("midnight"))
                        dim.setTime(18000);
                    else if (args[1].equalsIgnoreCase("dawn"))
                        dim.setTime(23000);
                    else if (args[1].equalsIgnoreCase("dusk"))
                        dim.setTime(13000);
                    else if (Utils.legalLong(args[1]))
                        dim.setTime(Long.parseLong(args[1]));
                } else if (args[0].equalsIgnoreCase("day") || args[1].equalsIgnoreCase("noon"))
                    dim.setTime(6000);
                else if (args[0].equalsIgnoreCase("night") || args[1].equalsIgnoreCase("midnight"))
                    dim.setTime(18000);
                else if (args[0].equalsIgnoreCase("dawn"))
                    dim.setTime(23000);
                else if (args[0].equalsIgnoreCase("dusk"))
                    dim.setTime(13000);
                else if (Utils.legalLong(args[0]))
                    dim.setTime(Long.parseLong(args[0]));
            } else {
                if (args[1].equalsIgnoreCase("add")) {
                    if (Utils.legalLong(args[2]))
                        dim.setTime(dim.getTime() + Long.parseLong(args[2]));
                } else if (args[1].equalsIgnoreCase("set")) {
                    if (args[2].equalsIgnoreCase("day") || args[2].equalsIgnoreCase("noon"))
                        dim.setTime(6000);
                    else if (args[2].equalsIgnoreCase("night") || args[2].equalsIgnoreCase("midnight"))
                        dim.setTime(18000);
                    else if (args[2].equalsIgnoreCase("dawn"))
                        dim.setTime(23000);
                    else if (args[2].equalsIgnoreCase("dusk"))
                        dim.setTime(13000);
                    else if (Utils.legalLong(args[2]))
                        dim.setTime(Long.parseLong(args[2]));
                } else if (args[1].equalsIgnoreCase("day") || args[1].equalsIgnoreCase("noon"))
                    dim.setTime(6000);
                else if (args[1].equalsIgnoreCase("night") || args[1].equalsIgnoreCase("midnight"))
                    dim.setTime(18000);
                else if (args[1].equalsIgnoreCase("dawn"))
                    dim.setTime(23000);
                else if (args[1].equalsIgnoreCase("dusk"))
                    dim.setTime(13000);
                else if (Utils.legalLong(args[1]))
                    dim.setTime(Long.parseLong(args[1]));
            }
        } else {
            if (args.length == 1) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a world name and time.");
                return true;
            }
            dim = sender.getServer().getWorld(args[0]);
            if (dim == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid world.");
                return true;
            }
            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("day") || args[1].equalsIgnoreCase("noon"))
                    dim.setTime(6000);
                else if (args[1].equalsIgnoreCase("night") || args[1].equalsIgnoreCase("midnight"))
                    dim.setTime(18000);
                else if (args[1].equalsIgnoreCase("dawn"))
                    dim.setTime(23000);
                else if (args[1].equalsIgnoreCase("dusk"))
                    dim.setTime(13000);
                else if (Utils.legalLong(args[1]))
                    dim.setTime(Long.parseLong(args[1]));
            } else {
                if (args[1].equalsIgnoreCase("add")) {
                    if (Utils.legalLong(args[2]))
                        dim.setTime(dim.getTime() + Long.parseLong(args[2]));
                } else if (args[1].equalsIgnoreCase("set")) {
                    if (args[2].equalsIgnoreCase("day") || args[2].equalsIgnoreCase("noon"))
                        dim.setTime(6000);
                    else if (args[2].equalsIgnoreCase("night") || args[2].equalsIgnoreCase("midnight"))
                        dim.setTime(18000);
                    else if (args[2].equalsIgnoreCase("dawn"))
                        dim.setTime(23000);
                    else if (args[2].equalsIgnoreCase("dusk"))
                        dim.setTime(13000);
                    else if (Utils.legalLong(args[2]))
                        dim.setTime(Long.parseLong(args[2]));
                } else if (args[1].equalsIgnoreCase("day") || args[1].equalsIgnoreCase("noon"))
                    dim.setTime(6000);
                else if (args[1].equalsIgnoreCase("night") || args[1].equalsIgnoreCase("midnight"))
                    dim.setTime(18000);
                else if (args[1].equalsIgnoreCase("dawn"))
                    dim.setTime(23000);
                else if (args[1].equalsIgnoreCase("dusk"))
                    dim.setTime(13000);
                else if (Utils.legalLong(args[1]))
                    dim.setTime(Long.parseLong(args[1]));
            }
        }
        sender.sendMessage(var.getMessages() + "The time was set to " + var.getObj() + dim.getTime() + var.getMessages() + " ticks in " + var.getObj() + dim.getName() + var.getMessages() + ".");
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}