package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CmdEnchant implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            if (args.length == 0) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you enter an enchantment and a level to enchant it with.");
                return true;
            }
            Player player = (Player) sender;
            ItemStack hand = player.getInventory().getItemInMainHand();
            if (hand == null || hand.getType().equals(Material.AIR)) {
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
                if (ench.canEnchantItem(hand)) {
                    hand.addEnchantment(ench, level);
                    player.sendMessage(var.getMessages() + "Added the enchantment " + var.getObj() + trueName(ench.getName()) + var.getMessages() + " at level " + var.getObj() + Integer.toString(level) +
                            var.getMessages() + ".");
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
                    if (!Utils.legalInt(args[1])) {
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
                } else if (ench != null && (ench.canEnchantItem(hand) || player.hasPermission("Necessities.unsafeEnchant"))) {
                    if (level == -1) {
                        hand.addUnsafeEnchantment(ench, ench.getMaxLevel());
                        player.sendMessage(var.getMessages() + "Added the enchantment " + var.getObj() + trueName(ench.getName()) + var.getMessages() + " at max level.");
                    } else {
                        if (level == 0) {
                            hand.removeEnchantment(ench);
                            player.sendMessage(var.getMessages() + "Removed enchantment " + var.getObj() + trueName(ench.getName()) + var.getMessages() + ".");
                        } else {
                            hand.addUnsafeEnchantment(ench, level);
                            player.sendMessage(var.getMessages() + "Added the enchantment " + var.getObj() + trueName(ench.getName()) + var.getMessages() + " at level " + var.getObj() + Integer.toString(level) +
                                    var.getMessages() + ".");
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

    private void enchantAll(int level, Player player, boolean override, boolean max) {
        ItemStack hand = player.getInventory().getItemInMainHand();
        for (Enchantment e : Enchantment.values())
            if (e.canEnchantItem(hand)) {
                if (override && !max)
                    hand.addUnsafeEnchantment(e, level);
                else
                    hand.addEnchantment(e, e.getMaxLevel());
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
                    if (e.canEnchantItem(p.getInventory().getItemInMainHand()) && e.getName().startsWith(search))
                        complete.add(e.getName());
            } else {
                if ("MAX".startsWith(search))
                    complete.add("MAX");
                for (Enchantment e : Enchantment.values())
                    if (e.canEnchantItem(p.getInventory().getItemInMainHand()) && e.equals(Enchantment.getByName(enchantFinder(args[0]))) &&
                            Integer.toString(e.getMaxLevel()).startsWith(search))
                        for (int i = 0; i < e.getMaxLevel(); i++)
                            complete.add(Integer.toString(i + 1));
            }
        }
        return complete;
    }

    private String enchantFinder(String enchant) {
        enchant = enchant.toUpperCase();
        switch (enchant) {
            case "POWER":
                return "ARROW_DAMAGE";
            case "FLAME":
                return "ARROW_FIRE";
            case "INFINITY":
                return "ARROW_INFINITY";
            case "PUNCH":
                return "ARROW_KNOCKBACK";
            case "SHARPNESS":
                return "DAMAGE_ALL";
            case "BANEOFARTHROPODS":
                return "DAMAGE_ARTHROPODS";
            case "BANE":
                return "DAMAGE_ARTHROPODS";
            case "SMITE":
                return "DAMAGE_UNDEAD";
            case "EFFICIENCY":
                return "DIG_SPEED";
            case "UNBREAKING":
                return "DURABILITY";
            case "FIREASPECT":
                return "FIRE_ASPECT";
            case "FORTUNE":
                return "LOOT_BONUS_BLOCKS";
            case "LOOTING":
                return "LOOT_BONUS_MOBS";
            case "RESPIRATION":
                return "OXYGEN";
            case "PROTECTION":
                return "PROTECTION_ENVIRONMENTAL";
            case "BLASTPROTECTION":
                return "PROTECTION_EXPLOSIONS";
            case "FEATHERFALLING":
                return "PROTECTION_FALL";
            case "FIREPROTECTION":
                return "PROTECTION_FIRE";
            case "PROJECTILEPROTECTION":
                return "PROTECTION_PROJECTILE";
            case "SILKTOUCH":
                return "SILK_TOUCH";
            case "AQUAINFINITY":
                return "WATER_WORKER";
            case "DEPTHSTRIDER":
                return "DEPTH_STRIDER";
            case "LUCKOFTHESEAS":
                return "LUCK";
            case "LURE":
                return "LURE";
            case "FROSTWALKER":
                return "FROST_WALKER";
            case "MENDING":
                return "MENDING";
            case "CURSEOFBINDING":
                return "BINDING_CURSE";
            case "CURSEOFVANISHING":
                return "VANISHING_CURSE";
            case "SWEEPINGEDGE":
                return "SWEEPING_EDGE";
            default:
                return enchant;
        }
    }

    private String trueName(String enchant) {
        switch (enchant) {
            case "ARROW_DAMAGE":
                return "power";
            case "ARROW_FIRE":
                return "flame";
            case "ARROW_INFINITY":
                return "infinity";
            case "ARROW_KNOCKBACK":
                return "punch";
            case "DAMAGE_ALL":
                return "sharpness";
            case "DAMAGE_ARTHROPODS":
                return "bane of arthropods";
            case "DAMAGE_UNDEAD":
                return "smite";
            case "DIG_SPEED":
                return "efficiency";
            case "DURABILITY":
                return "unbreaking";
            case "FIRE_ASPECT":
                return "fire aspect";
            case "LOOT_BONUS_BLOCKS":
                return "fortune";
            case "LOOT_BONUS_MOBS":
                return "looting";
            case "OXYGEN":
                return "respiration";
            case "PROTECTION_ENVIRONMENTAL":
                return "protection";
            case "PROTECTION_EXPLOSIONS":
                return "blast protection";
            case "PROTECTION_FALL":
                return "feather falling";
            case "PROTECTION_FIRE":
                return "fire protection";
            case "PROTECTION_PROJECTILE":
                return "projectile protection";
            case "SILK_TOUCH":
                return "silk touch";
            case "WATER_WORKER":
                return "aqua infinity";
            case "DEPTH_STRIDER":
                return "depth strider";
            case "LURE":
                return "lure";
            case "LUCK":
                return "luck";
            case "FROST_WALKER":
                return "frost walker";
            case "MENDING":
                return "mending";
            case "BINDING_CURSE":
                return "curse of binding";
            case "VANISHING_CURSE":
                return "curse of vanishing";
            case "SWEEPING_EDGE":
                return "sweeping edge";
            default:
                return enchant.toLowerCase();
        }
    }
}