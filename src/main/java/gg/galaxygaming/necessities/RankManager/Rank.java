package gg.galaxygaming.necessities.RankManager;

import gg.galaxygaming.necessities.Necessities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;

import java.io.File;
import java.util.ArrayList;

public class Rank {
    private final File configFileRanks = new File("plugins/Necessities/RankManager", "ranks.yml");
    private final File configFileSubranks = new File("plugins/Necessities/RankManager", "subranks.yml");
    private final ArrayList<String> permissions = new ArrayList<>();
    private String title = "", name = "";
    private int maxHomes = 1, tpdelay;
    private Rank previous, next;

    Rank(String name) {
        this.name = name;
        YamlConfiguration configRanks = YamlConfiguration.loadConfiguration(configFileRanks);
        if (configRanks.contains(getName() + ".rankTitle"))
            this.title = configRanks.getString(getName() + ".rankTitle");
        if (configRanks.contains(getName() + ".previousRank")) {
            RankManager rm = Necessities.getRM();
            this.previous = rm.getRank(configRanks.getString(getName() + ".previousRank"));
            this.previous.setNext(this);
        }
        if (configRanks.contains(getName() + ".maxHomes"))
            this.maxHomes = configRanks.getInt(getName() + ".maxHomes");
        else if (this.previous != null)
            this.maxHomes = this.previous.getMaxHomes();
        if (configRanks.contains(getName() + ".teleportDelay"))
            this.tpdelay = configRanks.getInt(getName() + ".teleportDelay");
        else if (this.previous != null)
            this.tpdelay = this.previous.getTpDelay();
        setPerms();
    }

    /**
     * Gets the rank above this one.
     * @return The rank that is above this one.
     */
    public Rank getNext() {
        return this.next;
    }

    /**
     * Sets the rank above this one.
     * @param r The rank to set as the next rank.
     */
    public void setNext(Rank r) {
        this.next = r;
    }

    int getTpDelay() {
        return this.tpdelay;
    }

    private void setPerms() {
        if (this.previous != null)
            this.permissions.addAll(this.previous.getNodes());
        YamlConfiguration configRanks = YamlConfiguration.loadConfiguration(configFileRanks);
        YamlConfiguration configSubranks = YamlConfiguration.loadConfiguration(configFileSubranks);
        for (String subrank : configRanks.getStringList(getName() + ".subranks"))
            if (!subrank.equals("") && configSubranks.contains(subrank))
                this.permissions.addAll(configSubranks.getStringList(subrank));
        this.permissions.addAll(configRanks.getStringList(getName() + ".permissions"));
    }

    void refreshPerms() {
        this.permissions.clear();
        setPerms();
        if (this.next != null)
            this.next.refreshPerms();
    }

    void addPerm(String permission) {
        this.permissions.add(permission);
        refreshPerms();
    }

    void removePerm(String permission) {
        this.permissions.remove(permission);
        refreshPerms();
    }

    /**
     * Gets the maximum number of homes for this rank.
     * @return The maximum number of homes for this rank.
     */
    public int getMaxHomes() {
        return this.maxHomes;
    }

    ArrayList<String> getNodes() {
        return this.permissions;
    }

    /**
     * Gets the name of this rank.
     * @return The name of this rank.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the title for this rank.
     * @return The title for this rank.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Gets the rank below this one.
     * @return The rank that is below this one.
     */
    public Rank getPrevious() {
        return this.previous;
    }

    void setPrevious(Rank r) {
        this.previous = r;
    }

    /**
     * Gets the color of this rank.
     * @return The color of this rank.
     */
    public String getColor() {
        return ChatColor.translateAlternateColorCodes('&', this.title.split("]")[1]).trim();
    }

    /**
     * Gets the list of commands that this rank has access to.
     * @return The list of commands that this rank has access to.
     */
    public String getCommands() {
        YamlConfiguration configRanks = YamlConfiguration.loadConfiguration(configFileRanks);
        YamlConfiguration configSubranks = YamlConfiguration.loadConfiguration(configFileSubranks);
        StringBuilder commandsBuilder = new StringBuilder();
        for (String subrank : configRanks.getStringList(getName() + ".subranks"))
            if (!subrank.equals("") && configSubranks.contains(subrank))
                for (String node : configSubranks.getStringList(subrank))
                    commandsBuilder.append(cmdName(node)).append(", ");
        for (String node : configRanks.getStringList(getName() + ".permissions"))
            commandsBuilder.append(cmdName(node)).append(", ");
        String commands = commandsBuilder.toString();
        return commands.equals("") ? "" : commands.trim().substring(0, commands.length() - 2);
    }

    private String cmdName(String node) {//TODO: finish
        Permission perm = Bukkit.getPluginManager().getPermission(node);
        return perm == null ? node : perm.getName();
    }
}