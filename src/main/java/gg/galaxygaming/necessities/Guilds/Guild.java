package gg.galaxygaming.necessities.Guilds;

import gg.galaxygaming.necessities.Commands.CmdHide;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.UserManager;
import gg.galaxygaming.necessities.Variables;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

public class Guild {

    private final File configFileGuild;
    private final ArrayList<String> allies = new ArrayList<>();
    private final ArrayList<String> enemies = new ArrayList<>();
    private final ArrayList<String> mods = new ArrayList<>();
    private final ArrayList<String> members = new ArrayList<>();
    private final ArrayList<String> claims = new ArrayList<>();
    private final ArrayList<Guild> allyInvites = new ArrayList<>();
    private final ArrayList<Guild> neutralInvites = new ArrayList<>();
    private final ArrayList<UUID> invited = new ArrayList<>();
    private boolean pvp = true, permanent, explosions = true, interact, hostileSpawn = true;
    private String description = "", leader, name;
    private int totalMembers;
    private double power;
    private Location home;

    Guild(String name) {
        this.name = name;
        this.configFileGuild = new File("plugins/Necessities/Guilds", this.name + ".yml");
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        if (configGuild.contains("power")) {
            this.power = configGuild.getDouble("power");
        }
        if (configGuild.contains("flag.permanent")) {
            this.permanent = configGuild.getBoolean("flag.permanent");
        }
        if (configGuild.contains("flag.pvp")) {
            this.pvp = configGuild.getBoolean("flag.pvp");
        }
        if (configGuild.contains("flag.explosions")) {
            this.explosions = configGuild.getBoolean("flag.explosions");
        }
        if (configGuild.contains("flag.interact")) {
            this.interact = configGuild.getBoolean("flag.interact");
        }
        if (configGuild.contains("flag.hostileSpawn")) {
            this.hostileSpawn = configGuild.getBoolean("flag.hostileSpawn");
        }
        if (configGuild.contains("leader")) {
            this.leader = configGuild.getString("leader");
        }
        if (configGuild.contains("description")) {
            this.description = configGuild.getString("description");
        }
        if (configGuild.contains("home")) {
            World world = Bukkit.getWorld(configGuild.getString("home.world"));
            double x = Double.parseDouble(configGuild.getString("home.x"));
            double y = Double.parseDouble(configGuild.getString("home.y"));
            double z = Double.parseDouble(configGuild.getString("home.z"));
            float yaw = Float.parseFloat(configGuild.getString("home.yaw"));
            float pitch = Float.parseFloat(configGuild.getString("home.pitch"));
            home = new Location(world, x, y, z, yaw, pitch);
        }
        if (configGuild.contains("mods")) {
            List<String> mods = configGuild.getStringList("mods");
            mods.remove("");
            if (!mods.isEmpty()) {
                this.mods.addAll(mods);
            }
        }
        if (configGuild.contains("members")) {
            List<String> members = configGuild.getStringList("members");
            members.remove("");
            if (!members.isEmpty()) {
                this.members.addAll(members);
            }
        }
        if (configGuild.contains("allies")) {
            List<String> allies = configGuild.getStringList("allies");
            allies.remove("");
            if (!allies.isEmpty()) {
                this.allies.addAll(allies);
            }
        }
        if (configGuild.contains("enemies")) {
            List<String> enemies = configGuild.getStringList("enemies");
            enemies.remove("");
            if (!enemies.isEmpty()) {
                this.enemies.addAll(enemies);
            }
        }
        if (configGuild.contains("claims")) {
            List<String> claims = configGuild.getStringList("claims");
            claims.remove("");
            if (!claims.isEmpty()) {
                for (String claim : claims) {
                    if (claim.split(" ").length == 3 && Bukkit.getWorld(claim.split(" ")[0]) != null) {
                        this.claims.add(claim);
                    }
                }
            }
        }
        if (this.leader != null && !this.leader.equals("") && !this.leader.equals("Janet")) {
            this.totalMembers++;
        }
        this.totalMembers += this.mods.size() + this.members.size();
    }

