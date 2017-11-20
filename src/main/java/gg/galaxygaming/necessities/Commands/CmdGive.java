package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Economy.Material;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdGive implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (args.length < 2) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You need to enter an item and a player to give that item to.");
            return true;
        }
        UUID uuid = Utils.getID(args[0]);
        if (uuid == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
            return true;
        }
        Player t = Bukkit.getPlayer(uuid);
        Material mat = Utils.legalInt(args[1]) ? Material.fromID(Integer.parseInt(args[1])) : Material.fromString(args[1]);
        if (mat == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That item does not exist.");
            return true;
        }
        if (args.length == 2) {
            t.getInventory().addItem(mat.getBukkitMaterial().toItemStack(64));
            sender.sendMessage(var.getMessages() + "Giving " + var.getObj() + "64 " + mat.getFriendlyName(64) + " to " + t.getDisplayName() + var.getMessages() + '.');
            return true;
        }
        if (!Utils.legalInt(args[2])) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Illegal amount the amount must be numeric.");
            return true;
        }
        int amount = Integer.parseInt(args[2]);
        short data = 0;
        if (args.length > 3 && Utils.legalInt(args[3]))
            data = Short.parseShort(args[3]);
        mat = mat.getChild(data);
        t.getInventory().addItem(mat.getBukkitMaterial().toItemStack(amount));
        sender.sendMessage(var.getMessages() + "Giving " + var.getObj() + amount + ' ' + mat.getFriendlyName(amount) + " to " + t.getDisplayName() + var.getMessages() + '.');
        return true;
    }
}