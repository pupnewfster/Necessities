package com.crossge.necessities;

import com.TentacleLabs.GoogleAnalyticsPlugin.GoogleAnalyticsPlugin;
import com.TentacleLabs.GoogleAnalyticsPlugin.Tracker;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.*;
import com.crossge.necessities.Commands.*;
import com.crossge.necessities.Commands.Economy.*;
import com.crossge.necessities.Commands.Guilds.CmdGuild;
import com.crossge.necessities.Commands.RankManager.*;
import com.crossge.necessities.Commands.WorldManager.*;
import com.crossge.necessities.Economy.BalChecks;
import com.crossge.necessities.Guilds.PowerManager;
import com.crossge.necessities.Janet.Janet;
import com.crossge.necessities.Janet.JanetSlack;
import com.crossge.necessities.RankManager.RankManager;
import com.crossge.necessities.RankManager.User;
import com.crossge.necessities.RankManager.UserManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Necessities extends JavaPlugin {
    private static Necessities INSTANCE;
    private final List<String> devs = Arrays.asList("pupnewfster", "Mod_Chris", "hypereddie10");
    private File configFile = new File("plugins/Necessities", "config.yml");
    private ProtocolManager protocolManager = null;
    private Tracker googleAnalyticsTracker;
    private WrappedSignedProperty skin;
    private UUID janetID;
    DonationReader dr = new DonationReader();
    UserManager um = new UserManager();
    RankManager rm = new RankManager();

    public static Necessities getInstance() {
        return INSTANCE;
    }

    public boolean isProtocolLibLoaded() {
        return getServer().getPluginManager().getPlugin("ProtocolLib") != null && this.protocolManager != null;
    }

    @Override
    public void onEnable() {
        getLogger().info("Enabling Necessities...");
        INSTANCE = this;

        if (!hookGoogle())
            getLogger().warning("Could not hook into Google Analytics!");

        janetID = UUID.randomUUID();
        if (getServer().getPluginManager().getPlugin("ProtocolLib") != null)
            try {
                this.protocolManager = ProtocolLibrary.getProtocolManager();
                addPacketListener();
            } catch (Exception e) {
            }//Not using protocollib
        Initialization init = new Initialization();
        init.initiateFiles();
        getServer().getPluginManager().registerEvents(new Listeners(), this);

        Bukkit.getServicesManager().register(Economy.class, new BalChecks(), Bukkit.getPluginManager().getPlugin("Vault"), ServicePriority.Normal);

        dr.init();
        getLogger().info("Necessities enabled.");
    }

    private boolean hookGoogle() {
        GoogleAnalyticsPlugin plugin;
        if ((plugin = (GoogleAnalyticsPlugin) getServer().getPluginManager().getPlugin("GoogleAnalyticsPlugin")) == null)
            return false;
        googleAnalyticsTracker = plugin.getTracker();
        return true;
    }

    public static boolean isTracking() {
        return getTracker() != null;
    }

    public static Tracker getTracker() {
        return getInstance().googleAnalyticsTracker;
    }

    private void addPacketListener() {
        final CmdHide hide = new CmdHide();
        this.protocolManager.addPacketListener(new PacketAdapter(this, PacketType.Play.Server.BLOCK_ACTION, PacketType.Play.Server.NAMED_SOUND_EFFECT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketType type = event.getPacketType();
                if (type == PacketType.Play.Server.BLOCK_ACTION) {
                    BlockPosition pos = event.getPacket().getBlockPositionModifier().read(0);
                    Block b = event.getPlayer().getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());
                    if (b.getType() == Material.CHEST || b.getType() == Material.TRAPPED_CHEST) {
                        Block n = b.getRelative(BlockFace.NORTH);
                        Block e = b.getRelative(BlockFace.EAST);
                        Block s = b.getRelative(BlockFace.SOUTH);
                        Block w = b.getRelative(BlockFace.WEST);
                        for (User u : um.getUsers().values()) {
                            Location loc = u.getInvLoc();
                            if (loc != null && hide.isHidden(u.getPlayer()))
                                if (b.getLocation().equals(loc) || (n.getLocation().equals(loc) && n.getType().equals(b.getType())) || (e.getLocation().equals(loc) && e.getType().equals(b.getType())) ||
                                        (s.getLocation().equals(loc) && s.getType().equals(b.getType())) || (w.getLocation().equals(loc) && w.getType().equals(b.getType()))) {
                                    event.setCancelled(true);
                                    break;
                                }
                        }
                    }
                } else if (type == PacketType.Play.Server.NAMED_SOUND_EFFECT) {
                    String soundEffectName = event.getPacket().getSpecificModifier(String.class).read(0);
                    if (soundEffectName.contains("chest")) {
                        Block b = event.getPlayer().getWorld().getBlockAt(event.getPacket().getIntegers().read(0) / 8, event.getPacket().getIntegers().read(1) / 8, event.getPacket().getIntegers().read(2) / 8);
                        if (b.getType() == Material.CHEST || b.getType() == Material.TRAPPED_CHEST) {
                            Block n = b.getRelative(BlockFace.NORTH);
                            Block e = b.getRelative(BlockFace.EAST);
                            Block s = b.getRelative(BlockFace.SOUTH);
                            Block w = b.getRelative(BlockFace.WEST);
                            for (User u : um.getUsers().values()) {
                                Location loc = u.getInvLoc();
                                if (loc != null && hide.isHidden(u.getPlayer()))
                                    if (b.getLocation().equals(loc) || (n.getLocation().equals(loc) && n.getType().equals(b.getType())) || (e.getLocation().equals(loc) && e.getType().equals(b.getType())) ||
                                            (s.getLocation().equals(loc) && s.getType().equals(b.getType())) || (w.getLocation().equals(loc) && w.getType().equals(b.getType()))) {
                                        event.setCancelled(true);
                                        break;
                                    }
                            }
                        }
                    }
                }
            }
        });
    }

    public void removePlayer(Player p) {
        if (isProtocolLibLoaded())
            try {
                PacketContainer tabList = this.protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO, true);
                StructureModifier<List<PlayerInfoData>> infoData = tabList.getPlayerInfoDataLists();
                StructureModifier<EnumWrappers.PlayerInfoAction> infoAction = tabList.getPlayerInfoAction();
                List<PlayerInfoData> playerInfo = infoData.read(0);
                playerInfo.add(new PlayerInfoData(WrappedGameProfile.fromPlayer(p), 0, EnumWrappers.NativeGameMode.fromBukkit(p.getGameMode()), WrappedChatComponent.fromText(p.getName())));
                infoData.write(0, playerInfo);
                infoAction.write(0, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
                for (Player x : Bukkit.getOnlinePlayers())
                    if (!x.canSee(p) && !x.equals(p))
                        this.protocolManager.sendServerPacket(x, tabList);
            } catch (Exception e) {
            }
    }

    public void addPlayer(Player p) {
        if (isProtocolLibLoaded())
            try {
                PacketContainer tabList = this.protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO, true);
                StructureModifier<List<PlayerInfoData>> infoData = tabList.getPlayerInfoDataLists();
                StructureModifier<EnumWrappers.PlayerInfoAction> infoAction = tabList.getPlayerInfoAction();
                List<PlayerInfoData> playerInfo = infoData.read(0);
                User u = um.getUser(p.getUniqueId());
                playerInfo.add(new PlayerInfoData(WrappedGameProfile.fromPlayer(p), 0, EnumWrappers.NativeGameMode.fromBukkit(p.getGameMode()),
                        WrappedChatComponent.fromText((u.getRank() == null ? "" : ChatColor.translateAlternateColorCodes('&', u.getRank().getTitle() + " ")) + p.getDisplayName())));

                infoData.write(0, playerInfo);
                infoAction.write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
                for (Player x : Bukkit.getOnlinePlayers())
                    if (!x.hasPermission("Necessities.seehidden") && x.canSee(p) && !x.equals(p))
                        this.protocolManager.sendServerPacket(x, tabList);
            } catch (Exception e) {
            }
    }

    public void updateName(Player p) {
        if (isProtocolLibLoaded())
            try {
                PacketContainer tabList = this.protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO, true);
                StructureModifier<List<PlayerInfoData>> infoData = tabList.getPlayerInfoDataLists();
                StructureModifier<EnumWrappers.PlayerInfoAction> infoAction = tabList.getPlayerInfoAction();
                List<PlayerInfoData> playerInfo = infoData.read(0);
                User u = um.getUser(p.getUniqueId());
                playerInfo.add(new PlayerInfoData(WrappedGameProfile.fromPlayer(p), 0, EnumWrappers.NativeGameMode.fromBukkit(p.getGameMode()),
                        WrappedChatComponent.fromText((u.getRank() == null ? "" : ChatColor.translateAlternateColorCodes('&', u.getRank().getTitle() + " ")) + p.getDisplayName())));
                infoData.write(0, playerInfo);
                infoAction.write(0, EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME);
                for (Player x : Bukkit.getOnlinePlayers())
                    this.protocolManager.sendServerPacket(x, tabList);
            } catch (Exception e) {
            }
    }

    public void updateAll(Player x) {
        if (isProtocolLibLoaded())
            try {
                PacketContainer tabList = this.protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO, true);
                StructureModifier<List<PlayerInfoData>> infoData = tabList.getPlayerInfoDataLists();
                StructureModifier<EnumWrappers.PlayerInfoAction> infoAction = tabList.getPlayerInfoAction();
                List<PlayerInfoData> playerInfo = infoData.read(0);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    User u = um.getUser(p.getUniqueId());
                    playerInfo.add(new PlayerInfoData(WrappedGameProfile.fromPlayer(p), 0, EnumWrappers.NativeGameMode.fromBukkit(p.getGameMode()),
                            WrappedChatComponent.fromText((u.getRank() == null ? "" : ChatColor.translateAlternateColorCodes('&', u.getRank().getTitle() + " ")) + p.getDisplayName())));
                }
                infoData.write(0, playerInfo);
                infoAction.write(0, EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME);
                this.protocolManager.sendServerPacket(x, tabList);
            } catch (Exception e) {
            }
    }

    public void addJanet(Player p) {
        if (isProtocolLibLoaded())
            try {
                PacketContainer tabList = this.protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO, true);
                StructureModifier<List<PlayerInfoData>> infoData = tabList.getPlayerInfoDataLists();
                StructureModifier<EnumWrappers.PlayerInfoAction> infoAction = tabList.getPlayerInfoAction();
                List<PlayerInfoData> playerInfo = infoData.read(0);
                WrappedGameProfile janetProfile = new WrappedGameProfile(janetID, "Janet");
                if (this.skin == null)
                    this.skin = getSkin();
                if (this.skin != null)
                    janetProfile.getProperties().put("textures", this.skin);
                playerInfo.add(new PlayerInfoData(janetProfile, 0, EnumWrappers.NativeGameMode.CREATIVE,
                        WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', rm.getRank(rm.getOrder().size() - 1).getTitle() + " ") + "Janet")));
                infoData.write(0, playerInfo);
                infoAction.write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
                this.protocolManager.sendServerPacket(p, tabList);
            } catch (Exception e) {
            }
    }

    public void refreshJanet(Player p) {
        if (isProtocolLibLoaded())
            try {
                PacketContainer tabList = this.protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO, true);
                StructureModifier<List<PlayerInfoData>> infoData = tabList.getPlayerInfoDataLists();
                StructureModifier<EnumWrappers.PlayerInfoAction> infoAction = tabList.getPlayerInfoAction();
                List<PlayerInfoData> playerInfo = infoData.read(0);
                WrappedGameProfile janetProfile = new WrappedGameProfile(janetID, "Janet");
                if (this.skin == null)
                    this.skin = getSkin();
                if (this.skin != null)
                    janetProfile.getProperties().put("textures", this.skin);
                playerInfo.add(new PlayerInfoData(janetProfile, 0, EnumWrappers.NativeGameMode.CREATIVE,
                        WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', rm.getRank(rm.getOrder().size() - 1).getTitle() + " ") + "Janet")));
                infoData.write(0, playerInfo);
                infoAction.write(0, EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME);
                this.protocolManager.sendServerPacket(p, tabList);
            } catch (Exception e) {
            }
    }

    public void addHeader(Player p) {
        if (isProtocolLibLoaded())
            try {
                PacketContainer tabList = this.protocolManager.createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
                StructureModifier<WrappedChatComponent> chatStuff = tabList.getChatComponents();
                chatStuff.write(0, WrappedChatComponent.fromText(ChatColor.GREEN + "GamezGalaxy"));
                chatStuff.write(1, WrappedChatComponent.fromText(ChatColor.BLUE + "http://gamezgalaxy.com"));
                this.protocolManager.sendServerPacket(p, tabList);
            } catch (Exception e) {
            }
    }

    private WrappedSignedProperty getSkin() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL("https://sessionserver.mojang.com/session/minecraft/profile/136f2ba62be3444ca2968ec597edb57e?unsigned=false").openConnection().getInputStream()));
            String value = "", signature = "";
            int count = 0;
            for (char c : in.readLine().toCharArray()) {
                if (c == '"')
                    count++;
                else if (count == 17)
                    value += c;
                else if (count == 21)
                    signature += c;
                if (count > 21)
                    break;
            }
            in.close();
            return new WrappedSignedProperty("textures", value, signature);
        } catch (Exception ignored) {
        }
        return null;
    }

    public boolean isDev(String name) {
        return devs.contains(name);
    }

    public List<String> getDevs() {
        return this.devs;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        return getCmd(cmd.getName()).commandUse(sender, args);
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (sender == null || cmd == null)
            return null;
        List<String> tab = getCmd(cmd.getName()).tabComplete(sender, args);
        if (tab == null || tab.isEmpty())
            return null;
        return tab;
    }

    private boolean isEqual(String command, String tocheck) {
        return command.equalsIgnoreCase(tocheck);
    }

    private Cmd getCmd(String name) {
        Cmd com = new Cmd();
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
            com = new CmdHide();
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
            com = new CmdCommandSpy();
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
            com = new CmdInvsee();
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
        else if (isEqual(name, "taccept"))
            com = new CmdTAccept();
        else if (isEqual(name, "tdeny"))
            com = new CmdTDeny();
        else if (isEqual(name, "trade"))
            com = new CmdTrade();
        else if (isEqual(name, "tradeitems"))
            com = new CmdTradeItems();
        else if (isEqual(name, "players"))
            com = new CmdPlayers();
        else if (isEqual(name, "rankprices"))
            com = new CmdRankPrices();
        else if (isEqual(name, "setrankprice"))
            com = new CmdSetRankPrice();
        else if (isEqual(name, "buyrank"))
            com = new CmdBuyRank();
        else if (isEqual(name, "l2m"))
            com = new Cmdl2m();
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

        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (com instanceof WorldCmd && config.contains("Necessities.WorldManager") && !config.getBoolean("Necessities.WorldManager"))
            com = new Cmd();
        else if (com instanceof CmdGuild && config.contains("Necessities.Guilds") && !config.getBoolean("Necessities.Guilds"))
            com = new Cmd();
        else if (com instanceof EconomyCmd && config.contains("Necessities.Economy") && !config.getBoolean("Necessities.Economy"))
            com = new Cmd();
        return com;
    }

    @Override
    public void onDisable() {
        CmdCommandSpy cs = new CmdCommandSpy();
        PowerManager power = new PowerManager();
        JanetSlack slack = new JanetSlack();
        CmdHide hide = new CmdHide();
        Janet bot = new Janet();
        power.unload();
        um.unload();
        cs.unload();
        hide.unload();
        slack.disconnect();
        bot.unload();
        dr.disconnect();
        getLogger().info("Necessities disabled.");
    }

    public static void trackAction(UUID uuid, String action, Object label) {
        boolean usesPluginChannel = false;
        String clientId, ip;
        Player p = Bukkit.getPlayer(uuid);
        if (p == null) {
            clientId = Bukkit.getOfflinePlayer(uuid).getName();
            ip = "0.0.0.0";
        } else {
            clientId = p.getName();
            ip = (p.getAddress() != null ? p.getAddress().toString().substring(1) : "0.0.0.0");
            usesPluginChannel = p.getListeningPluginChannels().size() != 0;
        }
        String clientVersion = Bukkit.getVersion().substring("git-Bukkit".length());
        String clientName = "Minecraft " + clientVersion.substring(0, clientVersion.indexOf("-")) + (usesPluginChannel ? " [Supports Plugin Channels]" : "");
        getTracker().TrackAction(clientName, clientId, ip, clientId, action, label.toString());
    }

    public static void trackAction(Player p, String action, Object label) {
        String clientId = p.getName(), ip = (p.getAddress() != null ? p.getAddress().toString().substring(1) : "0.0.0.0");
        boolean usesPluginChannel = p.getListeningPluginChannels().size() != 0;
        String clientVersion = Bukkit.getVersion().substring("git-Bukkit".length());
        String clientName = "Minecraft " + clientVersion.substring(0, clientVersion.indexOf("-")) + (usesPluginChannel ? " [Supports Plugin Channels]" : "");
        getTracker().TrackAction(clientName, clientId, ip, clientId, action, label.toString());
    }

    public static void trackActionWithValue(UUID uuid, String action, Object label, Object value) {
        boolean usesPluginChannel = false;
        String clientId, ip;
        Player p = Bukkit.getPlayer(uuid);
        if (p == null) {
            clientId = Bukkit.getOfflinePlayer(uuid).getName();
            ip = "0.0.0.0";
        } else {
            clientId = p.getName();
            ip = (p.getAddress() != null ? p.getAddress().toString().substring(1) : "0.0.0.0");
            usesPluginChannel = p.getListeningPluginChannels().size() != 0;
        }
        String clientVersion = Bukkit.getVersion().substring("git-Bukkit".length());
        String clientName = "Minecraft " + clientVersion.substring(0, clientVersion.indexOf("-")) + (usesPluginChannel ? " [Supports Plugin Channels]" : "");
        getTracker().TrackActionWithValue(clientName, clientId, ip, clientId, action, label.toString(), value.toString());
    }

    public static void trackActionWithValue(Player p, String action, Object label, Object value) {
        String clientId = p.getName(), ip = (p.getAddress() != null ? p.getAddress().toString().substring(1) : "0.0.0.0");
        boolean usesPluginChannel = p.getListeningPluginChannels().size() != 0;
        String clientVersion = Bukkit.getVersion().substring("git-Bukkit".length());
        String clientName = "Minecraft " + clientVersion.substring(0, clientVersion.indexOf("-")) + (usesPluginChannel ? " [Supports Plugin Channels]" : "");
        getTracker().TrackActionWithValue(clientName, clientId, ip, clientId, action, label.toString(), value.toString());
    }
}