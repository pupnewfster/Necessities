package gg.galaxygaming.necessities.Commands.Economy;

import gg.galaxygaming.necessities.Economy.Economy;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdBalance implements EconomyCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Economy eco = Necessities.getEconomy();
        Variables var = Necessities.getVar();
        if (args.length == 0 && sender instanceof Player) {
            Player player = (Player) sender;
            double balance = eco.getBalance(player.getUniqueId());
            if (balance == -1.0) {
                player.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "You do not seem to exist let me add you now.");
                eco.addPlayerIfNotExists(player.getUniqueId());
                return true;
            }
            player.sendMessage(var.getMessages() + "Balance: " + var.getMoney() + Economy.format(balance));
        }
        if (args.length != 0) {
            String playersName;
            UUID uuid = Utils.getID(args[0]);
            if (uuid == null) {
                uuid = Utils.getOfflineID(args[0]);
                if (uuid == null || !Bukkit.getOfflinePlayer(uuid).hasPlayedBefore()) {
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                          + "That player does not exist or has not joined the server. If the player is offline, please use the full and most recent name.");
                    return true;
                }
                playersName = Bukkit.getOfflinePlayer(uuid).getName();
            } else {
                playersName = Bukkit.getPlayer(uuid).getName();
            }
            double balance = eco.getBalance(uuid);
            if (balance == -1.0) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "That player is not in my records. If the player is offline, please use the full and most recent name.");
                return true;
            }
            sender.sendMessage(
                  var.getObj() + Utils.ownerShip(playersName) + var.getMessages() + " balance is: " + var.getMoney()
                        + Economy.format(balance));
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> complete = new ArrayList<>();
        String search = "";
        if (args.length > 0) {
            search = args[args.length - 1];
        }
        if (sender instanceof Player) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().startsWith(search) && ((Player) sender).canSee(p)) {
                    complete.add(p.getName());
                }
            }
        } else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().startsWith(search)) {
                    complete.add(p.getName());
                }
            }
        }
        return complete;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}