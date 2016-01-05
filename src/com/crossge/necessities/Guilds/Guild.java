package com.crossge.necessities.Guilds;

import com.crossge.necessities.Commands.CmdHide;
import com.crossge.necessities.RankManager.UserManager;
import com.crossge.necessities.Variables;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Guild {
    private File configFileGuild;
    private ArrayList<String> allies = new ArrayList<>(), enemies = new ArrayList<>(), mods = new ArrayList<>(), members = new ArrayList<>(), claims = new ArrayList<>();
    private ArrayList<Guild> allyInvites = new ArrayList<>(), neutralInvites = new ArrayList<>();
    private ArrayList<UUID> invited = new ArrayList<>();
    private boolean pvp = true, permanent = false, explosions = true, interact = false, hostileSpawn = true;
    private String description = "", leader, name;
    private int totalMembers = 0;
    private double power = 0.0;
    private Location home;

    public Guild(String name) {
        this.name = name;
        this.configFileGuild = new File("plugins/Necessities/Guilds", this.name + ".yml");
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        if (configGuild.contains("power"))
            this.power = configGuild.getDouble("power");
        if (configGuild.contains("flag.permanent"))
            this.permanent = configGuild.getBoolean("flag.permanent");
        if (configGuild.contains("flag.pvp"))
            this.pvp = configGuild.getBoolean("flag.pvp");
        if (configGuild.contains("flag.explosions"))
            this.explosions = configGuild.getBoolean("flag.explosions");
        if (configGuild.contains("flag.interact"))
            this.interact = configGuild.getBoolean("flag.interact");
        if (configGuild.contains("flag.hostileSpawn"))
            this.hostileSpawn = configGuild.getBoolean("flag.hostileSpawn");
        if (configGuild.contains("leader"))
            this.leader = configGuild.getString("leader");
        if (configGuild.contains("description"))
            this.description = configGuild.getString("description");
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
            if (mods.contains(""))
                mods.remove("");
            if (!mods.isEmpty())
                for (String mod : mods)
                    this.mods.add(mod);
        }
        if (configGuild.contains("members")) {
            List<String> members = configGuild.getStringList("members");
            if (members.contains(""))
                members.remove("");
            if (!members.isEmpty())
                for (String member : members)
                    this.members.add(member);
        }
        if (configGuild.contains("allies")) {
            List<String> allies = configGuild.getStringList("allies");
            if (allies.contains(""))
                allies.remove("");
            if (!allies.isEmpty())
                for (String ally : allies)
                    this.allies.add(ally);
        }
        if (configGuild.contains("enemies")) {
            List<String> enemies = configGuild.getStringList("enemies");
            if (enemies.contains(""))
                enemies.remove("");
            if (!enemies.isEmpty())
                for (String enemy : enemies)
                    this.enemies.add(enemy);
        }
        if (configGuild.contains("claims")) {
            List<String> claims = configGuild.getStringList("claims");
            if (claims.contains(""))
                claims.remove("");
            if (!claims.isEmpty())
                for (String claim : claims)
                    if (claim.split(" ").length == 3 && Bukkit.getWorld(claim.split(" ")[0]) != null)
                        this.claims.add(claim);
        }
        if (!this.leader.equals("") && !this.leader.equals("Janet"))
            this.totalMembers++;
        this.totalMembers += this.mods.size();
        this.totalMembers += this.members.size();
    }

    public void updatePower() {
        UserManager um = new UserManager();
        if (this.power == -1)
            return;
        this.power = 0;
        if (!this.leader.equals("") && !this.leader.equals("Janet"))
            this.power += um.getUser(UUID.fromString(this.leader)).getPower();
        if (!this.mods.isEmpty())
            for (String id : this.mods)
                this.power += um.getUser(UUID.fromString(id)).getPower();
        if (!this.members.isEmpty())
            for (String id : this.members)
                this.power += um.getUser(UUID.fromString(id)).getPower();
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        configGuild.set("power", this.power);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception e) { }
    }

    public boolean isPermanent() {
        return this.permanent;
    }

    public void setPermanent(boolean perm) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        this.permanent = perm;
        configGuild.set("flag.permanent", perm);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception e) { }
    }

    public void rename(String newName) {
        UserManager um = new UserManager();
        GuildManager gm = new GuildManager();
        for (String enemy : this.enemies)
            gm.getGuild(enemy).setNeutral(this);
        for (String ally : this.allies)
            gm.getGuild(ally).setNeutral(this);
        this.name = newName;
        if (!this.leader.equals("") && !this.leader.equals("Janet"))
            um.getUser(UUID.fromString(this.leader)).joinGuild(this);//Already is this but force updates the name
        for (String id : this.mods)
            um.getUser(UUID.fromString(id)).joinGuild(this);//Already is this but force updates the name
        for (String id : this.members)
            um.getUser(UUID.fromString(id)).joinGuild(this);//Already is this but force updates the name
        for (String enemy : this.enemies)
            gm.getGuild(enemy).addEnemy(this);
        for (String ally : this.allies)
            gm.getGuild(ally).addAlly(this);
    }

    public boolean allowInteract() {
        return this.interact;
    }

    public void setInteract(boolean allow) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        this.interact = allow;
        configGuild.set("flag.interact", allow);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception e) { }
    }

    public boolean canHostileSpawn() {
        return this.hostileSpawn;
    }

    public void setHostileSpawn(boolean can) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        this.hostileSpawn = can;
        configGuild.set("flag.hostileSpawn", can);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception e) { }
    }

    public boolean canExplode() {
        return this.explosions;
    }

    public void setLeader(UUID uuid) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        String name = uuid.toString();
        if (this.leader != null && !this.leader.equalsIgnoreCase("Janet") && !this.leader.equals(""))
            addMod(UUID.fromString(this.leader));
        if (this.mods.contains(name)) {
            this.mods.remove(name);
            List<String> modList = configGuild.getStringList("mods");
            modList.remove(name);
            if (modList.isEmpty())
                modList.add("");
            configGuild.set("mods", modList);
        }
        if (this.members.contains(name)) {
            this.members.remove(name);
            List<String> memberList = configGuild.getStringList("members");
            memberList.remove(name);
            if (memberList.isEmpty())
                memberList.add("");
            configGuild.set("members", memberList);
        }
        this.leader = name;
        configGuild.set("leader", name);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception e) { }
    }

    public double getPower() {
        return this.power;
    }

    public String getName() {
        return this.name;
    }

    public boolean claimed(Chunk c) {
        return this.claims.contains(c.getWorld().getName() + " " + c.getX() + " " + c.getZ());
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String newDesc) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        this.description = newDesc;
        configGuild.set("description", this.description);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception e) { }
    }

    public Location getHome() {
        if (this.home != null && !claimed(this.home.getChunk()))
            delHome();
        return this.home;
    }

    public void setHome(Location l) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        this.home = l;
        configGuild.set("home.world", l.getWorld().getName());
        configGuild.set("home.x", Double.toString(l.getX()));
        configGuild.set("home.y", Double.toString(l.getY()));
        configGuild.set("home.z", Double.toString(l.getZ()));
        configGuild.set("home.yaw", Float.toString(l.getYaw()));
        configGuild.set("home.pitch", Float.toString(l.getPitch()));
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception e) { }
    }

    public boolean isAlly(Guild ally) {
        return ally != null && this.allies.contains(ally.getName());
    }

    public boolean isEnemy(Guild enemy) {
        return enemy != null && this.enemies.contains(enemy.getName());
    }

    public int getLand() {
        return this.claims.size();
    }

    public boolean canPVP() {
        return this.pvp;
    }

    public int getTotal() {
        return totalMembers;
    }

    public int getMaxPower() {
        if (this.power == -1)
            return -1;
        return getTotal() * 20;
    }

    public String onlineMembers(boolean countHidden) {
        CmdHide hide = new CmdHide();
        String member = "";
        if (!this.leader.equals("") && !this.leader.equals("Janet") && Bukkit.getPlayer(UUID.fromString(this.leader)) != null &&
                (countHidden || !hide.isHidden(Bukkit.getPlayer(UUID.fromString(this.leader)))))
            member += Bukkit.getPlayer(UUID.fromString(this.leader)).getName() + ", ";
        if (!this.mods.isEmpty())
            for (String id : this.mods)
                if (Bukkit.getPlayer(UUID.fromString(id)) != null && (countHidden || !hide.isHidden(Bukkit.getPlayer(UUID.fromString(id)))))
                    member += Bukkit.getPlayer(UUID.fromString(id)).getName() + ", ";
        if (!this.members.isEmpty())
            for (String id : this.members)
                if (Bukkit.getPlayer(UUID.fromString(id)) != null && (countHidden || !hide.isHidden(Bukkit.getPlayer(UUID.fromString(id)))))
                    member += Bukkit.getPlayer(UUID.fromString(id)).getName() + ", ";
        if (member.contains(", "))
            member = member.substring(0, member.length() - 2);
        return member;
    }

    public String offlineMember(boolean countHidden) {
        CmdHide hide = new CmdHide();
        String member = "";
        if (!this.leader.equals("") && !this.leader.equals("Janet") && (Bukkit.getPlayer(UUID.fromString(this.leader)) == null ||
                (!countHidden && hide.isHidden(Bukkit.getPlayer(UUID.fromString(this.leader))))))
            member += Bukkit.getOfflinePlayer(UUID.fromString(this.leader)).getName() + ", ";
        if (!this.mods.isEmpty())
            for (String id : this.mods)
                if (Bukkit.getPlayer(UUID.fromString(id)) == null || (!countHidden && hide.isHidden(Bukkit.getPlayer(UUID.fromString(id)))))
                    member += Bukkit.getOfflinePlayer(UUID.fromString(id)).getName() + ", ";
        if (!this.members.isEmpty())
            for (String id : this.members)
                if (Bukkit.getPlayer(UUID.fromString(id)) == null || (!countHidden && hide.isHidden(Bukkit.getPlayer(UUID.fromString(id)))))
                    member += Bukkit.getOfflinePlayer(UUID.fromString(id)).getName() + ", ";
        if (member.contains(", "))
            member = member.substring(0, member.length() - 2);
        return member;
    }

    public int getOnline(boolean countHidden) {
        CmdHide hide = new CmdHide();
        int online = 0;
        if (!this.leader.equals("") && !this.leader.equals("Janet") && Bukkit.getPlayer(UUID.fromString(this.leader)) != null &&
                (countHidden || !hide.isHidden(Bukkit.getPlayer(UUID.fromString(this.leader)))))
            online++;
        if (!this.mods.isEmpty())
            for (String id : this.mods)
                if (Bukkit.getPlayer(UUID.fromString(id)) != null && (countHidden || !hide.isHidden(Bukkit.getPlayer(UUID.fromString(id)))))
                    online++;
        if (!this.members.isEmpty())
            for (String id : this.members)
                if (Bukkit.getPlayer(UUID.fromString(id)) != null && (countHidden || !hide.isHidden(Bukkit.getPlayer(UUID.fromString(id)))))
                    online++;
        return online;
    }

    public String getRank(UUID uuid) {
        if (!this.leader.equals("Janet") && this.leader.equals(uuid.toString()))
            return "leader";
        if (this.mods.contains(uuid.toString()))
            return "mod";
        if (this.members.contains(uuid.toString()))
            return "member";
        return null;
    }

    public ChatColor relation(Guild other) {
        Variables var = new Variables();
        ChatColor col = var.getNeutral();
        if (other != null) {
            if (this.equals(other))
                col = var.getGuildCol();
            else if (isAlly(other))
                col = var.getAlly();
            else if (isEnemy(other))
                col = var.getEnemy();
        }
        return col;
    }

    public ArrayList<String> getAllies() {
        return this.allies;
    }

    public ArrayList<String> getEnemies() {
        return this.enemies;
    }

    private void delHome() {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        this.home = null;
        configGuild.set("home", null);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception e) { }
    }

    public void claim(Chunk c) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        if (!this.claims.contains(c.getWorld().getName() + " " + c.getX() + " " + c.getZ()))
            this.claims.add(c.getWorld().getName() + " " + c.getX() + " " + c.getZ());
        List<String> chunkList = configGuild.getStringList("claims");
        if (!chunkList.contains(c.getWorld().getName() + " " + c.getX() + " " + c.getZ())) {
            chunkList.add(c.getWorld().getName() + " " + c.getX() + " " + c.getZ());
            if (chunkList.contains(""))
                chunkList.remove("");
        }
        configGuild.set("claims", chunkList);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception e) { }
    }

    public void unclaim(Chunk c) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        this.claims.remove(c.getWorld().getName() + " " + c.getX() + " " + c.getZ());
        List<String> chunkList = configGuild.getStringList("claims");
        chunkList.remove(c.getWorld().getName() + " " + c.getX() + " " + c.getZ());
        if (chunkList.isEmpty())
            chunkList.add("");
        configGuild.set("claims", chunkList);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception e) { }
        if (this.home != null && this.home.getChunk().equals(c))
            delHome();
    }

    public void unclaimAll() {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        this.claims.clear();
        delHome();
        configGuild.set("claims", Arrays.asList(""));
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception e) { }
    }

    public boolean isInvited(UUID uuid) {
        return this.invited.contains(uuid);
    }

    public void invite(UUID uuid) {
        this.invited.add(uuid);
    }

    public void uninvite(UUID uuid) {
        this.invited.remove(uuid);
    }

    public boolean isInvitedAlly(Guild g) {
        return this.allyInvites.contains(g);
    }

    public void addAllyInvite(Guild g) {
        this.allyInvites.add(g);
    }

    public void removeAllyInvite(Guild g) {
        this.allyInvites.remove(g);
    }

    public boolean isInvitedNeutral(Guild g) {
        return this.neutralInvites.contains(g);
    }

    public void addNeutralInvite(Guild g) {
        this.neutralInvites.add(g);
    }

    public void removeNeutralInvite(Guild g) {
        this.neutralInvites.remove(g);
    }

    public void addEnemy(Guild g) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        setNeutral(g);
        this.enemies.add(g.getName());
        List<String> enemyList = configGuild.getStringList("enemies");
        enemyList.add(g.getName());
        if (enemyList.contains(""))
            enemyList.remove("");
        configGuild.set("enemies", enemyList);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception e) { }
    }

    public void addAlly(Guild g) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        this.allyInvites.remove(g);
        setNeutral(g);
        this.allies.add(g.getName());
        List<String> allyList = configGuild.getStringList("allies");
        allyList.add(g.getName());
        if (allyList.contains(""))
            allyList.remove("");
        configGuild.set("allies", allyList);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception e) { }
    }

    public void setNeutral(Guild g) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        if (this.allies.contains(g.getName())) {
            this.allies.remove(g.getName());
            List<String> allyList = configGuild.getStringList("allies");
            allyList.remove(g.getName());
            if (allyList.isEmpty())
                allyList.add("");
            configGuild.set("allies", allyList);
        }
        if (this.enemies.contains(g.getName())) {
            this.enemies.remove(g.getName());
            List<String> enemyList = configGuild.getStringList("enemies");
            enemyList.remove(g.getName());
            if (enemyList.isEmpty())
                enemyList.add("");
            configGuild.set("enemies", enemyList);
        }
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception e) { }
        this.neutralInvites.remove(g);
    }

    public boolean isNeutral(Guild g) {
        return !this.allies.contains(g.getName()) && !this.enemies.contains(g.getName());
    }

    public void kick(UUID uuid) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        String name = uuid.toString();
        if (this.leader.equalsIgnoreCase(name)) {
            this.leader = "";
            configGuild.set("leader", "");
        }
        if (this.mods.contains(name)) {
            this.mods.remove(name);
            List<String> modList = configGuild.getStringList("mods");
            modList.remove(name);
            if (modList.isEmpty())
                modList.add("");
            configGuild.set("mods", modList);
        }
        if (this.members.contains(name)) {
            this.members.remove(name);
            List<String> memberList = configGuild.getStringList("members");
            memberList.remove(name);
            if (memberList.isEmpty())
                memberList.add("");
            configGuild.set("members", memberList);
        }
        UserManager um = new UserManager();
        um.getUser(uuid).leaveGuild();
        if (this.totalMembers != 0)
            this.totalMembers--;
        if (!this.permanent && getTotal() == 0) {
            GuildManager gm = new GuildManager();
            gm.disband(this);
        }
        if (this.power != -1)
            this.power -= um.getUser(uuid).getPower();
        configGuild.set("power", this.power);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception e) { }
    }

    public void join(UUID uuid) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        String name = uuid.toString();
        this.members.add(name);
        List<String> memberList = configGuild.getStringList("members");
        memberList.add(name);
        if (memberList.contains(""))
            memberList.remove("");
        configGuild.set("members", memberList);
        UserManager um = new UserManager();
        um.getUser(uuid).joinGuild(this);
        this.invited.remove(uuid);
        this.totalMembers++;
        if (this.power != -1)
            this.power += um.getUser(uuid).getPower();
        configGuild.set("power", this.power);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception e) { }
    }

    public void sendMods(String message) {
        if (!this.leader.equals("") && !this.leader.equals("Janet") && Bukkit.getPlayer(UUID.fromString(this.leader)) != null)
            Bukkit.getPlayer(UUID.fromString(this.leader)).sendMessage(message);
        if (!this.mods.isEmpty())
            for (String id : this.mods)
                if (Bukkit.getPlayer(UUID.fromString(id)) != null)
                    Bukkit.getPlayer(UUID.fromString(id)).sendMessage(message);
    }

    public void setExplode(boolean exp) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        this.explosions = exp;
        configGuild.set("flag.explosions", exp);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception e) { }
    }

    public void setPVP(boolean canPVP) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        this.pvp = canPVP;
        configGuild.set("flag.pvp", canPVP);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception e) { }
    }

    public void toggleInfinite() {
        if (this.power != -1) {
            YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
            configGuild.set("power", -1);
            try {
                configGuild.save(this.configFileGuild);
            } catch (Exception e) { }
        } else {
            this.power = 0;
            updatePower();
        }
    }

    public void disband() {
        UserManager um = new UserManager();
        if (!this.leader.equalsIgnoreCase("Janet") && !this.leader.equalsIgnoreCase(""))
            um.getUser(UUID.fromString(this.leader)).leaveGuild();
        for (String mod : this.mods)
            um.getUser(UUID.fromString(mod)).leaveGuild();
        for (String member : this.members)
            um.getUser(UUID.fromString(member)).leaveGuild();
        this.configFileGuild.delete();
    }

    public void removeMod(UUID uuid) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        String name = uuid.toString();
        this.mods.remove(name);
        List<String> modList = configGuild.getStringList("mods");
        modList.remove(name);
        if (modList.isEmpty())
            modList.add("");
        configGuild.set("mods", modList);
        this.members.add(name);
        List<String> memberList = configGuild.getStringList("members");
        memberList.add(name);
        if (memberList.contains(""))
            memberList.remove("");
        configGuild.set("members", memberList);
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception e) { }
    }

    public void addMod(UUID uuid) {
        YamlConfiguration configGuild = YamlConfiguration.loadConfiguration(this.configFileGuild);
        String name = uuid.toString();
        this.mods.add(name);
        List<String> modList = configGuild.getStringList("mods");
        modList.add(name);
        if (modList.contains(""))
            modList.remove("");
        configGuild.set("mods", modList);
        if (this.leader.equalsIgnoreCase(name)) {
            this.leader = "";
            configGuild.set("leader", "");
        } else {
            this.members.remove(name);
            List<String> memberList = configGuild.getStringList("members");
            memberList.remove(name);
            if (memberList.isEmpty())
                memberList.add("");
            configGuild.set("members", memberList);
        }
        try {
            configGuild.save(this.configFileGuild);
        } catch (Exception e) { }
    }
}