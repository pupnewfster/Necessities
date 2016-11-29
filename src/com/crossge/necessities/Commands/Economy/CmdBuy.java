package com.crossge.necessities.Commands.Economy;

import com.crossge.necessities.Economy.BalChecks;
import com.crossge.necessities.Economy.Materials;
import com.crossge.necessities.Economy.Prices;
import com.crossge.necessities.Necessities;
import com.crossge.necessities.Utils;
import com.crossge.necessities.Variables;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;

public class CmdBuy implements EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 2 || args.length == 0) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a the amount you want to buy and of what.");
                return true;
            }
            PlayerInventory inventory = player.getInventory();
            BalChecks balc = Necessities.getInstance().getBalChecks();
            String balance = balc.bal(player.getUniqueId());
            double intBal = Double.parseDouble(balance);
            int amount;
            String itemName, temp;
            short data = 0;
            Materials mat = Necessities.getInstance().getMaterials();
            if (args.length == 2) {
                temp = args[0].replaceAll(":", " ");
                itemName = temp.split(" ")[0];
                if (!Utils.legalInt(args[1])) {
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid amount to buy.");
                    return true;
                }
                amount = Integer.parseInt(args[1]);
                try {
                    data = Short.parseShort(temp.split(" ")[1]);
                } catch (Exception ignored) {
                }
                if (Utils.legalInt(itemName))
                    itemName = mat.idToName(Integer.parseInt(itemName));
                else if (itemName.equalsIgnoreCase("hand")) {
                    itemName = inventory.getItemInMainHand().getType().name();
                    data = inventory.getItemInMainHand().getDurability();
                }
            } else {
                itemName = inventory.getItemInMainHand().getType().name();
                if (!Utils.legalInt(args[0])) {
                    sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid amount to buy.");
                    return true;
                }
                data = inventory.getItemInMainHand().getDurability();
                amount = Integer.parseInt(args[0]);
            }
            itemName = mat.findItem(itemName);
            if (itemName == null) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That item does not exist");
                return true;
            }
            itemName = itemName.toUpperCase();
            Prices pr = Necessities.getInstance().getPrices();
            double cost = pr.getCost("buy", itemName, amount);
            if (cost == -1.00) {
                itemName = Utils.capFirst(mat.getName(itemName));
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + mat.pluralize(itemName, 2) + " cannot be bought from the server.");
            } else {
                if (intBal < cost) {
                    player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You don't have enough money to buy that item.");
                    return true;
                }
                if (Material.matchMaterial(itemName) == null) {
                    player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + mat.pluralize(itemName, 2) + " is not a valid item as of yet.");
                    return true;
                }
                ItemStack itemstack = new ItemStack(Material.matchMaterial(mat.findItem(itemName)), amount, data);
                HashMap<Integer, ItemStack> noFit = inventory.addItem(itemstack);
                if (!noFit.isEmpty()) {
                    amount = amount - noFit.get(0).getAmount();
                    cost = pr.getCost("buy", itemName, amount);
                    player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have enough inventory space to buy that much of that item, buying the amount you have inventory space for.");
                }
                balc.removeMoney(player.getUniqueId(), cost);
                itemName = Utils.capFirst(mat.getName(itemName));
                player.sendMessage(var.getMessages() + "You bought " + var.getObj() + Integer.toString(amount) + " " + mat.pluralize(itemName, amount) + var.getMessages() + ".");
                player.sendMessage(var.getMoney() + "$" + Utils.addCommas(Utils.roundTwoDecimals(cost)) + var.getMessages() + " was removed from your account.");
            }
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be logged in to use this command");
        return true;
    }
}