package com.crossge.necessities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;

public class Wrenched {
    private static ArrayList<Location> locs = new ArrayList<Location>();
    private File configFileWrench = new File("plugins/Necessities", "wrenched.yml");

    public void initiate() {
        YamlConfiguration wrenches = YamlConfiguration.loadConfiguration(configFileWrench);
        for (String key : wrenches.getKeys(false)) {
            if (key.split(",").length == 4) {
                World world = Bukkit.getWorld(key.split(",")[0]);
                int x = Integer.parseInt(key.split(",")[1]);
                int y = Integer.parseInt(key.split(",")[2]);
                int z = Integer.parseInt(key.split(",")[3]);
                Location l = new Location(world, x, y, z);
                if (!locs.contains(l))
                    locs.add(l);
            }
        }
    }

    public void wrench(Block b) {
        YamlConfiguration wrenches = YamlConfiguration.loadConfiguration(configFileWrench);
        Location l = b.getLocation();
        String blockForm = l.getWorld().getName() + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ();
        if (locs.contains(l)) {
            locs.remove(l);
            wrenches.set(blockForm, null);
        } else {
            locs.add(l);
            wrenches.set(blockForm, true);
        }
        try {
            wrenches.save(configFileWrench);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isWrenched(Block b) {
        return locs.contains(b.getLocation());
    }
}