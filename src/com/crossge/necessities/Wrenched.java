package com.crossge.necessities;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;

public class Wrenched {
    private static ArrayList<String> locs = new ArrayList<>();
    private File configFileWrench = new File("plugins/Necessities", "wrenched.yml");

    public void initiate() {
        YamlConfiguration wrenches = YamlConfiguration.loadConfiguration(configFileWrench);
        for (String key : wrenches.getKeys(false))
            if (!locs.contains(key.toLowerCase()))
                locs.add(key.toLowerCase());
    }

    public void wrench(Block b) {
        YamlConfiguration wrenches = YamlConfiguration.loadConfiguration(configFileWrench);
        Location l = b.getLocation();
        String blockForm = (l.getWorld().getName() + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ()).toLowerCase();
        if (locs.contains(blockForm)) {
            locs.remove(blockForm);
            wrenches.set(blockForm, null);
        } else {
            locs.add(blockForm);
            wrenches.set(blockForm, true);
        }
        try {
            wrenches.save(configFileWrench);
        } catch (Exception e) {
        }
    }

    public boolean isWrenched(Block b) {
        Location l = b.getLocation();
        return locs.contains((l.getWorld().getName() + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ()).toLowerCase());
    }
}