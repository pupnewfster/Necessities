package com.crossge.necessities;

import com.crossge.necessities.Commands.*;
import com.crossge.necessities.Commands.Creative.CmdRequestReview;
import com.crossge.necessities.Commands.Creative.CmdReviewList;
import com.crossge.necessities.Commands.Creative.CreativeCmd;
import com.crossge.necessities.Commands.Economy.*;
import com.crossge.necessities.Commands.Guilds.CmdGuild;
import com.crossge.necessities.Commands.RankManager.*;
import com.crossge.necessities.Commands.WorldManager.*;
import com.crossge.necessities.Economy.*;
import com.crossge.necessities.Guilds.GuildManager;
import com.crossge.necessities.Guilds.PowerManager;
import com.crossge.necessities.Janet.*;
import com.crossge.necessities.RankManager.RankManager;
import com.crossge.necessities.RankManager.User;
import com.crossge.necessities.RankManager.UserManager;
import com.crossge.necessities.WorldManager.PortalManager;
import com.crossge.necessities.WorldManager.WarpManager;
import com.crossge.necessities.WorldManager.WorldManager;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_11_R1.*;
import net.nyvaria.googleanalytics.hit.Hit;
import net.nyvaria.openanalytics.bukkit.OpenAnalytics;
import net.nyvaria.openanalytics.bukkit.OpenAnalyticsTracker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_11_R1.CraftServer;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JsonArray;
import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Necessities extends JavaPlugin {//TODO refactor to gg.galaxygaming instead of com.crossge
    private static Necessities INSTANCE;
    private final List<DevInfo> devs = new ArrayList<>();
    private final File configFile = new File("plugins/Necessities", "config.yml");
    private OpenAnalyticsTracker googleAnalyticsTracker;
    private PacketPlayOutPlayerInfo janetInfo;
    private CmdCommandSpy spy = new CmdCommandSpy();
    private CmdInvsee invsee = new CmdInvsee();
    private PortalManager pm = new PortalManager();
    private WarpManager warps = new WarpManager();
    private WorldManager wm = new WorldManager();
    private UserManager um = new UserManager();
    private RankManager rm = new RankManager();
    private ScoreBoards sb = new ScoreBoards();
    private JanetWarn warns = new JanetWarn();
    private Console console = new Console();
    private Variables var = new Variables();
    private Teleports tps = new Teleports();
    private JanetLog log = new JanetLog();
    private CmdHide hide = new CmdHide();
    private Janet bot = new Janet();
    private JanetNet net = new JanetNet();
    private JanetAI ai = new JanetAI();
    private JanetSlack slack = new JanetSlack();
    private Reviews rev = new Reviews();
    private Economy eco = new Economy();
    private Prices pr = new Prices();
    private RankPrices rp = new RankPrices();
    private CmdPrices cmdp = new CmdPrices();
    private SafeLocation safe = new SafeLocation();
    private GuildManager gm = new GuildManager();
    private PowerManager power = new PowerManager();
    private Signs economySigns = new Signs();
    private JanetSigns signs = new JanetSigns();
    private Wrenched wrench = new Wrenched();
    private AntiCombatLog acb = new AntiCombatLog();
    private JanetRename rename = new JanetRename();
    private JanetBooks books = new JanetBooks();
    private Announcer announcer = new Announcer();
    private final DisabledCmd disabled = new DisabledCmd();

    public static Necessities getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        getLogger().info("Enabling Necessities...");
        INSTANCE = this;

        if (!hookGoogle())
            getLogger().warning("Could not hook into Google Analytics!");

        Initialization init = new Initialization();
        init.initiateFiles();
        getServer().getPluginManager().registerEvents(new Listeners(), this);

        if (Bukkit.getPluginManager().getPlugin("Vault") != null)
            Bukkit.getServicesManager().register(net.milkbowl.vault.economy.Economy.class, new VaultEconomy(), Bukkit.getPluginManager().getPlugin("Vault"), ServicePriority.Normal);

        getDevInfo();
        getLogger().info("Necessities enabled.");
        GameProfile janetProfile = new GameProfile(UUID.randomUUID(), "Janet");
        janetProfile.getProperties().put("textures", getSkin());
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = server.getWorldServer(0);
        PlayerInteractManager manager = new PlayerInteractManager(world);
        EntityPlayer player = new EntityPlayer(server, world, janetProfile, manager);
        player.listName = formatMessage(ChatColor.translateAlternateColorCodes('&', rm.getRank(rm.getOrder().size() - 1).getTitle() + " ") + "Janet");
        this.janetInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, player);
    }

    private boolean hookGoogle() {
        OpenAnalytics plugin;
        if ((plugin = (OpenAnalytics) getServer().getPluginManager().getPlugin("OpenAnalytics")) == null)
            return false;
        googleAnalyticsTracker = plugin.getTracker();
        return true;
    }

    public static boolean isTracking() {
        return getTracker() != null;
    }

    private static OpenAnalyticsTracker getTracker() {
        return getInstance().googleAnalyticsTracker;
    }

    private IChatBaseComponent formatMessage(String message) {
        return IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
    }

    public void removePlayer(Player p) {
        PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) p).getHandle());
        for (Player x : Bukkit.getOnlinePlayers())
            if (!x.canSee(p) && !x.equals(p))
                ((CraftPlayer) x).getHandle().playerConnection.sendPacket(info);
    }

    public void addPlayer(Player p) {
        EntityPlayer ep = ((CraftPlayer) p).getHandle();
        User u = um.getUser(p.getUniqueId());
        ep.listName = formatMessage(u.getRank() == null ? "" : ChatColor.translateAlternateColorCodes('&', u.getRank().getTitle() + " ") + p.getDisplayName());
        PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ep);
        for (Player x : Bukkit.getOnlinePlayers())
            if (!x.hasPermission("Necessities.seehidden") && x.canSee(p) && !x.equals(p))
                ((CraftPlayer) x).getHandle().playerConnection.sendPacket(info);
    }

    public void updateName(Player p) {
        EntityPlayer ep = ((CraftPlayer) p).getHandle();
        User u = um.getUser(p.getUniqueId());
        ep.listName = formatMessage(u.getRank() == null ? "" : ChatColor.translateAlternateColorCodes('&', u.getRank().getTitle() + " ") + p.getDisplayName());
        PacketPlayOutPlayerInfo tabList = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, ep);
        Bukkit.getOnlinePlayers().forEach(x -> ((CraftPlayer) x).getHandle().playerConnection.sendPacket(tabList));
    }

    public void updateAll(Player x) {
        ArrayList<EntityPlayer> players = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            EntityPlayer ep = ((CraftPlayer) p).getHandle();
            User u = um.getUser(p.getUniqueId());
            ep.listName = formatMessage(u.getRank() == null ? "" : ChatColor.translateAlternateColorCodes('&', u.getRank().getTitle() + " ") + p.getDisplayName());
            players.add(ep);
        }
        ((CraftPlayer) x).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, players));
    }

    void addJanet(Player p) {
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(this.janetInfo);
    }

    void addHeader(Player p) {
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(formatMessage(ChatColor.AQUA + "Galaxy Gaming"));
        try {
            Field field = packet.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(packet, formatMessage(ChatColor.GREEN + "http://galaxygaming.gg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }

    private Property getSkin() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL("https://sessionserver.mojang.com/session/minecraft/profile/136f2ba62be3444ca2968ec597edb57e?unsigned=false").openConnection().getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            in.close();
            JsonObject json = Jsoner.deserialize(response.toString(), new JsonObject());
            JsonObject jo = (JsonObject) ((JsonArray) json.get("properties")).get(0);
            String signature = jo.getString("signature"), value = jo.getString("value");
            return new Property("textures", value, signature);
        } catch (Exception ignored) {
        }
        return null;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        Cmd c = getCmd(cmd.getName());
        return c != null && c.commandUse(sender, args);
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (sender == null || cmd == null)
            return null;
        Cmd c = getCmd(cmd.getName());
        if (c == null)
            return null;
        List<String> tab = c.tabComplete(sender, args);
        if (tab == null || tab.isEmpty())
            return null;
        return tab;
    }

    private boolean isEqual(String command, String toCheck) {
        return command.equalsIgnoreCase(toCheck);
    }

    private Cmd getCmd(String name) {
        Cmd com = null;
        if (isEqual(name, "slap"))
            com = new CmdSlap();
        else if (isEqual(name, "warn"))
            com = new CmdWarn();
        else if (isEqual(name, "enchant"))
            com = new CmdEnchant();
        else if (isEqual(name, "ragequit"))
            com = new CmdRagequit();
        else if (isEqual(name, "highfive"))
            com = new CmdHighfive();
        else if (isEqual(name, "devs"))
            com = new CmdDevs();
        else if (isEqual(name, "loginmessage"))
            com = new CmdLogInMessage();
        else if (isEqual(name, "logoutmessage"))
            com = new CmdLogOutMessage();
        else if (isEqual(name, "imp"))
            com = new CmdImp();
        else if (isEqual(name, "pants"))
            com = new CmdPants();
        else if (isEqual(name, "boots"))
            com = new CmdBoots();
        else if (isEqual(name, "chest"))
            com = new CmdChest();
        else if (isEqual(name, "blockhat"))
            com = new CmdBlockHat();
        else if (isEqual(name, "hat"))
            com = new CmdHat();
        else if (isEqual(name, "workbench"))
            com = new CmdWorkbench();
        else if (isEqual(name, "hide"))
            com = this.hide;
        else if (isEqual(name, "rename"))
            com = new CmdRename();
        else if (isEqual(name, "title"))
            com = new CmdTitle();
        else if (isEqual(name, "table"))
            com = new CmdTable();
        else if (isEqual(name, "tpdim"))
            com = new CmdTpDim();
        else if (isEqual(name, "bracketcolor"))
            com = new CmdBracketColor();
        else if (isEqual(name, "commandspy"))
            com = this.spy;
        else if (isEqual(name, "setspawn"))
            com = new CmdSetspawn();
        else if (isEqual(name, "spawn"))
            com = new CmdSpawn();
        else if (isEqual(name, "time"))
            com = new CmdTime();
        else if (isEqual(name, "weather"))
            com = new CmdWeather();
        else if (isEqual(name, "gamemode"))
            com = new CmdGamemode();
        else if (isEqual(name, "god"))
            com = new CmdGod();
        else if (isEqual(name, "heal"))
            com = new CmdHeal();
        else if (isEqual(name, "feed"))
            com = new CmdFeed();
        else if (isEqual(name, "fly"))
            com = new CmdFly();
        else if (isEqual(name, "more"))
            com = new CmdMore();
        else if (isEqual(name, "repair"))
            com = new CmdRepair();
        else if (isEqual(name, "afk"))
            com = new CmdAfk();
        else if (isEqual(name, "who"))
            com = new CmdWho();
        else if (isEqual(name, "me"))
            com = new CmdMe();
        else if (isEqual(name, "nick"))
            com = new CmdNick();
        else if (isEqual(name, "kick"))
            com = new CmdKick();
        else if (isEqual(name, "ext"))
            com = new CmdExt();
        else if (isEqual(name, "enderchest"))
            com = new CmdEnderChest();
        else if (isEqual(name, "invsee"))
            com = this.invsee;
        else if (isEqual(name, "slack"))
            com = new CmdSlack();
        else if (isEqual(name, "requestmod"))
            com = new CmdRequestMod();
        else if (isEqual(name, "top"))
            com = new CmdTop();
        else if (isEqual(name, "speed"))
            com = new CmdSpeed();
        else if (isEqual(name, "suicide"))
            com = new CmdSuicide();
        else if (isEqual(name, "back"))
            com = new CmdBack();
        else if (isEqual(name, "setjail"))
            com = new CmdSetJail();
        else if (isEqual(name, "jail"))
            com = new CmdJail();
        else if (isEqual(name, "kill"))
            com = new CmdKill();
        else if (isEqual(name, "mute"))
            com = new CmdMute();
        else if (isEqual(name, "motd"))
            com = new CmdMotd();
        else if (isEqual(name, "rules"))
            com = new CmdRules();
        else if (isEqual(name, "exp"))
            com = new CmdExp();
        else if (isEqual(name, "lightning"))
            com = new CmdLightning();
        else if (isEqual(name, "skull"))
            com = new CmdSkull();
        else if (isEqual(name, "sethome"))
            com = new CmdSetHome();
        else if (isEqual(name, "home"))
            com = new CmdHome();
        else if (isEqual(name, "delhome"))
            com = new CmdDelHome();
        else if (isEqual(name, "help"))
            com = new CmdHelp();
        else if (isEqual(name, "spawner"))
            com = new CmdSpawner();
        else if (isEqual(name, "spawnmob"))
            com = new CmdSpawnmob();
        else if (isEqual(name, "ignore"))
            com = new CmdIgnore();
        else if (isEqual(name, "msg"))
            com = new CmdMsg();
        else if (isEqual(name, "reply"))
            com = new CmdReply();
        else if (isEqual(name, "say"))
            com = new CmdSay();
        else if (isEqual(name, "item"))
            com = new CmdItem();
        else if (isEqual(name, "give"))
            com = new CmdGive();
        else if (isEqual(name, "clear"))
            com = new CmdClear();
        else if (isEqual(name, "killall"))
            com = new CmdKillall();
        else if (isEqual(name, "togglechat"))
            com = new CmdToggleChat();
        else if (isEqual(name, "ban"))
            com = new CmdBan();
        else if (isEqual(name, "tempban"))
            com = new CmdTempban();
        else if (isEqual(name, "unban"))
            com = new CmdUnban();
        else if (isEqual(name, "banip"))
            com = new CmdBanIP();
        else if (isEqual(name, "unbanip"))
            com = new CmdUnbanIP();
        else if (isEqual(name, "jump"))
            com = new CmdJump();
        else if (isEqual(name, "tp"))
            com = new CmdTp();
        else if (isEqual(name, "tphere"))
            com = new CmdTphere();
        else if (isEqual(name, "tppos"))
            com = new CmdTppos();
        else if (isEqual(name, "tpall"))
            com = new CmdTpall();
        else if (isEqual(name, "tpa"))
            com = new CmdTpa();
        else if (isEqual(name, "tpahere"))
            com = new CmdTpahere();
        else if (isEqual(name, "tpaccept"))
            com = new CmdTpaccept();
        else if (isEqual(name, "tpdeny"))
            com = new CmdTpdeny();
        else if (isEqual(name, "faq"))
            com = new CmdFaq();
        else if (isEqual(name, "opbroadcast"))
            com = new CmdOpChat();
        else if (isEqual(name, "craft"))
            com = new CmdCraft();
        else if (isEqual(name, "machinegun"))
            com = new CmdMachineGun();
        else if (isEqual(name, "bazooka"))
            com = new CmdBazooka();
        else if (isEqual(name, "wrench"))
            com = new CmdWrench();
        else if (isEqual(name, "tps"))
            com = new CmdTps();
        else if (isEqual(name, "reloadannouncer"))
            com = new CmdReloadAnnouncer();
            //Economy
        else if (isEqual(name, "bal"))
            com = new CmdBalance();
        else if (isEqual(name, "baltop"))
            com = new CmdBaltop();
        else if (isEqual(name, "pricelist"))
            com = new CmdPriceList();
        else if (isEqual(name, "pay"))
            com = new CmdPay();
        else if (isEqual(name, "eco"))
            com = new CmdEco();
        else if (isEqual(name, "price"))
            com = new CmdPrice();
        else if (isEqual(name, "setprice"))
            com = new CmdSetPrice();
        else if (isEqual(name, "buy"))
            com = new CmdBuy();
        else if (isEqual(name, "sell"))
            com = new CmdSell();
        else if (isEqual(name, "players"))
            com = new CmdPlayers();
        else if (isEqual(name, "rankprices"))
            com = new CmdRankPrices();
        else if (isEqual(name, "setrankprice"))
            com = new CmdSetRankPrice();
        else if (isEqual(name, "buyrank"))
            com = new CmdBuyRank();
        else if (isEqual(name, "l2m"))
            com = new CmdL2M();
        else if (isEqual(name, "commandprices"))
            com = new CmdCmdPrices();
        else if (isEqual(name, "setcommandprice"))
            com = new CmdSetCmdPrice();
            //RankManager
        else if (isEqual(name, "promote"))
            com = new CmdPromote();
        else if (isEqual(name, "demote"))
            com = new CmdDemote();
        else if (isEqual(name, "setrank"))
            com = new CmdSetrank();
        else if (isEqual(name, "addpermission"))
            com = new CmdAddPermission();
        else if (isEqual(name, "delpermission"))
            com = new CmdDelPermission();
        else if (isEqual(name, "addpermsubrank"))
            com = new CmdAddPermSubrank();
        else if (isEqual(name, "delpermsubrank"))
            com = new CmdDelPermSubrank();
        else if (isEqual(name, "addpermissionuser"))
            com = new CmdAddPermissionUser();
        else if (isEqual(name, "delpermissionuser"))
            com = new CmdDelPermissionUser();
        else if (isEqual(name, "addsubrank"))
            com = new CmdAddSubrank();
        else if (isEqual(name, "delsubrank"))
            com = new CmdDelSubrank();
        else if (isEqual(name, "addsubrankuser"))
            com = new CmdAddSubrankUser();
        else if (isEqual(name, "delsubrankuser"))
            com = new CmdDelSubrankUser();
        else if (isEqual(name, "createsubrank"))
            com = new CmdCreateSubrank();
        else if (isEqual(name, "removesubrank"))
            com = new CmdRemoveSubrank();
        else if (isEqual(name, "createrank"))
            com = new CmdCreateRank();
        else if (isEqual(name, "removerank"))
            com = new CmdRemoveRank();
        else if (isEqual(name, "whois"))
            com = new CmdWhois();
        else if (isEqual(name, "ranks"))
            com = new CmdRanks();
        else if (isEqual(name, "subranks"))
            com = new CmdSubranks();
        else if (isEqual(name, "rankcmds"))
            com = new CmdRankCmds();
        else if (isEqual(name, "reloadpermissions"))
            com = new CmdReloadPermissions();
            //WorldManager
        else if (isEqual(name, "createworld"))
            com = new CmdCreateWorld();
        else if (isEqual(name, "worldspawn"))
            com = new CmdWorldSpawn();
        else if (isEqual(name, "loadworld"))
            com = new CmdLoadWorld();
        else if (isEqual(name, "unloadworld"))
            com = new CmdUnloadWorld();
        else if (isEqual(name, "removeworld"))
            com = new CmdRemoveWorld();
        else if (isEqual(name, "worlds"))
            com = new CmdWorlds();
        else if (isEqual(name, "world"))
            com = new CmdWorld();
        else if (isEqual(name, "setworldspawn"))
            com = new CmdSetWorldSpawn();
        else if (isEqual(name, "modifyworld"))
            com = new CmdModifyWorld();
        else if (isEqual(name, "createportal"))
            com = new CmdCreatePortal();
        else if (isEqual(name, "removeportal"))
            com = new CmdRemovePortal();
        else if (isEqual(name, "warps"))
            com = new CmdWarps();
        else if (isEqual(name, "warp"))
            com = new CmdWarp();
        else if (isEqual(name, "createwarp"))
            com = new CmdCreateWarp();
        else if (isEqual(name, "removewarp"))
            com = new CmdRemoveWarp();
            //Guilds
        else if (isEqual(name, "guild"))
            com = new CmdGuild();
            //Creative
        else if (isEqual(name, "requestreview"))
            com = new CmdRequestReview();
        else if (isEqual(name, "reviewlist"))
            com = new CmdReviewList();

        if (com == null)
            return null;

        YamlConfiguration config = getConfig();
        if (!com.isPaintballEnabled() && config.contains("Necessities.Paintball") && config.getBoolean("Necessities.Paintball"))
            com = this.disabled;
        else if (com instanceof WorldCmd && config.contains("Necessities.WorldManager") && !config.getBoolean("Necessities.WorldManager"))
            com = this.disabled;
        else if (com instanceof CmdGuild && config.contains("Necessities.Guilds") && !config.getBoolean("Necessities.Guilds"))
            com = this.disabled;
        else if (com instanceof EconomyCmd && config.contains("Necessities.Economy") && !config.getBoolean("Necessities.Economy"))
            com = this.disabled;
        else if (com instanceof CreativeCmd && config.contains("Necessities.Creative") && !config.getBoolean("Necessities.Creative"))
            com = this.disabled;
        return com;
    }

    public boolean isCommandEnabled(String cmd) {
        Cmd c = getCmd(cmd);
        return c != null && !c.equals(this.disabled);
    }

    @Override
    public void onDisable() {
        this.power.unload();
        this.um.unload();
        this.spy.unload();
        this.hide.unload();
        this.slack.disconnect();
        this.bot.unload();
        this.announcer.exit();
        getLogger().info("Necessities disabled.");
    }

    public boolean isDev(UUID uuid) {
        for (DevInfo i : this.devs)
            if (uuid.equals(i.getMCUUID()))
                return true;
        return false;
    }

    public List<DevInfo> getDevs() {
        return Collections.unmodifiableList(this.devs);
    }

    private void getDevInfo() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL("http://galaxygaming.gg/staff.json").openConnection().getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            in.close();
            JsonObject json = Jsoner.deserialize(response.toString(), new JsonObject());
            JsonArray ar = (JsonArray) json.get("devs");
            JsonObject ls = (JsonObject) json.get("Lavasurvival");
            JsonArray lsDevs = (JsonArray) ls.get("devs");
            for (int i = 0; i < lsDevs.size(); i++) {
                JsonObject dev = null;
                int devID = lsDevs.getInteger(i);
                for (Object a : ar)
                    if (devID == ((JsonObject) a).getInteger("devID")) {
                        dev = (JsonObject) a;
                        break;
                    }
                if (dev != null)
                    this.devs.add(new DevInfo(dev));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class DevInfo {
        private final UUID mcUUID;
        private final String slackID;
        private final String name;

        private DevInfo(JsonObject dev) {
            this.mcUUID = UUID.fromString(dev.getString("mcUUID"));
            this.slackID = dev.getString("slackID");
            this.name = dev.getString("name");
        }

        public String getName() {
            return this.name;
        }

        public UUID getMCUUID() {
            return this.mcUUID;
        }
    }

    public static void trackAction(Hit hit) {
        OpenAnalyticsTracker tracker;
        if ((tracker = getTracker()) != null)
            tracker.trackHit(hit);
    }

    public UserManager getUM() {
        return this.um == null ? this.um = new UserManager() : this.um;
    }

    public JanetNet getNet() {
        return this.net == null ? this.net = new JanetNet() : this.net;
    }

    CmdCommandSpy getSpy() {
        return this.spy == null ? this.spy = new CmdCommandSpy() : this.spy;
    }

    public RankManager getRM() {
        return this.rm == null ? this.rm = new RankManager() : this.rm;
    }

    public PortalManager getPM() {
        return this.pm == null ? this.pm = new PortalManager() : this.pm;
    }

    public JanetSlack getSlack() {
        return this.slack == null ? this.slack = new JanetSlack() : this.slack;
    }

    public Console getConsole() {
        return this.console == null ? this.console = new Console() : this.console;
    }

    public Variables getVar() {
        return this.var == null ? this.var = new Variables() : this.var;
    }

    public Teleports getTPs() {
        return this.tps == null ? this.tps = new Teleports() : this.tps;
    }

    public JanetWarn getWarns() {
        return this.warns == null ? this.warns = new JanetWarn() : this.warns;
    }

    public ScoreBoards getSBs() {
        return this.sb == null ? this.sb = new ScoreBoards() : this.sb;
    }

    public CmdHide getHide() {
        return this.hide == null ? this.hide = new CmdHide() : this.hide;
    }

    public JanetAI getAI() {
        return this.ai == null ? this.ai = new JanetAI() : this.ai;
    }

    public Janet getBot() {
        return this.bot == null ? this.bot = new Janet() : this.bot;
    }

    public WarpManager getWarps() {
        return this.warps == null ? this.warps = new WarpManager() : this.warps;
    }

    public WorldManager getWM() {
        return this.wm == null ? this.wm = new WorldManager() : this.wm;
    }

    public GuildManager getGM() {
        return this.gm == null ? this.gm = new GuildManager() : this.gm;
    }

    public Teleports getTeleports() {
        return this.tps == null ? this.tps = new Teleports() : this.tps;
    }

    CmdInvsee getInvsee() {
        return this.invsee == null ? this.invsee = new CmdInvsee() : this.invsee;
    }

    JanetBooks getBooks() {
        return this.books == null ? this.books = new JanetBooks() : this.books;
    }

    public JanetRename getRename() {
        return this.rename == null ? this.rename = new JanetRename() : this.rename;
    }

    public Reviews getRev() {
        return this.rev == null ? this.rev = new Reviews() : this.rev;
    }

    Signs getEconomySigns() {
        return this.economySigns == null ? this.economySigns = new Signs() : this.economySigns;
    }

    JanetSigns getSigns() {
        return this.signs == null ? this.signs = new JanetSigns() : this.signs;
    }

    Wrenched getWrench() {
        return this.wrench == null ? this.wrench = new Wrenched() : this.wrench;
    }

    AntiCombatLog getACB() {
        return this.acb == null ? this.acb = new AntiCombatLog() : this.acb;
    }

    public PowerManager getPower() {
        return this.power == null ? this.power = new PowerManager() : this.power;
    }

    public SafeLocation getSafeLocations() {
        return this.safe == null ? this.safe = new SafeLocation() : this.safe;
    }

    public JanetLog getLog() {
        return this.log == null ? this.log = new JanetLog() : this.log;
    }

    public Economy getEconomy() {
        return this.eco == null ? this.eco = new Economy() : this.eco;
    }

    public CmdPrices getCommandPrices() {
        return this.cmdp == null ? this.cmdp = new CmdPrices() : this.cmdp;
    }

    public RankPrices getRankPrices() {
        return this.rp == null ? this.rp = new RankPrices() : this.rp;
    }

    public Prices getPrices() {
        return this.pr == null ? this.pr = new Prices() : this.pr;
    }

    public Announcer getAnnouncer() {
        return this.announcer == null ? this.announcer = new Announcer() : this.announcer;
    }

    public File getConfigFile() {
        return this.configFile;
    }

    public YamlConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(getConfigFile());
    }
}