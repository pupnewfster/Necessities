package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class CmdEnchant implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
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
                Enchantment ench = Enchantment.getByKey(enchantFinder(args[0]));
                if (ench == null) {
                    player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Enchantment not found.");
                    return true;
                }
                int level = ench.getMaxLevel();
                if (ench.canEnchantItem(hand)) {
                    hand.addEnchantment(ench, level);
                    player.sendMessage(var.getMessages() + "Added the enchantment " + var.getObj() + trueName(ench.getKey()) + var.getMessages() + " at level " + var.getObj() + Integer.toString(level) +
                            var.getMessages() + '.');
                    return true;
                }
                player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "This item can not support given enchantment.");
                return true;
            }
            if (args.length == 2) {
                Enchantment ench = null;
                if (!args[0].equalsIgnoreCase("all")) {
                    ench = Enchantment.getByKey(enchantFinder(args[0]));
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
                        player.sendMessage(var.getMessages() + "Added all enchantments at level " + var.getObj() + Integer.toString(level) + var.getMessages() + '.');
                    }
                    return true;
                } else if (ench != null && (ench.canEnchantItem(hand) || player.hasPermission("Necessities.unsafeEnchant"))) {
                    if (level == -1) {
                        hand.addUnsafeEnchantment(ench, ench.getMaxLevel());
                        player.sendMessage(var.getMessages() + "Added the enchantment " + var.getObj() + trueName(ench.getKey()) + var.getMessages() + " at max level.");
                    } else {
                        if (level == 0) {
                            hand.removeEnchantment(ench);
                            player.sendMessage(var.getMessages() + "Removed enchantment " + var.getObj() + trueName(ench.getKey()) + var.getMessages() + '.');
                        } else {
                            hand.addUnsafeEnchantment(ench, level);
                            player.sendMessage(var.getMessages() + "Added the enchantment " + var.getObj() + trueName(ench.getKey()) + var.getMessages() + " at level " + var.getObj() + Integer.toString(level) +
                                    var.getMessages() + '.');
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
                for (Enchantment e : Enchantment.values()) {
                    String key = e.getKey().toString();
                    if (e.canEnchantItem(p.getInventory().getItemInMainHand()) && key.startsWith(search))
                        complete.add(key);
                }
            } else {
                if ("MAX".startsWith(search))
                    complete.add("MAX");
                for (Enchantment e : Enchantment.values())
                    if (e.canEnchantItem(p.getInventory().getItemInMainHand()) && e.equals(Enchantment.getByKey(enchantFinder(args[0]))) &&
                            Integer.toString(e.getMaxLevel()).startsWith(search))
                        for (int i = 0; i < e.getMaxLevel(); i++)
                            complete.add(Integer.toString(i + 1));
            }
        }
        return complete;
    }

    private NamespacedKey enchantFinder(String enchant) {
        enchant = enchant.toUpperCase();
        switch (enchant) {
            case "POWER":
                return Enchantment.ARROW_DAMAGE.getKey();
            case "FLAME":
                return Enchantment.ARROW_FIRE.getKey();
            case "INFINITY":
                return Enchantment.ARROW_INFINITE.getKey();
            case "PUNCH":
                return Enchantment.ARROW_KNOCKBACK.getKey();
            case "SHARPNESS":
                return Enchantment.DAMAGE_ALL.getKey();
            case "BANEOFARTHROPODS":
                return Enchantment.DAMAGE_ARTHROPODS.getKey();
            case "BANE":
                return Enchantment.DAMAGE_ARTHROPODS.getKey();
            case "SMITE":
                return Enchantment.DAMAGE_UNDEAD.getKey();
            case "EFFICIENCY":
                return Enchantment.DIG_SPEED.getKey();
            case "UNBREAKING":
                return Enchantment.DURABILITY.getKey();
            case "FIREASPECT":
                return Enchantment.FIRE_ASPECT.getKey();
            case "FORTUNE":
                return Enchantment.LOOT_BONUS_BLOCKS.getKey();
            case "LOOTING":
                return Enchantment.LOOT_BONUS_MOBS.getKey();
            case "RESPIRATION":
                return Enchantment.OXYGEN.getKey();
            case "PROTECTION":
                return Enchantment.PROTECTION_ENVIRONMENTAL.getKey();
            case "BLASTPROTECTION":
                return Enchantment.PROTECTION_EXPLOSIONS.getKey();
            case "FEATHERFALLING":
                return Enchantment.PROTECTION_FALL.getKey();
            case "FIREPROTECTION":
                return Enchantment.PROTECTION_FIRE.getKey();
            case "PROJECTILEPROTECTION":
                return Enchantment.PROTECTION_PROJECTILE.getKey();
            case "SILKTOUCH":
                return Enchantment.SILK_TOUCH.getKey();
            case "AQUAINFINITY":
                return Enchantment.WATER_WORKER.getKey();
            case "DEPTHSTRIDER":
                return Enchantment.DEPTH_STRIDER.getKey();
            case "LUCKOFTHESEAS":
                return Enchantment.LUCK.getKey();
            case "LURE":
                return Enchantment.LURE.getKey();
            case "FROSTWALKER":
                return Enchantment.FROST_WALKER.getKey();
            case "MENDING":
                return Enchantment.MENDING.getKey();
            case "CURSEOFBINDING":
                return Enchantment.BINDING_CURSE.getKey();
            case "CURSEOFVANISHING":
                return Enchantment.VANISHING_CURSE.getKey();
            case "SWEEPINGEDGE":
                return Enchantment.SWEEPING_EDGE.getKey();
            case "CHANNELING":
                return Enchantment.CHANNELING.getKey();
            case "IMPALING":
                return Enchantment.IMPALING.getKey();
            case "LOYALTY":
                return Enchantment.LOYALTY.getKey();
            case "RIPTIDE":
                return Enchantment.RIPTIDE.getKey();
            default:
                String[] pieces = enchant.toLowerCase().split(":");
                if (pieces.length == 0) {
                    return null;
                }
                if (pieces.length == 1) {
                    return NamespacedKey.minecraft(enchant.toLowerCase());
                }
                Plugin p = Bukkit.getPluginManager().getPlugin(pieces[0]);
                return p == null ? null : new NamespacedKey(p, pieces[1]);
        }
    }

    private String trueName(NamespacedKey key) {
        if (key == null) {
            return null;
        }
        if (Enchantment.ARROW_DAMAGE.getKey().equals(key)) {
            return "power";
        } else if (Enchantment.ARROW_FIRE.getKey().equals(key)) {
            return "flame";
        } else if (Enchantment.ARROW_INFINITE.getKey().equals(key)) {
            return "infinity";
        } else if (Enchantment.ARROW_KNOCKBACK.getKey().equals(key)) {
            return "punch";
        } else if (Enchantment.DAMAGE_ALL.getKey().equals(key)) {
            return "sharpness";
        } else if (Enchantment.DAMAGE_ARTHROPODS.getKey().equals(key)) {
            return "bane of arthropods";
        } else if (Enchantment.DAMAGE_UNDEAD.getKey().equals(key)) {
            return "smite";
        } else if (Enchantment.DIG_SPEED.getKey().equals(key)) {
            return "efficiency";
        } else if (Enchantment.DURABILITY.getKey().equals(key)) {
            return "unbreaking";
        } else if (Enchantment.FIRE_ASPECT.getKey().equals(key)) {
            return "fire aspect";
        } else if (Enchantment.LOOT_BONUS_BLOCKS.getKey().equals(key)) {
            return "fortune";
        } else if (Enchantment.LOOT_BONUS_MOBS.getKey().equals(key)) {
            return "looting";
        } else if (Enchantment.OXYGEN.getKey().equals(key)) {
            return "respiration";
        } else if (Enchantment.PROTECTION_ENVIRONMENTAL.getKey().equals(key)) {
            return "protection";
        } else if (Enchantment.PROTECTION_EXPLOSIONS.getKey().equals(key)) {
            return "blast protection";
        } else if (Enchantment.PROTECTION_FALL.getKey().equals(key)) {
            return "feather falling";
        } else if (Enchantment.PROTECTION_FIRE.getKey().equals(key)) {
            return "fire protection";
        } else if (Enchantment.PROTECTION_PROJECTILE.getKey().equals(key)) {
            return "projectile protection";
        } else if (Enchantment.SILK_TOUCH.getKey().equals(key)) {
            return "silk touch";
        } else if (Enchantment.WATER_WORKER.getKey().equals(key)) {
            return "aqua infinity";
        } else if (Enchantment.DEPTH_STRIDER.getKey().equals(key)) {
            return "depth strider";
        } else if (Enchantment.LURE.getKey().equals(key)) {
            return "lure";
        } else if (Enchantment.LUCK.getKey().equals(key)) {
            return "luck";
        } else if (Enchantment.FROST_WALKER.getKey().equals(key)) {
            return "frost walker";
        } else if (Enchantment.MENDING.getKey().equals(key)) {
            return "mending";
        } else if (Enchantment.BINDING_CURSE.getKey().equals(key)) {
            return "curse of binding";
        } else if (Enchantment.VANISHING_CURSE.getKey().equals(key)) {
            return "curse of vanishing";
        } else if (Enchantment.SWEEPING_EDGE.getKey().equals(key)) {
            return "sweeping edge";
        } else if (Enchantment.CHANNELING.getKey().equals(key)) {
            return "channeling";
        } else if (Enchantment.IMPALING.getKey().equals(key)) {
            return "impaling";
        } else if (Enchantment.LOYALTY.getKey().equals(key)) {
            return "loyalty";
        } else if (Enchantment.RIPTIDE.getKey().equals(key)) {
            return "riptide";
        }
        return key.toString().toLowerCase();
    }
}