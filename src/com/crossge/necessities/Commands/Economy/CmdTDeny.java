package com.crossge.necessities.Commands.Economy;

import com.crossge.necessities.Economy.Trade;
import com.crossge.necessities.Necessities;
import com.crossge.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdTDeny implements EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            if (args.length < 1) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter the player you want to deny the trade of.");
                return true;
            }
            Player player = (Player) sender;
            UUID uuid = Necessities.getInstance().getUUID().getID(args[0]);
            if (uuid == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
                return true;
            }
            Player target = Bukkit.getPlayer(uuid);
            String pname = player.getName(), offerPname = target.getName();
            Trade tr = Necessities.getInstance().getTrades();
            if (tr.hasTrade(pname, offerPname)) {
                player.sendMessage(var.getMessages() + "You have denied the trade from " + var.getObj() + offerPname);
                target.sendMessage(var.getMessages() + "Your trade to " + var.getObj() + pname + var.getMessages() + " has been denied");
                tr.denyTrade(pname, offerPname);
            } else
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have a trade offer from " + offerPname);
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You don't have an inventory. Please log in to trade.");
        return true;
    }
}