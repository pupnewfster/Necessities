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
    private int maxHomes = 1, tpdelay = 0;
    private Rank previous, next;

    public Rank(String name) {
        this.name = name;
        YamlConfiguration configRanks = YamlConfiguration.loadConfiguration(configFileRanks);
        if (configRanks.contains(getName() + ".rankTitle"))
            this.title = configRanks.getString(getName() + ".rankTitle");
        if (configRanks.contains(getName() + ".previousRank")) {
            RankManager rm = Necessities.getInstance().getRM();
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

    public Rank getNext() {
        return this.next;
    }

    public void setNext(Rank r) {
        this.next = r;
    }

    int getTpDelay() {
        return this.tpdelay;
    }

    private void setPerms() {
        if (this.previous != null)
            for (String node : this.previous.getNodes())
                this.permissions.add(node);
        YamlConfiguration configRanks = YamlConfiguration.loadConfiguration(configFileRanks);
        YamlConfiguration configSubranks = YamlConfiguration.loadConfiguration(configFileSubranks);
        for (String subrank : configRanks.getStringList(getName() + ".subranks"))
            if (!subrank.equals("") && configSubranks.contains(subrank))
                for (String node : configSubranks.getStringList(subrank))
                    this.permissions.add(node);
        for (String node : configRanks.getStringList(getName() + ".permissions"))
            this.permissions.add(node);
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

    public int getMaxHomes() {
        return this.maxHomes;
    }

    ArrayList<String> getNodes() {
        return this.permissions;
    }

    public String getName() {
        return this.name;
    }

    public String getTitle() {
        return this.title;
    }

    public Rank getPrevious() {
        return this.previous;
    }

    void setPrevious(Rank r) {
        this.previous = r;
    }

    public String getColor() {
        return ChatColor.translateAlternateColorCodes('&', this.title.split("]")[1]).trim();
    }

    public String getCommands() {
        YamlConfiguration configRanks = YamlConfiguration.loadConfiguration(configFileRanks);
        YamlConfiguration configSubranks = YamlConfiguration.loadConfiguration(configFileSubranks);
        String commands = "";
        for (String subrank : configRanks.getStringList(getName() + ".subranks"))
            if (!subrank.equals("") && configSubranks.contains(subrank))
                for (String node : configSubranks.getStringList(subrank))
                    commands += cmdName(node) + ", ";
        for (String node : configRanks.getStringList(getName() + ".permissions"))
            commands += cmdName(node) + ", ";
        return commands.equals("") ? "" : commands.trim().substring(0, commands.length() - 2);
    }

    private String cmdName(String node) {//TODO: finish
        Permission perm = Bukkit.getPluginManager().getPermission(node);
        return perm == null ? node : perm.getName();
    }
}