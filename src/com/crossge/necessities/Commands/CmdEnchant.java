package com.crossge.necessities.Commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CmdEnchant extends Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0 || args.length > 2) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you enter an enchantment and a level to enchant it with.");
                return true;
            }
            Player player = (Player) sender;
            if (player.getInventory().getItemInHand() == null || player.getInventory().getItemInHand().getType().equals(Material.AIR)) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You may not enchant air.");
                return true;
            }
            if (args.length == 1) {
                Enchantment ench = Enchantment.getByName(enchantFinder(args[0]));
                if (ench == null) {
                    player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Enchantment not found.");
                    return true;
                }
                int level = ench.getMaxLevel();
                if (ench.canEnchantItem(player.getInventory().getItemInHand())) {
                    player.getInventory().getItemInHand().addEnchantment(ench, level);
                    player.sendMessage(var.getMessages() + "Added the enchantment " + var.getObj() + trueName(ench.getName()) + var.getMessages() +
                            " at level " + var.getObj() + Integer.toString(level) + var.getMessages() + ".");
                    return true;
                }
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "This item can not support given enchantment.");
                return true;
            }
            if (args.length == 2) {
                Enchantment ench = null;
                if (!args[0].equalsIgnoreCase("all")) {
                    ench = Enchantment.getByName(enchantFinder(args[0]));
                    if (ench == null) {
                        player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Enchantment does not exist.");
                        return true;
                    }
                }
                int level = -1;
                if (!args[1].equalsIgnoreCase("max")) {
                    try {
                        Integer.parseInt(args[1]);
                    } catch (Exception e) {
                        sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid enchantment level.");
                        return true;
                    }
                    level = Integer.parseInt(args[1]);
                }
                if (!args[0].equalsIgnoreCase("all") && ench != null && level > ench.getMaxLevel() && !player.hasPermission("Necessities.unsafeEnchant"))
                    level = ench.getMaxLevel();
                if (player.hasPermission("Necessities.unsafeEnchant") && level > 127)
                    level = 127;
                else if (!player.hasPermission("Necessities.unsafeEnchant") && ench != null && level > ench.getMaxLevel())
                    level = ench.getMaxLevel();
                if (args[0].equalsIgnoreCase("all")) {
                    if (level == -1) {
                        player.sendMessage(var.getMessages() + "Added all enchantments at max level.");
                        enchantAll(level, player, player.hasPermission("Necessities.unsafeEnchant"), true);
                    } else {
                        enchantAll(level, player, player.hasPermission("Necessities.unsafeEnchant"), false);
                        player.sendMessage(var.getMessages() + "Added all enchantments at level " + var.getObj() + Integer.toString(level) + var.getMessages() + ".");
                    }
                    return true;
                } else if (ench != null && ench.canEnchantItem(player.getInventory().getItemInHand()) || player.hasPermission("Necessities.unsafeEnchant")) {
                    if (level == -1) {
                        player.getInventory().getItemInHand().addUnsafeEnchantment(ench, ench.getMaxLevel());
                        player.sendMessage(var.getMessages() + "Added the enchantment " + var.getObj() + trueName(ench.getName()) + var.getMessages() +
                                " at max level.");
                    } else {
                        if (level == 0) {
                            player.getInventory().getItemInHand().removeEnchantment(ench);
                            player.sendMessage(var.getMessages() + "Removed enchantment " + var.getObj() + trueName(ench.getName()) + var.getMessages() + ".");
                        } else {
                            player.getInventory().getItemInHand().addUnsafeEnchantment(ench, level);
                            player.sendMessage(var.getMessages() + "Added the enchantment " + var.getObj() + trueName(ench.getName()) + var.getMessages() +
                                    " at level " + var.getObj() + Integer.toString(level) + var.getMessages() + ".");
                        }
                    }
                    return true;
                }
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "This item can not support given enchantment.");
            }
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are not a player so you do not have an items to enchant.");
        return true;
    }

    private void enchantAll(int level, Player player, boolean overide, boolean max) {
        for (Enchantment e : Enchantment.values())
            if (e.canEnchantItem(player.getInventory().getItemInHand())) {
                if (overide && !max)
                    player.getInventory().getItemInHand().addUnsafeEnchantment(e, level);
                else
                    player.getInventory().getItemInHand().addEnchantment(e, e.getMaxLevel());
            }
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> complete = new ArrayList<>();
        String search = "";
        if (args.length > 0)
            search = args[args.length - 1];
        search = search.toUpperCase();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 1) {
                if ("ALL".startsWith(search))
                    complete.add("ALL");
                for (Enchantment e : Enchantment.values())
                    if (e.canEnchantItem(p.getInventory().getItemInHand()) && e.getName().startsWith(search))
                        complete.add(e.getName());
            } else {
                if ("MAX".startsWith(search))
                    complete.add("MAX");
                for (Enchantment e : Enchantment.values())
                    if (e.canEnchantItem(p.getInventory().getItemInHand()) && e.equals(Enchantment.getByName(enchantFinder(args[0]))) &&
                            Integer.toString(e.getMaxLevel()).startsWith(search))
                        for (int i = 0; i < e.getMaxLevel(); i++)
                            complete.add(Integer.toString(i + 1));
            }
        }
        return complete;
    }

    private String enchantFinder(String enchant) {//TODO: Update with 1.8 enchantment
        enchant = enchant.toUpperCase();
        if (enchant.equals("POWER"))
            enchant = "ARROW_DAMAGE";
        else if (enchant.equals("FLAME"))
            enchant = "ARROW_FIRE";
        else if (enchant.equals("INFINITY"))
            enchant = "ARROW_INFINITY";
        else if (enchant.equals("PUNCH"))
            enchant = "ARROW_KNOCKBACK";
        else if (enchant.equals("SHARPNESS"))
            enchant = "DAMAGE_ALL";
        else if (enchant.equals("BANEOFARTHROPODS"))
            enchant = "DAMAGE_ARTHROPODS";
        else if (enchant.equals("BANE"))
            enchant = "DAMAGE_ARTHROPODS";
        else if (enchant.equals("SMITE"))
            enchant = "DAMAGE_UNDEAD";
        else if (enchant.equals("EFFICIENCY"))
            enchant = "DIG_SPEED";
        else if (enchant.equals("UNBREAKING"))
            enchant = "DURABILITY";
        else if (enchant.equals("FIREASPECT"))
            enchant = "FIRE_ASPECT";
        else if (enchant.equals("FORTUNE"))
            enchant = "LOOT_BONUS_BLOCKS";
        else if (enchant.equals("LOOTING"))
            enchant = "LOOT_BONUS_MOBS";
        else if (enchant.equals("RESPIRATION"))
            enchant = "OXYGEN";
        else if (enchant.equals("PROTECTION"))
            enchant = "PROTECTION_ENVIRONMENTAL";
        else if (enchant.equals("BLASTPROTECTION"))
            enchant = "PROTECTION_EXPLOSIONS";
        else if (enchant.equals("FEATHERFALLING"))
            enchant = "PROTECTION_FALL";
        else if (enchant.equals("FIREPROTECTION"))
            enchant = "PROTECTION_FIRE";
        else if (enchant.equals("PROJECTILEPROTECTION"))
            enchant = "PROTECTION_PROJECTILE";
        else if (enchant.equals("SILKTOUCH"))
            enchant = "SILK_TOUCH";
        else if (enchant.equals("AQUAINFINITY"))
            enchant = "WATER_WORKER";
        return enchant;
    }

    private String trueName(String enchant) {
        if (enchant.equals("ARROW_DAMAGE"))
            enchant = "power";
        else if (enchant.equals("ARROW_FIRE"))
            enchant = "flame";
        else if (enchant.equals("ARROW_INFINITY"))
            enchant = "infinity";
        else if (enchant.equals("ARROW_KNOCKBACK"))
            enchant = "punch";
        else if (enchant.equals("DAMAGE_ALL"))
            enchant = "sharpness";
        else if (enchant.equals("DAMAGE_ARTHROPODS"))
            enchant = "bane of arthropods";
        else if (enchant.equals("DAMAGE_UNDEAD"))
            enchant = "smite";
        else if (enchant.equals("DIG_SPEED"))
            enchant = "efficiency";
        else if (enchant.equals("DURABILITY"))
            enchant = "unbreaking";
        else if (enchant.equals("FIRE_ASPECT"))
            enchant = "fire aspect";
        else if (enchant.equals("LOOT_BONUS_BLOCKS"))
            enchant = "fortune";
        else if (enchant.equals("LOOT_BONUS_MOBS"))
            enchant = "looting";
        else if (enchant.equals("OXYGEN"))
            enchant = "respiration";
        else if (enchant.equals("PROTECTION_ENVIRONMENTAL"))
            enchant = "protection";
        else if (enchant.equals("PROTECTION_EXPLOSIONS"))
            enchant = "blast protection";
        else if (enchant.equals("PROTECTION_FALL"))
            enchant = "feather falling";
        else if (enchant.equals("PROTECTION_FIRE"))
            enchant = "fire protection";
        else if (enchant.equals("PROTECTION_PROJECTILE"))
            enchant = "projectile protection";
        else if (enchant.equals("SILK_TOUCH"))
            enchant = "silk touch";
        else if (enchant.equals("WATER_WORKER"))
            enchant = "aqua infinity";
        return enchant.toLowerCase();
    }
}