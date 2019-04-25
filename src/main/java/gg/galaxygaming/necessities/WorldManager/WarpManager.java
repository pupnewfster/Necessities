package gg.galaxygaming.necessities.WorldManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

public class WarpManager {

    private final File configFileWarps = new File("plugins/Necessities/WorldManager", "warps.yml");
    private final Map<String, String> lowerNames = new HashMap<>();
    private final Map<String, Warp> warps = new HashMap<>();

    /**
     * Initializes the warp manager.
     */
    public void initiate() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Loading warps...");
        YamlConfiguration configWarps = YamlConfiguration.loadConfiguration(configFileWarps);
        for (String warp : configWarps.getKeys(false)) {
            warps.put(warp, new Warp(warp));
            lowerNames.put(warp.toLowerCase(), warp);
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "All warps loaded.");
    }

    /**
     * Checks if a warp with the given name exists.
     *
     * @param name The name to check.
     * @return True if the warp exists, false otherwise.
     */
    public boolean isWarp(String name) {
        return lowerNames.containsKey(name.toLowerCase());
    }

    /**
     * Retrieves the warp with the specified name.
     *
     * @param name The name to search for.
     * @return The warp with the specified name.
     */
    public Warp getWarp(String name) {
        return !isWarp(name) ? null : warps.get(lowerNames.get(name.toLowerCase()));
    }

    /**
     * Gets the list of warps.
     *
     * @return The list of warps in string form separated by commas.
     */
    public String getWarps() {
        List<String> ws = new ArrayList<>(warps.keySet());
        Collections.sort(ws);
        StringBuilder warpsBuilder = new StringBuilder();
        for (String w : ws) {
            warpsBuilder.append(w).append(", ");
        }
        String warps = warpsBuilder.toString();
        return warps.equals("") ? "" : warps.trim().substring(0, warps.length() - 2);
    }

    /**
     * Removes the warp with the specified name.
     *
     * @param name The name of the warp to remove.
     */
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

    /**
     * Creates a warp with the specified name and location.
     *
     * @param name The name of the warp to create.
     * @param loc The destination of the warp.
     */
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