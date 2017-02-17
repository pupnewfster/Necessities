package gg.galaxygaming.necessities;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;

class Wrenched {
    private final ArrayList<String> locations = new ArrayList<>();
    private final File configFileWrench = new File("plugins/Necessities", "wrenched.yml");

    /**
     * Loads previously wrenched locations.
     */
    public void initiate() {
        YamlConfiguration wrenches = YamlConfiguration.loadConfiguration(configFileWrench);
        for (String key : wrenches.getKeys(false))
            if (!locations.contains(key.toLowerCase()))
                locations.add(key.toLowerCase());
    }

    void wrench(Block b) {
        YamlConfiguration wrenches = YamlConfiguration.loadConfiguration(configFileWrench);
        Location l = b.getLocation();
        String blockForm = (l.getWorld().getName() + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ()).toLowerCase();
        if (locations.contains(blockForm)) {
            locations.remove(blockForm);
            wrenches.set(blockForm, null);
        } else {
            locations.add(blockForm);
            wrenches.set(blockForm, true);
        }
        try {
            wrenches.save(configFileWrench);
        } catch (Exception ignored) {
        }
    }

    boolean isWrenched(Block b) {
        Location l = b.getLocation();
        return locations.contains((l.getWorld().getName() + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ()).toLowerCase());
    }
}