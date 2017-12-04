package gg.galaxygaming.necessities.Commands.Economy;

import gg.galaxygaming.necessities.Economy.Economy;
import gg.galaxygaming.necessities.Economy.Material;
import gg.galaxygaming.necessities.Economy.Prices;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;

public class CmdBuy implements EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 2 || args.length == 0) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a the amount you want to buy and of what.");
                return true;
            }
            PlayerInventory inventory = player.getInventory();
            Economy eco = Necessities.getEconomy();
            double intBal = eco.getBalance(player.getUniqueId());
            int amount;
            Material mat;
            if (args.length == 2) {
                String temp = args[0].replaceAll(":", " ");
                String itemName = temp.split(" ")[0];
                if (!Utils.legalInt(args[1])) {
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid amount to buy.");
                    return true;
                }
                amount = Integer.parseInt(args[1]);
                short data = 0;
                try {
                    data = Short.parseShort(temp.split(" ")[1]);
                } catch (Exception ignored) {
                }
                if (Utils.legalInt(itemName))
                    mat = Material.fromData(Integer.parseInt(itemName), data);
                else if (itemName.equalsIgnoreCase("hand")) {
                    mat = Material.fromString(inventory.getItemInMainHand().getType().toString());
                    if (mat != null && !mat.isTool()) {
                        data = inventory.getItemInMainHand().getDurability();
                        mat = Material.fromData(data != 0 ? mat.getParent() : mat, data);
                    }
                } else
                    mat = Material.fromData(itemName, data);
            } else {
                String handType = inventory.getItemInMainHand().getType().toString();
                if (handType.equals("NETHER_BRICK") || handType.equals("BRICK"))
                    handType += "_BLOCK";
                mat = Material.fromString(handType);
                if (mat != null && !mat.isTool()) {
                    short data = inventory.getItemInMainHand().getDurability();
                    mat = Material.fromData(data != 0 ? mat.getParent() : mat, data);
                }
                if (!Utils.legalInt(args[0])) {
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid amount to buy.");
                    return true;
                }
                amount = Integer.parseInt(args[0]);
            }
            if (mat == null) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That item does not exist");
                return true;
            }
            Prices pr = Necessities.getPrices();
            double cost = pr.getPrice("buy", mat.getName(), amount);
            if (cost == -1.00)
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + mat.getFriendlyName(2) + " cannot be bought from the server.");
            else {
                if (intBal < cost) {
                    player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You don't have enough money to buy that item.");
                    return true;
                }
                HashMap<Integer, ItemStack> noFit = inventory.addItem(mat.getBukkitMaterial().toItemStack(amount));
                if (!noFit.isEmpty()) {
                    amount = amount - noFit.get(0).getAmount();
                    cost = pr.getPrice("buy", mat.getName(), amount);
                    player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have enough inventory space to buy that much of that item, buying the amount you have inventory space for.");
                }
                eco.removeMoney(player.getUniqueId(), cost);
                player.sendMessage(var.getMessages() + "You bought " + var.getObj() + Integer.toString(amount) + ' ' + mat.getFriendlyName(amount) + var.getMessages() + '.');
                player.sendMessage(var.getMoney() + Economy.format(cost) + var.getMessages() + " was removed from your account.");
            }
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be logged in to use this command");
        return true;
    }
}