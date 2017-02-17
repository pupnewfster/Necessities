package gg.galaxygaming.necessities.WorldManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class PortalManager {//TODO: add a method to update the things when a world is unloaded or loaded
    private final HashMap<String, Portal> portals = new HashMap<>();
    private final HashMap<String, String> lowerNames = new HashMap<>();
    private final File configFilePM = new File("plugins/Necessities/WorldManager", "portals.yml");

    /**
     * Initiates the portal manager.
     */
    public void initiate() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Loading portals...");
        YamlConfiguration configPM = YamlConfiguration.loadConfiguration(configFilePM);
        for (String portal : configPM.getKeys(false)) {
            portals.put(portal, new Portal(portal));
            lowerNames.put(portal.toLowerCase(), portal);
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "All portals loaded.");
    }

    /**
     * Gets the destination of the portal at the given location.
     * @param l The location to check if it is a portal and if so retrieve its destination.
     * @return The destination of the portal.
     */
    public Location portalDestination(Location l) {
        for (String key : portals.keySet())
            if (portals.get(key).isPortal(l)) {
                if (portals.get(key).isWarp())
                    return portals.get(key).getWarp().getDestination();
                return portals.get(key).getWorldTo().getSpawnLocation();
            }
        return null;
    }

    /**
     * Checks if a portal exists with the given name.
     * @param name The name of the portal.
     * @return True if there is a portal with the given name, false otherwise.
     */
    public boolean exists(String name) {
        return lowerNames.containsKey(name.toLowerCase());
    }

    /**
     * Removes a specified portal.
     * @param name The name of the portal to remove.
     */
    public void remove(String name) {
        YamlConfiguration configPM = YamlConfiguration.loadConfiguration(configFilePM);
        configPM.set(name, null);
        try {
            configPM.save(configFilePM);
        } catch (Exception ignored) {
        }
        portals.remove(name);
        lowerNames.remove(name.toLowerCase());
    }

    /**
     * Creates a new portal wth the specified name and bounds with the given destination and.
     * @param name        The name of the portal.
     * @param destination The destination of the portal.
     * @param left        The first boundary of the portal.
     * @param right       The second boundary of the portal.
     */
    public void create(String name, String destination, Location left, Location right) {
        YamlConfiguration configPM = YamlConfiguration.loadConfiguration(configFilePM);
        configPM.set(name + ".world", left.getWorld().getName());
        configPM.set(name + ".destination", destination);
        configPM.set(name + ".location.x1", left.getBlockX());
        configPM.set(name + ".location.y1", left.getBlockY());
        configPM.set(name + ".location.z1", left.getBlockZ());
        configPM.set(name + ".location.x2", right.getBlockX());
        configPM.set(name + ".location.y2", right.getBlockY());
        configPM.set(name + ".location.z2", right.getBlockZ());
        try {
            configPM.save(configFilePM);
        } catch (Exception ignored) {
        }
        portals.put(name, new Portal(name));
        lowerNames.put(name.toLowerCase(), name);
    }
} 