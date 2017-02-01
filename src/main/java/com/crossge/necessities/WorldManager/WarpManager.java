package com.crossge.necessities.WorldManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class WarpManager {
    private final File configFileWarps = new File("plugins/Necessities/WorldManager", "warps.yml");
    private final HashMap<String, String> lowerNames = new HashMap<>();
    private final HashMap<String, Warp> warps = new HashMap<>();

    public void initiate() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Loading warps...");
        YamlConfiguration configWarps = YamlConfiguration.loadConfiguration(configFileWarps);
        for (String warp : configWarps.getKeys(false)) {
            warps.put(warp, new Warp(warp));
            lowerNames.put(warp.toLowerCase(), warp);
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "All warps loaded.");
    }

    public boolean isWarp(String name) {
        return lowerNames.containsKey(name.toLowerCase());
    }

    public Warp getWarp(String name) {
        return !isWarp(name) ? null : warps.get(lowerNames.get(name.toLowerCase()));
    }

    public String getWarps() {
        ArrayList<String> ws = new ArrayList<>();
        warps.keySet().forEach(ws::add);
        Collections.sort(ws);
        String warps = "";
        for (String w : ws)
            warps += w + ", ";
        return warps.equals("") ? "" : warps.trim().substring(0, warps.length() - 2);
    }

    public void remove(String name) {
        YamlConfiguration configWarps = YamlConfiguration.loadConfiguration(configFileWarps);
        configWarps.set(name, null);
        try {
            configWarps.save(configFileWarps);
        } catch (Exception ignored) {
        }
        warps.remove(name);
        lowerNames.remove(name.toLowerCase());
    }

    public void create(String name, Location loc) {
        YamlConfiguration configWarps = YamlConfiguration.loadConfiguration(configFileWarps);
        configWarps.set(name + ".world", loc.getWorld().getName());
        configWarps.set(name + ".x", loc.getX());
        configWarps.set(name + ".y", loc.getY());
        configWarps.set(name + ".z", loc.getZ());
        configWarps.set(name + ".yaw", loc.getYaw());
        configWarps.set(name + ".pitch", loc.getPitch());
        try {
            configWarps.save(configFileWarps);
        } catch (Exception ignored) {
        }
        warps.put(name, new Warp(name));
        lowerNames.put(name.toLowerCase(), name);
    }
}