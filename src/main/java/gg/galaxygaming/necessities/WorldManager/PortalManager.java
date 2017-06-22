package gg.galaxygaming.necessities.WorldManager;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import gg.galaxygaming.necessities.Necessities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(Necessities.getInstance(), "BungeeCord");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "All portals loaded.");
    }

    /**
     * Teleports the given player to the destination of the portal at the given location.
     * @param l      The location to check if it is a portal and if so retrieve its destination.
     * @param player The player to teleport.
     */
    public void portalDestination(Location l, Player player) {
        for (Map.Entry<String, Portal> stringPortalEntry : portals.entrySet()) {
            Portal p = stringPortalEntry.getValue();
            if (p.isPortal(l)) {
                if (p.isWarp())
                    player.teleport(p.getWarp().getDestination());
                else if (p.isCrossServer()) {
                    YamlConfiguration config = Necessities.getInstance().getConfig();
                    if (config.contains("Spawn")) {
                        World world = Bukkit.getWorld(config.getString("Spawn.world"));
                        double x = Double.parseDouble(config.getString("Spawn.x"));
                        double y = Double.parseDouble(config.getString("Spawn.y"));
                        double z = Double.parseDouble(config.getString("Spawn.z"));
                        float yaw = Float.parseFloat(config.getString("Spawn.yaw"));
                        float pitch = Float.parseFloat(config.getString("Spawn.pitch"));
                        player.teleport(new Location(world, x, y, z, yaw, pitch));
                    } else
                        player.teleport(player.getWorld().getSpawnLocation());//Teleport them back to spawn
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("Connect");
                    out.writeUTF(p.getServer());
                    player.sendPluginMessage(Necessities.getInstance(), "BungeeCord", out.toByteArray());
                } else
                    player.teleport(p.getWorldTo().getSpawnLocation());
            }
        }
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