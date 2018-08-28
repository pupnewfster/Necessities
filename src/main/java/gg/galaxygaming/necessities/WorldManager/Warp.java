package gg.galaxygaming.necessities.WorldManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Warp {
    private final String name;
    private Location loc;

    Warp(String name) {
        File configFileWarps = new File("plugins/Necessities/WorldManager", "warps.yml");
        YamlConfiguration configWarps = YamlConfiguration.loadConfiguration(configFileWarps);
        this.name = name;
        if (configWarps.contains(this.name))
            this.loc = new Location(Bukkit.getWorld(configWarps.getString(this.name + ".world")), Double.parseDouble(configWarps.getString(this.name + ".x")),
                    Double.parseDouble(configWarps.getString(this.name + ".y")), Double.parseDouble(configWarps.getString(this.name + ".z")),
                    Float.parseFloat(configWarps.getString(this.name + ".yaw")), Float.parseFloat(configWarps.getString(this.name + ".pitch")));
    }

    /**
     * Gets the name of the warp.
     * @return The name of the warp.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves if this warp has a destination.
     * @return True if there is a destination to the portal, false otherwise.
     */
    public boolean hasDestination() {
        return this.loc != null;
    }

    /**
     * Retrieves the location of the warp.
     * @return The location of the warp.
     */
    public Location getDestination() {
        return this.loc;
    }
}