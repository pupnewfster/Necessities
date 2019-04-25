package gg.galaxygaming.necessities;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import gg.galaxygaming.necessities.Commands.Cmd;
import gg.galaxygaming.necessities.Commands.CmdAfk;
import gg.galaxygaming.necessities.Commands.CmdBack;
import gg.galaxygaming.necessities.Commands.CmdBan;
import gg.galaxygaming.necessities.Commands.CmdBanIP;
import gg.galaxygaming.necessities.Commands.CmdBazooka;
import gg.galaxygaming.necessities.Commands.CmdBlockHat;
import gg.galaxygaming.necessities.Commands.CmdBoots;
import gg.galaxygaming.necessities.Commands.CmdBracketColor;
import gg.galaxygaming.necessities.Commands.CmdChest;
import gg.galaxygaming.necessities.Commands.CmdClear;
import gg.galaxygaming.necessities.Commands.CmdCommandSpy;
import gg.galaxygaming.necessities.Commands.CmdCraft;
import gg.galaxygaming.necessities.Commands.CmdDelHome;
import gg.galaxygaming.necessities.Commands.CmdDevs;
import gg.galaxygaming.necessities.Commands.CmdEnchant;
import gg.galaxygaming.necessities.Commands.CmdEnderChest;
import gg.galaxygaming.necessities.Commands.CmdExp;
import gg.galaxygaming.necessities.Commands.CmdExt;
import gg.galaxygaming.necessities.Commands.CmdFaq;
import gg.galaxygaming.necessities.Commands.CmdFeed;
import gg.galaxygaming.necessities.Commands.CmdFly;
import gg.galaxygaming.necessities.Commands.CmdFreeze;
import gg.galaxygaming.necessities.Commands.CmdGamemode;
import gg.galaxygaming.necessities.Commands.CmdGive;
import gg.galaxygaming.necessities.Commands.CmdGod;
import gg.galaxygaming.necessities.Commands.CmdHat;
import gg.galaxygaming.necessities.Commands.CmdHeal;
import gg.galaxygaming.necessities.Commands.CmdHelp;
import gg.galaxygaming.necessities.Commands.CmdHide;
import gg.galaxygaming.necessities.Commands.CmdHighfive;
import gg.galaxygaming.necessities.Commands.CmdHome;
import gg.galaxygaming.necessities.Commands.CmdIgnore;
import gg.galaxygaming.necessities.Commands.CmdImp;
import gg.galaxygaming.necessities.Commands.CmdInvsee;
import gg.galaxygaming.necessities.Commands.CmdItem;
import gg.galaxygaming.necessities.Commands.CmdJail;
import gg.galaxygaming.necessities.Commands.CmdJump;
import gg.galaxygaming.necessities.Commands.CmdKick;
import gg.galaxygaming.necessities.Commands.CmdKill;
import gg.galaxygaming.necessities.Commands.CmdKillall;
import gg.galaxygaming.necessities.Commands.CmdLightning;
import gg.galaxygaming.necessities.Commands.CmdLogInMessage;
import gg.galaxygaming.necessities.Commands.CmdLogOutMessage;
import gg.galaxygaming.necessities.Commands.CmdMachineGun;
import gg.galaxygaming.necessities.Commands.CmdMe;
import gg.galaxygaming.necessities.Commands.CmdMore;
import gg.galaxygaming.necessities.Commands.CmdMotd;
import gg.galaxygaming.necessities.Commands.CmdMsg;
import gg.galaxygaming.necessities.Commands.CmdMute;
import gg.galaxygaming.necessities.Commands.CmdNick;
import gg.galaxygaming.necessities.Commands.CmdOpChat;
import gg.galaxygaming.necessities.Commands.CmdPants;
import gg.galaxygaming.necessities.Commands.CmdRagequit;
import gg.galaxygaming.necessities.Commands.CmdReloadAnnouncer;
import gg.galaxygaming.necessities.Commands.CmdRename;
import gg.galaxygaming.necessities.Commands.CmdRepair;
import gg.galaxygaming.necessities.Commands.CmdReply;
import gg.galaxygaming.necessities.Commands.CmdRequestMod;
import gg.galaxygaming.necessities.Commands.CmdRules;
import gg.galaxygaming.necessities.Commands.CmdSay;
import gg.galaxygaming.necessities.Commands.CmdSetHome;
import gg.galaxygaming.necessities.Commands.CmdSetJail;
import gg.galaxygaming.necessities.Commands.CmdSetspawn;
import gg.galaxygaming.necessities.Commands.CmdSkull;
import gg.galaxygaming.necessities.Commands.CmdSlack;
import gg.galaxygaming.necessities.Commands.CmdSlap;
import gg.galaxygaming.necessities.Commands.CmdSpawn;
import gg.galaxygaming.necessities.Commands.CmdSpawner;
import gg.galaxygaming.necessities.Commands.CmdSpawnmob;
import gg.galaxygaming.necessities.Commands.CmdSpeed;
import gg.galaxygaming.necessities.Commands.CmdSuicide;
import gg.galaxygaming.necessities.Commands.CmdTable;
import gg.galaxygaming.necessities.Commands.CmdTempban;
import gg.galaxygaming.necessities.Commands.CmdTime;
import gg.galaxygaming.necessities.Commands.CmdTitle;
import gg.galaxygaming.necessities.Commands.CmdToggleChat;
import gg.galaxygaming.necessities.Commands.CmdTop;
import gg.galaxygaming.necessities.Commands.CmdTp;
import gg.galaxygaming.necessities.Commands.CmdTpDim;
import gg.galaxygaming.necessities.Commands.CmdTpa;
import gg.galaxygaming.necessities.Commands.CmdTpaccept;
import gg.galaxygaming.necessities.Commands.CmdTpahere;
import gg.galaxygaming.necessities.Commands.CmdTpall;
import gg.galaxygaming.necessities.Commands.CmdTpdeny;
import gg.galaxygaming.necessities.Commands.CmdTphere;
import gg.galaxygaming.necessities.Commands.CmdTppos;
import gg.galaxygaming.necessities.Commands.CmdTps;
import gg.galaxygaming.necessities.Commands.CmdUnban;
import gg.galaxygaming.necessities.Commands.CmdUnbanIP;
import gg.galaxygaming.necessities.Commands.CmdWarn;
import gg.galaxygaming.necessities.Commands.CmdWeather;
import gg.galaxygaming.necessities.Commands.CmdWho;
import gg.galaxygaming.necessities.Commands.CmdWorkbench;
import gg.galaxygaming.necessities.Commands.CmdWrench;
import gg.galaxygaming.necessities.Commands.Creative.CmdRequestReview;
import gg.galaxygaming.necessities.Commands.Creative.CmdReviewList;
import gg.galaxygaming.necessities.Commands.Creative.CreativeCmd;
import gg.galaxygaming.necessities.Commands.DisabledCmd;
import gg.galaxygaming.necessities.Commands.Economy.CmdBalance;
import gg.galaxygaming.necessities.Commands.Economy.CmdBaltop;
import gg.galaxygaming.necessities.Commands.Economy.CmdBuy;
import gg.galaxygaming.necessities.Commands.Economy.CmdBuyCmd;
import gg.galaxygaming.necessities.Commands.Economy.CmdBuyRank;
import gg.galaxygaming.necessities.Commands.Economy.CmdCmdPrices;
import gg.galaxygaming.necessities.Commands.Economy.CmdEco;
import gg.galaxygaming.necessities.Commands.Economy.CmdL2M;
import gg.galaxygaming.necessities.Commands.Economy.CmdPay;
import gg.galaxygaming.necessities.Commands.Economy.CmdPlayers;
import gg.galaxygaming.necessities.Commands.Economy.CmdPrice;
import gg.galaxygaming.necessities.Commands.Economy.CmdPriceList;
import gg.galaxygaming.necessities.Commands.Economy.CmdRankPrices;
import gg.galaxygaming.necessities.Commands.Economy.CmdSell;
import gg.galaxygaming.necessities.Commands.Economy.CmdSetCmdPrice;
import gg.galaxygaming.necessities.Commands.Economy.CmdSetPrice;
import gg.galaxygaming.necessities.Commands.Economy.CmdSetRankPrice;
import gg.galaxygaming.necessities.Commands.Economy.EconomyCmd;
import gg.galaxygaming.necessities.Commands.Guilds.CmdGuild;
import gg.galaxygaming.necessities.Commands.RankManager.CmdAddPermSubrank;
import gg.galaxygaming.necessities.Commands.RankManager.CmdAddPermission;
import gg.galaxygaming.necessities.Commands.RankManager.CmdAddPermissionUser;
import gg.galaxygaming.necessities.Commands.RankManager.CmdAddSubrank;
import gg.galaxygaming.necessities.Commands.RankManager.CmdAddSubrankUser;
import gg.galaxygaming.necessities.Commands.RankManager.CmdCreateRank;
import gg.galaxygaming.necessities.Commands.RankManager.CmdCreateSubrank;
import gg.galaxygaming.necessities.Commands.RankManager.CmdDelPermSubrank;
import gg.galaxygaming.necessities.Commands.RankManager.CmdDelPermission;
import gg.galaxygaming.necessities.Commands.RankManager.CmdDelPermissionUser;
import gg.galaxygaming.necessities.Commands.RankManager.CmdDelSubrank;
import gg.galaxygaming.necessities.Commands.RankManager.CmdDelSubrankUser;
import gg.galaxygaming.necessities.Commands.RankManager.CmdDemote;
import gg.galaxygaming.necessities.Commands.RankManager.CmdPromote;
import gg.galaxygaming.necessities.Commands.RankManager.CmdRankCmds;
import gg.galaxygaming.necessities.Commands.RankManager.CmdRanks;
import gg.galaxygaming.necessities.Commands.RankManager.CmdReloadPermissions;
import gg.galaxygaming.necessities.Commands.RankManager.CmdRemoveRank;
import gg.galaxygaming.necessities.Commands.RankManager.CmdRemoveSubrank;
import gg.galaxygaming.necessities.Commands.RankManager.CmdSetrank;
import gg.galaxygaming.necessities.Commands.RankManager.CmdSubranks;
import gg.galaxygaming.necessities.Commands.RankManager.CmdWhois;
import gg.galaxygaming.necessities.Commands.WorldManager.CmdCreatePortal;
import gg.galaxygaming.necessities.Commands.WorldManager.CmdCreateWarp;
import gg.galaxygaming.necessities.Commands.WorldManager.CmdCreateWorld;
import gg.galaxygaming.necessities.Commands.WorldManager.CmdLoadWorld;
import gg.galaxygaming.necessities.Commands.WorldManager.CmdModifyWorld;
import gg.galaxygaming.necessities.Commands.WorldManager.CmdRemovePortal;
import gg.galaxygaming.necessities.Commands.WorldManager.CmdRemoveWarp;
import gg.galaxygaming.necessities.Commands.WorldManager.CmdRemoveWorld;
import gg.galaxygaming.necessities.Commands.WorldManager.CmdSetWorldSpawn;
import gg.galaxygaming.necessities.Commands.WorldManager.CmdUnloadWorld;
import gg.galaxygaming.necessities.Commands.WorldManager.CmdWarp;
import gg.galaxygaming.necessities.Commands.WorldManager.CmdWarps;
import gg.galaxygaming.necessities.Commands.WorldManager.CmdWorld;
import gg.galaxygaming.necessities.Commands.WorldManager.CmdWorldSpawn;
import gg.galaxygaming.necessities.Commands.WorldManager.CmdWorlds;
import gg.galaxygaming.necessities.Commands.WorldManager.WorldCmd;
import gg.galaxygaming.necessities.Economy.CmdPrices;
import gg.galaxygaming.necessities.Economy.Economy;
import gg.galaxygaming.necessities.Economy.Prices;
import gg.galaxygaming.necessities.Economy.RankPrices;
import gg.galaxygaming.necessities.Economy.Signs;
import gg.galaxygaming.necessities.Economy.VaultEconomy;
import gg.galaxygaming.necessities.Guilds.GuildManager;
import gg.galaxygaming.necessities.Guilds.PowerManager;
import gg.galaxygaming.necessities.Janet.Janet;
import gg.galaxygaming.necessities.Janet.JanetAI;
import gg.galaxygaming.necessities.Janet.JanetBooks;
import gg.galaxygaming.necessities.Janet.JanetLog;
import gg.galaxygaming.necessities.Janet.JanetRename;
import gg.galaxygaming.necessities.Janet.JanetSigns;
import gg.galaxygaming.necessities.Janet.JanetSlack;
import gg.galaxygaming.necessities.Janet.JanetWarn;
import gg.galaxygaming.necessities.RankManager.RankManager;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.RankManager.UserManager;
import gg.galaxygaming.necessities.WorldManager.PortalManager;
import gg.galaxygaming.necessities.WorldManager.WarpManager;
import gg.galaxygaming.necessities.WorldManager.WorldManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraft.server.v1_14_R1.DimensionManager;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import net.minecraft.server.v1_14_R1.IChatBaseComponent;
import net.minecraft.server.v1_14_R1.MinecraftServer;
import net.minecraft.server.v1_14_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_14_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_14_R1.PlayerInteractManager;
import net.minecraft.server.v1_14_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_14_R1.CraftServer;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

