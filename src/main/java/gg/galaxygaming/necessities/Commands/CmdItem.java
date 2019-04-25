package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Material.Material;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdItem implements Cmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You need to enter an item.");
                return true;
            }
            Material mat = Material.fromString(args[0]);
            if (mat == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That item does not exist.");
                return true;
            }
            if (args.length == 1) {
                p.getInventory().addItem(mat.toItemStack(64));
                p.sendMessage(
                      var.getMessages() + "Giving " + var.getObj() + "64 " + mat.getFriendlyName(64) + var.getMessages()
                            + '.');
                return true;
            }
            if (!Utils.legalInt(args[1])) {
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "Illegal amount the amount must be numeric.");
                return true;
            }
            int amount = Integer.parseInt(args[1]);
            if (args.length == 2) {
                p.getInventory().addItem(mat.toItemStack(amount));
                p.sendMessage(
                      var.getMessages() + "Giving " + var.getObj() + amount + ' ' + mat.getFriendlyName(amount) + var
                            .getMessages() + '.');
                return true;
            }
            p.getInventory().addItem(mat.toItemStack(amount));
            p.sendMessage(
                  var.getMessages() + "Giving " + var.getObj() + amount + ' ' + mat.getFriendlyName(amount) + var
                        .getMessages() + '.');
        } else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be logged in to use this command.");
        }
        return true;
    }
}