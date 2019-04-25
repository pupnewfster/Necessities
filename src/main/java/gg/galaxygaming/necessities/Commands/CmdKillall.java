package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Variables;
import java.util.EnumSet;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class CmdKillall implements Cmd {

    private final Set<EntityType> hostile = EnumSet.of(EntityType.BLAZE, EntityType.CAVE_SPIDER, EntityType.CREEPER,
          EntityType.ENDER_DRAGON, EntityType.ENDERMAN, EntityType.GHAST, EntityType.GIANT, EntityType.MAGMA_CUBE,
          EntityType.PIG_ZOMBIE, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.SLIME, EntityType.SPIDER,
          EntityType.WITCH, EntityType.WITHER, EntityType.ZOMBIE, EntityType.GUARDIAN, EntityType.ENDERMITE,
          EntityType.SHULKER, EntityType.SHULKER_BULLET, EntityType.HUSK, EntityType.STRAY, EntityType.VINDICATOR,
          EntityType.EVOKER, EntityType.VEX, EntityType.PHANTOM, EntityType.DROWNED, EntityType.PILLAGER,
          EntityType.RAVAGER);
    private final Set<EntityType> passive = EnumSet.of(EntityType.CHICKEN, EntityType.COW, EntityType.HORSE,
          EntityType.IRON_GOLEM, EntityType.MUSHROOM_COW, EntityType.OCELOT, EntityType.PIG, EntityType.SHEEP,
          EntityType.SNOWMAN, EntityType.SQUID, EntityType.VILLAGER, EntityType.WOLF, EntityType.RABBIT,
          EntityType.POLAR_BEAR, EntityType.LLAMA, EntityType.DOLPHIN, EntityType.TROPICAL_FISH, EntityType.PUFFERFISH,
          EntityType.COD, EntityType.SALMON, EntityType.TURTLE, EntityType.CAT, EntityType.FOX, EntityType.PANDA,
          EntityType.TRADER_LLAMA, EntityType.WANDERING_TRADER);
    private final Set<EntityType> misc = EnumSet.of(EntityType.DROPPED_ITEM, EntityType.BOAT, EntityType.ENDER_CRYSTAL,
          EntityType.EXPERIENCE_ORB, EntityType.MINECART);

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                int i = 0;
                for (Entity t : p.getWorld().getEntities()) {
                    if (hostile.contains(t.getType())) {
                        t.remove();
                        i++;
                    }
                }
                p.sendMessage(var.getMessages() + "Removed " + var.getObj() + i + var.getMessages() + " entities.");
                return true;
            }
            int i = 0;
            if (args[0].equalsIgnoreCase("all")) {
                for (Entity t : p.getWorld().getEntities()) {
                    EntityType type = t.getType();
                    if (hostile.contains(type) || passive.contains(type) || misc.contains(type)) {
                        t.remove();
                        i++;
                    }
                }
            } else if (args[0].equalsIgnoreCase("drops")) {
                for (Entity t : p.getWorld().getEntities()) {
                    if (t.getType().equals(EntityType.DROPPED_ITEM)) {
                        t.remove();
                        i++;
                    }
                }
            } else if (args[0].equalsIgnoreCase("monsters")) {
                for (Entity t : p.getWorld().getEntities()) {
                    if (hostile.contains(t.getType())) {
                        t.remove();
                        i++;
                    }
                }
            } else if (args[0].equalsIgnoreCase("animals")) {
                for (Entity t : p.getWorld().getEntities()) {
                    if (passive.contains(t.getType())) {
                        t.remove();
                        i++;
                    }
                }
            } else if (args[0].equalsIgnoreCase("mobs")) {
                for (Entity t : p.getWorld().getEntities()) {
                    EntityType type = t.getType();
                    if (hostile.contains(type) || passive.contains(type)) {
                        t.remove();
                        i++;
                    }
                }
            } else if (isMob(args[0]) != null) {
                EntityType type = isMob(args[0]);
                for (Entity t : p.getWorld().getEntities()) {
                    if (t.getType().equals(type)) {
                        t.remove();
                        i++;
                    }
                }
            } else {
                p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid mob type.");
                p.sendMessage(var.getMessages() + "Valid mob types: " + ChatColor.WHITE
                      + "all, drops, monsters, animals, mobs, [mobType]>");
                return true;
            }
            p.sendMessage(var.getMessages() + "Removed " + var.getObj() + i + var.getMessages() + " entities.");
        } else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                  + "You cannot kill all the mobs in your world, because you are not in a world");
        }
        return true;
    }

    private EntityType isMob(String name) {//Also checks if entity sorta so not most proper name
        name = name.toLowerCase().replaceAll("_", "").trim();
        if (name.equals("bat")) {
            return EntityType.BAT;
        }
        if (name.equals("boat")) {
            return EntityType.BOAT;
        }
        if (name.equals("endercrystal")) {
            return EntityType.ENDER_CRYSTAL;
        }
        if (name.equals("arrow")) {
            return EntityType.ARROW;
        }
        if (name.equals("xp") || name.equals("exp") || name.equals("experience") || name.equals("experienceorb")) {
            return EntityType.EXPERIENCE_ORB;
        }
        if (name.equals("blaze")) {
            return EntityType.BLAZE;
        }
        if (name.equals("cavespider")) {
            return EntityType.CAVE_SPIDER;
        }
        if (name.equals("chicken")) {
            return EntityType.CHICKEN;
        }
        if (name.equals("cow")) {
            return EntityType.COW;
        }
        if (name.equals("creeper")) {
            return EntityType.CREEPER;
        }
        if (name.equals("enderdragon")) {
            return EntityType.ENDER_DRAGON;
        }
        if (name.equals("enderman")) {
            return EntityType.ENDERMAN;
        }
        if (name.equals("ghast")) {
            return EntityType.GHAST;
        }
        if (name.equals("giant")) {
            return EntityType.GIANT;
        }
        if (name.equals("horse")) {
            return EntityType.HORSE;
        }
        if (name.equals("irongolem")) {
            return EntityType.IRON_GOLEM;
        }
        if (name.equals("magmacube")) {
            return EntityType.MAGMA_CUBE;
        }
        if (name.equals("mushroomcow") || name.equals("mooshroom") || name.equals("mooshroomcow")) {
            return EntityType.MUSHROOM_COW;
        }
        if (name.equals("ocelot")) {
            return EntityType.OCELOT;
        }
        if (name.equals("pig")) {
            return EntityType.PIG;
        }
        if (name.equals("pigzombie") || name.equals("zombiepigman")) {
            return EntityType.PIG_ZOMBIE;
        }
        if (name.equals("sheep")) {
            return EntityType.SHEEP;
        }
        if (name.equals("silverfish")) {
            return EntityType.SILVERFISH;
        }
        if (name.equals("skeleton")) {
            return EntityType.SKELETON;
        }
        if (name.equals("slime")) {
            return EntityType.SLIME;
        }
        if (name.equals("snowman")) {
            return EntityType.SNOWMAN;
        }
        if (name.equals("spider")) {
            return EntityType.SPIDER;
        }
        if (name.equals("squid")) {
            return EntityType.SQUID;
        }
        if (name.equals("villager")) {
            return EntityType.VILLAGER;
        }
        if (name.equals("witch")) {
            return EntityType.WITCH;
        }
        if (name.equals("wither")) {
            return EntityType.WITHER;
        }
        if (name.equals("wolf")) {
            return EntityType.WOLF;
        }
        if (name.equals("zombie")) {
            return EntityType.ZOMBIE;
        }
        if (name.equals("endermite")) {
            return EntityType.ENDERMITE;
        }
        if (name.equals("guardian")) {
            return EntityType.GUARDIAN;
        }
        if (name.equals("rabbit")) {
            return EntityType.RABBIT;
        }
        if (name.equals("fireball")) {
            return EntityType.FIREBALL;
        }
        if (name.equals("witherskull")) {
            return EntityType.WITHER_SKULL;
        }
        if (name.equals("itemframe")) {
            return EntityType.ITEM_FRAME;
        }
        if (name.equals("armorstand")) {
            return EntityType.ARMOR_STAND;
        }
        if (name.equals("shulker")) {
            return EntityType.SHULKER;
        }
        if (name.equals("shulkerbullet")) {
            return EntityType.SHULKER_BULLET;
        }
        if (name.equals("polarbear")) {
            return EntityType.POLAR_BEAR;
        }
        if (name.equals("vindicator")) {
            return EntityType.VINDICATOR;
        }
        if (name.equals("evoker")) {
            return EntityType.EVOKER;
        }
        if (name.equals("vex")) {
            return EntityType.VEX;
        }
        if (name.equals("llama")) {
            return EntityType.LLAMA;
        }
        if (name.equals("dolphin")) {
            return EntityType.DOLPHIN;
        }
        if (name.equals("tropicalfish") || name.equals("clownfish")) {
            return EntityType.TROPICAL_FISH;
        }
        if (name.equals("pufferfish")) {
            return EntityType.PUFFERFISH;
        }
        if (name.equals("cod") || name.equals("fish")) {
            return EntityType.COD;
        }
        if (name.equals("salmon")) {
            return EntityType.SALMON;
        }
        if (name.equals("turtle")) {
            return EntityType.TURTLE;
        }
        if (name.equals("phantom")) {
            return EntityType.PHANTOM;
        }
        if (name.equals("drowned")) {
            return EntityType.DROWNED;
        }

        if (name.equals("cat")) {
            return EntityType.CAT;
        }
        if (name.equals("fox") || name.equals("foxes")) {
            return EntityType.FOX;
        }
        if (name.equals("panda")) {
            return EntityType.PANDA;
        }
        if (name.equals("pillager")) {
            return EntityType.PILLAGER;
        }
        if (name.equals("ravager")) {
            return EntityType.RAVAGER;
        }
        if (name.equals("traderllama")) {
            return EntityType.TRADER_LLAMA;
        }
        if (name.equals("trader") || name.equals("wanderingtrader")) {
            return EntityType.WANDERING_TRADER;
        }
        return null;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}