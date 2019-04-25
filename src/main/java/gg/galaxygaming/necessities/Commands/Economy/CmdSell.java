package gg.galaxygaming.necessities.Commands.Economy;

import gg.galaxygaming.necessities.Economy.Economy;
import gg.galaxygaming.necessities.Material.Material;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class CmdSell implements EconomyCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 2 || args.length == 0) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "You must enter a the amount you want to sell and of what.");
                return true;
            }
            PlayerInventory inventory = player.getInventory();
            int amount = 0;
            Material mat;
            if (args.length == 2) {
                String itemName = args[0];
                if (itemName.equalsIgnoreCase("hand")) {
                    mat = Material.fromBukkit(inventory.getItemInMainHand().getType());
                } else {
                    mat = Material.fromString(itemName);
                }
                if (!Utils.legalInt(args[1])) {
                    if (!args[1].equalsIgnoreCase("all")) {
                        sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                              + "You must enter a the amount you want to sell.");
                        return true;
                    }
                    if (mat != null) {
                        amount = itemAmount(inventory, mat);
                    }
                } else {
                    amount = Integer.parseInt(args[1]);
                }
            } else {
                mat = Material.fromBukkit(inventory.getItemInMainHand().getType());
                if (!Utils.legalInt(args[0])) {
                    if (!args[0].equalsIgnoreCase("all")) {
                        sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                              + "You must enter a the amount you want to sell.");
                        return true;
                    }
                    if (mat != null) {
                        amount = itemAmount(inventory, mat);
                    }
                } else {
                    amount = Integer.parseInt(args[0]);
                }
            }
            if (mat == null) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That item does not exist");
                return true;
            }
            boolean isTool = new ItemStack(mat.getBukkitMaterial(), 1).getItemMeta() instanceof Damageable;
            double cost = Necessities.getPrices().getPrice("sell", mat.getName(), isTool ? 1 : amount);
            if (cost == -1.00) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + mat.getFriendlyName(2)
                      + " cannot be sold to the server.");
            } else {
                if (!isTool && inventory.containsAtLeast(mat.toItemStack(1), amount)) {
                    Necessities.getEconomy().addMoney(player.getUniqueId(), cost);
                    inventory.removeItem(mat.toItemStack(amount));
                    player.sendMessage(
                          var.getMessages() + "You sold " + var.getObj() + Integer.toString(amount) + ' ' + mat
                                .getFriendlyName(amount) + var.getMessages() + '.');
                    player.sendMessage(
                          var.getMoney() + Economy.format(cost) + var.getMessages() + " was added to your account.");
                } else if (isTool && inventory.contains(mat.toItemStack(1))) {
                    cost = sell(inventory, amount, mat, player.getUniqueId(), cost);
                    if (cost != -1.00) {
                        player.sendMessage(
                              var.getMessages() + "You sold " + var.getObj() + Integer.toString(amount) + ' ' + mat
                                    .getFriendlyName(amount) + var.getMessages() + '.');
                        player.sendMessage(var.getMoney() + Economy.format(cost) + var.getMessages()
                              + " was added to your account.");
                    } else {
                        player.sendMessage(
                              var.getEr() + "Error: " + var.getErMsg() + "You do not have " + var.getObj() + Integer
                                    .toString(amount) + ' ' + mat.getFriendlyName(amount) + var.getMessages() + '.');
                    }
                } else {
                    player.sendMessage(
                          var.getEr() + "Error: " + var.getErMsg() + "You do not have " + var.getObj() + Integer
                                .toString(amount) + ' ' + mat.getFriendlyName(amount) + var.getMessages() + '.');
                }
            }
        } else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be logged in to use this command");
        }
        return true;
    }

    private double sell(PlayerInventory inv, int cAmount, Material type, UUID uuid, double baseCost) {
        double totalCost = 0.0;
        ArrayList<ItemStack> foundItems = new ArrayList<>();
        for (ItemStack s : inv.getContents()) {
            if (s == null) {
                continue;
            }
            ItemMeta meta = s.getItemMeta();
            if (cAmount > 0 && s.getType().equals(type.getBukkitMaterial()) && s.getEnchantments().size() == 0
                  && meta instanceof Damageable) {
                int maxDur = s.getType().getMaxDurability() + 1, dur = maxDur - ((Damageable) meta).getDamage();
                cAmount--;
                double cost = s.getAmount() * baseCost * ((maxDur - 1.0 * dur)
                      / maxDur);//why does it not work if not also divided by two?
                ItemStack toRemove = s.clone();
                toRemove.setAmount(1);
                foundItems.add(toRemove);
                totalCost += cost;
            }
            if (cAmount == 0) {
                foundItems.forEach(inv::remove);
                Necessities.getEconomy().addMoney(uuid, totalCost);
                return totalCost;
            }
        }
        return -1.00;
    }

    private int itemAmount(PlayerInventory inv, Material type) {
        return Arrays.stream(inv.getContents()).filter(s -> !(s == null || s.getType() != type.getBukkitMaterial()))
              .filter(s -> s.getEnchantments().size() == 0).mapToInt(ItemStack::getAmount).sum();
    }
}