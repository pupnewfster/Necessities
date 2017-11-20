package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Economy.Material;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.Arrays;

public class CmdClear implements Cmd {
    @SuppressWarnings("deprecation")
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                p.getInventory().clear();
                p.sendMessage(var.getMessages() + "Inventory cleared.");
                return true;
            }
            Material mat = Utils.legalInt(args[0]) ? Material.fromID(Integer.parseInt(args[0])) : Material.fromString(args[0]);
            if (mat == null) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That item does not exist.");
                return true;
            }
            MaterialData bukkitMaterial = mat.getBukkitMaterial();
            if (args.length == 1) {
                if (!p.getInventory().contains(bukkitMaterial.toItemStack(1))) {
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have any of that item.");
                    return true;
                }
                int amount = Arrays.stream(p.getInventory().getContents()).filter(i -> i != null && i.getType().equals(bukkitMaterial.getItemType()) && (Material.isTool(i.getType()) ||
                        i.getDurability() == bukkitMaterial.getData())).mapToInt(ItemStack::getAmount).sum();
                p.getInventory().remove(bukkitMaterial.toItemStack());
                p.sendMessage(var.getMessages() + "Removed " + var.getObj() + amount + ' ' + mat.getFriendlyName(amount) + var.getMessages() + " from your inventory.");
                return true;
            }
            if (!Utils.legalInt(args[1])) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Illegal amount the amount must be numeric.");
                return true;
            }
            int amount = Integer.parseInt(args[1]);
            if (args.length == 2) {
                if (!p.getInventory().contains(bukkitMaterial.toItemStack(amount))) {
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have " + var.getObj() + amount + var.getMessages() + " of that item.");
                    return true;
                }
                p.getInventory().remove(bukkitMaterial.toItemStack(amount));
                p.sendMessage(var.getMessages() + "Removed " + var.getObj() + amount + ' ' + mat.getFriendlyName(amount) + var.getMessages() + " from your inventory.");
                return true;
            }
            short data = 0;
            if (Utils.legalInt(args[2])) //Really is a short
                data = Short.parseShort(args[2]);
            mat = mat.getChild(data);
            if (!p.getInventory().contains(bukkitMaterial.toItemStack(amount))) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have " + var.getObj() + amount + var.getMessages() + " of that item.");
                return true;
            }
            p.getInventory().remove(bukkitMaterial.toItemStack(amount));
            p.sendMessage(var.getMessages() + "Removed " + var.getObj() + amount + ' ' + mat.getFriendlyName(amount) + var.getMessages() + " from your inventory.");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be logged in to use this command.");
        return true;
    }
}