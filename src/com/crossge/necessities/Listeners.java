package com.crossge.necessities;

import com.crossge.necessities.Commands.CmdCommandSpy;
import com.crossge.necessities.Commands.CmdHide;
import com.crossge.necessities.Commands.CmdInvsee;
import com.crossge.necessities.Economy.BalChecks;
import com.crossge.necessities.Economy.Materials;
import com.crossge.necessities.Economy.Signs;
import com.crossge.necessities.Guilds.Guild;
import com.crossge.necessities.Guilds.GuildManager;
import com.crossge.necessities.Guilds.PowerManager;
import com.crossge.necessities.Hats.Hat;
import com.crossge.necessities.Janet.*;
import com.crossge.necessities.RankManager.RankManager;
import com.crossge.necessities.RankManager.User;
import com.crossge.necessities.RankManager.UserManager;
import com.crossge.necessities.WorldManager.PortalManager;
import com.crossge.necessities.WorldManager.WorldManager;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Diode;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Listeners implements Listener {
    private File configFileLogOut = new File("plugins/Necessities", "logoutmessages.yml"), configFileLogIn = new File("plugins/Necessities", "loginmessages.yml"),
            configFileTitles = new File("plugins/Necessities", "titles.yml"), configFile = new File("plugins/Necessities", "config.yml");
    private static String JanetName = "";
    AntiCombatLog acb = new AntiCombatLog();
    CmdCommandSpy spy = new CmdCommandSpy();
    PowerManager power = new PowerManager();
    SafeLocation safe = new SafeLocation();
    PortalManager pm = new PortalManager();
    JanetRename rename = new JanetRename();
    WorldManager wm = new WorldManager();
    GuildManager gm = new GuildManager();
    JanetSigns signs = new JanetSigns();
    JanetBooks books = new JanetBooks();
    JanetSlack slack = new JanetSlack();
    CmdInvsee invsee = new CmdInvsee();
    UserManager um = new UserManager();
    Wrenched wrench = new Wrenched();
    BalChecks balc = new BalChecks();
    Formatter form = new Formatter();
    Signs economySigns = new Signs();
    Console console = new Console();
    Materials mat = new Materials();
    Variables var = new Variables();
    Teleports tps = new Teleports();
    CmdHide hide = new CmdHide();
    GetUUID get = new GetUUID();
    JanetAI ai = new JanetAI();
    Janet bot = new Janet();

    public Listeners() {
        RankManager rm = new RankManager();
        JanetName = (!rm.getOrder().isEmpty() ? ChatColor.translateAlternateColorCodes('&', rm.getRank(rm.getOrder().size() - 1).getTitle() + " ") : "") + "Janet" + ChatColor.DARK_RED + ": " + ChatColor.WHITE;
    }

    private String corTime(String time) {
        return time.length() == 1 ? "0" + time : time;
    }

    private String getDateAndTime(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        String second = Integer.toString(c.get(Calendar.SECOND));
        String minute = Integer.toString(c.get(Calendar.MINUTE));
        String hour = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
        String day = Integer.toString(c.get(Calendar.DATE));
        String month = Integer.toString(c.get(Calendar.MONTH) + 1);
        String year = Integer.toString(c.get(Calendar.YEAR));
        hour = corTime(hour);
        minute = corTime(minute);
        second = corTime(second);
        return month + "/" + day + "/" + year + " at " + hour + ":" + minute + ":" + second;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        if (e.getResult().equals(PlayerLoginEvent.Result.KICK_BANNED)) {
            BanEntry banEntry = null;
            if (Bukkit.getBanList(BanList.Type.NAME).isBanned(e.getPlayer().getName()))
                banEntry = Bukkit.getBanList(BanList.Type.NAME).getBanEntry(e.getPlayer().getName());
            else if (Bukkit.getBanList(BanList.Type.IP).isBanned(e.getAddress().toString().split("/")[1].split(":")[0]))
                banEntry = Bukkit.getBanList(BanList.Type.IP).getBanEntry(e.getAddress().toString().split("/")[1].split(":")[0]);
            if (banEntry == null)
                return;
            e.setKickMessage("You were " + ChatColor.DARK_RED + "" + ChatColor.BOLD + "BANNED" + ChatColor.RESET + " on " + getDateAndTime(banEntry.getCreated()) + " by " +
                    banEntry.getSource() + ChatColor.RESET + ".\n" + (banEntry.getExpiration() == null ? "" : "Your ban will expire on " + getDateAndTime(banEntry.getExpiration()) + ".\n") +
                    ChatColor.RESET + "Reason: " + banEntry.getReason() + ChatColor.RESET + "\nAppeal at smp.gamezgalaxy.com/ban");
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        um.addUser(p);
        um.forceParseUser(p);
        final User u = um.getUser(p.getUniqueId());
        if (u.getNick() != null)
            p.setDisplayName(u.getNick());
        UUID uuid = p.getUniqueId();
        YamlConfiguration configLogIn = YamlConfiguration.loadConfiguration(configFileLogIn);
        if (!configLogIn.contains(uuid.toString())) {
            configLogIn.set(uuid.toString(), "{RANK} {NAME}&r joined the game.");
            try {
                configLogIn.save(configFileLogIn);
            } catch (Exception er) {
            }
        }
        YamlConfiguration configLogOut = YamlConfiguration.loadConfiguration(configFileLogOut);
        if (!configLogOut.contains(uuid.toString())) {
            configLogOut.set(uuid.toString(), "{RANK} {NAME}&r Disconnected.");
            try {
                configLogOut.save(configFileLogOut);
            } catch (Exception er) {
            }
        }
        e.setJoinMessage((ChatColor.GREEN + " + " + var.getGuildMsgs() + ChatColor.translateAlternateColorCodes('&',
                configLogIn.getString(uuid.toString()).replaceAll("\\{NAME\\}", p.getDisplayName()).replaceAll("\\{RANK\\}",
                        um.getUser(p.getUniqueId()).getRank().getTitle()))).replaceAll(ChatColor.RESET + "", var.getGuildMsgs() + ""));
        if (!get.hasJoined(uuid)) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            final String welcome = ChatColor.translateAlternateColorCodes('&', config.getString("Necessities.firstTime")).replaceAll("\\{NAME\\}", p.getName());
            if (Necessities.isTracking())
                Necessities.trackAction(p, "NewLogin", p.getName());
            if (config.contains("Spawn")) {
                World world = Bukkit.getWorld(config.getString("Spawn.world"));
                double x = Double.parseDouble(config.getString("Spawn.x"));
                double y = Double.parseDouble(config.getString("Spawn.y"));
                double z = Double.parseDouble(config.getString("Spawn.z"));
                float yaw = Float.parseFloat(config.getString("Spawn.yaw"));
                float pitch = Float.parseFloat(config.getString("Spawn.pitch"));
                p.teleport(new Location(world, x, y, z, yaw, pitch));
            }
            List<String> items = config.getStringList("Necessities.firstItems");
            if (items.contains(""))
                items.remove("");
            if (!items.isEmpty()) {
                PlayerInventory i = p.getInventory();
                for (String item : items)
                    if (item.contains(" ")) {
                        String name = item.split(" ")[0];
                        String data = "0";
                        if (name.contains(":"))
                            data = name.split(":")[1];
                        name = name.split(":")[0];
                        i.addItem(new ItemStack(Material.matchMaterial(mat.idToName(Integer.parseInt(name))), Integer.parseInt(item.split(" ")[1]), Short.parseShort(data)));
                    }
            }
            get.addUUID(uuid);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
                @Override
                public void run() {
                    Bukkit.broadcastMessage(welcome);
                    Bukkit.broadcastMessage(JanetName + "Welcome to GamezGalaxy! Enjoy your stay.");
                }
            });
        } else {
            final boolean hidden = hide.isHidden(e.getPlayer());
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
                @Override
                public void run() {
                    if (hidden)
                        Bukkit.broadcast(var.getMessages() + "To Ops - " + JanetName + "Welcome back.", "Necessities.opBroadcast");
                    else
                        Bukkit.broadcastMessage(JanetName + "Welcome back.");
                }
            });
        }
        if (!balc.doesPlayerExist(uuid))
            balc.addPlayerToList(uuid);
        bot.logIn(uuid);
        hide.playerJoined(p);
        u.setLastAction(System.currentTimeMillis());
        if (hide.isHidden(e.getPlayer())) {
            Bukkit.broadcast(var.getMessages() + "To Ops -" + e.getJoinMessage(), "Necessities.opBroadcast");
            e.setJoinMessage(null);
            hide.hidePlayer(e.getPlayer());
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds"))
            power.addPlayer(p);
        if (!Necessities.getInstance().isProtocolLibLoaded())
            for (User m : um.getUsers().values())
                m.updateListName();
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (!p.hasPermission("Necessities.ignoreGameMode"))
                    p.setGameMode(wm.getGameMode(p.getWorld().getName()));
                if (p.hasPermission("Necessities.fly") && safe.wouldFall(p.getLocation())) {
                    p.setAllowFlight(true);
                    p.setFlying(true);
                    p.sendMessage(var.getMessages() + "Fly enabled.");
                }
                Necessities.getInstance().addHeader(p);
                Necessities.getInstance().addJanet(p);
                Necessities.getInstance().refreshJanet(p);
                Necessities.getInstance().updateAll(p);
                u.updateListName();
                File f = new File("plugins/Necessities/motd.txt");
                if (f.exists())
                    try {
                        BufferedReader read = new BufferedReader(new FileReader(f));
                        String line;
                        while ((line = read.readLine()) != null)
                            if (!line.equals(""))
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                        read.close();
                    } catch (Exception er) {
                    }
            }
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        YamlConfiguration configLogOut = YamlConfiguration.loadConfiguration(configFileLogOut);
        e.setQuitMessage((ChatColor.RED + " - " + var.getGuildMsgs() + ChatColor.translateAlternateColorCodes('&',
                configLogOut.getString(uuid.toString()).replaceAll("\\{NAME\\}", e.getPlayer().getDisplayName()).replaceAll("\\{RANK\\}",
                        um.getUser(e.getPlayer().getUniqueId()).getRank().getTitle()))).replaceAll(ChatColor.RESET + "", var.getGuildMsgs() + ""));
        User u = um.getUser(e.getPlayer().getUniqueId());
        if (hide.isHidden(e.getPlayer())) {
            Bukkit.broadcast(var.getMessages() + "To Ops -" + e.getQuitMessage(), "Necessities.opBroadcast");
            e.setQuitMessage(null);
        }
        if (u.isAfk())
            u.setAfk(false);
        u.logOut();
        bot.logOut(uuid);
        um.removeUser(uuid);
        if (acb.contains(e.getPlayer())) {
            e.getPlayer().setHealth(0);
            Bukkit.broadcastMessage(var.getMessages() + e.getPlayer().getName() + " combat logged.");
        }
        hide.playerLeft(e.getPlayer());
        tps.removeRequests(uuid);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds"))
            power.removePlayer(e.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (config.contains("Spawn")) {
            if (e.getPlayer().getBedSpawnLocation() == null) {
                World world = Bukkit.getWorld(config.getString("Spawn.world"));
                double x = Double.parseDouble(config.getString("Spawn.x"));
                double y = Double.parseDouble(config.getString("Spawn.y"));
                double z = Double.parseDouble(config.getString("Spawn.z"));
                float yaw = Float.parseFloat(config.getString("Spawn.yaw"));
                float pitch = Float.parseFloat(config.getString("Spawn.pitch"));
                e.setRespawnLocation(new Location(world, x, y, z, yaw, pitch));
            } else
                e.setRespawnLocation(safe.getSafe(e.getPlayer().getBedSpawnLocation()));
        }
        final User u = um.getUser(e.getPlayer().getUniqueId());
        final String s = wm.getSysPath(e.getRespawnLocation().getWorld().getName()), from = wm.getSysPath(e.getPlayer().getWorld().getName());
        if (u.isAfk())
            u.setAfk(false);
        u.setLastAction(System.currentTimeMillis());
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (!from.equals(s))
                    u.saveInventory(s, from);
            }
        });
    }

    @EventHandler
    public void foodLevelChange(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player && um.getUser(e.getEntity().getUniqueId()).godmode())
            e.setFoodLevel(20);
    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent e) {
        User u = um.getUser(e.getPlayer().getUniqueId());
        Location from = e.getFrom();
        Location to = e.getTo();
        boolean locationChanged = Math.abs(from.getX() - to.getX()) > 0.1 || Math.abs(from.getY() - to.getY()) > 0.1 || Math.abs(from.getZ() - to.getZ()) > 0.1;
        Hat h = u.getHat();
        if (h != null && !e.isCancelled())
            h.move(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - from.getZ(), to.getYaw() - from.getYaw(), to.getPitch() - from.getPitch());
        if (u.isTeleporting() && locationChanged)
            u.cancelTp();
        if (u.isAfk() && locationChanged)
            u.setAfk(false);
        if (locationChanged)
            u.setLastAction(System.currentTimeMillis());
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (config.contains("Necessities.WorldManager") && config.getBoolean("Necessities.WorldManager") && locationChanged) {
            Location destination = pm.portalDestination(to);
            if (destination != null)
                e.getPlayer().teleport(destination);
        }
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") && !from.getChunk().equals(to.getChunk())) {
            if (u.isClaiming()) {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        e.getPlayer().performCommand("guild claim");
                    }
                });
            }
            Guild owner = gm.chunkOwner(to.getChunk());
            if (owner != gm.chunkOwner(from.getChunk()))
                e.getPlayer().sendMessage(var.getGuildMsgs() + " ~ " + (owner == null ? var.getWild() + "Wilderness" : owner.relation(u.getGuild()) + owner.getName() + " ~ " + owner.getDescription()));
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        User u = um.getUser(e.getPlayer().getUniqueId());
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") && !e.getPlayer().hasPermission("Necessities.guilds.admin")) {
            Guild owner = gm.chunkOwner(e.getBlock().getChunk());
            if (owner != null && u.getGuild() != owner) {
                e.getPlayer().sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are not a part of that guild, and are not allowed to build there.");
                e.setCancelled(true);
            }
        }
        if (wrench.isWrenched(e.getBlock()))
            wrench.wrench(e.getBlock());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        //Guild check
        User u = um.getUser(e.getPlayer().getUniqueId());
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") && !e.getPlayer().hasPermission("Necessities.guilds.admin")) {
            Guild owner = gm.chunkOwner(e.getBlock().getChunk());
            if (owner != null && u.getGuild() != owner) {
                e.getPlayer().sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are not a part of that guild, and are not allowed to build there.");
                e.setCancelled(true);
            }
        }

        ItemMeta meta = e.getItemInHand().getItemMeta();
        Inventory inv = null;
        Block b = e.getBlock();
        if (b.getType().equals(Material.CHEST) || b.getType().equals(Material.TRAPPED_CHEST))
            inv = ((Chest) b.getState()).getBlockInventory();
        else if (b.getType().equals(Material.FURNACE) || b.getType().equals(Material.BURNING_FURNACE))
            inv = ((Furnace) b.getState()).getInventory();
        else if (b.getType().equals(Material.HOPPER))
            inv = ((Hopper) b.getState()).getInventory();
        else if (b.getType().equals(Material.DISPENSER))
            inv = ((Dispenser) b.getState()).getInventory();
        else if (b.getType().equals(Material.DROPPER))
            inv = ((Dropper) b.getState()).getInventory();
        else if (b.getType().equals(Material.BREWING_STAND))
            inv = ((BrewingStand) b.getState()).getInventory();
        if (inv != null && meta.hasLore()) {
            for (String s : meta.getLore())
                if (s.split(" ").length == 6 || s.split(" ").length == 7) {
                    int amount = Integer.parseInt(s.split(" ")[1]);
                    Material type = Material.matchMaterial(mat.idToName(Integer.parseInt(s.split(" ")[2])));
                    short data = Short.parseShort(s.split(" ")[3]);
                    ItemStack i = new ItemStack(type, amount, data);
                    for (String en : s.split(" ")[4].split(","))
                        if (!en.equals("n"))
                            i.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(en.split("-")[0])), Integer.parseInt(en.split("-")[1]));
                    ArrayList<String> lore = new ArrayList<>();
                    for (String l : s.split(" ")[5].split(","))
                        if (!l.equals("n")) {
                            int lStart = l.indexOf('[');
                            if (lStart == -1) {
                                if (l.replaceAll("~", " ").trim().split(" ").length == 5)
                                    lore.add(l.replaceAll("~", " ").trim() + " n");
                                else
                                    lore.add(l.replaceAll("~", " "));
                            } else {
                                String nonMeta = "";
                                if (l.lastIndexOf(']') == l.length() - 1)
                                    l = l.substring(0, l.length() - 1);
                                String subMeta = l.substring(1, l.length());
                                int subStart = subMeta.indexOf('[');
                                if (subStart != -1) {
                                    nonMeta = subMeta.substring(0, subStart);
                                    subMeta = getSub(subMeta.substring(subStart));
                                } else
                                    subMeta = getSub(subMeta.replaceAll("~", " "));
                                String newMeta = getSub(nonMeta.replaceAll("~", " ")) + subMeta;
                                if (newMeta.trim().split(" ").length == 5)
                                    newMeta += " n";
                                lore.add(newMeta.trim());
                            }
                        }
                    ItemMeta im = i.getItemMeta();
                    if (s.split(" ").length == 7)
                        im.setDisplayName(s.split(" ")[6].replaceAll("`", " "));
                    im.setLore(lore);
                    i.setItemMeta(im);
                    for (String loc : (s.split(" ")[0]).split(","))
                        inv.setItem(Integer.parseInt(loc), i);
                }
        } else if (b.getType().equals(Material.MOB_SPAWNER) && meta.hasLore())
            ((CreatureSpawner) b.getState()).setCreatureTypeByName(meta.getLore().get(0));
        else if ((b.getType().equals(Material.SIGN) || b.getType().equals(Material.SIGN_POST) || b.getType().equals(Material.WALL_SIGN)) && meta.hasLore()) {
            Sign s = (Sign) b.getState();
            s.setLine(0, meta.getLore().get(0));
            s.setLine(1, meta.getLore().get(1));
            s.setLine(2, meta.getLore().get(2));
            s.setLine(3, meta.getLore().get(3));
            s.update();
        }
    }

    private String getSub(String sub) {
        String temp = "";
        int in = 0;
        for (int i = 0; i < sub.length(); i++) {
            if (sub.charAt(i) == '[')
                in++;
            else if (sub.charAt(i) == ']')
                in--;
            temp += (in == 0 && sub.charAt(i) == '@') ? "," : sub.charAt(i);
        }
        return temp;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerClick(PlayerInteractEvent e) {
        User u = um.getUser(e.getPlayer().getUniqueId());
        if (u.isAfk())
            u.setAfk(false);
        if (hide.isHidden(u.getPlayer()) && e.getAction().equals(Action.PHYSICAL))//cancel crop breaking when hidden
            e.setCancelled(true);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") && !e.getPlayer().hasPermission("Necessities.guilds.admin")) {
            if ((e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && ((e.getItem() != null && !e.getItem().getType().isEdible()/*.isBlock()*/) ||
                    e.getClickedBlock().getState() instanceof InventoryHolder || e.getClickedBlock().getType().equals(Material.WOODEN_DOOR) ||
                    e.getClickedBlock().getType().equals(Material.ACACIA_DOOR) || e.getClickedBlock().getType().equals(Material.BIRCH_DOOR) ||
                    e.getClickedBlock().getType().equals(Material.DARK_OAK_DOOR) || e.getClickedBlock().getType().equals(Material.JUNGLE_DOOR) ||
                    e.getClickedBlock().getType().equals(Material.SPRUCE_DOOR) || e.getClickedBlock().getType().equals(Material.ACACIA_FENCE_GATE) ||
                    e.getClickedBlock().getType().equals(Material.BIRCH_FENCE_GATE) || e.getClickedBlock().getType().equals(Material.DARK_OAK_FENCE_GATE) ||
                    e.getClickedBlock().getType().equals(Material.FENCE_GATE) || e.getClickedBlock().getType().equals(Material.JUNGLE_FENCE_GATE) ||
                    e.getClickedBlock().getType().equals(Material.SPRUCE_FENCE_GATE) || e.getClickedBlock().getType().equals(Material.STONE_BUTTON) ||
                    e.getClickedBlock().getType().equals(Material.WOOD_BUTTON) || e.getClickedBlock().getType().equals(Material.WOOD_PLATE) ||
                    e.getClickedBlock().getType().equals(Material.STONE_PLATE) || e.getClickedBlock().getType().equals(Material.GOLD_PLATE) ||
                    e.getClickedBlock().getType().equals(Material.IRON_PLATE) || e.getClickedBlock().getType().equals(Material.TRAP_DOOR) ||
                    e.getClickedBlock().getType().equals(Material.LEVER) || e.getClickedBlock().getType().equals(Material.BED_BLOCK) ||
                    e.getClickedBlock().getType().equals(Material.DIODE_BLOCK_OFF) || e.getClickedBlock().getType().equals(Material.DIODE_BLOCK_ON) ||
                    e.getClickedBlock().getType().equals(Material.REDSTONE_COMPARATOR_ON) || e.getClickedBlock().getType().equals(Material.REDSTONE_COMPARATOR_OFF))) ||
                    e.getAction().equals(Action.PHYSICAL)) {
                Guild owner = gm.chunkOwner(e.getClickedBlock().getChunk());
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (e.getBlockFace().equals(BlockFace.EAST))
                        owner = gm.chunkOwner(new Location(e.getPlayer().getWorld(), e.getClickedBlock().getX() + 1, e.getClickedBlock().getY(),
                                e.getClickedBlock().getZ()).getChunk());
                    else if (e.getBlockFace().equals(BlockFace.SOUTH))
                        owner = gm.chunkOwner(new Location(e.getPlayer().getWorld(), e.getClickedBlock().getX(), e.getClickedBlock().getY(),
                                e.getClickedBlock().getZ() + 1).getChunk());
                    else if (e.getBlockFace().equals(BlockFace.WEST))
                        owner = gm.chunkOwner(new Location(e.getPlayer().getWorld(), e.getClickedBlock().getX() - 1, e.getClickedBlock().getY(),
                                e.getClickedBlock().getZ()).getChunk());
                    else if (e.getBlockFace().equals(BlockFace.NORTH))
                        owner = gm.chunkOwner(new Location(e.getPlayer().getWorld(), e.getClickedBlock().getX(), e.getClickedBlock().getY(),
                                e.getClickedBlock().getZ() - 1).getChunk());
                }
                if (owner != null && u.getGuild() != owner) {
                    if ((e.getClickedBlock().getType().equals(Material.WOODEN_DOOR) || e.getClickedBlock().getType().equals(Material.SPRUCE_FENCE_GATE) ||
                            e.getClickedBlock().getType().equals(Material.ACACIA_DOOR) || e.getClickedBlock().getType().equals(Material.BIRCH_DOOR) ||
                            e.getClickedBlock().getType().equals(Material.DARK_OAK_DOOR) || e.getClickedBlock().getType().equals(Material.JUNGLE_DOOR) ||
                            e.getClickedBlock().getType().equals(Material.SPRUCE_DOOR) || e.getClickedBlock().getType().equals(Material.ACACIA_FENCE_GATE) ||
                            e.getClickedBlock().getType().equals(Material.BIRCH_FENCE_GATE) || e.getClickedBlock().getType().equals(Material.DARK_OAK_FENCE_GATE) ||
                            e.getClickedBlock().getType().equals(Material.FENCE_GATE) || e.getClickedBlock().getType().equals(Material.JUNGLE_FENCE_GATE)) && owner.allowInteract()) {

                    } else if (e.getAction().equals(Action.PHYSICAL))
                        e.setCancelled(true);
                    else {
                        e.getPlayer().sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are not a part of that guild, and are not allowed to build there.");
                        if (e.getClickedBlock().getType().equals(Material.WOODEN_DOOR) || e.getClickedBlock().getType().equals(Material.SPRUCE_FENCE_GATE) ||
                                e.getClickedBlock().getType().equals(Material.ACACIA_DOOR) || e.getClickedBlock().getType().equals(Material.BIRCH_DOOR) ||
                                e.getClickedBlock().getType().equals(Material.DARK_OAK_DOOR) || e.getClickedBlock().getType().equals(Material.JUNGLE_DOOR) ||
                                e.getClickedBlock().getType().equals(Material.SPRUCE_DOOR) || e.getClickedBlock().getType().equals(Material.ACACIA_FENCE_GATE) ||
                                e.getClickedBlock().getType().equals(Material.BIRCH_FENCE_GATE) || e.getClickedBlock().getType().equals(Material.DARK_OAK_FENCE_GATE) ||
                                e.getClickedBlock().getType().equals(Material.FENCE_GATE) || e.getClickedBlock().getType().equals(Material.JUNGLE_FENCE_GATE)) {
                            e.getClickedBlock().getState().update();
                            if (e.getClickedBlock().getLocation().getBlockY() > 0)
                                e.getClickedBlock().getRelative(BlockFace.DOWN).getState().update();
                        }
                        e.setCancelled(true);
                    }
                }
            } else if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                Block b = e.getClickedBlock();
                if (e.getBlockFace().equals(BlockFace.EAST) && b.getRelative(BlockFace.EAST).getType().equals(Material.FIRE))
                    b = e.getClickedBlock().getRelative(BlockFace.EAST);
                else if (e.getBlockFace().equals(BlockFace.SOUTH) && b.getRelative(BlockFace.SOUTH).getType().equals(Material.FIRE))
                    b = e.getClickedBlock().getRelative(BlockFace.SOUTH);
                else if (e.getBlockFace().equals(BlockFace.WEST) && b.getRelative(BlockFace.WEST).getType().equals(Material.FIRE))
                    b = e.getClickedBlock().getRelative(BlockFace.WEST);
                else if (e.getBlockFace().equals(BlockFace.NORTH) && b.getRelative(BlockFace.NORTH).getType().equals(Material.FIRE))
                    b = e.getClickedBlock().getRelative(BlockFace.NORTH);
                else if (e.getBlockFace().equals(BlockFace.UP) && b.getRelative(BlockFace.UP).getType().equals(Material.FIRE))
                    b = e.getClickedBlock().getRelative(BlockFace.UP);
                else if (e.getBlockFace().equals(BlockFace.DOWN) && b.getRelative(BlockFace.DOWN).getType().equals(Material.FIRE))
                    b = e.getClickedBlock().getRelative(BlockFace.DOWN);
                Guild owner = gm.chunkOwner(b.getChunk());
                if (owner != null && u.getGuild() != owner && b.getType().equals(Material.FIRE)) {
                    e.getPlayer().sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are not a part of that guild, and are not allowed to build there.");
                    e.setCancelled(true);
                }
            }
        }
        if (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getAction() == Action.LEFT_CLICK_BLOCK)
                u.setLeft(e.getClickedBlock().getLocation());
            else if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
                u.setRight(e.getClickedBlock().getLocation());
            if (u.isJailed())
                e.setCancelled(true);
            else {
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType().equals(Material.BED_BLOCK)) {
                    e.getPlayer().setBedSpawnLocation(e.getClickedBlock().getLocation());
                    e.getPlayer().sendMessage(var.getMessages() + "Bed spawn set.");
                }
                u.setLastAction(System.currentTimeMillis());
                Sign sign = economySigns.sign(e.getClickedBlock().getLocation());
                if (sign != null && e.getAction() == Action.LEFT_CLICK_BLOCK && economySigns.checkFormat(sign)) {
                    String operation = economySigns.operation(sign);
                    String itemName = economySigns.itemLine(sign);
                    int amount = economySigns.amount(sign);
                    Player p = e.getPlayer();
                    p.performCommand(operation + " " + itemName + " " + Integer.toString(amount));
                } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getItem() != null && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasLore() &&
                        e.getItem().getItemMeta().getLore().contains("Wrench")) {
                    Block b = e.getClickedBlock();
                    if (b.getType().equals(Material.REDSTONE_LAMP_OFF)) {//TODO: Add more things wrench works on
                        final Block up = b.getRelative(BlockFace.UP);
                        final Material type = up.getType();
                        final byte metadata = up.getData();
                        final byte raw = up.getState().getRawData();
                        final MaterialData data = up.getState().getData();
                        final ItemStack[] contents;
                        if (up.getState() instanceof InventoryHolder) {
                            InventoryHolder i = (InventoryHolder) up.getState();
                            contents = i.getInventory().getContents();
                            i.getInventory().clear();
                        } else
                            contents = null;
                        up.setType(Material.REDSTONE_BLOCK);
                        wrench.wrench(b);
                        e.setCancelled(true);
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
                            @Override
                            public void run() {
                                up.setType(type);
                                up.setData(metadata);
                                up.getState().setData(data);
                                up.getState().setRawData(raw);
                                if (up.getState() instanceof InventoryHolder)
                                    ((InventoryHolder) up.getState()).getInventory().setContents(contents);
                            }
                        });
                    } else if (b.getType().equals(Material.REDSTONE_LAMP_ON)) {
                        wrench.wrench(b);
                        b.setType(Material.REDSTONE_LAMP_OFF);
                        e.setCancelled(true);
                    } else if (b.getType().equals(Material.DIODE_BLOCK_OFF)) {
                        Diode d = (Diode) b.getState().getData();
                        BlockFace facing = d.getFacing();
                        d.getDelay();
                        b.setType(Material.DIODE_BLOCK_ON);
                        wrench.wrench(b);
                        Diode n = (Diode) b.getState().getData();
                        n.setDelay(d.getDelay());
                        n.setFacingDirection(facing);
                        e.setCancelled(true);
                    } else if (b.getType().equals(Material.DIODE_BLOCK_ON)) {
                        Diode d = (Diode) b.getState().getData();
                        BlockFace facing = d.getFacing();
                        d.getDelay();
                        b.setType(Material.DIODE_BLOCK_OFF);
                        wrench.wrench(b);
                        Diode n = (Diode) b.getState().getData();
                        n.setDelay(d.getDelay());
                        n.setFacingDirection(facing);
                        e.setCancelled(true);
                    } else if (b.getType().equals(Material.REDSTONE_TORCH_OFF))
                        b.setType(Material.REDSTONE_TORCH_ON);
                    else if (b.getType().equals(Material.REDSTONE_TORCH_ON))
                        b.setType(Material.REDSTONE_TORCH_OFF);
                    else if (b.getType().equals(Material.FURNACE) || b.getType().equals(Material.BURNING_FURNACE) || b.getType().equals(Material.HOPPER) ||
                            b.getType().equals(Material.DISPENSER) || b.getType().equals(Material.CHEST) || b.getType().equals(Material.ENDER_CHEST) ||
                            b.getType().equals(Material.DROPPER) || b.getType().equals(Material.PISTON_BASE) || b.getType().equals(Material.PISTON_STICKY_BASE) ||
                            b.getType().equals(Material.TRAPPED_CHEST) || b.getType().equals(Material.BREWING_STAND) || b.getType().equals(Material.MOB_SPAWNER) ||
                            b.getType().equals(Material.SIGN) || b.getType().equals(Material.SIGN_POST) || b.getType().equals(Material.WALL_SIGN)) {
                        if (e.getPlayer().isSneaking()) {
                            ItemStack contents = new ItemStack(b.getType(), 1);
                            if (b.getType().equals(Material.BREWING_STAND))
                                contents = new ItemStack(Material.BREWING_STAND_ITEM, 1);
                            else if (b.getType().equals(Material.SIGN) || b.getType().equals(Material.SIGN_POST) || b.getType().equals(Material.WALL_SIGN))
                                contents = new ItemStack(Material.SIGN, 1);
                            ItemMeta meta = contents.getItemMeta();
                            Inventory inv = null;
                            if (b.getType().equals(Material.CHEST) || b.getType().equals(Material.TRAPPED_CHEST))
                                inv = ((Chest) b.getState()).getBlockInventory();
                            else if (b.getType().equals(Material.FURNACE) || b.getType().equals(Material.BURNING_FURNACE))
                                inv = ((Furnace) b.getState()).getInventory();
                            else if (b.getType().equals(Material.HOPPER))
                                inv = ((Hopper) b.getState()).getInventory();
                            else if (b.getType().equals(Material.DISPENSER))
                                inv = ((Dispenser) b.getState()).getInventory();
                            else if (b.getType().equals(Material.DROPPER))
                                inv = ((Dropper) b.getState()).getInventory();
                            else if (b.getType().equals(Material.BREWING_STAND))
                                inv = ((BrewingStand) b.getState()).getInventory();
                            if (inv != null) {
                                meta.setLore(getLore(inv));
                                contents.setItemMeta(meta);
                                for (HumanEntity ent : inv.getViewers())
                                    ent.closeInventory();
                                inv.clear();
                            } else if (b.getType().equals(Material.MOB_SPAWNER)) {
                                CreatureSpawner cs = (CreatureSpawner) b.getState();
                                ArrayList<String> lore = new ArrayList<>();
                                lore.add(cs.getCreatureTypeName());
                                meta.setLore(lore);
                                contents.setItemMeta(meta);
                            } else if (b.getType().equals(Material.SIGN) || b.getType().equals(Material.SIGN_POST) || b.getType().equals(Material.WALL_SIGN)) {
                                Sign s = (Sign) b.getState();
                                ArrayList<String> lore = new ArrayList<>();
                                lore.add(s.getLine(0));
                                lore.add(s.getLine(1));
                                lore.add(s.getLine(2));
                                lore.add(s.getLine(3));
                                meta.setLore(lore);
                                contents.setItemMeta(meta);
                            }
                            e.getPlayer().getWorld().dropItem(b.getLocation(), contents);
                            b.setType(Material.AIR);
                        } else
                            b.setData(getDir(e.getBlockFace()));
                        e.setCancelled(true);
                    } else if (b.getType().equals(Material.IRON_DOOR_BLOCK)) {
                        if (!b.getRelative(BlockFace.UP).getType().equals(Material.IRON_DOOR_BLOCK))
                            b = b.getRelative(BlockFace.DOWN);
                        wrench.wrench(b.getRelative(BlockFace.UP));
                        wrench.wrench(b);
                        if (b.getData() < 4)
                            b.setData((byte) (b.getData() + 4));
                        else
                            b.setData((byte) (b.getData() - 4));
                        b.getWorld().playEffect(b.getLocation(), Effect.DOOR_TOGGLE, 0);
                        e.setCancelled(true);
                    } else if (b.getType().equals(Material.IRON_TRAPDOOR)) {
                        wrench.wrench(b);
                        if (b.getData() < 4 || (b.getData() > 7 && b.getData() < 12))
                            b.setData((byte) (b.getData() + 4));
                        else
                            b.setData((byte) (b.getData() - 4));
                        b.getWorld().playEffect(b.getLocation(), Effect.DOOR_TOGGLE, 0);
                        e.setCancelled(true);
                    } else
                        e.setCancelled(true);
                    if (!e.isCancelled())
                        wrench.wrench(b);
                }
            }
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && (e.getClickedBlock().getType().equals(Material.CHEST) || e.getClickedBlock().getType().equals(Material.TRAPPED_CHEST)) && hide.isHidden(e.getPlayer())) {
            e.setCancelled(true);
            u.setInvLoc(e.getClickedBlock().getLocation());
            e.getPlayer().openInventory(((InventoryHolder) e.getClickedBlock().getState()).getInventory());
        }
    }

    private ArrayList<String> getLore(Inventory inv) {
        ArrayList<String> lore = new ArrayList<>();
        HashMap<String, String> condensedLore = new HashMap<>();
        for (int i = 0; i < inv.getSize(); i++) {//loc amount type damage enchants meta name
            if (inv.getItem(i) != null && !inv.getItem(i).getType().equals(Material.AIR)) {
                String enchants = "";
                for (Enchantment en : inv.getItem(i).getEnchantments().keySet())
                    enchants += en.getId() + "-" + inv.getItem(i).getEnchantments().get(en) + ",";
                String meta = "";
                ItemMeta innerMeta = inv.getItem(i).getItemMeta();
                if (innerMeta.hasLore()) {
                    for (String l : innerMeta.getLore())
                        meta += "[" + l.replaceAll(" ", "~").replaceAll(",", "@") + "]" + ",";
                    if (meta.length() > 1)
                        meta = meta.substring(0, meta.length() - 1);
                }
                String disp = "";
                if (innerMeta.hasDisplayName())
                    disp = " " + innerMeta.getDisplayName().replaceAll(" ", "`");
                enchants = enchants.trim();
                if (enchants.equals(""))
                    enchants = "n";
                if (meta.equals(""))
                    meta = "n";
                String info = inv.getItem(i).getAmount() + " " + mat.nameToId(inv.getItem(i).getType().toString()) + " " + inv.getItem(i).getDurability() + " " + enchants + " " + meta + disp;
                condensedLore.put(info, condensedLore.containsKey(info) ? condensedLore.get(info) + "," + i : Integer.toString(i));
            }
        }
        for (String key : condensedLore.keySet())
            lore.add(condensedLore.get(key) + " " + key);
        return lore;
    }

    private byte getDir(BlockFace f) {
        if (f.equals(BlockFace.UP))
            return 1;
        else if (f.equals(BlockFace.NORTH))
            return 2;
        else if (f.equals(BlockFace.SOUTH))
            return 3;
        else if (f.equals(BlockFace.WEST))
            return 4;
        else if (f.equals(BlockFace.EAST))
            return 5;
        return 0;
    }

    @EventHandler
    public void onRedstone(BlockRedstoneEvent e) {
        if (e.getBlock().getState() instanceof CommandBlock) {
            CommandBlock b = (CommandBlock) e.getBlock().getState();
            b.setCommand(ChatColor.translateAlternateColorCodes('&', b.getCommand()));
            b.update(true);
        }
        if (wrench.isWrenched(e.getBlock()))
            e.setNewCurrent(((e.getBlock().getType().equals(Material.IRON_DOOR_BLOCK) || e.getBlock().getType().equals(Material.IRON_TRAPDOOR)) && e.getOldCurrent() == 0) ? 0 : 1);
    }

    @EventHandler
    public void onPVP(EntityDamageByEntityEvent e) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            final Player p = (Player) e.getEntity(), damager = (Player) e.getDamager();
            User u = um.getUser(p.getUniqueId()), d = um.getUser(damager.getUniqueId());
            if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") && (gm.chunkOwner(p.getLocation().getChunk()) != null &&
                    !gm.chunkOwner(p.getLocation().getChunk()).canPVP()) ||
                    (gm.chunkOwner(damager.getLocation().getChunk()) != null && !gm.chunkOwner(damager.getLocation().getChunk()).canPVP()))
                e.setCancelled(true);
            else if (p.getWorld().getPVP() && !u.godmode() && !d.godmode() && ((config.contains("Necessities.Guilds") && !config.getBoolean("Necessities.Guilds")) ||
                    (u.getGuild() == null || d.getGuild() == null || u.getGuild().equals(d.getGuild()) ||
                            u.getGuild().isAlly(d.getGuild())) && (gm.chunkOwner(p.getLocation().getChunk()) == null || gm.chunkOwner(p.getLocation().getChunk()).canPVP()) &&
                            (gm.chunkOwner(damager.getLocation().getChunk()) == null || gm.chunkOwner(damager.getLocation().getChunk()).canPVP()))) {
                acb.addToCombat(p, damager);
                try {
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            acb.removeFromCombat(p);
                            acb.removeFromCombat(damager);
                        }
                    }, 10 * 20);
                } catch (Exception er) {
                }
            }
        } else if (e.getDamager() instanceof Player && config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds")) {
            Player damager = (Player) e.getDamager();
            Guild owner = gm.chunkOwner(e.getEntity().getLocation().getChunk());
            User u = um.getUser(damager.getUniqueId());
            if ((e.getEntity() instanceof ItemFrame || e.getEntity() instanceof Painting) && !damager.hasPermission("Necessities.guilds.admin") && owner != null && u.getGuild() != owner) {
                u.getPlayer().sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are not a part of that guild, and are not allowed to build there.");
                e.setCancelled(true);
            } else if (owner != null && !owner.canHostileSpawn() && !damager.hasPermission("Necessities.guilds.admin"))
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractAtEntityEvent e) {
        User u = um.getUser(e.getPlayer().getUniqueId());
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (e.getRightClicked() instanceof ItemFrame && config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") &&
                !e.getPlayer().hasPermission("Necessities.guilds.admin")) {
            Guild owner = gm.chunkOwner(e.getRightClicked().getLocation().getChunk());
            if (owner != null && u.getGuild() != owner) {
                u.getPlayer().sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are not a part of that guild, and are not allowed to build there.");
                ItemFrame frame = (ItemFrame) e.getRightClicked();
                frame.setRotation(frame.getRotation().rotateCounterClockwise());
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHangingBreak(HangingBreakByEntityEvent e) {
        if (e.getRemover() != null && e.getRemover() instanceof Player) {
            Player p = (Player) e.getRemover();
            User u = um.getUser(p.getUniqueId());
            YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") && !p.hasPermission("Necessities.guilds.admin")) {
                Guild owner = gm.chunkOwner(e.getEntity().getLocation().getChunk());
                if (owner != null && u.getGuild() != owner) {
                    u.getPlayer().sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are not a part of that guild, and are not allowed to build there.");
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && um.getUser(e.getEntity().getUniqueId()).godmode())
            e.setCancelled(true);
    }

    @EventHandler
    public void onBowFire(final EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player && e.getProjectile() instanceof Arrow && e.getBow().hasItemMeta()) {
            final Arrow a = (Arrow) e.getProjectile();
            final Location l = a.getLocation();
            final boolean isCrit = a.isCritical();
            final int knockback = a.getKnockbackStrength();
            final Vector v = a.getVelocity();
            final int fire = a.getFireTicks();
            final Player shooter = (Player) e.getEntity();
            if (!e.getBow().getItemMeta().hasLore())
                return;
            if (e.getBow().getItemMeta().getLore().contains("Machine gun"))
                for (int i = 0; i < 63; i++)
                    try {
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
                            @Override
                            public void run() {
                                Arrow temp = (Arrow) e.getEntity().getWorld().spawnEntity(l, EntityType.ARROW);
                                temp.setCritical(isCrit);
                                temp.setFireTicks(fire);
                                temp.setKnockbackStrength(knockback);
                                temp.setTicksLived(100);
                                temp.setVelocity(v);
                                temp.setShooter(shooter);
                            }
                        }, i);
                    } catch (Exception er) {
                    }
            else if (e.getBow().getItemMeta().getLore().contains("Bazooka"))
                for (int i = 0; i < 63; i++) {
                    Arrow temp = (Arrow) e.getEntity().getWorld().spawnEntity(l, EntityType.ARROW);
                    temp.setCritical(isCrit);
                    temp.setFireTicks(fire);
                    temp.setKnockbackStrength(knockback);
                    temp.setTicksLived(100);
                    temp.setVelocity(v);
                    temp.setShooter(shooter);
                }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        User u = um.getUser(e.getPlayer().getUniqueId());
        if (u.isAfk())
            u.setAfk(false);
        u.setLastAction(System.currentTimeMillis());
        Player player = e.getPlayer();
        final UUID uuid = player.getUniqueId();
        String m = e.getMessage();
        if (m.endsWith(">") && !m.equals(">")) {
            String appended = u.getAppended() + " " + m.substring(0, m.length() - 1);
            u.setAppended(appended.trim());
            player.sendMessage(ChatColor.GREEN + "Message appended.");
            e.setCancelled(true);
            return;
        } else if (!u.getAppended().equals("")) {
            e.setMessage(u.getAppended() + " " + m);
            u.setAppended("");
        }
        YamlConfiguration configTitles = YamlConfiguration.loadConfiguration(configFileTitles);
        e.setFormat(ChatColor.translateAlternateColorCodes('&', config.getString("Necessities.ChatFormat")));
        boolean isop = false;
        if (u.slackChat()) {
            e.setFormat(var.getMessages() + "To Slack - " + ChatColor.WHITE + e.getFormat());
            e.setFormat(e.getFormat().replaceAll("\\{WORLD\\} ", ""));
            e.setFormat(e.getFormat().replaceAll("\\{GUILD\\} ", ""));
            e.setFormat(e.getFormat().replaceAll("\\{TITLE\\} ", ""));
        } else if (u.opChat()) {
            isop = true;
            e.setFormat(var.getMessages() + "To Ops - " + ChatColor.WHITE + e.getFormat());
            e.setFormat(e.getFormat().replaceAll("\\{WORLD\\} ", ""));
            e.setFormat(e.getFormat().replaceAll("\\{GUILD\\} ", ""));
            e.setFormat(e.getFormat().replaceAll("\\{TITLE\\} ", ""));
        } else if (player.hasPermission("Necessities.opBroadcast") && e.getMessage().startsWith("#")) {
            isop = true;
            e.setFormat(var.getMessages() + "To Ops - " + ChatColor.WHITE + e.getFormat());
            e.setFormat(e.getFormat().replaceAll("\\{WORLD\\} ", ""));
            e.setFormat(e.getFormat().replaceAll("\\{GUILD\\} ", ""));
            e.setFormat(e.getFormat().replaceAll("\\{TITLE\\} ", ""));
            e.setMessage(e.getMessage().replaceFirst("#", ""));
        } else if (u.guildChat()) {
            e.setFormat(var.getMessages() + "To Guild - " + ChatColor.WHITE + e.getFormat());
            e.setFormat(e.getFormat().replaceAll("\\{WORLD\\} ", ""));
            e.setFormat(e.getFormat().replaceAll("\\{GUILD\\} ", ""));
            e.setFormat(e.getFormat().replaceAll("\\{TITLE\\} ", ""));
        }
        e.setFormat(e.getFormat().replaceAll("\\{GUILD\\} ", config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") && u.getGuild() != null && u.getGuild().getRank(uuid) != null ?
                "{GCOLOR}" + gm.getPrefix(u.getGuild().getRank(uuid)) + u.getGuild().getName() + " " : ""));
        String fullTitle = "";
        if (configTitles.contains(player.getUniqueId() + ".title")) {
            ChatColor brackets = ChatColor.getByChar(configTitles.getString(player.getUniqueId() + ".color"));
            String title = configTitles.getString(player.getUniqueId() + ".title");
            title = ChatColor.translateAlternateColorCodes('&', title);
            fullTitle = ChatColor.RESET + "" + brackets + "[" + ChatColor.RESET + title + ChatColor.RESET + "" + brackets + "] " + ChatColor.RESET;
        }
        e.setFormat(e.getFormat().replaceAll("\\{TITLE\\} ", fullTitle));
        e.setFormat(e.getFormat().replaceAll("\\{NAME\\}", player.getDisplayName()));
        String world = player.getWorld().getName();
        if (world.contains("_"))
            world = form.capFirst(world.replaceAll("world_", "").replaceAll("_", " ")).replaceAll(" ", "_");
        e.setFormat(e.getFormat().replaceAll("\\{WORLD\\}", world));
        String rank = ChatColor.translateAlternateColorCodes('&', um.getUser(uuid).getRank().getTitle());
        e.setFormat(e.getFormat().replaceAll("\\{RANK\\}", rank));
        final String message = bot.logChat(uuid, e.getMessage());
        e.setMessage(message);//Why did it not previously setMessage?
        if (player.hasPermission("Necessities.colorchat"))
            e.setMessage(ChatColor.translateAlternateColorCodes('&', (player.hasPermission("Necessities.magicchat") ? message : message.replaceAll("&k", ""))));
        if (u.isMuted())
            player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are muted.");
        else {
            if (!e.getRecipients().isEmpty()) {
                ArrayList<Player> toRem = new ArrayList<>();
                for (Player recip : e.getRecipients())
                    if (um.getUser(recip.getUniqueId()).isIgnoring(player.getUniqueId()) || (isop && !recip.hasPermission("Necessities.opBroadcast")) || (u.slackChat() && !recip.hasPermission("Necessities.slack"))
                            || (u.guildChat() && u.getGuild() != null && u.getGuild() != um.getUser(recip.getUniqueId()).getGuild()))
                        toRem.add(recip);
                for (Player recip : toRem)
                    e.getRecipients().remove(recip);
            }
            if (!e.getRecipients().isEmpty())
                for (Player recip : e.getRecipients()) {
                    User r = um.getUser(recip.getUniqueId());
                    if (u.getGuild() == null || u.getGuild().getRank(uuid) == null || (config.contains("Necessities.Guilds") && !config.getBoolean("Necessities.Guilds")))
                        recip.sendMessage(e.getFormat().replaceFirst("\\{GCOLOR\\}", "").replaceAll("\\{MESSAGE\\}", "") + e.getMessage());
                    else
                        recip.sendMessage(e.getFormat().replaceFirst("\\{GCOLOR\\}", u.getGuild().relation(r.getGuild()) + "").replaceAll("\\{MESSAGE\\}", "") + e.getMessage());
                }
            Bukkit.getConsoleSender().sendMessage(e.getFormat().replaceFirst("\\{GCOLOR\\}", var.getNeutral() + "").replaceAll("\\{MESSAGE\\}", "") + e.getMessage());
            if (u.slackChat())
                slack.sendMessage((e.getFormat().replaceFirst("\\{GCOLOR\\}", var.getNeutral() + "").replaceAll("\\{MESSAGE\\}", "") + e.getMessage()).replaceFirst("To Slack - ", ""));
            else if (!u.guildChat())
                slack.handleIngameChat((e.getFormat().replaceFirst("\\{GCOLOR\\}", var.getNeutral() + "").replaceAll("\\{MESSAGE\\}", "") + e.getMessage()));
        }
        e.setCancelled(true);
        if (config.contains("Necessities.AI") && config.getBoolean("Necessities.AI") && (!isop || message.startsWith("!")) && !u.guildChat())
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
                @Override
                public void run() {
                    ai.parseMessage(uuid, message);
                }
            });
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        Player player = e.getEntity().getPlayer();
        if (player != null) {
            bot.logDeath(player.getUniqueId(), e.getDeathMessage());
            e.setKeepLevel(player.hasPermission("Necessities.keepxp"));
            e.setKeepInventory(player.hasPermission("Necessities.keepitems"));
            if (player.hasPermission("Necessities.keepxp"))
                e.setDroppedExp(0);
            User u = um.getUser(player.getUniqueId());
            u.setLastPos(player.getLocation());
            if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") && !player.hasPermission("Necessities.guilds.admin"))
                u.removePower();
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        User u = um.getUser(e.getPlayer().getUniqueId());
        if (u.isJailed())
            e.setCancelled(true);
        if (!e.isCancelled() && !e.getMessage().contains("login") && !e.getMessage().contains("register")) {
            spy.broadcast(player.getName(), e.getMessage());
            String message = bot.logCom(player.getUniqueId(), e.getMessage());
            e.setMessage(message);
            u.setLastAction(System.currentTimeMillis());
            if (u.isAfk() && !message.startsWith("/afk") && !message.startsWith("/away"))
                u.setAfk(false);
            YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            if (config.contains("Necessities.customDeny") && config.getBoolean("Necessities.customDeny")) {
                PluginCommand pc = null;
                try {
                    pc = Bukkit.getPluginCommand(e.getMessage().split(" ")[0].replaceFirst("/", ""));
                } catch (Exception er) {
                }//Invalid command
                if (pc != null && !pc.testPermissionSilent(player)) {
                    player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have permission to perform this command.");
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onCommand(ServerCommandEvent e) {
        if (console.chatToggled() && !e.getCommand().equalsIgnoreCase("togglechat") && !e.getCommand().equalsIgnoreCase("tc"))
            e.setCommand("say " + e.getCommand());
        e.setCommand(ChatColor.translateAlternateColorCodes('&', e.getCommand()));
        spy.broadcast(console.getName().replaceAll(":", "") + ChatColor.AQUA, e.getCommand());
        bot.logConsole(e.getCommand());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        final User u = um.getUser(e.getPlayer().getUniqueId());
        if (!e.getFrom().getWorld().equals(e.getTo().getWorld())) {
            if (wm.multiple()) {
                String s = wm.getSysPath(e.getTo().getWorld().getName()), from = wm.getSysPath(e.getFrom().getWorld().getName());
                if (!from.equals(s)) {
                    u.saveInventory(s, from);
                    e.getPlayer().setGameMode(wm.getGameMode(e.getTo().getWorld().getName()));//sets gamemode of player to world they teleported to
                }
            }
            if (!e.getPlayer().hasPermission("Necessities.ignoreGameMode"))
                e.getPlayer().setGameMode(wm.getGameMode(e.getTo().getWorld().getName()));//sets gamemode of player to world they teleported to
            try {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        u.respawnHat();
                    }
                });
            } catch (Exception er) {
            }
            if (!Necessities.getInstance().isProtocolLibLoaded())
                for (User m : um.getUsers().values())
                    m.updateListName();

        } else {
            Hat h = u.getHat();
            if (h != null) {
                Location from = e.getFrom();
                Location to = e.getTo();
                h.move(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - from.getZ(), to.getYaw() - from.getYaw(), to.getPitch() - from.getPitch());
            }
        }
        if (!u.isJailed() && (e.getCause().equals(TeleportCause.COMMAND) || e.getCause().equals(TeleportCause.PLUGIN)))
            u.setLastPos(e.getFrom());
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") && u.isClaiming()) {
            u.setClaiming(false);
            e.getPlayer().sendMessage(var.getMessages() + "No longer automatically claiming land.");
        }
        if (u.isJailed())
            e.setCancelled(true);
        if (!e.isCancelled() && config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") && !e.getFrom().getChunk().equals(e.getTo().getChunk())) {
            Guild owner = gm.chunkOwner(e.getTo().getChunk());
            if (owner != gm.chunkOwner(e.getFrom().getChunk()))
                e.getPlayer().sendMessage(var.getGuildMsgs() + " ~ " + (owner == null ? var.getWild() + "Wilderness" : owner.relation(u.getGuild()) + owner.getName() + " ~ " + owner.getDescription()));
        }
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent e) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds")) {
            ArrayList<Integer> indexes = new ArrayList<>();
            for (int i = 0; i < e.blockList().size(); i++) {
                Guild g = gm.chunkOwner(e.blockList().get(i).getChunk());
                if (g != null && (!g.canExplode() || e.getEntity() instanceof Creeper))
                    indexes.add(0, i);
            }
            for (int i : indexes)
                e.blockList().remove(i);
        }
    }

    @EventHandler
    public void onSignCreate(SignChangeEvent e) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        String line0 = e.getLine(0);
        String line1 = e.getLine(1);
        String line2 = e.getLine(2);
        String line3 = e.getLine(3);
        e.setCancelled(true);
        Sign sign = (Sign) e.getBlock().getState();
        if (sign != null && sign.getLine(0).equals("") && sign.getLine(1).equals("") && sign.getLine(2).equals("") && sign.getLine(3).equals("")) {
            sign.setLine(0, line0);
            sign.setLine(1, line1);
            sign.setLine(2, line2);
            sign.setLine(3, line3);
            sign.update();
            signs.censorSign(sign, e.getPlayer());
            if (config.contains("Necessities.Economy") && config.getBoolean("Necessities.Economy") && economySigns.checkFormat(sign)) {
                if (e.getPlayer().hasPermission("Necessities.economy.setSign"))
                    economySigns.setSign(sign);
                else {
                    sign.setLine(0, "");
                    sign.setLine(1, "");
                    sign.setLine(2, "");
                    sign.setLine(3, "");
                    sign.update();
                }
            }
        }
        User u = um.getUser(e.getPlayer().getUniqueId());
        u.setLastAction(System.currentTimeMillis());
        if (u.isAfk())
            u.setAfk(false);
    }

    @EventHandler
    public void onBookWrite(PlayerEditBookEvent e) {
        BookMeta book = e.getNewBookMeta();
        if (e.isSigning())
            book.setTitle(books.newTitle(book.getTitle(), e.getPlayer()));
        e.setNewBookMeta(books.newMeta(book, e.getPlayer()));
        User u = um.getUser(e.getPlayer().getUniqueId());
        u.setLastAction(System.currentTimeMillis());
        if (u.isAfk())
            u.setAfk(false);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        int rawSlot = e.getRawSlot();
        final Inventory inv = e.getInventory();
        Player p = (Player) e.getWhoClicked();
        if (inv instanceof PlayerInventory) {
            PlayerInventory pinv = (PlayerInventory) inv;
            Player target = invsee.getFromInv(pinv);
            if (target != null && target.hasPermission("Necessities.invseeonly"))
                e.setCancelled(true);
        } else if (inv instanceof AnvilInventory && rawSlot == e.getView().convertSlot(rawSlot) && rawSlot == 2) {
            ItemStack item = e.getCurrentItem();
            if (item != null) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null && meta.hasDisplayName()) {
                    meta.setDisplayName(rename.parseRename(meta.getDisplayName(), p));
                    item.setItemMeta(meta);
                    e.setCurrentItem(item);
                    try {
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), new Runnable() {
                            @Override
                            public void run() {//removes the client side bug of item showing as still there
                                inv.setItem(0, new ItemStack(Material.STONE));
                                inv.clear(0);
                            }
                        });
                    } catch (Exception er) {
                    }
                }
            }
        }
        User u = um.getUser(p.getUniqueId());
        u.setLastAction(System.currentTimeMillis());
        if (u.isAfk())
            u.setAfk(false);
    }

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        User u = um.getUser(e.getPlayer().getUniqueId());
        u.setLastAction(System.currentTimeMillis());
        if (u.isAfk())
            u.setAfk(false);
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        User u = um.getUser(e.getPlayer().getUniqueId());
        u.setLastAction(System.currentTimeMillis());
        if (u.isAfk())
            u.setAfk(false);
        Hat h = u.getHat();
        if (h != null) {
            if (e.isSneaking())
                h.move(0, -0.25, 0, 0, 0);
            else
                h.move(0, 0.25, 0, 0, 0);
        }
    }

    @EventHandler
    public void onSprint(PlayerToggleSprintEvent e) {
        User u = um.getUser(e.getPlayer().getUniqueId());
        u.setLastAction(System.currentTimeMillis());
        if (u.isAfk())
            u.setAfk(false);
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        User u = um.getUser(e.getPlayer().getUniqueId());
        u.setLastAction(System.currentTimeMillis());
        if (u.isAfk())
            u.setAfk(false);
    }

    @EventHandler
    public void onTabComplete(PlayerChatTabCompleteEvent e) {
        User u = um.getUser(e.getPlayer().getUniqueId());
        u.setLastAction(System.currentTimeMillis());
        if (u.isAfk())
            u.setAfk(false);
    }

    @EventHandler
    public void onBreak(PlayerItemBreakEvent e) {
        if (e.getPlayer().hasPermission("Necessities.infiniteDurability"))
            e.getBrokenItem().setAmount(1);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory() instanceof PlayerInventory)
            invsee.closeInv((PlayerInventory) e.getInventory());
        User u = um.getUser(e.getPlayer().getUniqueId());
        u.setLastAction(System.currentTimeMillis());
        if (u.isAfk())
            u.setAfk(false);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        User u = um.getUser(e.getPlayer().getUniqueId());
        if (e.getInventory() instanceof PlayerInventory)
            u.setInvLoc(null);
        u.setLastAction(System.currentTimeMillis());
        if (u.isAfk())
            u.setAfk(false);
    }

    @EventHandler
    public void onPickupItem(PlayerPickupItemEvent e) {
        if (hide.isHidden(e.getPlayer()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) {
        if (e.getTarget() instanceof Player && e.getReason().equals(EntityTargetEvent.TargetReason.CLOSEST_PLAYER) && hide.isHidden((Player) e.getTarget())) {
            e.setCancelled(true);
            e.setTarget(null);
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds")) {
            Guild g = gm.chunkOwner(e.getLocation().getChunk());
            if (g != null && !g.canHostileSpawn() && e.getEntity() instanceof Monster)
                e.setCancelled(true);
        }
        if (!e.isCancelled() && config.contains("Necessities.MaxSingleTypeEntities")) {
            int max = config.getInt("Necessities.MaxSingleTypeEntities");
            int cur = 0;
            for (Entity t : e.getLocation().getChunk().getEntities())
                if (t.getType().equals(e.getEntityType()))
                    cur++;
            if (cur >= max)
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onVehicleDamage(VehicleDamageEvent e) {
        if (e.getAttacker() instanceof Player) {
            Player player = (Player) e.getAttacker();
            User u = um.getUser(player.getUniqueId());
            YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") && !player.hasPermission("Necessities.guilds.admin")) {
                Guild g = gm.chunkOwner(e.getVehicle().getLocation().getChunk());
                if (g != null && u.getGuild() != g)
                    e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onArmorStandChange(PlayerArmorStandManipulateEvent e) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") && !e.getPlayer().hasPermission("Necessities.guilds.admin")) {
            Guild owner = gm.chunkOwner(e.getRightClicked().getLocation().getChunk());
            User u = um.getUser(e.getPlayer().getUniqueId());
            if (owner != null && u.getGuild() != owner) {
                e.getPlayer().sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are not a part of that guild, and are not allowed to build there.");
                e.setCancelled(true);
            }
        }
    }
}