package com.crossge.necessities.Commands;

import com.crossge.necessities.Economy.Formatter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class CmdKillall extends Cmd {
    Formatter form = new Formatter();

    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                int i = 0;
                for (Entity t : p.getWorld().getEntities())
                    if (t.getType().equals(EntityType.BLAZE) || t.getType().equals(EntityType.CAVE_SPIDER) || t.getType().equals(EntityType.CREEPER) ||
                            t.getType().equals(EntityType.ENDER_DRAGON) || t.getType().equals(EntityType.ENDERMAN) || t.getType().equals(EntityType.GHAST) ||
                            t.getType().equals(EntityType.GIANT) || t.getType().equals(EntityType.MAGMA_CUBE) || t.getType().equals(EntityType.PIG_ZOMBIE) ||
                            t.getType().equals(EntityType.SILVERFISH) || t.getType().equals(EntityType.SKELETON) || t.getType().equals(EntityType.SLIME) ||
                            t.getType().equals(EntityType.SPIDER) || t.getType().equals(EntityType.WITCH) || t.getType().equals(EntityType.WITHER) ||
                            t.getType().equals(EntityType.ZOMBIE) || t.getType().equals(EntityType.GUARDIAN) || t.getType().equals(EntityType.ENDERMITE) ||
                            t.getType().equals(EntityType.RABBIT)) {
                        t.remove();
                        i++;
                    }
                p.sendMessage(var.getMessages() + "Removed " + var.getObj() + i + var.getMessages() + " entities.");
                return true;
            }
            int i = 0;
            if (args[0].equalsIgnoreCase("all")) {
                for (Entity t : p.getWorld().getEntities())
                    if (t.getType().equals(EntityType.BAT) || t.getType().equals(EntityType.BLAZE) || t.getType().equals(EntityType.CAVE_SPIDER) ||
                            t.getType().equals(EntityType.CHICKEN) || t.getType().equals(EntityType.COW) || t.getType().equals(EntityType.CREEPER) ||
                            t.getType().equals(EntityType.DROPPED_ITEM) || t.getType().equals(EntityType.ENDER_DRAGON) || t.getType().equals(EntityType.BOAT) ||
                            t.getType().equals(EntityType.ENDER_CRYSTAL) || t.getType().equals(EntityType.ENDERMAN) || t.getType().equals(EntityType.EXPERIENCE_ORB) ||
                            t.getType().equals(EntityType.GHAST) || t.getType().equals(EntityType.GIANT) || t.getType().equals(EntityType.HORSE) ||
                            t.getType().equals(EntityType.IRON_GOLEM) || t.getType().equals(EntityType.MAGMA_CUBE) || t.getType().equals(EntityType.MINECART) ||
                            t.getType().equals(EntityType.MUSHROOM_COW) || t.getType().equals(EntityType.OCELOT) || t.getType().equals(EntityType.PIG) ||
                            t.getType().equals(EntityType.PIG_ZOMBIE) || t.getType().equals(EntityType.SHEEP) || t.getType().equals(EntityType.SILVERFISH) ||
                            t.getType().equals(EntityType.SKELETON) || t.getType().equals(EntityType.SLIME) || t.getType().equals(EntityType.SNOWMAN) ||
                            t.getType().equals(EntityType.SPIDER) || t.getType().equals(EntityType.SQUID) || t.getType().equals(EntityType.VILLAGER) ||
                            t.getType().equals(EntityType.WITCH) || t.getType().equals(EntityType.WITHER) || t.getType().equals(EntityType.WOLF) ||
                            t.getType().equals(EntityType.ZOMBIE) || t.getType().equals(EntityType.GUARDIAN) || t.getType().equals(EntityType.ENDERMITE) ||
                            t.getType().equals(EntityType.RABBIT)) {
                        t.remove();
                        i++;
                    }
            } else if (args[0].equalsIgnoreCase("drops")) {
                for (Entity t : p.getWorld().getEntities())
                    if (t.getType().equals(EntityType.DROPPED_ITEM)) {
                        t.remove();
                        i++;
                    }
            } else if (args[0].equalsIgnoreCase("monsters")) {
                for (Entity t : p.getWorld().getEntities())
                    if (t.getType().equals(EntityType.BLAZE) || t.getType().equals(EntityType.CAVE_SPIDER) || t.getType().equals(EntityType.CREEPER) ||
                            t.getType().equals(EntityType.ENDER_DRAGON) || t.getType().equals(EntityType.ENDERMAN) || t.getType().equals(EntityType.GHAST) ||
                            t.getType().equals(EntityType.GIANT) || t.getType().equals(EntityType.MAGMA_CUBE) || t.getType().equals(EntityType.PIG_ZOMBIE) ||
                            t.getType().equals(EntityType.SILVERFISH) || t.getType().equals(EntityType.SKELETON) || t.getType().equals(EntityType.SLIME) ||
                            t.getType().equals(EntityType.SPIDER) || t.getType().equals(EntityType.WITCH) || t.getType().equals(EntityType.WITHER) ||
                            t.getType().equals(EntityType.ZOMBIE) || t.getType().equals(EntityType.GUARDIAN) || t.getType().equals(EntityType.ENDERMITE)) {
                        t.remove();
                        i++;
                    }
            } else if (args[0].equalsIgnoreCase("animals")) {
                for (Entity t : p.getWorld().getEntities())
                    if (t.getType().equals(EntityType.BAT) ||
                            t.getType().equals(EntityType.CHICKEN) || t.getType().equals(EntityType.COW) || t.getType().equals(EntityType.HORSE) ||
                            t.getType().equals(EntityType.IRON_GOLEM) || t.getType().equals(EntityType.MUSHROOM_COW) || t.getType().equals(EntityType.OCELOT) ||
                            t.getType().equals(EntityType.PIG) || t.getType().equals(EntityType.SHEEP) || t.getType().equals(EntityType.SNOWMAN) ||
                            t.getType().equals(EntityType.SQUID) || t.getType().equals(EntityType.VILLAGER) || t.getType().equals(EntityType.WOLF) ||
                            t.getType().equals(EntityType.RABBIT)) {
                        t.remove();
                        i++;
                    }
            } else if (args[0].equalsIgnoreCase("mobs")) {
                for (Entity t : p.getWorld().getEntities())
                    if (t.getType().equals(EntityType.BAT) || t.getType().equals(EntityType.BLAZE) || t.getType().equals(EntityType.CAVE_SPIDER) ||
                            t.getType().equals(EntityType.CHICKEN) || t.getType().equals(EntityType.COW) || t.getType().equals(EntityType.CREEPER) ||
                            t.getType().equals(EntityType.ENDER_DRAGON) || t.getType().equals(EntityType.BOAT) || t.getType().equals(EntityType.ENDER_CRYSTAL) ||
                            t.getType().equals(EntityType.ENDERMAN) || t.getType().equals(EntityType.GHAST) || t.getType().equals(EntityType.GIANT) ||
                            t.getType().equals(EntityType.HORSE) || t.getType().equals(EntityType.IRON_GOLEM) || t.getType().equals(EntityType.MAGMA_CUBE) ||
                            t.getType().equals(EntityType.MUSHROOM_COW) || t.getType().equals(EntityType.OCELOT) || t.getType().equals(EntityType.PIG) ||
                            t.getType().equals(EntityType.PIG_ZOMBIE) || t.getType().equals(EntityType.SHEEP) || t.getType().equals(EntityType.SILVERFISH) ||
                            t.getType().equals(EntityType.SKELETON) || t.getType().equals(EntityType.SLIME) || t.getType().equals(EntityType.SNOWMAN) ||
                            t.getType().equals(EntityType.SPIDER) || t.getType().equals(EntityType.SQUID) || t.getType().equals(EntityType.VILLAGER) ||
                            t.getType().equals(EntityType.WITCH) || t.getType().equals(EntityType.WITHER) || t.getType().equals(EntityType.WOLF) ||
                            t.getType().equals(EntityType.ZOMBIE) || t.getType().equals(EntityType.GUARDIAN) || t.getType().equals(EntityType.ENDERMITE) ||
                            t.getType().equals(EntityType.RABBIT)) {
                        t.remove();
                        i++;
                    }
            } else if (isMob(args[0]) != null) {
                EntityType type = isMob(args[0]);
                for (Entity t : p.getWorld().getEntities())
                    if (t.getType().equals(type)) {
                        t.remove();
                        i++;
                    }
            } else {
                p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid mob type.");
                p.sendMessage(var.getMessages() + "Valid mob types: " + ChatColor.WHITE + "all, drops, monsters, animals, mobs, [mobType]>");
                return true;
            }
            p.sendMessage(var.getMessages() + "Removed " + var.getObj() + i + var.getMessages() + " entities.");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You cannot kill all the mobs in your world, because you are not in a world");
        return true;
    }

    private EntityType isMob(String name) {//Also checks if entity sorta so not most proper name
        name = name.toLowerCase().replaceAll("_", "").trim();
        if (name.equals("bat"))
            return EntityType.BAT;
        if (name.equals("boat"))
            return EntityType.BOAT;
        if (name.equals("endercrystal"))
            return EntityType.ENDER_CRYSTAL;
        if (name.equals("arrow"))
            return EntityType.ARROW;
        if (name.equals("xp") || name.equals("exp") || name.equals("experience") || name.equals("experienceorb"))
            return EntityType.EXPERIENCE_ORB;
        if (name.equals("blaze"))
            return EntityType.BLAZE;
        if (name.equals("cavespider"))
            return EntityType.CAVE_SPIDER;
        if (name.equals("chicken"))
            return EntityType.CHICKEN;
        if (name.equals("cow"))
            return EntityType.COW;
        if (name.equals("creeper"))
            return EntityType.CREEPER;
        if (name.equals("enderdragon"))
            return EntityType.ENDER_DRAGON;
        if (name.equals("enderman"))
            return EntityType.ENDERMAN;
        if (name.equals("ghast"))
            return EntityType.GHAST;
        if (name.equals("giant"))
            return EntityType.GIANT;
        if (name.equals("horse"))
            return EntityType.HORSE;
        if (name.equals("irongolem"))
            return EntityType.IRON_GOLEM;
        if (name.equals("magmacube"))
            return EntityType.MAGMA_CUBE;
        if (name.equals("mushroomcow") || name.equals("mooshroom") || name.equals("mooshroomcow"))
            return EntityType.MUSHROOM_COW;
        if (name.equals("ocelot"))
            return EntityType.OCELOT;
        if (name.equals("pig"))
            return EntityType.PIG;
        if (name.equals("pigzombie") || name.equals("zombiepigman"))
            return EntityType.PIG_ZOMBIE;
        if (name.equals("sheep"))
            return EntityType.SHEEP;
        if (name.equals("silverfish"))
            return EntityType.SILVERFISH;
        if (name.equals("skeleton"))
            return EntityType.SKELETON;
        if (name.equals("slime"))
            return EntityType.SLIME;
        if (name.equals("snowman"))
            return EntityType.SNOWMAN;
        if (name.equals("spider"))
            return EntityType.SPIDER;
        if (name.equals("squid"))
            return EntityType.SQUID;
        if (name.equals("villager"))
            return EntityType.VILLAGER;
        if (name.equals("witch"))
            return EntityType.WITCH;
        if (name.equals("wither"))
            return EntityType.WITHER;
        if (name.equals("wolf"))
            return EntityType.WOLF;
        if (name.equals("zombie"))
            return EntityType.ZOMBIE;
        if (name.equals("endermite"))
            return EntityType.ENDERMITE;
        if (name.equals("guardian"))
            return EntityType.GUARDIAN;
        if (name.equals("rabbit"))
            return EntityType.RABBIT;
        if (name.equals("fireball"))
            return EntityType.FIREBALL;
        if (name.equals("witherskull"))
            return EntityType.WITHER_SKULL;
        if (name.equals("itemframe"))
            return EntityType.ITEM_FRAME;
        return null;
    }
}