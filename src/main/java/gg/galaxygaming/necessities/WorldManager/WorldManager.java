package gg.galaxygaming.necessities.WorldManager;

import gg.galaxygaming.necessities.Necessities;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;

public class WorldManager {
    private final HashMap<String, String> invSys = new HashMap<>();
    private final File configFileWM = new File("plugins/Necessities/WorldManager", "worlds.yml");

    /**
     * Initializes the world manager.
     */
    public void initiate() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Loading worlds...");
        YamlConfiguration configWM = YamlConfiguration.loadConfiguration(configFileWM);
        for (String world : configWM.getKeys(false)) {
            if (!worldExists(world) && new File(world + "/level.dat").exists()) {
                WorldCreator creator = new WorldCreator(world);
                if (configWM.contains(world + ".environment"))
                    creator.environment(getEnvironment(configWM.getString(world + ".environment")));
                if (configWM.contains(world + ".type"))
                    creator.type(getType(configWM.getString(world + ".type")));
                if (configWM.contains(world + ".structures"))
                    creator.generateStructures(configWM.getBoolean(world + ".structures"));
                if (configWM.contains(world + ".generator"))
                    creator.generator(configWM.getString(world + ".generator"));
                if (configWM.contains(world + ".inventorySystem"))
                    invSys.put(world.toLowerCase(), configWM.getString(world + ".inventorySystem"));
                Bukkit.createWorld(creator);
            }
            setWorldProps(world);
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "All worlds loaded.");
    }

    /**
     * Retrieves the inventory system for the given world.
     * @param world The name of the world to get the inventory system of.
     * @return The inventory system for the given world.
     */
    public String getSysPath(String world) {
        if (!invSys.containsKey(world.toLowerCase())) {
            setSetting(world, "inventorySystem", "default");
            invSys.put(world.toLowerCase(), "default");
        }
        return invSys.get(world.toLowerCase());
    }

    /**
     * Checks if there is more than one inventory system.
     * @return True if there are multiple inventory systems, false otherwise.
     */
    public boolean multiple() {
        return invSys.values().size() > 1;
    }

    /**
     * Gets the default gamemode for the specified world.
     * @param world The name of the world to get the default gamemode of.
     * @return The default gamemode of the specified world.
     */
    public GameMode getGameMode(String world) {
        YamlConfiguration configWM = YamlConfiguration.loadConfiguration(configFileWM);
        if (configWM.contains(world + ".gamemode")) {
            String gamemode = configWM.getString(world + ".gamemode");
            if (gamemode.equalsIgnoreCase("adventure"))
                return GameMode.ADVENTURE;
            if (gamemode.equalsIgnoreCase("creative"))
                return GameMode.CREATIVE;
            if (gamemode.equalsIgnoreCase("spectator"))
                return GameMode.SPECTATOR;
        }
        return GameMode.SURVIVAL;
    }

    private void setWorldProps(String world) {
        YamlConfiguration configWM = YamlConfiguration.loadConfiguration(configFileWM);
        if (worldExists(world)) {
            World w = Bukkit.getWorld(world);
            if (configWM.contains(world + ".difficulty"))
                w.setDifficulty(getDifficulty(configWM.getString(world + ".difficulty")));
            if (configWM.contains(world + ".pvp"))
                w.setPVP(configWM.getBoolean(world + ".pvp"));
            boolean spawnMonsters = true;
            boolean spawnAnimals = true;
            if (configWM.contains(world + ".spawning.animals"))
                spawnAnimals = configWM.getBoolean(world + ".spawning.animals");
            if (configWM.contains(world + ".spawning.monsters"))
                spawnMonsters = configWM.getBoolean(world + ".spawning.monsters");
            w.setSpawnFlags(spawnMonsters, spawnAnimals);
            if (configWM.contains(world + ".defaultSpawn") && !configWM.getBoolean(world + ".defaultSpawn")) {
                int x = 0;
                int y = 0;
                int z = 0;
                if (configWM.contains(world + ".spawn.x"))
                    x = configWM.getInt(world + ".spawn.x");
                if (configWM.contains(world + ".spawn.y"))
                    y = configWM.getInt(world + ".spawn.y");
                if (configWM.contains(world + ".spawn.z"))
                    z = configWM.getInt(world + ".spawn.z");
                w.setSpawnLocation(x, y, z);
            }
            w.setKeepSpawnInMemory(true);
        }
    }

    /**
     * Gets the difficulty based on the string representation.
     * @param difficulty The string representation of the difficulty.
     * @return The difficulty based on the string representation.
     */
    public Difficulty getDifficulty(String difficulty) {
        if (difficulty.equalsIgnoreCase("easy"))
            return Difficulty.EASY;
        if (difficulty.equalsIgnoreCase("normal"))
            return Difficulty.NORMAL;
        if (difficulty.equalsIgnoreCase("hard"))
            return Difficulty.HARD;
        return Difficulty.PEACEFUL;
    }

    /**
     * Gets the environment based on the string representation.
     * @param environment The string representation of the environment.
     * @return The environment based on the string representation.
     */
    public World.Environment getEnvironment(String environment) {
        if (environment.equalsIgnoreCase("nether"))
            return World.Environment.NETHER;
        if (environment.equalsIgnoreCase("the_end"))
            return World.Environment.THE_END;
        return World.Environment.NORMAL;
    }

    /**
     * Gets the world type based on the string representation.
     * @param type The string representation of the world type.
     * @return The world type based on the string representation.
     */
    public WorldType getType(String type) {
        if (type.equalsIgnoreCase("amplified"))
            return WorldType.AMPLIFIED;
        if (type.equalsIgnoreCase("flat"))
            return WorldType.FLAT;
        if (type.equalsIgnoreCase("large_biomes"))
            return WorldType.LARGE_BIOMES;
        return WorldType.NORMAL;
    }

    /**
     * Unloads the specified world.
     * @param name The name of the world to unload.
     */
    public void unloadWorld(String name) {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        Location spawn = null;
        if (config.contains("Spawn")) {
            World world = Bukkit.getWorld(config.getString("Spawn.world"));
            double x = Double.parseDouble(config.getString("Spawn.x"));
            double y = Double.parseDouble(config.getString("Spawn.y"));
            double z = Double.parseDouble(config.getString("Spawn.z"));
            float yaw = Float.parseFloat(config.getString("Spawn.yaw"));
            float pitch = Float.parseFloat(config.getString("Spawn.pitch"));
            spawn = new Location(world, x, y, z, yaw, pitch);
        }//TODO: fix error of if spawn not set not major though since we have a spawn set
        for (Player p : Bukkit.getWorld(name).getPlayers())
            p.teleport(spawn);
        invSys.remove(name.toLowerCase());
        Bukkit.unloadWorld(name, true);
    }

    /**
     * Loads a specified world.
     * @param name The name of the world to load.
     */
    public void loadWorld(String name) {
        YamlConfiguration configWM = YamlConfiguration.loadConfiguration(configFileWM);
        if (!worldExists(name) && new File(name + "/level.dat").exists()) {
            WorldCreator creator = new WorldCreator(name);
            if (configWM.contains(name + ".environment"))
                creator.environment(getEnvironment(configWM.getString(name + ".environment")));
            if (configWM.contains(name + ".type"))
                creator.type(getType(configWM.getString(name + ".type")));
            if (configWM.contains(name + ".structures"))
                creator.generateStructures(configWM.getBoolean(name + ".structures"));
            if (configWM.contains(name + ".inventorySystem"))
                invSys.put(name.toLowerCase(), configWM.getString(name + ".inventorySystem"));
            creator.generator(configWM.getString(name + ".generator"));
            Bukkit.createWorld(creator);
        }
        setWorldProps(name);
    }

    /**
     * Sets the spawn of a world.
     * @param l The location to make the default spawn location of that world.
     */
    public void setWorldSpawn(Location l) {
        YamlConfiguration configWM = YamlConfiguration.loadConfiguration(configFileWM);
        configWM.set(l.getWorld().getName() + ".defaultSpawn", false);
        configWM.set(l.getWorld().getName() + ".spawn.x", l.getBlockX());
        configWM.set(l.getWorld().getName() + ".spawn.y", l.getBlockY());
        configWM.set(l.getWorld().getName() + ".spawn.z", l.getBlockZ());
        try {
            configWM.save(configFileWM);
        } catch (Exception ignored) {
        }
        l.getWorld().setSpawnLocation(l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }

    /**
     * Sets a specified setting with the given value.
     * @param name    The name of the world to set the setting for.
     * @param setting The setting to set.
     * @param value   The value of the setting.
     */
    public void setSetting(String name, String setting, String value) {
        YamlConfiguration configWM = YamlConfiguration.loadConfiguration(configFileWM);
        configWM.set(name + '.' + setting, value);
        try {
            configWM.save(configFileWM);
        } catch (Exception ignored) {
        }
    }

    /**
     * Sets a specified setting with the given value.
     * @param name    The name of the world to set the setting for.
     * @param setting The setting to set.
     * @param value   The value of the setting.
     */
    public void setSetting(String name, String setting, boolean value) {
        YamlConfiguration configWM = YamlConfiguration.loadConfiguration(configFileWM);
        configWM.set(name + '.' + setting, value);
        try {
            configWM.save(configFileWM);
        } catch (Exception ignored) {
        }
    }

    /**
     * Creates a new world with the specified name, environment, generator, and world type.
     * @param name        The name of the new world.
     * @param environment The environment of the new world.
     * @param generator   The generator to use for the new world.
     * @param type        The type of the new world.
     */
    public void createWorld(String name, World.Environment environment, String generator, WorldType type) {
        YamlConfiguration configWM = YamlConfiguration.loadConfiguration(configFileWM);
        configWM.set(name + ".difficulty", "EASY");
        configWM.set(name + ".gamemode", "SURVIVAL");
        configWM.set(name + ".environment", environment.toString());
        if (generator != null)
            configWM.set(name + ".generator", generator);
        configWM.set(name + ".type", type.getName().toUpperCase());
        configWM.set(name + ".inventorySystem", "default");
        configWM.set(name + ".structures", true);
        configWM.set(name + ".pvp", true);
        configWM.set(name + ".spawning.animals", true);
        configWM.set(name + ".spawning.monsters", true);
        configWM.set(name + ".defaultSpawn", true);
        configWM.set(name + ".spawn.x", 0);
        configWM.set(name + ".spawn.y", 64);
        configWM.set(name + ".spawn.z", 0);
        try {
            configWM.save(configFileWM);
        } catch (Exception ignored) {
        }
        WorldCreator creator = new WorldCreator(name);
        creator.environment(environment);
        creator.type(type);
        creator.generateStructures(true);
        creator.generator(generator);
        Bukkit.createWorld(creator);
        setWorldProps(name);
    }

    /**
     * Removes the specified world from the config.
     * @param name The name of the world to remove.
     */
    public void removeWorld(String name) {
        YamlConfiguration configWM = YamlConfiguration.loadConfiguration(configFileWM);
        configWM.set(name, null);
        try {
            configWM.save(configFileWM);
        } catch (Exception ignored) {
        }
    }

    /**
     * Checks if a world is in the config but is not loaded.
     * @param name The name of the world to check.
     * @return True if the world is not loaded, but is in the config. False otherwise.
     */
    public boolean worldUnloaded(String name) {
        return !worldExists(name) && YamlConfiguration.loadConfiguration(configFileWM).contains(name);
    }

    /**
     * Checks if the world with the given name is loaded.
     * @param name The name of the world to check.
     * @return True if the world is loaded, false otherwise.
     */
    public boolean worldExists(String name) {
        return Bukkit.getWorld(name) != null;
    }
}