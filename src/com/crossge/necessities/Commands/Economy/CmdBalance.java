package com.crossge.necessities.Commands.Economy;

import com.crossge.necessities.Economy.BalChecks;
import com.crossge.necessities.GetUUID;
import com.crossge.necessities.Necessities;
import com.crossge.necessities.Utils;
import com.crossge.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CmdBalance implements EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        BalChecks balc = Necessities.getInstance().getBalChecks();
        Variables var = Necessities.getInstance().getVar();
        if (args.length == 0 && sender instanceof Player) {
            Player player = (Player) sender;
            String balance = balc.bal(player.getUniqueId());
            if (balance == null) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not seem to exist let me add you now.");
                balc.addPlayerToList(player.getUniqueId());
                return true;
            }
            player.sendMessage(var.getMessages() + "Balance: " + var.getMoney() + "$" + Utils.addCommas(balance));
        }
        if (args.length != 0) {
            String playersName;
            GetUUID get = Necessities.getInstance().getUUID();
            UUID uuid = get.getID(args[0]);
            if (uuid == null) {
                uuid = get.getOfflineID(args[0]);
                if (uuid == null) {
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That player has not joined the server. If the player is offline, please use the full and most recent name.");
                    return true;
                }
                playersName = Bukkit.getOfflinePlayer(uuid).getName();
            } else
                playersName = Bukkit.getPlayer(uuid).getName();
            String balance = balc.bal(uuid);
            if (balance == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That player is not in my records. If the player is offline, please use the full and most recent name.");
                return true;
            }
            sender.sendMessage(var.getObj() + Utils.ownerShip(playersName) + var.getMessages() + " balance is: " + var.getMoney() + "$" + Utils.addCommas(balance));
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> complete = new ArrayList<>();
        String search = "";
        if (args.length > 0)
            search = args[args.length - 1];
        if (sender instanceof Player) {
            for (Player p : Bukkit.getOnlinePlayers())
                if (p.getName().startsWith(search) && ((Player) sender).canSee(p))
                    complete.add(p.getName());
        } else
            for (Player p : Bukkit.getOnlinePlayers())
                if (p.getName().startsWith(search))
                    complete.add(p.getName());
        return complete;
    }
}