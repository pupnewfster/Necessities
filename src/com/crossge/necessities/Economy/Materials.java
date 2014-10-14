package com.crossge.necessities.Economy;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;

public class Materials {
    private static HashMap<Integer, String> idsToNames = new HashMap<Integer, String>();
    private static HashMap<String, String> friendlyNames = new HashMap<String, String>();
    private static HashMap<String, String> customNames = new HashMap<String, String>();
    private static HashMap<String, String> plural = new HashMap<String, String>();
    Formatter form = new Formatter();
    private File configFileNames = new File("plugins/Necessities/Economy", "itemnames.yml");
    private File configFileIds = new File("plugins/Necessities/Economy", "ids.yml");
    private File configFileFriendlyNames = new File("plugins/Necessities/Economy", "friendlynames.yml");
    private File configFilePluralyNames = new File("plugins/Necessities/Economy", "pluralnames.yml");

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
        if (friendlyNames.containsKey(item))
            return friendlyNames.get(item);
        return item;
    }

    public boolean isSlab(Block b) {
        return b.getType().equals(Material.STEP) || b.getType().equals(Material.WOOD_STEP);
    }

    public boolean isTool(ItemStack is) {
        //Wood tools
        if (is.getData().getItemType().equals(Material.WOOD_AXE))
            return true;
        if (is.getData().getItemType().equals(Material.WOOD_HOE))
            return true;
        if (is.getData().getItemType().equals(Material.WOOD_PICKAXE))
            return true;
        if (is.getData().getItemType().equals(Material.WOOD_SWORD))
            return true;
        if (is.getData().getItemType().equals(Material.WOOD_SPADE))
            return true;
        //Stone tools
        if (is.getData().getItemType().equals(Material.STONE_AXE))
            return true;
        if (is.getData().getItemType().equals(Material.STONE_HOE))
            return true;
        if (is.getData().getItemType().equals(Material.STONE_PICKAXE))
            return true;
        if (is.getData().getItemType().equals(Material.STONE_SWORD))
            return true;
        if (is.getData().getItemType().equals(Material.STONE_SPADE))
            return true;
        //Iron tools
        if (is.getData().getItemType().equals(Material.IRON_AXE))
            return true;
        if (is.getData().getItemType().equals(Material.IRON_HOE))
            return true;
        if (is.getData().getItemType().equals(Material.IRON_PICKAXE))
            return true;
        if (is.getData().getItemType().equals(Material.IRON_SWORD))
            return true;
        if (is.getData().getItemType().equals(Material.IRON_SPADE))
            return true;
        //Gold tools
        if (is.getData().getItemType().equals(Material.GOLD_AXE))
            return true;
        if (is.getData().getItemType().equals(Material.GOLD_HOE))
            return true;
        if (is.getData().getItemType().equals(Material.GOLD_PICKAXE))
            return true;
        if (is.getData().getItemType().equals(Material.GOLD_SWORD))
            return true;
        if (is.getData().getItemType().equals(Material.GOLD_SPADE))
            return true;
        //Diamond tools
        if (is.getData().getItemType().equals(Material.DIAMOND_AXE))
            return true;
        if (is.getData().getItemType().equals(Material.DIAMOND_HOE))
            return true;
        if (is.getData().getItemType().equals(Material.DIAMOND_PICKAXE))
            return true;
        if (is.getData().getItemType().equals(Material.DIAMOND_SWORD))
            return true;
        if (is.getData().getItemType().equals(Material.DIAMOND_SPADE))
            return true;
        //Leather Armor
        if (is.getData().getItemType().equals(Material.LEATHER_BOOTS))
            return true;
        if (is.getData().getItemType().equals(Material.LEATHER_CHESTPLATE))
            return true;
        if (is.getData().getItemType().equals(Material.LEATHER_HELMET))
            return true;
        if (is.getData().getItemType().equals(Material.LEATHER_LEGGINGS))
            return true;
        //Chainmail Armor
        if (is.getData().getItemType().equals(Material.CHAINMAIL_BOOTS))
            return true;
        if (is.getData().getItemType().equals(Material.CHAINMAIL_CHESTPLATE))
            return true;
        if (is.getData().getItemType().equals(Material.CHAINMAIL_HELMET))
            return true;
        if (is.getData().getItemType().equals(Material.CHAINMAIL_LEGGINGS))
            return true;
        //Iron Armor
        if (is.getData().getItemType().equals(Material.IRON_BOOTS))
            return true;
        if (is.getData().getItemType().equals(Material.IRON_CHESTPLATE))
            return true;
        if (is.getData().getItemType().equals(Material.IRON_HELMET))
            return true;
        if (is.getData().getItemType().equals(Material.IRON_LEGGINGS))
            return true;
        //Gold Armor
        if (is.getData().getItemType().equals(Material.GOLD_BOOTS))
            return true;
        if (is.getData().getItemType().equals(Material.GOLD_CHESTPLATE))
            return true;
        if (is.getData().getItemType().equals(Material.GOLD_HELMET))
            return true;
        if (is.getData().getItemType().equals(Material.GOLD_LEGGINGS))
            return true;
        //Diamond Armor
        if (is.getData().getItemType().equals(Material.DIAMOND_BOOTS))
            return true;
        if (is.getData().getItemType().equals(Material.DIAMOND_CHESTPLATE))
            return true;
        if (is.getData().getItemType().equals(Material.DIAMOND_HELMET))
            return true;
        if (is.getData().getItemType().equals(Material.DIAMOND_LEGGINGS))
            return true;
        //Other things that can have data values
        if (is.getData().getItemType().equals(Material.ANVIL))
            return true;
        if (is.getData().getItemType().equals(Material.CARROT_STICK))
            return true;
        if (is.getData().getItemType().equals(Material.FISHING_ROD))
            return true;
        if (is.getData().getItemType().equals(Material.FLINT_AND_STEEL))
            return true;
        if (is.getData().getItemType().equals(Material.SHEARS))
            return true;
        if (is.getData().getItemType().equals(Material.BOW))
            return true;
        return false;
    }
}