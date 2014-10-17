package com.crossge.necessities.Economy;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;

public class Materials {
    private File configFileNames = new File("plugins/Necessities/Economy", "itemnames.yml");
    private File configFileIds = new File("plugins/Necessities/Economy", "ids.yml");
    private File configFileFriendlyNames = new File("plugins/Necessities/Economy", "friendlynames.yml");
    private File configFilePluralyNames = new File("plugins/Necessities/Economy", "pluralnames.yml");
    private static HashMap<Integer, String> idsToNames = new HashMap<Integer, String>();
    private static HashMap<String, String> friendlyNames = new HashMap<String, String>();
    private static HashMap<String, String> customNames = new HashMap<String, String>();
    private static HashMap<String, String> plural = new HashMap<String, String>();
    Formatter form = new Formatter();

    public void readIds() {
        YamlConfiguration configNames = YamlConfiguration.loadConfiguration(configFileNames);
        YamlConfiguration configIds = YamlConfiguration.loadConfiguration(configFileIds);
        YamlConfiguration configFriendlyNames = YamlConfiguration.loadConfiguration(configFileFriendlyNames);
        YamlConfiguration configPluralNames = YamlConfiguration.loadConfiguration(configFilePluralyNames);
        for (String key : configIds.getKeys(false))
            idsToNames.put(configIds.getInt(key), key);
        for (String key : configNames.getKeys(false))
            for (String name : configNames.getStringList(key))
                customNames.put(name, key);
        for (String key : configFriendlyNames.getKeys(false))
            friendlyNames.put(key, configFriendlyNames.getString(key));
        for (String key : configPluralNames.getKeys(false))
            plural.put(key, configPluralNames.getString(key));
    }

    public String pluralize(String name, int amount) {
        if (amount != 1) {
            String tempname = name.toUpperCase().replaceAll(" ", "");
            if (plural.containsKey(tempname))
                return form.capFirst(plural.get(tempname));
        }
        return name;
    }

    public String findItem(String item) {
        if (item == null)
            return null;
        item = item.toUpperCase().replaceAll("_", "");
        if (customNames.containsKey(item))
            return customNames.get(item);
        for (int i = 0; i < Material.values().length; i++) {
            if (Material.values()[i].name().replaceAll("_", "").equalsIgnoreCase(item))
                return Material.values()[i].name();
            if (Material.values()[i].name().replaceAll("_", "").equalsIgnoreCase(item + "item") && !Material.values()[i].name().replaceAll("_", "").equalsIgnoreCase("netherbrickitem"))
                return Material.values()[i].name();
        }
        return null;
    }

    public String idToName(int id) {
        return idsToNames.get(id);
    }

    public int nameToId(String name) {
        if (idsToNames.containsValue(name))
            for (int id : idsToNames.keySet())
                if (idToName(id).equals(name))
                    return id;
        return 0;
    }

    public String getName(String item) {
        return friendlyNames.containsKey(item) ? friendlyNames.get(item) : item;
    }

    public boolean isSlab(Block b) {
        return b.getType().equals(Material.STEP) || b.getType().equals(Material.WOOD_STEP);
    }

    public boolean isTool(ItemStack is) {
        Material type = is.getData().getItemType();
        //Wood tools
        if (type.equals(Material.WOOD_AXE) || type.equals(Material.WOOD_HOE) || type.equals(Material.WOOD_PICKAXE) ||
                type.equals(Material.WOOD_SWORD) || type.equals(Material.WOOD_SPADE))
            return true;
        //Stone tools
        if (type.equals(Material.STONE_AXE) || type.equals(Material.STONE_HOE) || type.equals(Material.STONE_PICKAXE) ||
                type.equals(Material.STONE_SWORD) || type.equals(Material.STONE_SPADE))
            return true;
        //Iron tools
        if (type.equals(Material.IRON_AXE) || type.equals(Material.IRON_HOE) || type.equals(Material.IRON_PICKAXE) ||
                type.equals(Material.IRON_SWORD) || type.equals(Material.IRON_SPADE))
            return true;
        //Gold tools
        if (type.equals(Material.GOLD_AXE) || type.equals(Material.GOLD_HOE) || type.equals(Material.GOLD_PICKAXE) ||
                type.equals(Material.GOLD_SWORD) || type.equals(Material.GOLD_SPADE))
            return true;
        //Diamond tools
        if (type.equals(Material.DIAMOND_AXE) || type.equals(Material.DIAMOND_HOE) || type.equals(Material.DIAMOND_PICKAXE) ||
                type.equals(Material.DIAMOND_SWORD) || type.equals(Material.DIAMOND_SPADE))
            return true;
        //Leather Armor
        if (type.equals(Material.LEATHER_BOOTS) || type.equals(Material.LEATHER_CHESTPLATE) ||
                type.equals(Material.LEATHER_HELMET) || type.equals(Material.LEATHER_LEGGINGS))
            return true;
        //Chainmail Armor
        if (type.equals(Material.CHAINMAIL_BOOTS) || type.equals(Material.CHAINMAIL_CHESTPLATE) ||
                type.equals(Material.CHAINMAIL_HELMET) || type.equals(Material.CHAINMAIL_LEGGINGS))
            return true;
        //Iron Armor
        if (type.equals(Material.IRON_BOOTS) || type.equals(Material.IRON_CHESTPLATE) ||
                type.equals(Material.IRON_HELMET) || type.equals(Material.IRON_LEGGINGS))
            return true;
        //Gold Armor
        if (type.equals(Material.GOLD_BOOTS) || type.equals(Material.GOLD_CHESTPLATE) ||
                type.equals(Material.GOLD_HELMET) || type.equals(Material.GOLD_LEGGINGS))
            return true;
        //Diamond Armor
        if (type.equals(Material.DIAMOND_BOOTS) || type.equals(Material.DIAMOND_CHESTPLATE) ||
                type.equals(Material.DIAMOND_HELMET) || type.equals(Material.DIAMOND_LEGGINGS))
            return true;
        //Other things that can have data values
        return type.equals(Material.ANVIL) || type.equals(Material.CARROT_STICK) || type.equals(Material.FISHING_ROD) ||
                type.equals(Material.FLINT_AND_STEEL) || type.equals(Material.SHEARS) || type.equals(Material.BOW);
    }
}