/*import net.nyvaria.googleanalytics.hit.Hit;
import net.nyvaria.openanalytics.bukkit.OpenAnalytics;
import net.nyvaria.openanalytics.bukkit.OpenAnalyticsTracker;*/

public class Necessities extends JavaPlugin {

    private static Necessities INSTANCE;
    private final List<DevInfo> devs = new ArrayList<>();
    private final File configFile = new File("plugins/Necessities", "config.yml");
    //private OpenAnalyticsTracker googleAnalyticsTracker;
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
    //private JanetNet net = new JanetNet();
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

        /*if (!hookGoogle())
            getLogger().warning("Could not hook into Google Analytics!");*/

        Initialization init = new Initialization();
        init.initiateFiles();
        getServer().getPluginManager().registerEvents(new Listeners(), this);

        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            Bukkit.getServicesManager().register(net.milkbowl.vault.economy.Economy.class, new VaultEconomy(),
                  Bukkit.getPluginManager().getPlugin("Vault"), ServicePriority.Normal);
        }

        getDevInfo();
        getLogger().info("Necessities enabled.");
        GameProfile janetProfile = new GameProfile(UUID.randomUUID(), "Janet");
        Property skin = getSkin();
        if (skin != null) {
            janetProfile.getProperties().put("textures", skin);
        }
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = server.getWorldServer(DimensionManager.OVERWORLD);
        PlayerInteractManager manager = new PlayerInteractManager(world);
        EntityPlayer player = new EntityPlayer(server, world, janetProfile, manager);
        player.listName = formatMessage(
              ChatColor.translateAlternateColorCodes('&', rm.getRank(rm.getOrder().size() - 1).getTitle() + ' ')
                    + "Janet");
        this.janetInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, player);
    }

    /*private boolean hookGoogle() {
        OpenAnalytics plugin;
        if ((plugin = (OpenAnalytics) getServer().getPluginManager().getPlugin("OpenAnalytics")) == null)
            return false;
        googleAnalyticsTracker = plugin.getTracker();
        return true;
    }*/

    /**
     * Checks if Necessities is tracking.
     *
     * @return True if Necessities is tracking, false otherwise.
     */
    /*public static boolean isTracking() {
        return getTracker() != null;
    }

    private static OpenAnalyticsTracker getTracker() {
        return getInstance().googleAnalyticsTracker;
    }*/
    private IChatBaseComponent formatMessage(String message) {
        return IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
    }

    /**
     * Removes a player from the tab list.
     *
     * @param p The player to remove from the tab list.
     */
    public void removePlayer(Player p) {
        PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER,
              ((CraftPlayer) p).getHandle());
        for (Player x : Bukkit.getOnlinePlayers()) {
            if (!x.canSee(p) && !x.equals(p)) {
                ((CraftPlayer) x).getHandle().playerConnection.sendPacket(info);
            }
        }
    }

    /**
     * Adds a player to the tab list.
     *
     * @param p The player to add to the tab list.
     */
    public void addPlayer(Player p) {
        EntityPlayer ep = ((CraftPlayer) p).getHandle();
        //User u = um.getUser(p.getUniqueId());
        //ep.listName = formatMessage(u.getRank() == null ? "" : ChatColor.translateAlternateColorCodes('&', u.getRank().getTitle() + ' ') + p.getDisplayName());
        PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, ep);
        for (Player x : Bukkit.getOnlinePlayers()) {
            if (!x.hasPermission("Necessities.seehidden") && x.canSee(p) && !x.equals(p)) {
                ((CraftPlayer) x).getHandle().playerConnection.sendPacket(info);
            }
        }
    }

    /**
     * Updates the tab list of the specified player.
     *
     * @param p The player to update the tab list name of.
     */
    public void updateName(Player p) {
        User u = um.getUser(p.getUniqueId());
        /*EntityPlayer ep = ((CraftPlayer) p).getHandle();
        ep.listName = formatMessage(u.getRank() == null ? "" : ChatColor.translateAlternateColorCodes('&', u.getRank().getTitle() + ' ') + p.getDisplayName());
        PacketPlayOutPlayerInfo tabList = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, ep);
        Bukkit.getOnlinePlayers().forEach(x -> ((CraftPlayer) x).getHandle().playerConnection.sendPacket(tabList));*/
        p.setPlayerListName(u.getRank() == null ? ""
              : ChatColor.translateAlternateColorCodes('&', u.getRank().getTitle() + ' ') + p.getDisplayName());
    }

    /**
     * Shows the given player all the players on the tab list.
     *
     * @param x The player to refresh their tab list.
     */
    public void updateAll(Player x) {//TODO: Is this even needed
        List<EntityPlayer> players = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            EntityPlayer ep = ((CraftPlayer) p).getHandle();
            //User u = um.getUser(p.getUniqueId());
            //ep.listName = formatMessage(u.getRank() == null ? "" : ChatColor.translateAlternateColorCodes('&', u.getRank().getTitle() + ' ') + p.getDisplayName());
            players.add(ep);
        }
        ((CraftPlayer) x).getHandle().playerConnection
              .sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, players));
    }

    void addJanet(Player p) {
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(this.janetInfo);
    }

    void addHeader(Player p) {
        p.setPlayerListHeaderFooter(ChatColor.AQUA + "Galaxy Gaming", ChatColor.GREEN + "https://galaxygaming.gg");
    }

    private Property getSkin() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL(
                  "https://sessionserver.mojang.com/session/minecraft/profile/136f2ba62be3444ca2968ec597edb57e?unsigned=false")
                  .openConnection().getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JsonObject json = new Gson().fromJson(response.toString(), JsonObject.class);
            JsonObject properties = json.getAsJsonArray("properties").get(0).getAsJsonObject();
            JsonElement signature = properties.get("signature");
            JsonElement value = properties.get("value");
            return new Property("textures", value == null ? null : value.getAsString(),
                  signature == null ? null : signature.getAsString());
        } catch (Exception ignored) {
        }
        return null;
    }

    public boolean onCommand(@Nonnull CommandSender sender, Command cmd, @Nonnull String alias,
          @Nonnull String[] args) {
        Cmd c = getCmd(cmd.getName());
        return c != null && c.commandUse(sender, args);
    }

    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String alias,
          @Nonnull String[] args) {
        Cmd c = getCmd(cmd.getName());
        if (c == null) {
            return null;
        }
        List<String> tab = c.tabComplete(sender, args);
        /*if (tab == null || tab.isEmpty()) {
            return null;
        }*/
        return tab;
    }

    private boolean isEqual(String command, String toCheck) {
        return command.equalsIgnoreCase(toCheck);
    }

    private Cmd getCmd(String name) {
        Cmd c = null;
        if (isEqual(name, "slap")) {
            c = new CmdSlap();
        } else if (isEqual(name, "warn")) {
            c = new CmdWarn();
        } else if (isEqual(name, "enchant")) {
            c = new CmdEnchant();
        } else if (isEqual(name, "ragequit")) {
            c = new CmdRagequit();
        } else if (isEqual(name, "highfive")) {
            c = new CmdHighfive();
        } else if (isEqual(name, "devs")) {
            c = new CmdDevs();
        } else if (isEqual(name, "loginmessage")) {
            c = new CmdLogInMessage();
        } else if (isEqual(name, "logoutmessage")) {
            c = new CmdLogOutMessage();
        } else if (isEqual(name, "imp")) {
            c = new CmdImp();
        } else if (isEqual(name, "pants")) {
            c = new CmdPants();
        } else if (isEqual(name, "boots")) {
            c = new CmdBoots();
        } else if (isEqual(name, "chest")) {
            c = new CmdChest();
        } else if (isEqual(name, "blockhat")) {
            c = new CmdBlockHat();
        } else if (isEqual(name, "hat")) {
            c = new CmdHat();
        } else if (isEqual(name, "workbench")) {
            c = new CmdWorkbench();
        } else if (isEqual(name, "hide")) {
            c = this.hide;
        } else if (isEqual(name, "rename")) {
            c = new CmdRename();
        } else if (isEqual(name, "title")) {
            c = new CmdTitle();
        } else if (isEqual(name, "table")) {
            c = new CmdTable();
        } else if (isEqual(name, "tpdim")) {
            c = new CmdTpDim();
        } else if (isEqual(name, "bracketcolor")) {
            c = new CmdBracketColor();
        } else if (isEqual(name, "commandspy")) {
            c = this.spy;
        } else if (isEqual(name, "setspawn")) {
            c = new CmdSetspawn();
        } else if (isEqual(name, "spawn")) {
            c = new CmdSpawn();
        } else if (isEqual(name, "time")) {
            c = new CmdTime();
        } else if (isEqual(name, "weather")) {
            c = new CmdWeather();
        } else if (isEqual(name, "gamemode")) {
            c = new CmdGamemode();
        } else if (isEqual(name, "god")) {
            c = new CmdGod();
        } else if (isEqual(name, "heal")) {
            c = new CmdHeal();
        } else if (isEqual(name, "feed")) {
            c = new CmdFeed();
        } else if (isEqual(name, "fly")) {
            c = new CmdFly();
        } else if (isEqual(name, "more")) {
            c = new CmdMore();
        } else if (isEqual(name, "repair")) {
            c = new CmdRepair();
        } else if (isEqual(name, "afk")) {
            c = new CmdAfk();
        } else if (isEqual(name, "who")) {
            c = new CmdWho();
        } else if (isEqual(name, "me")) {
            c = new CmdMe();
        } else if (isEqual(name, "nick")) {
            c = new CmdNick();
        } else if (isEqual(name, "kick")) {
            c = new CmdKick();
        } else if (isEqual(name, "ext")) {
            c = new CmdExt();
        } else if (isEqual(name, "enderchest")) {
            c = new CmdEnderChest();
        } else if (isEqual(name, "invsee")) {
            c = this.invsee;
        } else if (isEqual(name, "slack")) {
            c = new CmdSlack();
        } else if (isEqual(name, "requestmod")) {
            c = new CmdRequestMod();
        } else if (isEqual(name, "top")) {
            c = new CmdTop();
        } else if (isEqual(name, "speed")) {
            c = new CmdSpeed();
        } else if (isEqual(name, "suicide")) {
            c = new CmdSuicide();
        } else if (isEqual(name, "back")) {
            c = new CmdBack();
        } else if (isEqual(name, "setjail")) {
            c = new CmdSetJail();
        } else if (isEqual(name, "jail")) {
            c = new CmdJail();
        } else if (isEqual(name, "kill")) {
            c = new CmdKill();
        } else if (isEqual(name, "mute")) {
            c = new CmdMute();
        } else if (isEqual(name, "motd")) {
            c = new CmdMotd();
        } else if (isEqual(name, "rules")) {
            c = new CmdRules();
        } else if (isEqual(name, "exp")) {
            c = new CmdExp();
        } else if (isEqual(name, "lightning")) {
            c = new CmdLightning();
        } else if (isEqual(name, "skull")) {
            c = new CmdSkull();
        } else if (isEqual(name, "sethome")) {
            c = new CmdSetHome();
        } else if (isEqual(name, "home")) {
            c = new CmdHome();
        } else if (isEqual(name, "delhome")) {
            c = new CmdDelHome();
        } else if (isEqual(name, "help")) {
            c = new CmdHelp();
        } else if (isEqual(name, "spawner")) {
            c = new CmdSpawner();
        } else if (isEqual(name, "spawnmob")) {
            c = new CmdSpawnmob();
        } else if (isEqual(name, "ignore")) {
            c = new CmdIgnore();
        } else if (isEqual(name, "msg")) {
            c = new CmdMsg();
        } else if (isEqual(name, "reply")) {
            c = new CmdReply();
        } else if (isEqual(name, "say")) {
            c = new CmdSay();
        } else if (isEqual(name, "item")) {
            c = new CmdItem();
        } else if (isEqual(name, "give")) {
            c = new CmdGive();
        } else if (isEqual(name, "clear")) {
            c = new CmdClear();
        } else if (isEqual(name, "killall")) {
            c = new CmdKillall();
        } else if (isEqual(name, "togglechat")) {
            c = new CmdToggleChat();
        } else if (isEqual(name, "ban")) {
            c = new CmdBan();
        } else if (isEqual(name, "tempban")) {
            c = new CmdTempban();
        } else if (isEqual(name, "unban")) {
            c = new CmdUnban();
        } else if (isEqual(name, "banip")) {
            c = new CmdBanIP();
        } else if (isEqual(name, "unbanip")) {
            c = new CmdUnbanIP();
        } else if (isEqual(name, "jump")) {
            c = new CmdJump();
        } else if (isEqual(name, "tp")) {
            c = new CmdTp();
        } else if (isEqual(name, "tphere")) {
            c = new CmdTphere();
        } else if (isEqual(name, "tppos")) {
            c = new CmdTppos();
        } else if (isEqual(name, "tpall")) {
            c = new CmdTpall();
        } else if (isEqual(name, "tpa")) {
            c = new CmdTpa();
        } else if (isEqual(name, "tpahere")) {
            c = new CmdTpahere();
        } else if (isEqual(name, "tpaccept")) {
            c = new CmdTpaccept();
        } else if (isEqual(name, "tpdeny")) {
            c = new CmdTpdeny();
        } else if (isEqual(name, "faq")) {
            c = new CmdFaq();
        } else if (isEqual(name, "opbroadcast")) {
            c = new CmdOpChat();
        } else if (isEqual(name, "craft")) {
            c = new CmdCraft();
        } else if (isEqual(name, "machinegun")) {
            c = new CmdMachineGun();
        } else if (isEqual(name, "bazooka")) {
            c = new CmdBazooka();
        } else if (isEqual(name, "wrench")) {
            c = new CmdWrench();
        } else if (isEqual(name, "tps")) {
            c = new CmdTps();
        } else if (isEqual(name, "reloadannouncer")) {
            c = new CmdReloadAnnouncer();
        } else if (isEqual(name, "freeze")) {
            c = new CmdFreeze();
        }
        //Economy
        else if (isEqual(name, "bal")) {
            c = new CmdBalance();
        } else if (isEqual(name, "baltop")) {
            c = new CmdBaltop();
        } else if (isEqual(name, "pricelist")) {
            c = new CmdPriceList();
        } else if (isEqual(name, "pay")) {
            c = new CmdPay();
        } else if (isEqual(name, "eco")) {
            c = new CmdEco();
        } else if (isEqual(name, "price")) {
            c = new CmdPrice();
        } else if (isEqual(name, "setprice")) {
            c = new CmdSetPrice();
        } else if (isEqual(name, "buy")) {
            c = new CmdBuy();
        } else if (isEqual(name, "sell")) {
            c = new CmdSell();
        } else if (isEqual(name, "players")) {
            c = new CmdPlayers();
        } else if (isEqual(name, "rankprices")) {
            c = new CmdRankPrices();
        } else if (isEqual(name, "setrankprice")) {
            c = new CmdSetRankPrice();
        } else if (isEqual(name, "buyrank")) {
            c = new CmdBuyRank();
        } else if (isEqual(name, "buycmd")) {
            c = new CmdBuyCmd();
        } else if (isEqual(name, "l2m")) {
            c = new CmdL2M();
        } else if (isEqual(name, "commandprices")) {
            c = new CmdCmdPrices();
        } else if (isEqual(name, "setcommandprice")) {
            c = new CmdSetCmdPrice();
        }
        //RankManager
        else if (isEqual(name, "promote")) {
            c = new CmdPromote();
        } else if (isEqual(name, "demote")) {
            c = new CmdDemote();
        } else if (isEqual(name, "setrank")) {
            c = new CmdSetrank();
        } else if (isEqual(name, "addpermission")) {
            c = new CmdAddPermission();
        } else if (isEqual(name, "delpermission")) {
            c = new CmdDelPermission();
        } else if (isEqual(name, "addpermsubrank")) {
            c = new CmdAddPermSubrank();
        } else if (isEqual(name, "delpermsubrank")) {
            c = new CmdDelPermSubrank();
        } else if (isEqual(name, "addpermissionuser")) {
            c = new CmdAddPermissionUser();
        } else if (isEqual(name, "delpermissionuser")) {
            c = new CmdDelPermissionUser();
        } else if (isEqual(name, "addsubrank")) {
            c = new CmdAddSubrank();
        } else if (isEqual(name, "delsubrank")) {
            c = new CmdDelSubrank();
        } else if (isEqual(name, "addsubrankuser")) {
            c = new CmdAddSubrankUser();
        } else if (isEqual(name, "delsubrankuser")) {
            c = new CmdDelSubrankUser();
        } else if (isEqual(name, "createsubrank")) {
            c = new CmdCreateSubrank();
        } else if (isEqual(name, "removesubrank")) {
            c = new CmdRemoveSubrank();
        } else if (isEqual(name, "createrank")) {
            c = new CmdCreateRank();
        } else if (isEqual(name, "removerank")) {
            c = new CmdRemoveRank();
        } else if (isEqual(name, "whois")) {
            c = new CmdWhois();
        } else if (isEqual(name, "ranks")) {
            c = new CmdRanks();
        } else if (isEqual(name, "subranks")) {
            c = new CmdSubranks();
        } else if (isEqual(name, "rankcmds")) {
            c = new CmdRankCmds();
        } else if (isEqual(name, "reloadpermissions")) {
            c = new CmdReloadPermissions();
        }
        //WorldManager
        else if (isEqual(name, "createworld")) {
            c = new CmdCreateWorld();
        } else if (isEqual(name, "worldspawn")) {
            c = new CmdWorldSpawn();
        } else if (isEqual(name, "loadworld")) {
            c = new CmdLoadWorld();
        } else if (isEqual(name, "unloadworld")) {
            c = new CmdUnloadWorld();
        } else if (isEqual(name, "removeworld")) {
            c = new CmdRemoveWorld();
        } else if (isEqual(name, "worlds")) {
            c = new CmdWorlds();
        } else if (isEqual(name, "world")) {
            c = new CmdWorld();
        } else if (isEqual(name, "setworldspawn")) {
            c = new CmdSetWorldSpawn();
        } else if (isEqual(name, "modifyworld")) {
            c = new CmdModifyWorld();
        } else if (isEqual(name, "createportal")) {
            c = new CmdCreatePortal();
        } else if (isEqual(name, "removeportal")) {
            c = new CmdRemovePortal();
        } else if (isEqual(name, "warps")) {
            c = new CmdWarps();
        } else if (isEqual(name, "warp")) {
            c = new CmdWarp();
        } else if (isEqual(name, "createwarp")) {
            c = new CmdCreateWarp();
        } else if (isEqual(name, "removewarp")) {
            c = new CmdRemoveWarp();
        }
        //Guilds
        else if (isEqual(name, "guild")) {
            c = new CmdGuild();
        }
        //Creative
        else if (isEqual(name, "requestreview")) {
            c = new CmdRequestReview();
        } else if (isEqual(name, "reviewlist")) {
            c = new CmdReviewList();
        }

        if (c == null) {
            return null;
        }

        YamlConfiguration config = getConfig();
        if (!c.isPaintballEnabled() && config.contains("Necessities.Paintball") && config
              .getBoolean("Necessities.Paintball")) {
            c = this.disabled;
        } else if (c instanceof WorldCmd && config.contains("Necessities.WorldManager") && !config
              .getBoolean("Necessities.WorldManager")) {
            c = this.disabled;
        } else if (c instanceof CmdGuild && config.contains("Necessities.Guilds") && !config
              .getBoolean("Necessities.Guilds")) {
            c = this.disabled;
        } else if (c instanceof EconomyCmd && config.contains("Necessities.Economy") && !config
              .getBoolean("Necessities.Economy")) {
            c = this.disabled;
        } else if (c instanceof CreativeCmd && config.contains("Necessities.Creative") && !config
              .getBoolean("Necessities.Creative")) {
            c = this.disabled;
        }
        return c;
    }

    /**
     * Checks if the given command is disabled.
     *
     * @param cmd The command to check if it is disabled.
     * @return True if the command is disabled, false otherwise.
     */
    public boolean isCommandDisabled(String cmd) {
        Cmd c = getCmd(cmd);
        return c == null || c.equals(this.disabled);
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

    /**
     * Checks if the player with the specified uuid is a dev.
     *
     * @param uuid The uuid of the player to check.
     * @return True if the uuid is the uuid of a dev, false otherwise.
     */
    public boolean isDev(UUID uuid) {
        return this.devs.stream().anyMatch(i -> uuid.equals(i.getMCUUID()));
    }

    /**
     * Checks if the player with the specified uuid is a dev.
     *
     * @param slackID The slack id of the player to check.
     * @return True if the uuid is the uuid of a dev, false otherwise.
     */
    public boolean isDev(String slackID) {
        return this.devs.stream().anyMatch(i -> slackID.equals(i.getSlackID()));
    }

    /**
     * Gets the list of devs.
     *
     * @return The list of devs.
     */
    public List<DevInfo> getDevs() {
        return Collections.unmodifiableList(this.devs);
    }

    private void getDevInfo() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                  new URL("https://galaxygaming.gg/staff.json").openConnection().getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JsonObject json = new Gson().fromJson(response.toString(), JsonObject.class);
            JsonArray devs = json.getAsJsonArray("devs");
            JsonObject ls = json.getAsJsonObject("Lavasurvival");
            JsonArray lsDevs = ls.getAsJsonArray("devs");
            for (int i = 0; i < lsDevs.size(); i++) {
                int devID = lsDevs.get(i).getAsInt();
                for (int j = 0; j < devs.size(); j++) {
                    JsonObject dev = devs.get(j).getAsJsonObject();
                    JsonElement id = dev.get("devID");
                    if (id != null && devID == id.getAsInt()) {
                        this.devs.add(new DevInfo(dev));
                        break;
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    public class DevInfo {

        private final UUID mcUUID;
        private final String slackID;
        private final String name;

        private DevInfo(JsonObject dev) {
            this.mcUUID = UUID.fromString(dev.get("mcUUID").getAsString());
            this.slackID = dev.get("slackID").getAsString();
            this.name = dev.get("name").getAsString();
        }

        /**
         * Gets the preferred name of the dev.
         *
         * @return The preferred name of the dev.
         */
        public String getName() {
            return this.name;
        }

        /**
         * Gets the dev's Minecraft UUID.
         *
         * @return The dev's Minecraft UUID.
         */
        public UUID getMCUUID() {
            return this.mcUUID;
        }

        /**
         * Gets the dev's Slack ID.
         *
         * @return The dev's Slack ID.
         */
        public String getSlackID() {
            return this.slackID;
        }
    }

    /*
     * Tracks the given action.
     * @param hit The action to track.
     */
    /*public static void trackAction(Hit hit) {
        OpenAnalyticsTracker tracker;
        if ((tracker = getTracker()) != null)
            tracker.trackHit(hit);
    }*/

    /**
     * Gets the UserManager.
     *
     * @return The UserManager.
     */
    public static UserManager getUM() {
        return INSTANCE.um == null ? INSTANCE.um = new UserManager() : INSTANCE.um;
    }

    /**
     * Gets JanetNet.
     *
     * @return JanetNet.
     */
    /*public static JanetNet getNet() {
        return INSTANCE.net == null ? INSTANCE.net = new JanetNet() : INSTANCE.net;
    }*/
    static CmdCommandSpy getSpy() {
        return INSTANCE.spy == null ? INSTANCE.spy = new CmdCommandSpy() : INSTANCE.spy;
    }

    /**
     * Gets the RankManager.
     *
     * @return The RankManager.
     */
    public static RankManager getRM() {
        return INSTANCE.rm == null ? INSTANCE.rm = new RankManager() : INSTANCE.rm;
    }

    /**
     * Gets the PortalManager.
     *
     * @return The PortalManager.
     */
    public static PortalManager getPM() {
        return INSTANCE.pm == null ? INSTANCE.pm = new PortalManager() : INSTANCE.pm;
    }

    /**
     * Gets JanetSlack.
     *
     * @return JanetSlack.
     */
    public static JanetSlack getSlack() {
        return INSTANCE.slack == null ? INSTANCE.slack = new JanetSlack() : INSTANCE.slack;
    }

    /**
     * Gets the Console.
     *
     * @return The Console.
     */
    public static Console getConsole() {
        return INSTANCE.console == null ? INSTANCE.console = new Console() : INSTANCE.console;
    }

    /**
     * Gets Variables.
     *
     * @return Variables.
     */
    public static Variables getVar() {
        return INSTANCE.var == null ? INSTANCE.var = new Variables() : INSTANCE.var;
    }

    /**
     * Gets Teleports.
     *
     * @return Teleports.
     */
    public static Teleports getTPs() {
        return INSTANCE.tps == null ? INSTANCE.tps = new Teleports() : INSTANCE.tps;
    }

    /**
     * Gets JanetWarns.
     *
     * @return JanetWarns.
     */
    public static JanetWarn getWarns() {
        return INSTANCE.warns == null ? INSTANCE.warns = new JanetWarn() : INSTANCE.warns;
    }

    /**
     * Gets ScoreBoards.
     *
     * @return ScoreBoards.
     */
    public static ScoreBoards getSBs() {
        return INSTANCE.sb == null ? INSTANCE.sb = new ScoreBoards() : INSTANCE.sb;
    }

    /**
     * Gets Hide.
     *
     * @return Hide.
     */
    public static CmdHide getHide() {
        return INSTANCE.hide == null ? INSTANCE.hide = new CmdHide() : INSTANCE.hide;
    }

    /**
     * Gets JanetAI.
     *
     * @return JanetAI.
     */
    public static JanetAI getAI() {
        return INSTANCE.ai == null ? INSTANCE.ai = new JanetAI() : INSTANCE.ai;
    }

    /**
     * Gets Janet.
     *
     * @return Janet.
     */
    public static Janet getBot() {
        return INSTANCE.bot == null ? INSTANCE.bot = new Janet() : INSTANCE.bot;
    }

    /**
     * Gets the WarpManager.
     *
     * @return The WarpManager.
     */
    public static WarpManager getWarps() {
        return INSTANCE.warps == null ? INSTANCE.warps = new WarpManager() : INSTANCE.warps;
    }

    /**
     * Gets the WorldManager.
     *
     * @return The WorldManager.
     */
    public static WorldManager getWM() {
        return INSTANCE.wm == null ? INSTANCE.wm = new WorldManager() : INSTANCE.wm;
    }

    /**
     * Gets the GuildManager.
     *
     * @return The GuildManager.
     */
    public static GuildManager getGM() {
        return INSTANCE.gm == null ? INSTANCE.gm = new GuildManager() : INSTANCE.gm;
    }

    static CmdInvsee getInvsee() {
        return INSTANCE.invsee == null ? INSTANCE.invsee = new CmdInvsee() : INSTANCE.invsee;
    }

    static JanetBooks getBooks() {
        return INSTANCE.books == null ? INSTANCE.books = new JanetBooks() : INSTANCE.books;
    }

    /**
     * Gets JanetRename.
     *
     * @return JanetRename.
     */
    public static JanetRename getRename() {
        return INSTANCE.rename == null ? INSTANCE.rename = new JanetRename() : INSTANCE.rename;
    }

    /**
     * Gets the Review.
     *
     * @return The Review.
     */
    public static Reviews getRev() {
        return INSTANCE.rev == null ? INSTANCE.rev = new Reviews() : INSTANCE.rev;
    }

    static Signs getEconomySigns() {
        return INSTANCE.economySigns == null ? INSTANCE.economySigns = new Signs() : INSTANCE.economySigns;
    }

    static JanetSigns getSigns() {
        return INSTANCE.signs == null ? INSTANCE.signs = new JanetSigns() : INSTANCE.signs;
    }

    static Wrenched getWrench() {
        return INSTANCE.wrench == null ? INSTANCE.wrench = new Wrenched() : INSTANCE.wrench;
    }

    static AntiCombatLog getACB() {
        return INSTANCE.acb == null ? INSTANCE.acb = new AntiCombatLog() : INSTANCE.acb;
    }

    /**
     * Gets the PowerManager.
     *
     * @return The PowerManager.
     */
    public static PowerManager getPower() {
        return INSTANCE.power == null ? INSTANCE.power = new PowerManager() : INSTANCE.power;
    }

    /**
     * Gets the SafeLocation.
     *
     * @return The SafeLocation.
     */
    public static SafeLocation getSafeLocations() {
        return INSTANCE.safe == null ? INSTANCE.safe = new SafeLocation() : INSTANCE.safe;
    }

    /**
     * Gets JanetLog.
     *
     * @return JanetLog.
     */
    public static JanetLog getLog() {
        return INSTANCE.log == null ? INSTANCE.log = new JanetLog() : INSTANCE.log;
    }

    /**
     * Gets the Economy.
     *
     * @return The Economy.
     */
    public static Economy getEconomy() {
        return INSTANCE.eco == null ? INSTANCE.eco = new Economy() : INSTANCE.eco;
    }

    /**
     * Gets the Command Prices.
     *
     * @return The Command Prices.
     */
    public static CmdPrices getCommandPrices() {
        return INSTANCE.cmdp == null ? INSTANCE.cmdp = new CmdPrices() : INSTANCE.cmdp;
    }

    /**
     * Gets the Rank Prices.
     *
     * @return The Rank Prices.
     */
    public static RankPrices getRankPrices() {
        return INSTANCE.rp == null ? INSTANCE.rp = new RankPrices() : INSTANCE.rp;
    }

    /**
     * Gets the Prices.
     *
     * @return The Prices.
     */
    public static Prices getPrices() {
        return INSTANCE.pr == null ? INSTANCE.pr = new Prices() : INSTANCE.pr;
    }

    /**
     * Gets the Announcer.
     *
     * @return The Announcer.
     */
    public static Announcer getAnnouncer() {
        return INSTANCE.announcer == null ? INSTANCE.announcer = new Announcer() : INSTANCE.announcer;
    }

    /**
     * Gets the config file.
     *
     * @return The config file.
     */
    public File getConfigFile() {
        return this.configFile;
    }

    /**
     * Gets the config..
     *
     * @return The config.
     */
    public YamlConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(getConfigFile());
    }
}