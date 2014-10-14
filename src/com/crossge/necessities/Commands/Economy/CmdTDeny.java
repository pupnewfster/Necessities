package com.crossge.necessities.Commands.Economy;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdTDeny extends EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (args.length != 1) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter the player you want to deny the trade of.");
                return true;
            }
            Player player = (Player) sender;
            UUID uuid = get.getID(args[0]);
            if (uuid == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
                return true;
            }
            Player target = sender.getServer().getPlayer(uuid);
            String pname = player.getName();
            String offerpname = target.getName();
            if (tr.hasTrade(pname, offerpname)) {
                player.sendMessage(var.getMessages() + "You have denied the trade from " + var.getObj() + offerpname);
                target.sendMessage(var.getMessages() + "Your trade to " + var.getObj() + pname + var.getMessages() + " has been denied");
                tr.denyTrade(pname, offerpname);
            } else
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have a trade offer from " + offerpname);
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You don't have an inventory. Please log in to trade.");
        return true;
    }
}