    /**
     * Updates the power for this guild based on the power of its members.
     */
    public void updatePower() {
        if (this.power == -1) {
            return;
        }
        this.power = 0;
        UserManager um = Necessities.getUM();
        if (this.leader != null && !this.leader.equals("") && !this.leader.equals("Janet")) {
            this.power += um.getUser(UUID.fromString(this.leader)).getPower();
        }
        if (!this.mods.isEmpty()) {
            for (String id : this.mods) {
                this.power += um.getUser(UUID.fromString(id)).getPower();
            }
        }
        if (!this.members.isEmpty()) {
            for (String id : this.members) {
                this.power += um.getUser(UUID.fromString(id)).getPower();
            }
        }
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        configGuild.set("power", this.power);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception ignored) {
        }
    }

    /**
     * Gets whether the guild is permanent or not.
     *
     * @return True if the guild is permanent, false otherwise.
     */
    public boolean isPermanent() {
        return this.permanent;
    }

    /**
     * Sets whether or not the guild is permanent.
     *
     * @param perm True to make the guild permanent, false otherwise.
     */
    public void setPermanent(boolean perm) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        this.permanent = perm;
        configGuild.set("flag.permanent", perm);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception ignored) {
        }
    }

    /**
     * Changes the name of the guild to the specified name.
     *
     * @param newName The name to change the guild to.
     */
    public void rename(String newName) {
        GuildManager gm = Necessities.getGM();
        this.enemies.forEach(enemy -> gm.getGuild(enemy).setNeutral(this));
        this.allies.forEach(ally -> gm.getGuild(ally).setNeutral(this));
        this.name = newName;
        UserManager um = Necessities.getUM();
        if (this.leader != null && !this.leader.equals("") && !this.leader.equals("Janet")) {
            um.getUser(UUID.fromString(this.leader)).joinGuild(this);//Already is this but force updates the name
        }
        this.mods.forEach(
              id -> um.getUser(UUID.fromString(id)).joinGuild(this));//Already is this but force updates the name
        this.members.forEach(
              id -> um.getUser(UUID.fromString(id)).joinGuild(this));//Already is this but force updates the name
        this.enemies.forEach(enemy -> gm.getGuild(enemy).addEnemy(this));
        this.allies.forEach(ally -> gm.getGuild(ally).addAlly(this));
    }

    /**
     * Check if players are allowed to interact with doors and the like in this guild's territory.
     *
     * @return True if players can interact, false otherwise.
     */
    public boolean allowInteract() {
        return this.interact;
    }

    /**
     * Sets whether or not players are allowed to interact with doors and the like in this guild's territory.
     *
     * @param allow True to allow players to interact, false otherwise.
     */
    public void setInteract(boolean allow) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        this.interact = allow;
        configGuild.set("flag.interact", allow);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception ignored) {
        }
    }

    /**
     * Checks if hostile mobs can spawn in this guild's territory.
     *
     * @return True if hostile mobs can spawn, false otherwise.
     */
    public boolean canHostileSpawn() {
        return this.hostileSpawn;
    }

    /**
     * Sets whether or not hostile mobs can spawn in this guild's territory.
     *
     * @param canSpawn True to make it so hostile mobs can spawn, false otherwise.
     */
    public void setHostileSpawn(boolean canSpawn) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        this.hostileSpawn = canSpawn;
        configGuild.set("flag.hostileSpawn", canSpawn);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception ignored) {
        }
    }

    /**
     * Checks if explosions are allowed in this guild's land.
     *
     * @return True if explosions are allowed, false otherwise.
     */
    public boolean canExplode() {
        return this.explosions;
    }

    /**
     * Sets the leader of the guild.
     *
     * @param uuid The uuid of the new leader of the guild.
     */
    public void setLeader(UUID uuid) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        String name = uuid.toString();
        if (this.leader != null && !this.leader.equalsIgnoreCase("Janet") && !this.leader.equals("")) {
            addMod(UUID.fromString(this.leader));
        }
        if (this.mods.contains(name)) {
            this.mods.remove(name);
            List<String> modList = configGuild.getStringList("mods");
            modList.remove(name);
            if (modList.isEmpty()) {
                modList.add("");
            }
            configGuild.set("mods", modList);
        }
        if (this.members.contains(name)) {
            this.members.remove(name);
            List<String> memberList = configGuild.getStringList("members");
            memberList.remove(name);
            if (memberList.isEmpty()) {
                memberList.add("");
            }
            configGuild.set("members", memberList);
        }
        this.leader = name;
        configGuild.set("leader", name);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception ignored) {
        }
    }

    /**
     * Retrieves the current power of the guild.
     *
     * @return The amount of power the guild has.
     */
    public double getPower() {
        return this.power;
    }

    /**
     * Retrieves the name of this guild.
     *
     * @return The name of the guild.
     */
    public String getName() {
        return this.name;
    }

    boolean claimed(Chunk c) {
        return this.claims.contains(c.getWorld().getName() + ' ' + c.getX() + ' ' + c.getZ());
    }

    /**
     * Retrieves the description of this guild.
     *
     * @return The description of the guild.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the description of the guild to the specified description.
     *
     * @param newDesc The new description for the guild.
     */
    public void setDescription(String newDesc) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        this.description = newDesc;
        configGuild.set("description", this.description);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception ignored) {
        }
    }

    /**
     * Retrieves the location representing the home of this guild.
     *
     * @return The location representing the home of this guild.
     */
    public Location getHome() {
        if (this.home != null && !claimed(this.home.getChunk())) {
            delHome();
        }
        return this.home;
    }

    /**
     * Sets the home location for this guild to the specified location.
     *
     * @param location The location to set as the new home.
     */
    public void setHome(Location location) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        this.home = location;
        configGuild.set("home.world", location.getWorld().getName());
        configGuild.set("home.x", Double.toString(location.getX()));
        configGuild.set("home.y", Double.toString(location.getY()));
        configGuild.set("home.z", Double.toString(location.getZ()));
        configGuild.set("home.yaw", Float.toString(location.getYaw()));
        configGuild.set("home.pitch", Float.toString(location.getPitch()));
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception ignored) {
        }
    }

    /**
     * Checks if the specified guild is an ally to the current guild.
     *
     * @param ally The guild to check for allyship.
     * @return True if the specified guild is an ally, false otherwise.
     */
    public boolean isAlly(Guild ally) {
        return ally != null && this.allies.contains(ally.getName());
    }

    /**
     * Checks if the specified guild is an enemy to the current guild.
     *
     * @param enemy The guild to check if it is an enemy.
     * @return True if the specified guild is an enemy, false otherwise.
     */
    public boolean isEnemy(Guild enemy) {
        return enemy != null && this.enemies.contains(enemy.getName());
    }

    /**
     * Retrieves how many chunks the guild has claimed.
     *
     * @return The number of chunks this guild has claimed.
     */
    public int getLand() {
        return this.claims.size();
    }

    /**
     * Retrieves if PVP is allowed in this guild's territory.
     *
     * @return True if PVP is enabled, false otherwise.
     */
    public boolean canPVP() {
        return this.pvp;
    }

    /**
     * Retrieves the number of members in the guild.
     *
     * @return The number of members in the guild.
     */
    public int getTotal() {
        return this.totalMembers;
    }

    /**
     * Retrieves the maximum power of the guild.
     *
     * @return The maximum power of the guild.
     */
    public int getMaxPower() {
        return this.power == -1 ? -1 : getTotal() * 20;
    }

    /**
     * Retrieves the list of online members.
     *
     * @param countHidden True to include members that are in hide, false otherwise.
     * @return The list of online members of the guild.
     */
    public String onlineMembers(boolean countHidden) {
        CmdHide hide = Necessities.getHide();
        StringBuilder memberBuilder = new StringBuilder();
        if (this.leader != null && !this.leader.equals("") && !this.leader.equals("Janet")
              && Bukkit.getPlayer(UUID.fromString(this.leader)) != null &&
              (countHidden || !hide.isHidden(Bukkit.getPlayer(UUID.fromString(this.leader))))) {
            memberBuilder.append(Bukkit.getPlayer(UUID.fromString(this.leader)).getName()).append(", ");
        }
        if (!this.mods.isEmpty()) {
            for (String id : this.mods) {
                if (Bukkit.getPlayer(UUID.fromString(id)) != null && (countHidden || !hide
                      .isHidden(Bukkit.getPlayer(UUID.fromString(id))))) {
                    memberBuilder.append(Bukkit.getPlayer(UUID.fromString(id)).getName()).append(", ");
                }
            }
        }
        if (!this.members.isEmpty()) {
            for (String id : this.members) {
                if (Bukkit.getPlayer(UUID.fromString(id)) != null && (countHidden || !hide
                      .isHidden(Bukkit.getPlayer(UUID.fromString(id))))) {
                    memberBuilder.append(Bukkit.getPlayer(UUID.fromString(id)).getName()).append(", ");
                }
            }
        }
        String member = memberBuilder.toString();
        if (member.contains(", ")) {
            member = member.substring(0, member.length() - 2);
        }
        return member;
    }

    /**
     * Retrieves the list of offline members.
     *
     * @param countHidden False to include members that are in hide, true otherwise.
     * @return The list of offline members of the guild.
     */
    public String offlineMember(boolean countHidden) {
        CmdHide hide = Necessities.getHide();
        StringBuilder memberBuilder = new StringBuilder();
        if (this.leader != null && !this.leader.equals("") && !this.leader.equals("Janet") && (
              Bukkit.getPlayer(UUID.fromString(this.leader)) == null ||
                    !countHidden && hide.isHidden(Bukkit.getPlayer(UUID.fromString(this.leader))))) {
            memberBuilder.append(Bukkit.getOfflinePlayer(UUID.fromString(this.leader)).getName()).append(", ");
        }
        if (!this.mods.isEmpty()) {
            for (String id : this.mods) {
                if (Bukkit.getPlayer(UUID.fromString(id)) == null || !countHidden && hide
                      .isHidden(Bukkit.getPlayer(UUID.fromString(id)))) {
                    memberBuilder.append(Bukkit.getOfflinePlayer(UUID.fromString(id)).getName()).append(", ");
                }
            }
        }
        if (!this.members.isEmpty()) {
            for (String id : this.members) {
                if (Bukkit.getPlayer(UUID.fromString(id)) == null || !countHidden && hide
                      .isHidden(Bukkit.getPlayer(UUID.fromString(id)))) {
                    memberBuilder.append(Bukkit.getOfflinePlayer(UUID.fromString(id)).getName()).append(", ");
                }
            }
        }
        String member = memberBuilder.toString();
        if (member.contains(", ")) {
            member = member.substring(0, member.length() - 2);
        }
        return member;
    }

    /**
     * Retrieves the number of online players in the guild.
     *
     * @param countHidden True to include hidden players in the count, false otherwise.
     * @return The number of online players in the guild.
     */
    public int getOnline(boolean countHidden) {
        CmdHide hide = Necessities.getHide();
        int online = 0;
        if (this.leader != null && !this.leader.equals("") && !this.leader.equals("Janet")
              && Bukkit.getPlayer(UUID.fromString(this.leader)) != null &&
              (countHidden || !hide.isHidden(Bukkit.getPlayer(UUID.fromString(this.leader))))) {
            online++;
        }
        if (!this.mods.isEmpty()) {
            online += this.mods.stream().filter(
                  id -> Bukkit.getPlayer(UUID.fromString(id)) != null && (countHidden || !hide
                        .isHidden(Bukkit.getPlayer(UUID.fromString(id))))).count();
        }
        if (!this.members.isEmpty()) {
            online += this.members.stream().filter(
                  id -> Bukkit.getPlayer(UUID.fromString(id)) != null && (countHidden || !hide
                        .isHidden(Bukkit.getPlayer(UUID.fromString(id))))).count();
        }
        return online;
    }

    /**
     * Gets the rank in the guild of the member with the specified uuid.
     *
     * @param uuid The uuid of the member to look for.
     * @return The name of the rank the user has or null if they are not in the guild.
     */
    public String getRank(UUID uuid) {
        return this.leader != null && !this.leader.equals("Janet") && this.leader.equals(uuid.toString()) ? "leader"
              : this.mods.contains(uuid.toString()) ? "mod" : this.members.contains(uuid.toString()) ?
                    "member" : null;
    }

    /**
     * Gets the color that represents the relation between this guild and the specified one.
     *
     * @param other The guild to check the relationship of.
     * @return The color that represents the relation between this guild and the specified one.
     */
    public ChatColor relation(Guild other) {
        Variables var = Necessities.getVar();
        ChatColor col = var.getNeutral();
        if (other != null) {
            if (this.equals(other)) {
                col = var.getGuildCol();
            } else if (isAlly(other)) {
                col = var.getAlly();
            } else if (isEnemy(other)) {
                col = var.getEnemy();
            }
        }
        return col;
    }

    /**
     * Retrieves the list of allies.
     *
     * @return The list of allies.
     */
    public ArrayList<String> getAllies() {
        return this.allies;
    }

    /**
     * Retrieves the list of enemies.
     *
     * @return The list of enemies.
     */
    public ArrayList<String> getEnemies() {
        return this.enemies;
    }

    private void delHome() {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        this.home = null;
        configGuild.set("home", null);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception ignored) {
        }
    }

    /**
     * Claims the specified chunk.
     *
     * @param c The chunk to claim.
     */
    public void claim(Chunk c) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        if (!this.claims.contains(c.getWorld().getName() + ' ' + c.getX() + ' ' + c.getZ())) {
            this.claims.add(c.getWorld().getName() + ' ' + c.getX() + ' ' + c.getZ());
        }
        List<String> chunkList = configGuild.getStringList("claims");
        if (!chunkList.contains(c.getWorld().getName() + ' ' + c.getX() + ' ' + c.getZ())) {
            chunkList.add(c.getWorld().getName() + ' ' + c.getX() + ' ' + c.getZ());
            chunkList.remove("");
        }
        configGuild.set("claims", chunkList);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception ignored) {
        }
    }

    /**
     * Unclaims the specified chunk.
     *
     * @param c The chunk to unclaim.
     */
    public void unclaim(Chunk c) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        this.claims.remove(c.getWorld().getName() + ' ' + c.getX() + ' ' + c.getZ());
        List<String> chunkList = configGuild.getStringList("claims");
        chunkList.remove(c.getWorld().getName() + ' ' + c.getX() + ' ' + c.getZ());
        if (chunkList.isEmpty()) {
            chunkList.add("");
        }
        configGuild.set("claims", chunkList);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception ignored) {
        }
        if (this.home != null && this.home.getChunk().equals(c)) {
            delHome();
        }
    }

    /**
     * Unclaims all the chunks that this guild has claimed.
     */
    public void unclaimAll() {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        this.claims.clear();
        delHome();
        configGuild.set("claims", Collections.singletonList(""));
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception ignored) {
        }
    }

    /**
     * Checks if the specified player has been invited to the guild.
     *
     * @param uuid The uuid of the player to check.
     * @return True if they have been invited, false otherwise.
     */
    public boolean isInvited(UUID uuid) {
        return this.invited.contains(uuid);
    }

    /**
     * Invites a player to join the guild.
     *
     * @param uuid The uuid of the player to invite.
     */
    public void invite(UUID uuid) {
        this.invited.add(uuid);
    }

    /**
     * Revoke the invite from the specified player.
     *
     * @param uuid The uuid of the player to revoke the invite from.
     */
    public void uninvite(UUID uuid) {
        this.invited.remove(uuid);
    }

    /**
     * Checks if the specified guild has been invited to be an ally.
     *
     * @param g The guild to check if they have been invited as an ally.
     * @return True if the specified guild has been invited to be an ally, false otherwise.
     */
    public boolean isInvitedAlly(Guild g) {
        return this.allyInvites.contains(g);
    }

    /**
     * Invites the specified guild to become an ally.
     *
     * @param g The guild to invite to become an ally.
     */
    public void addAllyInvite(Guild g) {
        this.allyInvites.add(g);
    }

    /**
     * Removes an invite to be allies from the specified guild.
     *
     * @param g The guild to revoke the offer of allyship from.
     */
    public void removeAllyInvite(Guild g) {
        this.allyInvites.remove(g);
    }

    /**
     * Checks if the specified guild has been invited to be neutral.
     *
     * @param g The guild to check.
     * @return True if the specified guild has been invited to be neutral, false otherwise.
     */
    public boolean isInvitedNeutral(Guild g) {
        return this.neutralInvites.contains(g);
    }

    /**
     * Invites the specified guild to be neutral.
     *
     * @param g The guild to invite.
     */
    public void addNeutralInvite(Guild g) {
        this.neutralInvites.add(g);
    }

    /**
     * Removes the invite to the specified guild about becoming neutral.
     *
     * @param g The guild to remove the invite from.
     */
    public void removeNeutralInvite(Guild g) {
        this.neutralInvites.remove(g);
    }

    /**
     * Adds the specified guild as an enemy.
     *
     * @param g The guild to add as an enemy.
     */
    public void addEnemy(Guild g) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        setNeutral(g);
        this.enemies.add(g.getName());
        List<String> enemyList = configGuild.getStringList("enemies");
        enemyList.add(g.getName());
        enemyList.remove("");
        configGuild.set("enemies", enemyList);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception ignored) {
        }
    }

    /**
     * Adds the specified guild as an ally.
     *
     * @param g The guild to add as an ally.
     */
    public void addAlly(Guild g) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        this.allyInvites.remove(g);
        setNeutral(g);
        this.allies.add(g.getName());
        List<String> allyList = configGuild.getStringList("allies");
        allyList.add(g.getName());
        allyList.remove("");
        configGuild.set("allies", allyList);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception ignored) {
        }
    }

    /**
     * Sets the specified guild as neutral.
     *
     * @param g The guild to set as neutral.
     */
    public void setNeutral(Guild g) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        if (this.allies.contains(g.getName())) {
            this.allies.remove(g.getName());
            List<String> allyList = configGuild.getStringList("allies");
            allyList.remove(g.getName());
            if (allyList.isEmpty()) {
                allyList.add("");
            }
            configGuild.set("allies", allyList);
        }
        if (this.enemies.contains(g.getName())) {
            this.enemies.remove(g.getName());
            List<String> enemyList = configGuild.getStringList("enemies");
            enemyList.remove(g.getName());
            if (enemyList.isEmpty()) {
                enemyList.add("");
            }
            configGuild.set("enemies", enemyList);
        }
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception ignored) {
        }
        this.neutralInvites.remove(g);
    }

    /**
     * Checks if the specified guild is neutral.
     *
     * @param g The guild to check.
     * @return True if the guilds are neutral towards one another, false otherwise.
     */
    public boolean isNeutral(Guild g) {
        return !this.allies.contains(g.getName()) && !this.enemies.contains(g.getName());
    }

    /**
     * Kicks a specified player from the guild
     *
     * @param uuid The uuid of the player to kick.
     */
    public void kick(UUID uuid) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        String name = uuid.toString();
        if (this.leader != null && this.leader.equalsIgnoreCase(name)) {
            this.leader = "";
            configGuild.set("leader", "");
        }
        if (this.mods.contains(name)) {
            this.mods.remove(name);
            List<String> modList = configGuild.getStringList("mods");
            modList.remove(name);
            if (modList.isEmpty()) {
                modList.add("");
            }
            configGuild.set("mods", modList);
        }
        if (this.members.contains(name)) {
            this.members.remove(name);
            List<String> memberList = configGuild.getStringList("members");
            memberList.remove(name);
            if (memberList.isEmpty()) {
                memberList.add("");
            }
            configGuild.set("members", memberList);
        }
        UserManager um = Necessities.getUM();
        um.getUser(uuid).leaveGuild();
        if (this.totalMembers != 0) {
            this.totalMembers--;
        }
        if (!this.permanent && getTotal() == 0) {
            Necessities.getGM().disband(this);
        }
        if (this.power != -1) {
            this.power -= um.getUser(uuid).getPower();
        }
        configGuild.set("power", this.power);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception ignored) {
        }
    }

    /**
     * Makes a specified player join the guild.
     *
     * @param uuid The uuid of the player to have join the guild.
     */
    public void join(UUID uuid) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        String name = uuid.toString();
        this.members.add(name);
        List<String> memberList = configGuild.getStringList("members");
        memberList.add(name);
        memberList.remove("");
        configGuild.set("members", memberList);
        UserManager um = Necessities.getUM();
        um.getUser(uuid).joinGuild(this);
        this.invited.remove(uuid);
        this.totalMembers++;
        if (this.power != -1) {
            this.power += um.getUser(uuid).getPower();
        }
        configGuild.set("power", this.power);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception ignored) {
        }
    }

    /**
     * Sends a message to the mods of the guild.
     *
     * @param message The message to send.
     */
    public void sendMods(String message) {
        if (this.leader != null && !this.leader.equals("") && !this.leader.equals("Janet")
              && Bukkit.getPlayer(UUID.fromString(this.leader)) != null) {
            Bukkit.getPlayer(UUID.fromString(this.leader)).sendMessage(message);
        }
        if (!this.mods.isEmpty()) {
            for (String id : this.mods) {
                if (Bukkit.getPlayer(UUID.fromString(id)) != null) {
                    Bukkit.getPlayer(UUID.fromString(id)).sendMessage(message);
                }
            }
        }
    }

    /**
     * Sets whether or not explosions are allowed in this guild's territory.
     *
     * @param exp True to allow explosions, false otherwise.
     */
    public void setExplode(boolean exp) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        this.explosions = exp;
        configGuild.set("flag.explosions", exp);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception ignored) {
        }
    }

    /**
     * Sets whether or not PVP is allowed in this guild's territory.
     *
     * @param canPVP True to allow pvp, false otherwise.
     */
    public void setPVP(boolean canPVP) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        this.pvp = canPVP;
        configGuild.set("flag.pvp", canPVP);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception ignored) {
        }
    }

    /**
     * Toggles whether or not this guild can claim infinite number of chunks.
     */
    public void toggleInfinite() {
        if (this.power != -1) {
            YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
            this.power = -1;
            configGuild.set("power", -1);
            try {
                configGuild.save(this.configFileGuild);
            } catch (Exception ignored) {
            }
        } else {
            this.power = 0;
            updatePower();
        }
    }

    void disband() {
        UserManager um = Necessities.getUM();
        if (this.leader != null && !this.leader.equalsIgnoreCase("Janet") && !this.leader.equalsIgnoreCase("")) {
            um.getUser(UUID.fromString(this.leader)).leaveGuild();
        }
        this.mods.forEach(mod -> um.getUser(UUID.fromString(mod)).leaveGuild());
        this.members.forEach(member -> um.getUser(UUID.fromString(member)).leaveGuild());
        this.configFileGuild.delete();
    }

    /**
     * Removes the specified player from being a mod of the guild.
     *
     * @param uuid The uuid of the player to remove.
     */
    public void removeMod(UUID uuid) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        String name = uuid.toString();
        this.mods.remove(name);
        List<String> modList = configGuild.getStringList("mods");
        modList.remove(name);
        if (modList.isEmpty()) {
            modList.add("");
        }
        configGuild.set("mods", modList);
        this.members.add(name);
        List<String> memberList = configGuild.getStringList("members");
        memberList.add(name);
        memberList.remove("");
        configGuild.set("members", memberList);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception ignored) {
        }
    }

    /**
     * Adds a specified player as a mod for the guild.
     *
     * @param uuid The uuid of the player to add.
     */
    public void addMod(UUID uuid) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        String name = uuid.toString();
        this.mods.add(name);
        List<String> modList = configGuild.getStringList("mods");
        modList.add(name);
        modList.remove("");
        configGuild.set("mods", modList);
        if (this.leader != null && this.leader.equalsIgnoreCase(name)) {
            this.leader = "";
            configGuild.set("leader", "");
        } else {
            this.members.remove(name);
            List<String> memberList = configGuild.getStringList("members");
            memberList.remove(name);
            if (memberList.isEmpty()) {
                memberList.add("");
            }
            configGuild.set("members", memberList);
        }
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception ignored) {
        }
    }
}