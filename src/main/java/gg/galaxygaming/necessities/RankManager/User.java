package gg.galaxygaming.necessities.RankManager;

import com.google.common.io.Files;
import gg.galaxygaming.necessities.Commands.CmdHide;
import gg.galaxygaming.necessities.Guilds.Guild;
import gg.galaxygaming.necessities.Hats.Hat;
import gg.galaxygaming.necessities.Hats.HatType;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.util.BlockIterator;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class User {
    private final File configFileSubranks = new File("plugins/Necessities/RankManager", "subranks.yml");
    private final File configFileUsers = new File("plugins/Necessities/RankManager", "users.yml");
    private boolean teleporting = false, jailed = false, opChat = false, afk = false, istpaing = false, god = false, muted = false, autoClaiming = false, guildChat = false, slackChat = false, frozen = false;
    private final ArrayList<String> permissions = new ArrayList<>();
    private final ArrayList<String> subranks = new ArrayList<>();
    private long lastAction = 0, lastAFK = 0, lastRequest = 0, login = 0;
    private int pastTotal = 0, lastActionTask = 0, afkTask = 0;
    private final HashMap<String, Location> homes = new HashMap<>();
    private String appended = "", nick = null, lastContact, prefix = "";
    private final ArrayList<UUID> ignored = new ArrayList<>();
    private Location lastPos, right, left;
    private PermissionAttachment attachment;
    private Player bukkitPlayer;
    private double power = 0.0;
    private Hat hat = null;
    private final UUID userUUID;
    private Guild guild;
    private Rank rank;

    User(Player p) {
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        YamlConfiguration config = Necessities.getInstance().getConfig();
        RankManager rm = Necessities.getRM();
        this.bukkitPlayer = p;
        this.right = p.getLocation();
        this.left = p.getLocation();
        this.userUUID = this.bukkitPlayer.getUniqueId();
        if (configUsers.contains(getUUID().toString() + ".rank"))
            this.rank = rm.getRank(configUsers.getString(getUUID().toString() + ".rank"));
        if (configUsers.contains(getUUID().toString() + ".nick"))
            this.nick = ChatColor.translateAlternateColorCodes('&', configUsers.getString(getUUID().toString() + ".nick"));
        if (this.nick != null && !this.nick.startsWith("~"))
            this.nick = "~" + this.nick;
        if (configUsers.contains(getUUID().toString() + ".jailed"))
            this.jailed = configUsers.getBoolean(getUUID().toString() + ".jailed");
        if (configUsers.contains(getUUID().toString() + ".afk"))
            this.afk = configUsers.getBoolean(getUUID().toString() + ".afk");
        if (configUsers.contains(getUUID().toString() + ".muted"))
            this.muted = configUsers.getBoolean(getUUID().toString() + ".muted");
        if (configUsers.contains(getUUID().toString() + ".frozen"))
            this.frozen = configUsers.getBoolean(getUUID().toString() + ".frozen");
        if (configUsers.contains(getUUID().toString() + ".power"))
            this.power = configUsers.getDouble(getUUID().toString() + ".power");
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") && configUsers.contains(getUUID().toString() + ".guild"))
            this.guild = Necessities.getGM().getGuild(configUsers.getString(getUUID().toString() + ".guild"));
        if (configUsers.contains(getUUID().toString() + ".timePlayed"))
            this.pastTotal = configUsers.getInt(getUUID().toString() + ".timePlayed");
        if (configUsers.contains(getUUID().toString() + ".hat"))
            this.hat = Hat.fromType(HatType.fromString(configUsers.getString(getUUID().toString() + ".hat")), this.bukkitPlayer.getLocation());
        if (configUsers.contains(getUUID().toString() + ".location"))
            this.lastPos = new Location(Bukkit.getWorld(configUsers.getString(getUUID().toString() + ".location.world")),
                    Double.parseDouble(configUsers.getString(getUUID().toString() + ".location.x")), Double.parseDouble(configUsers.getString(getUUID().toString() + ".location.y")),
                    Double.parseDouble(configUsers.getString(getUUID().toString() + ".location.z")), Float.parseFloat(configUsers.getString(getUUID().toString() + ".location.yaw")),
                    Float.parseFloat(configUsers.getString(getUUID().toString() + ".location.pitch")));
        this.login = System.currentTimeMillis();
        readHomes();
        readIgnored();
        updateListName();
        Necessities.getInstance().updateAll(this.bukkitPlayer);
        CmdHide hide = Necessities.getHide();
        if (hide.isHidden(this.bukkitPlayer)) {
            this.opChat = true;
            hide.hidePlayer(this.bukkitPlayer);
        }
    }

    User(UUID uuid) {
        this.userUUID = uuid;
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        YamlConfiguration config = Necessities.getInstance().getConfig();
        RankManager rm = Necessities.getRM();
        if (configUsers.contains(getUUID().toString() + ".rank"))
            this.rank = rm.getRank(configUsers.getString(getUUID().toString() + ".rank"));
        for (String subrank : configUsers.getStringList(uuid + ".subranks"))
            if (!subrank.equals(""))
                this.subranks.add(subrank);
        for (String node : configUsers.getStringList(uuid + ".permissions"))
            if (!node.equals(""))
                this.permissions.add(node);
        if (configUsers.contains(getUUID().toString() + ".nick"))
            this.nick = ChatColor.translateAlternateColorCodes('&', configUsers.getString(getUUID().toString() + ".nick"));
        if (this.nick != null && !this.nick.startsWith("~"))
            this.nick = "~" + this.nick;
        if (configUsers.contains(getUUID().toString() + ".power"))
            this.power = configUsers.getDouble(getUUID().toString() + ".power");
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") && configUsers.contains(getUUID().toString() + ".guild"))
            this.guild = Necessities.getGM().getGuild(configUsers.getString(getUUID().toString() + ".guild"));
        if (configUsers.contains(getUUID().toString() + ".timePlayed"))
            this.pastTotal = configUsers.getInt(getUUID().toString() + ".timePlayed");
        readHomes();
        readIgnored();
    }

    void updateTimePlayed() {
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        if (this.login != 0) {
            configUsers.set(getUUID().toString() + ".timePlayed", (int) (this.pastTotal + (System.currentTimeMillis() - this.login) / 1000));
            try {
                configUsers.save(configFileUsers);
            } catch (Exception ignored) {
            }
        }
        this.pastTotal = 0;
        this.login = 0;
    }

    /**
     * Called when the user logs out.
     */
    public void logOut() {
        updateTimePlayed();
        Necessities.getSBs().delPlayer(this);
        if (this.hat != null)
            this.hat.despawn();
        this.bukkitPlayer = null;
    }

    private void readIgnored() {
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        if (!configUsers.contains(getUUID().toString()))
            return;
        if (configUsers.contains(getUUID().toString() + ".ignored")) {
            for (String name : configUsers.getStringList(getUUID().toString() + ".ignored"))
                if (!name.equals(""))
                    this.ignored.add(UUID.fromString(name));
        } else {
            configUsers.set(getUUID().toString() + ".ignored", Collections.singletonList(""));
            try {
                configUsers.save(configFileUsers);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Checks if the current user is ignoring the player with the specified uuid.
     * @param uuid The uuid of the player to check.
     * @return True if the user is ignoring the specified player, false otherwise.
     */
    public boolean isIgnoring(UUID uuid) {
        return this.ignored.contains(uuid);
    }

    /**
     * Stars ignoring the specified player.
     * @param uuid The uuid of the player to start ignoring.
     */
    public void ignore(UUID uuid) {
        if (!this.ignored.contains(uuid)) {//this should already be checked but whatever
            this.ignored.add(uuid);
            YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
            if (!configUsers.contains(getUUID().toString()))
                return;
            List<String> ign = configUsers.getStringList(uuid.toString() + ".ignored");
            if (ign.contains(""))
                ign.remove("");
            ign.add(uuid.toString());
            configUsers.set(uuid.toString() + ".ignored", ign);
            try {
                configUsers.save(configFileUsers);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Stops ignoring the specified player.
     * @param uuid The uuid of the player to stop ignoring.
     */
    public void unignore(UUID uuid) {
        if (this.ignored.contains(uuid)) {//this should already be checked but whatever
            this.ignored.remove(uuid);
            YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
            if (!configUsers.contains(getUUID().toString()))
                return;
            List<String> ign = configUsers.getStringList(uuid.toString() + ".ignored");
            ign.remove(uuid.toString());
            if (ign.isEmpty())
                ign.add("");
            configUsers.set(uuid.toString() + ".ignored", ign);
            try {
                configUsers.save(configFileUsers);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Leaves the current guild.
     */
    public void leaveGuild() {
        this.guild = null;
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        if (!configUsers.contains(getUUID().toString()))
            return;
        configUsers.set(getUUID().toString() + ".guild", null);
        try {
            configUsers.save(configFileUsers);
        } catch (Exception ignored) {
        }
    }

    /**
     * Gets the text that the user has appended.
     * @return The text the user has appended.
     */
    public String getAppended() {
        return this.appended;
    }

    /**
     * Sets the text that the user has appended.
     * @param toAppend The text to set as appended.
     */
    public void setAppended(String toAppend) {
        this.appended = toAppend;
    }

    /**
     * Joins the given guild.
     * @param g The guild to join.
     */
    public void joinGuild(Guild g) {
        this.guild = g;
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        if (!configUsers.contains(getUUID().toString()))
            return;
        configUsers.set(getUUID().toString() + ".guild", g.getName());
        try {
            configUsers.save(configFileUsers);
        } catch (Exception ignored) {
        }
    }

    /**
     * Gets the power this user has.
     * @return The power this user has.
     */
    public double getPower() {
        return this.power;
    }

    /**
     * Teleports to the given user.
     * @param toTpTo The user to teleport to.
     */
    public void teleport(final User toTpTo) {
        this.teleporting = true;
        if (this.rank.getTpDelay() == 0) {
            if (isTpaing())
                setTpaing(false);
            getPlayer().teleport(toTpTo.getPlayer());
            tpSuccess();
            return;
        }
        Variables var = Necessities.getVar();
        this.bukkitPlayer.sendMessage(var.getMessages() + "Teleportation will begin in " + ChatColor.RED + this.rank.getTpDelay() + var.getMessages() + " seconds, don't move.");
        Bukkit.getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), () -> {
            if (toTpTo.getPlayer() != null && getPlayer() != null && isTeleporting()) {
                if (isTpaing())
                    setTpaing(false);
                getPlayer().teleport(toTpTo.getPlayer());
                tpSuccess();
            }
        }, 20 * this.rank.getTpDelay());
    }

    /**
     * Called when the user moves during teleportation.
     */
    public void cancelTp() {
        this.teleporting = false;
        setTpaing(false);
        getPlayer().sendMessage(Necessities.getVar().getMessages() + "Teleportation canceled.");
    }

    private void tpSuccess() {
        this.teleporting = false;
        getPlayer().sendMessage(Necessities.getVar().getMessages() + "Teleportation successful.");
    }

    /**
     * Teleports to the given location.
     * @param l The location to teleport to.
     */
    public void teleport(final Location l) {
        this.teleporting = true;
        if (this.rank.getTpDelay() == 0) {
            getPlayer().teleport(l);
            tpSuccess();
            return;
        }
        Variables var = Necessities.getVar();
        this.bukkitPlayer.sendMessage(var.getMessages() + "Teleportation will begin in " + ChatColor.RED + this.rank.getTpDelay() + var.getMessages() + " seconds, don't move.");
        Bukkit.getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), () -> {
            if (getPlayer() != null && isTeleporting()) {
                getPlayer().teleport(l);
                tpSuccess();
            }
        }, 20 * this.rank.getTpDelay());
    }

    private boolean isTpaing() {
        return this.istpaing;
    }

    /**
     * Sets whether or not the user is tpaing.
     * @param tpaing True if they are tpaing, false otherwise.
     */
    public void setTpaing(boolean tpaing) {
        this.istpaing = tpaing;
    }

    private long getLastAction() {
        return this.lastAction;
    }

    /**
     * Sets the time that the player last performed an action.
     * @param time The time to set.
     */
    public void setLastAction(long time) {
        this.lastAction = time;
        int temp = this.lastActionTask;
        if (getPlayer() != null && getPlayer().hasPermission("Necessities.afk"))
            try {
                this.lastActionTask = Bukkit.getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), () -> {
                    if (!isAfk() && getPlayer() != null && (System.currentTimeMillis() - getLastAction()) / 1000.0 >= 299.9)
                        setAfk(true);
                }, 20 * 300);
                Bukkit.getScheduler().cancelTask(temp);
            } catch (Exception ignored) {
            }
    }

    /**
     * Checks if the user is currently teleporting.
     * @return True if the user is teleporting, false otherwise.
     */
    public boolean isTeleporting() {
        return this.teleporting;
    }

    /**
     * Gets the uuid of the user.
     * @return The uuid of the user.
     */
    public UUID getUUID() {
        return this.bukkitPlayer == null ? this.userUUID : this.bukkitPlayer.getUniqueId();
    }

    /**
     * Gets the name of the user.
     * @return The name of the user.
     */
    public String getName() {
        return this.bukkitPlayer == null ? Bukkit.getOfflinePlayer(this.userUUID).getName() : this.bukkitPlayer.getName();
    }

    /**
     * Gets the last time this user requested a mod.
     * @return The last time this user requested a mod.
     */
    public long getLastRequest() {
        return this.lastRequest == 0 ? System.currentTimeMillis() : this.lastRequest;
    }

    /**
     * Sets the time that the user last requested a mod.
     * @param time The time that the user last requested a mod.
     */
    public void setLastRequest(long time) {
        this.lastRequest = time;
    }

    /**
     * Gets the user's rank.
     * @return The user's rank.
     */
    public Rank getRank() {
        return this.rank;
    }

    /**
     * Sets the user's rank to the specified rank.
     * @param r The rank to set the user at.
     */
    public void setRank(Rank r) {
        this.rank = r;
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        if (!configUsers.contains(getUUID().toString()))
            return;
        configUsers.set(getUUID().toString() + ".rank", r.getName());
        try {
            configUsers.save(configFileUsers);
        } catch (Exception ignored) {
        }
        refreshPerms();
    }

    /**
     * Gets the user's nickname.
     * @return The user's nickname.
     */
    public String getNick() {
        return this.nick;
    }

    /**
     * Sets the user's nickname.
     * @param message The new nickname of the user.
     */
    public void setNick(String message) {
        this.nick = message != null ? ChatColor.translateAlternateColorCodes('&', message) : null;
        updateListName();
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        if (!configUsers.contains(getUUID().toString()))
            return;
        configUsers.set(getUUID().toString() + ".nick", message);
        try {
            configUsers.save(configFileUsers);
        } catch (Exception ignored) {
        }
    }

    /**
     * Gets the last location of the user.
     * @return The last location of the user.
     */
    public Location getLastPos() {
        return this.lastPos;
    }

    /**
     * Sets the last location of the user.
     * @param l The location to set as the last location.
     */
    public void setLastPos(Location l) {
        this.lastPos = l;
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        if (!configUsers.contains(getUUID().toString()))
            return;
        configUsers.set(getUUID().toString() + ".location.world", l.getWorld().getName());
        configUsers.set(getUUID().toString() + ".location.x", l.getX());
        configUsers.set(getUUID().toString() + ".location.y", l.getY());
        configUsers.set(getUUID().toString() + ".location.z", l.getZ());
        configUsers.set(getUUID().toString() + ".location.yaw", l.getYaw());
        configUsers.set(getUUID().toString() + ".location.pitch", l.getPitch());
        try {
            configUsers.save(configFileUsers);
        } catch (Exception ignored) {
        }
    }

    /**
     * Gets the user's hat.
     * @return The user's hat.
     */
    public Hat getHat() {
        return this.hat;
    }

    /**
     * Respawns the user's hat.
     */
    public void respawnHat() {
        if (this.hat == null || this.bukkitPlayer == null)
            return;
        this.hat.despawn();
        this.hat = Hat.fromType(this.hat.getType(), this.bukkitPlayer.getLocation());
    }

    /**
     * Sets the user's hat.
     * @param hat The hat to set as the user's hat.
     */
    public void setHat(Hat hat) {
        if (this.hat != null)
            this.hat.despawn();
        this.hat = hat;
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        if (!configUsers.contains(getUUID().toString()))
            return;
        configUsers.set(getUUID().toString() + ".hat", this.hat == null ? null : this.hat.getType().getName());
        try {
            configUsers.save(configFileUsers);
        } catch (Exception ignored) {
        }
    }

    /**
     * Checks if the user is jailed.
     * @return True if the user is jailed, false otherwise.
     */
    public boolean isJailed() {
        return this.jailed;
    }

    /**
     * Jails or frees a user.
     * @param jail True to jail the user, false to free the user.
     */
    public void setJailed(boolean jail) {
        this.jailed = jail;
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        if (!configUsers.contains(getUUID().toString()))
            return;
        configUsers.set(getUUID().toString() + ".jailed", jail);
        try {
            configUsers.save(configFileUsers);
        } catch (Exception ignored) {
        }
    }

    /**
     * Updates the user's tab list name.
     */
    public void updateListName() {
        if (this.bukkitPlayer == null)
            return;
        this.bukkitPlayer.setDisplayName(getRank().getColor() + (this.nick == null ? this.bukkitPlayer.getName() : this.nick));
        Necessities.getInstance().updateName(this.bukkitPlayer);
    }

    /**
     * Checks if the user is muted.
     * @return True if the user is muted, false otherwise.
     */
    public boolean isMuted() {
        return this.muted;
    }

    /**
     * Mutes or unmutes the user.
     * @param toMute True to mute the user, false to unmute.
     */
    public void setMuted(boolean toMute) {
        this.muted = toMute;
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        if (!configUsers.contains(getUUID().toString()))
            return;
        configUsers.set(getUUID().toString() + ".muted", this.muted);
        try {
            configUsers.save(configFileUsers);
        } catch (Exception ignored) {
        }
    }

    /**
     * Checks if the user is frozen.
     * @return True if the user is frozen, false otherwise.
     */
    public boolean isFrozen() {
        return this.frozen;
    }

    /**
     * Freezes or defrosts the user.
     * @param toFreeze True to mute the user, false to defrost.
     */
    public void setFrozen(boolean toFreeze) {
        this.frozen = toFreeze;
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        if (!configUsers.contains(getUUID().toString()))
            return;
        configUsers.set(getUUID().toString() + ".frozen", this.frozen);
        try {
            configUsers.save(configFileUsers);
        } catch (Exception ignored) {
        }
    }

    /**
     * Sets whether or not the user is in god mode.
     * @param godMode True to make the user a god, false otherwise.
     */
    public void setGod(boolean godMode) {
        this.god = godMode;
    }

    /**
     * Checks if the user is in god mode.
     * @return True if the user is in god mode, false otherwise.
     */
    public boolean godMode() {
        return this.god;
    }

    /**
     * Checks if the user is afk.
     * @return True if the user is afk, false otherwise.
     */
    public boolean isAfk() {
        return this.afk;
    }

    /**
     * Sets the user as afk.
     * @param isAfk True to set the user as afk, false otherwise.
     */
    public void setAfk(boolean isAfk) {
        this.afk = isAfk;
        if (getPlayer() != null) {
            this.bukkitPlayer.setSleepingIgnored(this.afk);
            Variables var = Necessities.getVar();
            if (Necessities.getHide().isHidden(this.bukkitPlayer))
                Bukkit.broadcast(var.getMessages() + "To Ops - " + var.getMe() + "*" + getRank().getColor() + getPlayer().getDisplayName() + var.getMe() + " is " + (isAfk() ? "now" : "no longer") + " AFK",
                        "Necessities.opBroadcast");
            else
                Bukkit.broadcastMessage(var.getMe() + "*" + getRank().getColor() + getPlayer().getDisplayName() + var.getMe() + " is " + (isAfk() ? "now" : "no longer") + " AFK");
        }
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        if (!configUsers.contains(getUUID().toString()))
            return;
        configUsers.set(getUUID().toString() + ".afk", isAfk);
        try {
            configUsers.save(configFileUsers);
        } catch (Exception ignored) {
        }
        this.lastAFK = System.currentTimeMillis();
        if (!getPlayer().hasPermission("Necessities.afkkickimune")) {
            if (isAfk()) {
                int temp = this.afkTask;
                try {
                    this.afkTask = Bukkit.getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), () -> {
                        if (isAfk() && getPlayer() != null && (System.currentTimeMillis() - getLastAFK()) / 1000.0 >= 299.9)
                            bukkitPlayer.kickPlayer(ChatColor.RED + "AFK for too long!");
                    }, 20 * 300);
                    Bukkit.getScheduler().cancelTask(temp);
                } catch (Exception ignored) {
                }
            } else
                Bukkit.getScheduler().cancelTask(this.afkTask);
        }
    }

    private long getLastAFK() {
        return this.lastAFK;
    }

    /**
     * Gets the last contact of this user.
     * @return The last contact of this user.
     */
    public String getLastC() {
        return this.lastContact;
    }

    /**
     * Sets the last contact this user had.
     * @param last The last contact this user had.
     */
    public void setLastC(String last) {
        this.lastContact = last;
    }

    void givePerms() {
        Necessities.getSBs().addPlayer(this);
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        YamlConfiguration configSubranks = YamlConfiguration.loadConfiguration(configFileSubranks);
        this.attachment = this.bukkitPlayer.addAttachment(Necessities.getInstance());
        this.rank.getNodes().forEach(this::setPerm);
        for (String subrank : configUsers.getStringList(getUUID().toString() + ".subranks")) {
            if (!subrank.equals("") && configSubranks.contains(subrank)) {
                this.subranks.add(subrank);
                configSubranks.getStringList(subrank).forEach(this::setPerm);
            }
        }
        for (String node : configUsers.getStringList(getUUID().toString() + ".permissions")) {
            if (!node.equals(""))
                this.permissions.add(node);
            setPerm(node);
        }
    }

    private void setPerm(String node) {
        if (node.equals("") || node.equals("-*"))
            return;
        if (node.startsWith("-"))
            this.attachment.setPermission(node.replaceFirst("-", ""), false);
        else
            this.attachment.setPermission(node, true);
    }

    void updateRank(Rank r) {
        this.rank = r;
        if (this.bukkitPlayer != null)
            refreshPerms();
    }

    private void readHomes() {
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        if (!configUsers.contains(getUUID().toString()))
            return;
        if (configUsers.contains(getUUID().toString() + ".homeslist"))
            for (String home : configUsers.getStringList(getUUID().toString() + ".homeslist"))
                if (!home.equals(""))
                    this.homes.put(home, new Location(Bukkit.getWorld(configUsers.getString(getUUID().toString() + ".homes." + home + ".world")),
                            Double.parseDouble(configUsers.getString(getUUID().toString() + ".homes." + home + ".x")),
                            Double.parseDouble(configUsers.getString(getUUID().toString() + ".homes." + home + ".y")),
                            Double.parseDouble(configUsers.getString(getUUID().toString() + ".homes." + home + ".z")),
                            Float.parseFloat(configUsers.getString(getUUID().toString() + ".homes." + home + ".yaw")),
                            Float.parseFloat(configUsers.getString(getUUID().toString() + ".homes." + home + ".pitch"))));
    }

    /**
     * Adds a home at the specified location with the given name.
     * @param l    The location of the home.
     * @param name The name of the home.
     */
    public void addHome(Location l, String name) {
        name = name.toLowerCase().trim();
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        if (!configUsers.contains(getUUID().toString()))
            return;
        List<String> homelist = configUsers.getStringList(getUUID().toString() + ".homeslist");
        if (homelist.isEmpty()) {
            configUsers.set(getUUID().toString() + ".homeslist", Collections.singletonList(name));
        } else {
            if (homelist.contains(""))
                homelist.remove("");
            if (!homelist.contains(name))
                homelist.add(name);
            configUsers.set(getUUID().toString() + ".homeslist", homelist);
        }
        configUsers.set(getUUID().toString() + ".homes." + name + ".world", l.getWorld().getName());
        configUsers.set(getUUID().toString() + ".homes." + name + ".x", Double.toString(l.getX()));
        configUsers.set(getUUID().toString() + ".homes." + name + ".y", Double.toString(l.getY()));
        configUsers.set(getUUID().toString() + ".homes." + name + ".z", Double.toString(l.getZ()));
        configUsers.set(getUUID().toString() + ".homes." + name + ".yaw", Float.toString(l.getYaw()));
        configUsers.set(getUUID().toString() + ".homes." + name + ".pitch", Float.toString(l.getPitch()));
        try {
            configUsers.save(configFileUsers);
        } catch (Exception ignored) {
        }
        this.homes.put(name, l);
    }

    /**
     * Removes the home with the specified name.
     * @param name The name of the home to remove.
     */
    public void delHome(String name) {
        name = name.toLowerCase().trim();
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        if (!configUsers.contains(getUUID().toString()))
            return;
        List<String> homelist = configUsers.getStringList(getUUID().toString() + ".homeslist");
        homelist.remove(name);
        if (homelist.isEmpty())
            homelist.add("");
        configUsers.set(getUUID().toString() + ".homeslist", homelist);
        configUsers.set(getUUID().toString() + ".homes." + name, null);
        try {
            configUsers.save(configFileUsers);
        } catch (Exception ignored) {
        }
        this.homes.remove(name);
    }

    /**
     * Gets the list of homes this user has.
     * @return The list of homes this user has.
     */
    public String getHomes() {
        if (this.homes.isEmpty())
            return "";
        String homeslist;
        ArrayList<String> sortHomes = new ArrayList<>();
        sortHomes.addAll(this.homes.keySet());
        Collections.sort(sortHomes);
        StringBuilder homeslistBuilder = new StringBuilder();
        for (String name : sortHomes)
            homeslistBuilder.append(name).append(", ");
        homeslist = homeslistBuilder.toString();
        return homeslist.trim().substring(0, homeslist.length() - 2);
    }

    /**
     * Checks if the user has a home with the specified name.
     * @param name The name to check.
     * @return True if the user has a home with the given name, false otherwise.
     */
    public boolean hasHome(String name) {
        return this.homes.containsKey(name.toLowerCase().trim());
    }

    /**
     * Gets the location of the home with the given name.
     * @param name The name of the home to get.
     * @return The location of the home with the given name.
     */
    public Location getHome(String name) {
        return this.homes.get(name.toLowerCase().trim());
    }

    /**
     * Returns the number of homes this user has.
     * @return The number of homes this user has.
     */
    public int homeCount() {
        return this.homes.size();
    }

    void refreshPerms() {
        this.subranks.clear();
        this.permissions.clear();
        removePerms();
        givePerms();
        updateListName();
    }

    void removePerms() {
        this.attachment.getPermissions().keySet().forEach(p -> this.attachment.unsetPermission(p));
    }

    /**
     * Gets the list of subranks this user has.
     * @return The list of subranks this user has.
     */
    public String getSubranks() {
        String sublist;
        if (this.subranks.isEmpty())
            return null;
        StringBuilder sublistBuilder = new StringBuilder();
        for (String sub : this.subranks)
            sublistBuilder.append(sub).append(", ");
        sublist = sublistBuilder.toString();
        return sublist.trim().substring(0, sublist.length() - 2);
    }

    /**
     * Gets the list of permissions this user has.
     * @return The list of permissions this user has.
     */
    public String getPermissions() {
        String permlist;
        if (this.permissions.isEmpty())
            return null;
        StringBuilder permlistBuilder = new StringBuilder();
        for (String perm : this.permissions)
            permlistBuilder.append(perm).append(", ");
        permlist = permlistBuilder.toString();
        return permlist.trim().substring(0, permlist.length() - 2);
    }

    void addPerm(String permission) {
        setPerm(permission);
    }

    void removePerm(String permission) {
        this.attachment.unsetPermission(permission);
        if (this.permissions.contains(permission))
            this.permissions.remove(permission);
    }

    /**
     * Gets the Player object of this user.
     * @return The Player object of this user.
     */
    public Player getPlayer() {
        return this.bukkitPlayer;
    }

    /**
     * Gets the location the player last left clicked on.
     * @return The location the player last left clicked on.
     */
    public Location getLeft() {
        return this.left;
    }

    /**
     * Sets the location the player last left clicked on.
     * @param l The location the player last left clicked on.
     */
    public void setLeft(Location l) {
        this.left = l;
    }

    /**
     * Gets the location the player last right clicked on.
     * @return The location the player last right clicked on.
     */
    public Location getRight() {
        return this.right;
    }

    /**
     * Sets the location the player last right clicked on.
     * @param l The location the player last right clicked on.
     */
    public void setRight(Location l) {
        this.right = l;
    }

    /**
     * Gets the guild this user is in.
     * @return The guild this user is in.
     */
    public Guild getGuild() {
        return this.guild;
    }

    /**
     * Returns if the user is currently auto claiming land.
     * @return True if the user is auto claiming land, false otherwise.
     */
    public boolean isClaiming() {
        return this.autoClaiming;
    }

    /**
     * Sets whether the user is currently auto claiming land.
     * @param value True to make the user auto claim land, false otherwise.
     */
    public void setClaiming(boolean value) {
        this.autoClaiming = value;
    }

    /**
     * Checks if the user is currently sending messages to slack.
     * @return True if the user is currently sending messages to slack, false otherwise.
     */
    public boolean slackChat() {
        return this.slackChat;
    }

    /**
     * Toggles sending messages to slack.
     */
    public void toggleSlackChat() {
        this.slackChat = !this.slackChat;
    }

    /**
     * Checks if the user is currently sending messages to ops only.
     * @return True if the user is currently sending messages to ops, false otherwise.
     */
    public boolean opChat() {
        return this.opChat;
    }

    /**
     * Toggles sending messages to ops.
     */
    public void toggleOpChat() {
        this.opChat = !this.opChat;
    }

    /**
     * Checks if the user is currently sending messages to their guild.
     * @return True if the user is currently sending messages to their guild, false otherwise.
     */
    public boolean guildChat() {
        return this.guildChat;
    }

    /**
     * Toggles sending messages to the guild.
     */
    public void toggleGuildChat() {
        this.guildChat = !this.guildChat;
    }

    /**
     * Removes power from the user. Called when the user dies.
     */
    public void removePower() {
        if (this.power - 2 < -20)
            this.power = -20;
        else
            this.power -= 2;
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        if (!configUsers.contains(getUUID().toString()))
            return;
        configUsers.set(getUUID().toString() + ".power", this.power);
        try {
            configUsers.save(configFileUsers);
        } catch (Exception ignored) {
        }
        if (this.guild != null)
            this.guild.updatePower();
    }

    /**
     * Adds power to the user for being online.
     */
    public void addPower() {
        if (this.afk)
            return;
        if (this.power + 0.03333 > 20)
            this.power = 20;
        else
            this.power += 0.03333;
        YamlConfiguration configUsers = YamlConfiguration.loadConfiguration(configFileUsers);
        if (!configUsers.contains(getUUID().toString()))
            return;
        configUsers.set(getUUID().toString() + ".power", this.power);
        try {
            configUsers.save(configFileUsers);
        } catch (Exception ignored) {
        }
        if (this.guild != null)
            this.guild.updatePower();
    }

    /**
     * Gets the location the user is looking at.
     * @return The location the user is looking at.
     */
    @SuppressWarnings("deprecation")
    public Location getLookingAt() {
        if (this.bukkitPlayer == null)
            return null;
        Iterator<Block> itr = new BlockIterator(this.bukkitPlayer, 140);
        while (itr.hasNext()) {
            Block block = itr.next();
            if (block.getType().getId() != 0)
                return block.getLocation();
        }
        return null;
    }

    /**
     * Saves the user's inventory and loads the inventory for the world they are going to.
     * @param s    The inventory system to load their inventory from.
     * @param from The inventory system to save their current inventory to.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void saveInventory(String s, String from) {
        if (this.bukkitPlayer == null)
            return;
        this.bukkitPlayer.saveData();
        int ticks = this.bukkitPlayer.getTicksLived();
        File dirFrom = new File("plugins/Necessities/WorldManager/" + from + "/");
        File dir = new File("plugins/Necessities/WorldManager/" + s + "/");
        if (!dirFrom.exists())
            dirFrom.mkdir();
        if (!dir.exists())
            dir.mkdir();
        File f = new File("world/playerdata/" + this.bukkitPlayer.getUniqueId() + ".dat");
        try {
            Files.copy(f, new File(dirFrom, this.bukkitPlayer.getUniqueId() + ".dat"));
        } catch (IOException ignored) {
        }
        File saved = new File(dir, this.bukkitPlayer.getUniqueId() + ".dat");
        if (saved.exists()) {//if exists transfer and load otherwise just set to the defaults of world and inv and enderchest and xp
            try {
                Files.copy(saved, new File("world/playerdata/" + this.bukkitPlayer.getUniqueId() + ".dat"));
            } catch (IOException ignored) {
            }
            this.bukkitPlayer.loadData();
            this.bukkitPlayer.setTicksLived(ticks);
        } else {
            this.bukkitPlayer.getInventory().clear();
            this.bukkitPlayer.getEnderChest().clear();
            this.bukkitPlayer.getInventory().setHelmet(new ItemStack(Material.AIR));
            this.bukkitPlayer.getInventory().setChestplate(new ItemStack(Material.AIR));
            this.bukkitPlayer.getInventory().setLeggings(new ItemStack(Material.AIR));
            this.bukkitPlayer.getInventory().setBoots(new ItemStack(Material.AIR));
            this.bukkitPlayer.setExp(0);
        }
        this.bukkitPlayer.saveData();
    }

    /**
     * Sets the prefix for this user. Mainly used for team names in gamemodes.
     * @param prefix The prefix to set for the user.
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Gets the current prefix of the user.
     * @return The current prefix of the user.
     */
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * Gets the time that this user has played on the server.
     * @return The time that this user has played on the server.
     */
    public String getTimePlayed() {
        long seconds = this.pastTotal;
        if (this.login != 0)
            seconds += (System.currentTimeMillis() - this.login) / 1000;
        long first = seconds % 31536000;
        long second = first % 2592000;
        long third = second % 604800;
        long fourth = third % 86400;
        long fifth = fourth % 3600;
        int years = (int) (seconds / 31536000);
        int months = (int) (first / 2592000);
        int weeks = (int) (second / 604800);
        int days = (int) (third / 86400);
        int hours = (int) (fourth / 3600);
        int min = (int) (fifth / 60);
        int sec = (int) (fifth % 60);
        String time = "";
        if (years != 0)
            time = Integer.toString(years) + " year" + plural(years) + " ";
        if (months != 0)
            time += Integer.toString(months) + " month" + plural(months) + " ";
        if (weeks != 0)
            time += Integer.toString(weeks) + " week" + plural(weeks) + " ";
        if (days != 0)
            time += Integer.toString(days) + " day" + plural(days) + " ";
        if (hours != 0)
            time += Integer.toString(hours) + " hour" + plural(hours) + " ";
        if (min != 0)
            time += Integer.toString(min) + " minute" + plural(min) + " ";
        if (sec != 0)
            time += Integer.toString(sec) + " second" + plural(sec) + " ";
        time = time.trim();
        if (time.equals(""))
            time = "This player has not spent any time on our server";
        return time + ".";
    }

    private String plural(int times) {
        return times == 1 ? "" : "s";
    }
}