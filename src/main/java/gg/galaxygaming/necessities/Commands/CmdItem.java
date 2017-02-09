package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Economy.Material;
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
            Material mat;
            if (Utils.legalInt(args[0]))
                mat = Material.fromID(Integer.parseInt(args[0]));
            else
                mat = Material.fromString(args[0]);
            if (mat == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That item does not exist.");
                return true;
            }
            if (args.length == 1) {
                p.getInventory().addItem(mat.getBukkitMaterial().toItemStack(64));
                p.sendMessage(var.getMessages() + "Giving " + var.getObj() + "64 " + mat.getFriendlyName(64) + var.getMessages() + ".");
                return true;
            }
            if (!Utils.legalInt(args[1])) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Illegal amount the amount must be numeric.");
                return true;
            }
            int amount = Integer.parseInt(args[1]);
            if (args.length == 2) {
                p.getInventory().addItem(mat.getBukkitMaterial().toItemStack(amount));
                p.sendMessage(var.getMessages() + "Giving " + var.getObj() + amount + " " + mat.getFriendlyName(amount) + var.getMessages() + ".");
                return true;
            }
            short data = 0;
            if (Utils.legalInt(args[2])) //Really a short
                data = Short.parseShort(args[2]);
            mat = Material.fromData(mat, data);
            p.getInventory().addItem(mat.getBukkitMaterial().toItemStack(amount));
            p.sendMessage(var.getMessages() + "Giving " + var.getObj() + amount + " " + mat.getFriendlyName(amount) + var.getMessages() + ".");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be logged in to use this command.");
        return true;
    }
}