package gg.galaxygaming.necessities.Commands.Economy;

import gg.galaxygaming.necessities.Economy.Economy;
import gg.galaxygaming.necessities.Economy.Material;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class CmdSell implements EconomyCmd {
    @SuppressWarnings("deprecation")
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 2 || args.length == 0) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a the amount you want to sell and of what.");
                return true;
            }
            PlayerInventory inventory = player.getInventory();
            int amount = 0;
            short data = 0;
            Material mat;
            if (args.length == 2) {
                String temp = args[0].replaceAll(":", " ");
                String itemName = temp.split(" ")[0];
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
                if (!Utils.legalInt(args[1])) {
                    if (!args[1].equalsIgnoreCase("all")) {
                        sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a the amount you want to sell.");
                        return true;
                    }
                    if (mat != null)
                        amount = itemAmount(inventory, mat.getBukkitMaterial(), mat.isTool());
                } else
                    amount = Integer.parseInt(args[1]);
            } else {
                String handType = inventory.getItemInMainHand().getType().toString();
                if (handType.equals("NETHER_BRICK") || handType.equals("BRICK"))
                    handType += "_BLOCK";
                mat = Material.fromString(handType);
                if (mat != null) {
                    data = inventory.getItemInMainHand().getDurability();
                    if (!mat.isTool())
                        mat = Material.fromData(data != 0 ? mat.getParent() : mat, data);
                }
                if (!Utils.legalInt(args[0])) {
                    if (!args[0].equalsIgnoreCase("all")) {
                        sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a the amount you want to sell.");
                        return true;
                    }
                    if (mat != null)
                        amount = itemAmount(inventory, mat.getBukkitMaterial(), mat.isTool());
                } else
                    amount = Integer.parseInt(args[0]);
            }
            if (mat == null) {
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That item does not exist");
                return true;
            }
            if (mat.isTool() && mat.getData() != 0)
                data = mat.getData();
            double cost = Necessities.getPrices().getPrice("sell", mat.getName(), mat.isTool() ? 1 : amount);
            if (cost == -1.00)
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + mat.getFriendlyName(2) + " cannot be sold to the server.");
            else {
                MaterialData bukkitMaterial = mat.getBukkitMaterial();
                if (!mat.isTool() && inventory.containsAtLeast(bukkitMaterial.toItemStack(1), amount)) {
                    Necessities.getEconomy().addMoney(player.getUniqueId(), cost);
                    inventory.removeItem(bukkitMaterial.toItemStack(amount));
                    player.sendMessage(var.getMessages() + "You sold " + var.getObj() + Integer.toString(amount) + ' ' + mat.getFriendlyName(amount) + var.getMessages() + '.');
                    player.sendMessage(var.getMoney() + Economy.format(cost) + var.getMessages() + " was added to your account.");
                } else if (mat.isTool() && inventory.contains(new ItemStack(bukkitMaterial.getItemType(), 1, data))) {
                    cost = sell(inventory, amount, bukkitMaterial, player.getUniqueId(), cost);
                    if (cost != -1.00) {
                        player.sendMessage(var.getMessages() + "You sold " + var.getObj() + Integer.toString(amount) + ' ' + mat.getFriendlyName(amount) + var.getMessages() + '.');
                        player.sendMessage(var.getMoney() + Economy.format(cost) + var.getMessages() + " was added to your account.");
                    } else
                        player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have " + var.getObj() + Integer.toString(amount) + ' ' + mat.getFriendlyName(amount) + var.getMessages() + '.');
                } else
                    player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have " + var.getObj() + Integer.toString(amount) + ' ' + mat.getFriendlyName(amount) + var.getMessages() + '.');
            }
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be logged in to use this command");
        return true;
    }

    private double sell(PlayerInventory inv, int cAmount, MaterialData matType, UUID uuid, double baseCost) {
        double totalCost = 0.0;
        ArrayList<ItemStack> foundItems = new ArrayList<>();
        for (ItemStack s : inv.getContents()) {
            if (s == null)
                continue;
            if (cAmount > 0 && s.getType().equals(matType.getItemType()) && s.getEnchantments().size() == 0 && s.getType().getMaxDurability() != 0) {
                short maxDur = (short) (s.getType().getMaxDurability() + 1), dur = s.getDurability();
                cAmount--;
                double cost = s.getAmount() * baseCost * ((maxDur - 1.0 * dur) / maxDur);//why does it not work if not also divided by two?
                System.out.println(dur + " " + maxDur + " " + cost);
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

    @SuppressWarnings("deprecation")
    private int itemAmount(PlayerInventory inv, MaterialData matType, boolean isTool) {
        return Arrays.stream(inv.getContents()).filter(s -> !(s == null || s.getType() != matType.getItemType())).filter(s -> s.getEnchantments().size() == 0 && (isTool ||
                s.getDurability() == matType.getData())).mapToInt(ItemStack::getAmount).sum();
    }
}