package com.crossge.necessities.Commands.Economy;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdBalance extends EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (args.length == 0 && sender instanceof Player) {
            Player player = (Player) sender;
            String balance = balc.bal(player.getUniqueId());
            if (balance == null) {//cannot happen but whatevs
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not seem to exist let me add you now.");
                balc.addPlayerToList(player.getUniqueId());
                return true;
            }
            player.sendMessage(var.getMessages() + "Balance: " + var.getMoney() + "$" + form.addCommas(balance));
        }
        if (args.length != 0) {
            String playersname;
            UUID uuid = get.getID(args[0]);
            if (uuid == null) {
                uuid = get.getOfflineID(args[0]);
                if (uuid == null) {
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That player has not joined the server. If the player is offline, please use the full and most recent name.");
                    return true;
                }
                playersname = Bukkit.getOfflinePlayer(uuid).getName();
            } else
                playersname = sender.getServer().getPlayer(uuid).getName();
            String balance = balc.bal(uuid);
            if (balance == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That player is not in my records. If the player is offline, please use the full and most recent name.");
                return true;
            }
            sender.sendMessage(var.getObj() + ownerShip(playersname) + var.getMessages() + " balance is: " + var.getMoney() + "$" + form.addCommas(balance));
        }
        return true;
    }

    private String ownerShip(String name) {
        if (name.endsWith("s") || name.endsWith("S"))
            return name + "'";
        return name + "'s";
    }
}