package com.crossge.necessities.Commands.Economy;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class CmdSell extends EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 2 || args.length == 0) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a the amount you want to sell and of what.");
                return true;
            }
            PlayerInventory inventory = player.getInventory();
            int amount = 0;
            String itemName;
            String temp;
            short data = 0;
            if (args.length == 2) {
                temp = args[0].replaceAll(":", " ");
                itemName = temp.split(" ")[0];
                try {
                    data = Short.parseShort(temp.split(" ")[1]);
                } catch (Exception e) {
                }
                if (form.isLegal(itemName))
                    itemName = mat.idToName(Integer.parseInt(itemName));
                else if (itemName.equalsIgnoreCase("hand")) {
                    itemName = inventory.getItemInMainHand().getType().name();
                    data = inventory.getItemInMainHand().getDurability();
                }
                if (!form.isLegal(args[1])) {
                    if (!args[1].equalsIgnoreCase("all")) {
                        sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a the amount you want to sell.");
                        return true;
                    }
                    amount = itemAmount(inventory, Material.matchMaterial(mat.findItem(itemName)), data, mat.isTool(new ItemStack(Material.matchMaterial(itemName), amount, data)));
                } else
                    amount = Integer.parseInt(args[1]);
            } else {
                itemName = inventory.getItemInMainHand().getType().name();
                data = inventory.getItemInMainHand().getDurability();
                if (!form.isLegal(args[0])) {
                    if (!args[0].equalsIgnoreCase("all")) {
                        sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a the amount you want to sell.");
                        return true;
                    }
                    amount = itemAmount(inventory, Material.matchMaterial(mat.findItem(itemName)), data, mat.isTool(new ItemStack(Material.matchMaterial(itemName), amount, data)));
                } else
                    amount = Integer.parseInt(args[0]);
            }
            itemName = mat.findItem(itemName);
            if (itemName == null) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That item does not exist");
                return true;
            }
            double cost = pr.getCost("sell", itemName, amount);
            if (cost == -1.00) {
                itemName = form.capFirst(mat.getName(itemName));
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + mat.pluralize(itemName, 2) + " cannot be sold to the server.");
            } else {
                if (Material.matchMaterial(itemName) == null) {
                    player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + mat.pluralize(itemName, 2) + " is not a valid item as of yet.");
                    return true;
                }
                ItemStack itemstack = new ItemStack(Material.matchMaterial(itemName), amount, data);
                if (inventory.containsAtLeast(new ItemStack(Material.matchMaterial(itemName), 1, data), amount)) {
                    if (mat.isTool(itemstack) && itemstack.getType().getMaxDurability() != 0)
                        cost = cost * (1.0 * itemstack.getType().getMaxDurability() - itemstack.getDurability()) / itemstack.getType().getMaxDurability();
                    balc.addMoney(player.getUniqueId(), cost);
                    inventory.removeItem(itemstack);
                    itemName = form.capFirst(mat.getName(itemName));
                    player.sendMessage(var.getMessages() + "You sold " + var.getObj() + Integer.toString(amount) + " " + itemName + var.getMessages() + ".");
                    player.sendMessage(var.getMoney() + "$" + form.addCommas(form.roundTwoDecimals(cost)) + var.getMessages() + " was added to your account.");
                } else {
                    if (inventory.contains(Material.matchMaterial(itemName), amount) && mat.isTool(itemstack)) {
                        cost = sell(inventory, amount, Material.matchMaterial(itemName), player.getUniqueId(), cost);
                        if (cost != -1.00) {
                            itemName = mat.pluralize(mat.getName(itemName), amount);
                            player.sendMessage(var.getMessages() + "You sold " + var.getObj() + Integer.toString(amount) + " " + itemName + var.getMessages() + ".");
                            player.sendMessage(var.getMoney() + "$" + form.addCommas(form.roundTwoDecimals(cost)) + var.getMessages() + " was added to your account.");
                        } else {
                            itemName = mat.pluralize(mat.getName(itemName), amount);
                            player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have " + var.getObj() + Integer.toString(amount) + " " + itemName + var.getMessages() + ".");
                        }
                    } else {
                        itemName = mat.pluralize(mat.getName(itemName), amount);
                        player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have " + var.getObj() + Integer.toString(amount) + " " + itemName + var.getMessages() + ".");
                    }
                }
            }
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be logged in to use this command");
        return true;
    }

    private double sell(PlayerInventory inv, int cAmount, Material matType, UUID uuid, double baseCost) {
        double totalCost = 0.0;
        for (ItemStack s : inv.getContents()) {
            if (s == null)
                continue;
            if (cAmount > 0 && s.getType().equals(matType) && s.getEnchantments().size() == 0 && s.getType().getMaxDurability() != 0) {
                short maxDur = s.getType().getMaxDurability(), dur = s.getDurability();
                cAmount = cAmount - 1;
                double cost = baseCost * ((1.0 * maxDur - dur) / (maxDur * 2.0));//why does it not work if not also divided by two?
                inv.removeItem(new ItemStack(matType, 1, s.getDurability()));
                balc.addMoney(uuid, cost);
                totalCost += cost;
            }
            if (cAmount == 0)
                return totalCost;
        }
        return -1.00;
    }

    private int itemAmount(PlayerInventory inv, Material matType, short data, boolean isTool) {
        int amount = 0;
        for (ItemStack s : inv.getContents()) {
            if (s == null || s.getType() != matType)
                continue;
            if (s.getEnchantments().size() == 0 && (isTool || s.getDurability() == data))
                amount += s.getAmount();
        }
        return amount;
    }
}