package gg.galaxygaming.necessities;

import gg.galaxygaming.necessities.Commands.CmdHide;
import gg.galaxygaming.necessities.Economy.Signs;
import gg.galaxygaming.necessities.Guilds.Guild;
import gg.galaxygaming.necessities.Guilds.GuildManager;
import gg.galaxygaming.necessities.Hats.Hat;
import gg.galaxygaming.necessities.Janet.JanetAI;
import gg.galaxygaming.necessities.Janet.JanetBooks;
import gg.galaxygaming.necessities.Material.MaterialHelper;
import gg.galaxygaming.necessities.RankManager.RankManager;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.RankManager.UserManager;
import gg.galaxygaming.necessities.WorldManager.WorldManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.Container;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.Powerable;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

@SuppressWarnings("unused")
class Listeners implements Listener {

    private final File configFileLogOut = new File("plugins/Necessities", "logoutmessages.yml");
    private final File configFileLogIn = new File("plugins/Necessities", "loginmessages.yml");
    private final File configFileTitles = new File("plugins/Necessities", "titles.yml");
    private static String JanetName = "";
    private static BossBar welcomeBar;

    Listeners() {
        RankManager rm = Necessities.getRM();
        JanetName = (!rm.getOrder().isEmpty() ? ChatColor
              .translateAlternateColorCodes('&', rm.getRank(rm.getOrder().size() - 1).getTitle() + ' ') : "") + "Janet"
              + ChatColor.DARK_RED + ": " + ChatColor.WHITE;
        welcomeBar = Bukkit
              .createBossBar(ChatColor.GOLD + "Welcome to " + ChatColor.AQUA + "Galaxy Gaming", BarColor.GREEN,
                    BarStyle.SOLID);
    }

    private String corTime(String time) {
        return time.length() == 1 ? '0' + time : time;
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
        return month + '/' + day + '/' + year + " at " + hour + ':' + minute + ':' + second;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        if (e.getResult().equals(PlayerLoginEvent.Result.KICK_BANNED)) {
            BanEntry banEntry = null;
            if (Bukkit.getBanList(BanList.Type.NAME).isBanned(e.getPlayer().getName())) {
                banEntry = Bukkit.getBanList(BanList.Type.NAME).getBanEntry(e.getPlayer().getName());
            } else if (Bukkit.getBanList(BanList.Type.IP)
                  .isBanned(e.getAddress().toString().split("/")[1].split(":")[0])) {
                banEntry = Bukkit.getBanList(BanList.Type.IP)
                      .getBanEntry(e.getAddress().toString().split("/")[1].split(":")[0]);
            }
            if (banEntry == null) {
                return;
            }
            e.setKickMessage(
                  "You were " + ChatColor.DARK_RED.toString() + ChatColor.BOLD + "BANNED" + ChatColor.RESET + " on "
                        + getDateAndTime(banEntry.getCreated()) + " by " +
                        banEntry.getSource() + ChatColor.RESET + ".\n" + (banEntry.getExpiration() == null ? ""
                        : "Your ban will expire on " + getDateAndTime(banEntry.getExpiration()) + ".\n") +
                        ChatColor.RESET + "Reason: " + banEntry.getReason() + ChatColor.RESET
                        + "\nAppeal at galaxygaming.gg");
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {//TODO FIX economy mysql causing a crash
        Backup.tryBackup(); //Call before the UserManager tries to add the user in case it happens to be the time that messes up the users file.
        final Player p = e.getPlayer();
        UserManager um = Necessities.getUM();
        um.addUser(p);
        um.forceParseUser(p);
        final User u = um.getUser(p.getUniqueId());
        if (u.getNick() != null) {
            p.setDisplayName(u.getNick());
        }
        UUID uuid = p.getUniqueId();
        YamlConfiguration configLogIn = YamlConfiguration.loadConfiguration(configFileLogIn);
        if (!configLogIn.contains(uuid.toString())) {
            configLogIn.set(uuid.toString(), "{RANK} {NAME}&r joined the game.");
            try {
                configLogIn.save(configFileLogIn);
            } catch (Exception ignored) {
            }
        }
        YamlConfiguration configLogOut = YamlConfiguration.loadConfiguration(configFileLogOut);
        if (!configLogOut.contains(uuid.toString())) {
            configLogOut.set(uuid.toString(), "{RANK} {NAME}&r Disconnected.");
            try {
                configLogOut.save(configFileLogOut);
            } catch (Exception ignored) {
            }
        }
        Variables var = Necessities.getVar();
        e.setJoinMessage((ChatColor.GREEN + " + " + var.getGuildMsgs() + ChatColor.translateAlternateColorCodes('&',
              configLogIn.getString(uuid.toString()).replaceAll("\\{NAME}", p.getDisplayName()).replaceAll("\\{RANK}",
                    um.getUser(p.getUniqueId()).getRank().getTitle())))
              .replaceAll(ChatColor.RESET.toString(), var.getGuildMsgs().toString()));
        CmdHide hide = Necessities.getHide();
        if (!Bukkit.getOfflinePlayer(uuid).hasPlayedBefore()) {
            YamlConfiguration config = Necessities.getInstance().getConfig();
            final String welcome = ChatColor
                  .translateAlternateColorCodes('&', config.getString("Necessities.firstTime"))
                  .replaceAll("\\{NAME}", p.getName());
            /*if (Necessities.isTracking())
                OpenAnalyticsHook.trackNewLogin(p);*/
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
            items.remove("");
            if (!items.isEmpty()) {
                PlayerInventory i = p.getInventory();
                for (String item : items) {
                    if (item.contains(" ")) {
                        i.addItem(gg.galaxygaming.necessities.Material.Material.fromString(item.split(" ")[0])
                              .toItemStack(Integer.parseInt(item.split(" ")[1])));
                    }
                }
            }
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), () -> {
                Bukkit.broadcastMessage(welcome);
                Bukkit.broadcastMessage(JanetName + "Welcome to Galaxy Gaming! Enjoy your stay.");
            });
        } else {
            final boolean hidden = hide.isHidden(e.getPlayer());
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), () -> {
                if (hidden) {
                    Bukkit.broadcast(var.getMessages() + "To Ops - " + JanetName + "Welcome back.",
                          "Necessities.opBroadcast");
                } else {
                    Bukkit.broadcastMessage(JanetName + "Welcome back.");
                }
            });
        }
        Necessities.getEconomy().addPlayerIfNotExists(uuid);
        Necessities.getBot().logIn(uuid);
        hide.playerJoined(p);
        u.setLastAction(System.currentTimeMillis());
        if (hide.isHidden(e.getPlayer())) {
            Bukkit.broadcast(var.getMessages() + "To Ops -" + e.getJoinMessage(), "Necessities.opBroadcast");
            e.setJoinMessage(null);
            hide.hidePlayer(e.getPlayer());
        }
        YamlConfiguration config = Necessities.getInstance().getConfig();
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds")) {
            Necessities.getPower().addPlayer(p);
        }
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), () -> {
            if (!p.hasPermission("Necessities.ignoreGameMode")) {
                p.setGameMode(Necessities.getWM().getGameMode(p.getWorld().getName()));
            }
            if (p.hasPermission("Necessities.fly") && Necessities.getSafeLocations().wouldFall(p.getLocation())) {
                p.setAllowFlight(true);
                p.setFlying(true);
                p.sendMessage(var.getMessages() + "Fly enabled.");
            }
            Necessities.getInstance().addHeader(p);
            Necessities.getInstance().addJanet(p);
            Necessities.getInstance().updateAll(p);
            u.updateListName();
            File f = new File("plugins/Necessities/motd.txt");
            if (f.exists()) {
                try {
                    BufferedReader read = new BufferedReader(new FileReader(f));
                    String line;
                    while ((line = read.readLine()) != null) {
                        if (!line.equals("")) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                        }
                    }
                    read.close();
                } catch (Exception ignored) {
                }
            }
        });
        welcomeBar.addPlayer(p);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        UserManager um = Necessities.getUM();
        YamlConfiguration configLogOut = YamlConfiguration.loadConfiguration(configFileLogOut);
        Variables var = Necessities.getVar();
        e.setQuitMessage((ChatColor.RED + " - " + var.getGuildMsgs() + ChatColor.translateAlternateColorCodes('&',
              configLogOut.getString(uuid.toString()).replaceAll("\\{NAME}", e.getPlayer().getDisplayName())
                    .replaceAll("\\{RANK}",
                          um.getUser(e.getPlayer().getUniqueId()).getRank().getTitle())))
              .replaceAll(ChatColor.RESET.toString(), var.getGuildMsgs().toString()));
        User u = um.getUser(e.getPlayer().getUniqueId());
        CmdHide hide = Necessities.getHide();
        if (hide.isHidden(e.getPlayer())) {
            Bukkit.broadcast(var.getMessages() + "To Ops -" + e.getQuitMessage(), "Necessities.opBroadcast");
            e.setQuitMessage(null);
        }
        Necessities.getEconomy().unloadAccount(uuid);
        if (u.isAfk()) {
            u.setAfk(false);
        }
        u.logOut();
        Necessities.getBot().logOut(uuid);
        um.removeUser(uuid);
        if (Necessities.getACB().contains(e.getPlayer())) {
            e.getPlayer().setHealth(0);
            Bukkit.broadcastMessage(var.getMessages() + e.getPlayer().getName() + " combat logged.");
        }
        hide.playerLeft(e.getPlayer());
        Necessities.getTPs().removeRequests(uuid);
        YamlConfiguration config = Necessities.getInstance().getConfig();
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds")) {
            Necessities.getPower().removePlayer(e.getPlayer());
        }
        welcomeBar.removePlayer(e.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        if (config.contains("Spawn")) {
            if (e.getPlayer().getBedSpawnLocation() == null) {
                World world = Bukkit.getWorld(config.getString("Spawn.world"));
                double x = Double.parseDouble(config.getString("Spawn.x"));
                double y = Double.parseDouble(config.getString("Spawn.y"));
                double z = Double.parseDouble(config.getString("Spawn.z"));
                float yaw = Float.parseFloat(config.getString("Spawn.yaw"));
                float pitch = Float.parseFloat(config.getString("Spawn.pitch"));
                e.setRespawnLocation(new Location(world, x, y, z, yaw, pitch));
            } else {
                e.setRespawnLocation(Necessities.getSafeLocations().getSafe(e.getPlayer().getBedSpawnLocation()));
            }
        }
        final User u = Necessities.getUM().getUser(e.getPlayer().getUniqueId());
        WorldManager wm = Necessities.getWM();
        final String s = wm.getSysPath(e.getRespawnLocation().getWorld().getName()), from = wm
              .getSysPath(e.getPlayer().getWorld().getName());
        if (u.isAfk()) {
            u.setAfk(false);
        }
        u.setLastAction(System.currentTimeMillis());
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), () -> {
            if (!from.equals(s)) {
                u.saveInventory(s, from);
            }
        });
    }

    @EventHandler
    public void foodLevelChange(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player && Necessities.getUM().getUser(e.getEntity().getUniqueId()).godMode()) {
            e.setFoodLevel(20);
        }
    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent e) {
        User u = Necessities.getUM().getUser(e.getPlayer().getUniqueId());
        if (u.isFrozen()) {
            e.setCancelled(true);
            return;
        }
        Location from = e.getFrom();
        Location to = e.getTo();
        boolean locationChanged = Math.abs(from.getX() - to.getX()) > 0.1 || Math.abs(from.getY() - to.getY()) > 0.1
              || Math.abs(from.getZ() - to.getZ()) > 0.1;
        Hat h = u.getHat();
        if (h != null && !e.isCancelled()) {
            h.move(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - from.getZ(),
                  to.getYaw() - from.getYaw(), to.getPitch() - from.getPitch());
        }
        if (u.isTeleporting() && locationChanged) {
            u.cancelTp();
        }
        if (u.isAfk() && locationChanged) {
            u.setAfk(false);
        }
        if (locationChanged) {
            u.setLastAction(System.currentTimeMillis());
        }
        YamlConfiguration config = Necessities.getInstance().getConfig();
        if (config.contains("Necessities.WorldManager") && config.getBoolean("Necessities.WorldManager")
              && locationChanged) {
            Necessities.getPM().portalDestination(to, e.getPlayer());
        }
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") && !from.getChunk()
              .equals(to.getChunk())) {
            if (u.isClaiming()) {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(),
                      () -> e.getPlayer().performCommand("guild claim"));
            }
            GuildManager gm = Necessities.getGM();
            Guild owner = gm.chunkOwner(to.getChunk());
            if (owner != gm.chunkOwner(from.getChunk())) {
                e.getPlayer().sendActionBar(owner == null ? Necessities.getVar().getWild() + "Wilderness"
                      : owner.relation(u.getGuild()) + owner.getName() + " ~ " + owner.getDescription());
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        User u = Necessities.getUM().getUser(e.getPlayer().getUniqueId());
        YamlConfiguration config = Necessities.getInstance().getConfig();
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") && !e.getPlayer()
              .hasPermission("Necessities.guilds.admin")) {
            Guild owner = Necessities.getGM().chunkOwner(e.getBlock().getChunk());
            if (owner != null && u.getGuild() != owner) {
                Variables var = Necessities.getVar();
                e.getPlayer().sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "You are not a part of that guild, and are not allowed to build there.");
                e.setCancelled(true);
            }
        }
        Wrenched wrench = Necessities.getWrench();
        if (wrench.isWrenched(e.getBlock())) {
            wrench.wrench(e.getBlock());
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        //Guild check
        User u = Necessities.getUM().getUser(e.getPlayer().getUniqueId());
        YamlConfiguration config = Necessities.getInstance().getConfig();
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") && !e.getPlayer()
              .hasPermission("Necessities.guilds.admin")) {
            Guild owner = Necessities.getGM().chunkOwner(e.getBlock().getChunk());
            if (owner != null && u.getGuild() != owner) {
                Variables var = Necessities.getVar();
                e.getPlayer().sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "You are not a part of that guild, and are not allowed to build there.");
                e.setCancelled(true);
            }
        }

        ItemMeta meta = e.getItemInHand().getItemMeta();
        Inventory inv = null;
        Block b = e.getBlock();
        Material type = b.getType();
        if (type.equals(Material.CHEST) || type.equals(Material.TRAPPED_CHEST) || type.equals(Material.FURNACE) || type
              .equals(Material.HOPPER) ||
              type.equals(Material.DISPENSER) || type.equals(Material.DROPPER) || type.equals(Material.BREWING_STAND)) {
            inv = ((Container) b.getState()).getInventory();
        }
        if (inv != null && meta.hasLore()) {
            for (String s : meta.getLore()) {
                String[] splitSpace = s.split(" ");
                if (splitSpace.length == 5 || splitSpace.length == 6) {
                    int amount = Integer.parseInt(splitSpace[1]);
                    gg.galaxygaming.necessities.Material.Material mat = gg.galaxygaming.necessities.Material.Material
                          .fromString(splitSpace[2]);
                    if (mat == null) {
                        continue;
                    }
                    ItemStack i = mat.toItemStack(amount);
                    for (String en : splitSpace[3].split(",")) {
                        if (!en.equals("n")) {
                            String[] split = en.split("-");
                            NamespacedKey key;
                            if (split[0].equals("minecraft")) {
                                key = NamespacedKey.minecraft(split[1]);
                            } else {
                                Plugin p = Bukkit.getPluginManager().getPlugin(split[0]);
                                key = p == null ? null : new NamespacedKey(p, split[1]);
                            }
                            if (key != null) {
                                i.addUnsafeEnchantment(Enchantment.getByKey(key), Integer.parseInt(split[2]));
                            }
                        }
                    }
                    List<String> lore = new ArrayList<>();
                    for (String l : splitSpace[4].split(",")) {
                        if (!l.equals("n")) {
                            int lStart = l.indexOf('[');
                            if (lStart == -1) {
                                if (l.replaceAll("~", " ").trim().split(" ").length == 5) {
                                    lore.add(l.replaceAll("~", " ").trim() + " n");
                                } else {
                                    lore.add(l.replaceAll("~", " "));
                                }
                            } else {
                                String nonMeta = "";
                                if (l.lastIndexOf(']') == l.length() - 1) {
                                    l = l.substring(0, l.length() - 1);
                                }
                                String subMeta = l.substring(1, l.length());
                                int subStart = subMeta.indexOf('[');
                                if (subStart != -1) {
                                    nonMeta = subMeta.substring(0, subStart);
                                    subMeta = getSub(subMeta.substring(subStart));
                                } else {
                                    subMeta = getSub(subMeta.replaceAll("~", " "));
                                }
                                String newMeta = getSub(nonMeta.replaceAll("~", " ")) + subMeta;
                                if (newMeta.trim().split(" ").length == 4) {
                                    newMeta += " n";
                                }
                                lore.add(newMeta.trim());
                            }
                        }
                    }
                    ItemMeta im = i.getItemMeta();
                    if (splitSpace.length == 6) {
                        im.setDisplayName(splitSpace[5].replaceAll("`", " "));
                    }
                    im.setLore(lore);
                    i.setItemMeta(im);
                    for (String loc : splitSpace[0].split(",")) {
                        inv.setItem(Integer.parseInt(loc), i);
                    }
                }
            }
        } else if (type.equals(Material.SPAWNER) && meta.hasLore()) {
            CreatureSpawner spawner = (CreatureSpawner) b.getState();
            spawner.setSpawnedType(EntityType.valueOf(meta.getLore().get(0)));
            spawner.update();
        } else if ((type.equals(Material.SIGN) || type.equals(Material.WALL_SIGN)) && meta.hasLore()) {
            Sign s = (Sign) b.getState();
            s.setLine(0, meta.getLore().get(0));
            s.setLine(1, meta.getLore().get(1));
            s.setLine(2, meta.getLore().get(2));
            s.setLine(3, meta.getLore().get(3));
            s.update();
        }
    }

    private String getSub(String sub) {
        StringBuilder temp = new StringBuilder();
        int in = 0;
        for (int i = 0; i < sub.length(); i++) {
            if (sub.charAt(i) == '[') {
                in++;
            } else if (sub.charAt(i) == ']') {
                in--;
            }
            temp.append(in == 0 && sub.charAt(i) == '@' ? "," : sub.charAt(i));
        }
        return temp.toString();
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent e) {
        User u = Necessities.getUM().getUser(e.getPlayer().getUniqueId());
        if (u.isAfk()) {
            u.setAfk(false);
        }
        CmdHide hide = Necessities.getHide();
        if (hide.isHidden(u.getPlayer()) && e.getAction().equals(Action.PHYSICAL))//cancel crop breaking when hidden
        {
            e.setCancelled(true);
        }
        Block b = e.getClickedBlock();
        if (b == null) {
            return;
        }
        YamlConfiguration config = Necessities.getInstance().getConfig();
        Variables var = Necessities.getVar();
        GuildManager gm = Necessities.getGM();
        Material type = b.getType();
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") && !e.getPlayer()
              .hasPermission("Necessities.guilds.admin")) {
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && (
                  e.getItem() != null && !e.getItem().getType().isEdible() ||
                        b.getState() instanceof InventoryHolder || MaterialHelper.isWoodDoor(type) || MaterialHelper
                        .isFenceGate(type) || MaterialHelper.isButton(type) ||
                        MaterialHelper.isPressurePlate(type) || MaterialHelper.isWoodTrapdoor(type) || MaterialHelper
                        .isBed(type) || type.equals(Material.LEVER) ||
                        type.equals(Material.REPEATER) || type.equals(Material.COMPARATOR)) ||
                  e.getAction().equals(Action.PHYSICAL)) {
                Guild owner = gm.chunkOwner(b.getChunk());
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (e.getBlockFace().equals(BlockFace.EAST)) {
                        owner = gm.chunkOwner(new Location(e.getPlayer().getWorld(), b.getX() + 1, b.getY(),
                              b.getZ()).getChunk());
                    } else if (e.getBlockFace().equals(BlockFace.SOUTH)) {
                        owner = gm.chunkOwner(new Location(e.getPlayer().getWorld(), b.getX(), b.getY(),
                              b.getZ() + 1).getChunk());
                    } else if (e.getBlockFace().equals(BlockFace.WEST)) {
                        owner = gm.chunkOwner(new Location(e.getPlayer().getWorld(), b.getX() - 1, b.getY(),
                              b.getZ()).getChunk());
                    } else if (e.getBlockFace().equals(BlockFace.NORTH)) {
                        owner = gm.chunkOwner(new Location(e.getPlayer().getWorld(), b.getX(), b.getY(),
                              b.getZ() - 1).getChunk());
                    }
                }
                if (owner != null && u.getGuild() != owner) {
                    if ((!MaterialHelper.isWoodDoor(type) && !MaterialHelper.isFenceGate(type)) || !owner
                          .allowInteract()) {
                        if (e.getAction().equals(Action.PHYSICAL)) {
                            e.setCancelled(true);
                        } else {
                            e.getPlayer().sendMessage(var.getEr() + "Error: " + var.getErMsg()
                                  + "You are not a part of that guild, and are not allowed to build there.");
                            if (MaterialHelper.isWoodDoor(type) || MaterialHelper.isFenceGate(type)) {
                                b.getState().update();
                                if (b.getLocation().getBlockY() > 0) {
                                    b.getRelative(BlockFace.DOWN).getState().update();
                                }
                            }
                            e.setCancelled(true);
                        }
                    }
                }
            } else if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                Block br = b;
                if (e.getBlockFace().equals(BlockFace.EAST) && br.getRelative(BlockFace.EAST).getType()
                      .equals(Material.FIRE)) {
                    br = br.getRelative(BlockFace.EAST);
                } else if (e.getBlockFace().equals(BlockFace.SOUTH) && br.getRelative(BlockFace.SOUTH).getType()
                      .equals(Material.FIRE)) {
                    br = br.getRelative(BlockFace.SOUTH);
                } else if (e.getBlockFace().equals(BlockFace.WEST) && br.getRelative(BlockFace.WEST).getType()
                      .equals(Material.FIRE)) {
                    br = br.getRelative(BlockFace.WEST);
                } else if (e.getBlockFace().equals(BlockFace.NORTH) && br.getRelative(BlockFace.NORTH).getType()
                      .equals(Material.FIRE)) {
                    br = br.getRelative(BlockFace.NORTH);
                } else if (e.getBlockFace().equals(BlockFace.UP) && br.getRelative(BlockFace.UP).getType()
                      .equals(Material.FIRE)) {
                    br = br.getRelative(BlockFace.UP);
                } else if (e.getBlockFace().equals(BlockFace.DOWN) && br.getRelative(BlockFace.DOWN).getType()
                      .equals(Material.FIRE)) {
                    br = br.getRelative(BlockFace.DOWN);
                }
                Guild owner = gm.chunkOwner(br.getChunk());
                if (owner != null && u.getGuild() != owner && br.getType().equals(Material.FIRE)) {
                    e.getPlayer().sendMessage(var.getEr() + "Error: " + var.getErMsg()
                          + "You are not a part of that guild, and are not allowed to build there.");
                    e.setCancelled(true);
                }
            }
        }
        if (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                u.setLeft(b.getLocation());
            } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                u.setRight(b.getLocation());
            }
            if (u.isJailed()) {
                e.setCancelled(true);
            } else {
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK && MaterialHelper.isBed(type)) {
                    e.getPlayer().setBedSpawnLocation(b.getLocation());
                    e.getPlayer().sendMessage(var.getMessages() + "Bed spawn set.");
                }
                u.setLastAction(System.currentTimeMillis());
                Signs economySigns = Necessities.getEconomySigns();
                Sign sign = economySigns.sign(b.getLocation());
                if (sign != null && e.getAction() == Action.LEFT_CLICK_BLOCK && economySigns.checkFormat(sign)) {
                    String operation = economySigns.operation(sign);
                    String itemName = economySigns.itemLine(sign);
                    int amount = economySigns.amount(sign);
                    Player p = e.getPlayer();
                    p.performCommand(operation + ' ' + itemName + ' ' + Integer.toString(amount));
                } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getHand().equals(EquipmentSlot.HAND)
                      && e.getItem() != null && e.getItem().hasItemMeta() &&
                      e.getItem().getItemMeta().hasLore() && e.getItem().getItemMeta().getLore().contains("Wrench")) {
                    Wrenched wrench = Necessities.getWrench();
                    BlockState state = b.getState();
                    boolean isIronDoor;
                    if (type.equals(Material.REDSTONE_LAMP) || type.equals(Material.REDSTONE_TORCH)) {
                        Lightable lightable = (Lightable) state.getBlockData();
                        lightable.setLit(!lightable.isLit());
                        state.setBlockData(lightable);
                        state.update();
                    } else if (type.equals(Material.REPEATER) || type.equals(Material.COMPARATOR)) {
                        wrench.wrench(b);
                        Powerable powerable = (Powerable) state.getBlockData();
                        powerable.setPowered(!powerable.isPowered());
                        state.setBlockData(powerable);
                        e.setCancelled(true);
                        state.update();
                    } else if (type.equals(Material.FURNACE) || type.equals(Material.HOPPER) || type
                          .equals(Material.DISPENSER) || type.equals(Material.CHEST) ||
                          type.equals(Material.ENDER_CHEST) || type.equals(Material.DROPPER) || type
                          .equals(Material.PISTON) ||
                          type.equals(Material.STICKY_PISTON) || type.equals(Material.TRAPPED_CHEST) || type
                          .equals(Material.BREWING_STAND) ||
                          type.equals(Material.SPAWNER) || type.equals(Material.SIGN) || type
                          .equals(Material.WALL_SIGN)) {
                        if (e.getPlayer().isSneaking()) {
                            ItemStack contents = new ItemStack(type, 1);
                            if (type.equals(Material.BREWING_STAND)) {
                                contents = new ItemStack(Material.BREWING_STAND, 1);
                            } else if (type.equals(Material.SIGN) || type.equals(Material.WALL_SIGN)) {
                                contents = new ItemStack(Material.SIGN, 1);
                            }
                            ItemMeta meta = contents.getItemMeta();
                            Inventory inv = null;
                            if (type.equals(Material.CHEST) || type.equals(Material.TRAPPED_CHEST) || type
                                  .equals(Material.FURNACE) || type.equals(Material.HOPPER) ||
                                  type.equals(Material.DISPENSER) || type.equals(Material.DROPPER) || type
                                  .equals(Material.BREWING_STAND)) {
                                inv = ((Container) b.getState()).getInventory();
                            }
                            if (inv != null) {
                                meta.setLore(getLore(inv));
                                contents.setItemMeta(meta);
                                inv.getViewers().forEach(HumanEntity::closeInventory);
                                inv.clear();
                            } else if (type.equals(Material.SPAWNER)) {
                                CreatureSpawner cs = (CreatureSpawner) state;
                                List<String> lore = new ArrayList<>();
                                lore.add(cs.getSpawnedType().toString());
                                meta.setLore(lore);
                                contents.setItemMeta(meta);
                            } else if (type.equals(Material.SIGN) || type.equals(Material.WALL_SIGN)) {
                                Sign s = (Sign) state;
                                List<String> lore = new ArrayList<>();
                                lore.add(s.getLine(0));
                                lore.add(s.getLine(1));
                                lore.add(s.getLine(2));
                                lore.add(s.getLine(3));
                                meta.setLore(lore);
                                contents.setItemMeta(meta);
                            }
                            e.getPlayer().getWorld().dropItem(b.getLocation(), contents);
                            b.setType(Material.AIR);
                        } else if (b
                              .getBlockData() instanceof Directional) { //This if statement should be last as this is the fallback
                            Directional dir = (Directional) b.getBlockData();
                            if (dir.getFaces().contains(e.getBlockFace())) {
                                dir.setFacing(e.getBlockFace());
                                state.setBlockData(dir);
                                state.update();
                            }
                        }
                        e.setCancelled(true);
                    } else if ((isIronDoor = type.equals(Material.IRON_DOOR)) || type.equals(Material.IRON_TRAPDOOR)) {
                        if (isIronDoor) {
                            if (!b.getRelative(BlockFace.UP).getType().equals(Material.IRON_DOOR)) {
                                b = b.getRelative(BlockFace.DOWN);
                                state = b.getState();
                            }
                            wrench.wrench(b.getRelative(BlockFace.UP));
                        }
                        wrench.wrench(b);
                        Openable door = (Openable) b.getBlockData();
                        door.setOpen(!door.isOpen());
                        state.setBlockData(door);
                        Powerable powerable = (Powerable) state.getBlockData();
                        powerable.setPowered(!powerable.isPowered());
                        state.setBlockData(powerable);
                        b.getWorld().playEffect(b.getLocation(), Effect.DOOR_TOGGLE, 0);
                        e.setCancelled(true);
                        state.update();
                    } else {
                        e.setCancelled(true);
                    }
                    if (!e.isCancelled()) {
                        wrench.wrench(b);
                    }
                }
            }
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && hide.isHidden(e.getPlayer()) && (
              type.equals(Material.CHEST) ||
                    type.equals(Material.TRAPPED_CHEST) || MaterialHelper.isShulker(type))) {
            e.getPlayer().openInventory(((InventoryHolder) b.getState()).getInventory());
            e.getPlayer().closeInventory();
        }
    }

    private List<String> getLore(Inventory inv) {
        Map<String, String> condensedLore = new HashMap<>();
        for (int i = 0; i < inv.getSize(); i++) {//loc amount type damage enchants meta name
            ItemStack item = inv.getItem(i);
            if (item != null && !item.getType().equals(Material.AIR)) {
                StringBuilder enchantsBuilder = new StringBuilder();
                for (Enchantment en : item.getEnchantments().keySet()) {
                    enchantsBuilder.append(en.getKey().getNamespace()).append('-').append(en.getKey().getKey())
                          .append('-').append(item.getEnchantments().get(en)).append(',');
                }
                String meta = "";
                ItemMeta innerMeta = item.getItemMeta();
                if (innerMeta.hasLore()) {
                    StringBuilder metaBuilder = new StringBuilder();
                    for (String l : innerMeta.getLore()) {
                        metaBuilder.append('[').append(l.replaceAll(" ", "~").replaceAll(",", "@")).append(']')
                              .append(',');
                    }
                    meta = metaBuilder.toString();
                    if (meta.length() > 1) {
                        meta = meta.substring(0, meta.length() - 1);
                    }
                }
                String disp = "";
                if (innerMeta.hasDisplayName()) {
                    disp = ' ' + innerMeta.getDisplayName().replaceAll(" ", "`");
                }
                String enchants = enchantsBuilder.toString().trim();
                if (enchants.equals("")) {
                    enchants = "n";
                }
                if (meta.equals("")) {
                    meta = "n";
                }
                String info =
                      item.getAmount() + " " + gg.galaxygaming.necessities.Material.Material.fromBukkit(item.getType())
                            + ' ' + enchants + ' ' + meta + disp;
                condensedLore.put(info,
                      condensedLore.containsKey(info) ? condensedLore.get(info) + ',' + i : Integer.toString(i));
            }
        }
        return condensedLore.keySet().stream().map(key -> condensedLore.get(key) + ' ' + key)
              .collect(Collectors.toCollection(ArrayList::new));
    }

    @EventHandler
    public void onRedstone(BlockRedstoneEvent e) {
        Block block = e.getBlock();
        if (block.getState() instanceof CommandBlock) {
            CommandBlock b = (CommandBlock) block.getState();
            b.setCommand(ChatColor.translateAlternateColorCodes('&', b.getCommand()));
            b.update(true);
        }
        if (Necessities.getWrench().isWrenched(block)) {
            e.setNewCurrent(e.getOldCurrent());
        }
    }

    @EventHandler
    public void onPVP(EntityDamageByEntityEvent e) {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        UserManager um = Necessities.getUM();
        GuildManager gm = Necessities.getGM();
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            final Player p = (Player) e.getEntity(), damager = (Player) e.getDamager();
            User u = um.getUser(p.getUniqueId()), d = um.getUser(damager.getUniqueId());
            if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds")
                  && gm.chunkOwner(p.getLocation().getChunk()) != null &&
                  !gm.chunkOwner(p.getLocation().getChunk()).canPVP() ||
                  gm.chunkOwner(damager.getLocation().getChunk()) != null && !gm
                        .chunkOwner(damager.getLocation().getChunk()).canPVP()) {
                e.setCancelled(true);
            } else if (p.getWorld().getPVP() && !u.godMode() && !d.godMode() && (
                  config.contains("Necessities.Guilds") && !config.getBoolean("Necessities.Guilds") ||
                        (u.getGuild() == null || d.getGuild() == null || u.getGuild().equals(d.getGuild()) ||
                              u.getGuild().isAlly(d.getGuild())) && (gm.chunkOwner(p.getLocation().getChunk()) == null
                              || gm.chunkOwner(p.getLocation().getChunk()).canPVP()) &&
                              (gm.chunkOwner(damager.getLocation().getChunk()) == null || gm
                                    .chunkOwner(damager.getLocation().getChunk()).canPVP()))) {
                AntiCombatLog acb = Necessities.getACB();
                acb.addToCombat(p, damager);
                try {
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), () -> {
                        acb.removeFromCombat(p);
                        acb.removeFromCombat(damager);
                    }, 10 * 20);
                } catch (Exception ignored) {
                }
            }
        } else if (e.getDamager() instanceof Player && config.contains("Necessities.Guilds") && config
              .getBoolean("Necessities.Guilds")) {
            Player damager = (Player) e.getDamager();
            Guild owner = gm.chunkOwner(e.getEntity().getLocation().getChunk());
            User u = um.getUser(damager.getUniqueId());
            if ((e.getEntity() instanceof ItemFrame || e.getEntity() instanceof Painting) && !damager
                  .hasPermission("Necessities.guilds.admin") && owner != null && u.getGuild() != owner) {
                Variables var = Necessities.getVar();
                u.getPlayer().sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "You are not a part of that guild, and are not allowed to build there.");
                e.setCancelled(true);
            } else if (owner != null && !owner.canHostileSpawn() && !damager
                  .hasPermission("Necessities.guilds.admin")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractAtEntityEvent e) {
        User u = Necessities.getUM().getUser(e.getPlayer().getUniqueId());
        YamlConfiguration config = Necessities.getInstance().getConfig();
        Entity entity = e.getRightClicked();
        if (entity instanceof ItemFrame && config.contains("Necessities.Guilds") && config
              .getBoolean("Necessities.Guilds") &&
              !e.getPlayer().hasPermission("Necessities.guilds.admin")) {
            Guild owner = Necessities.getGM().chunkOwner(entity.getLocation().getChunk());
            if (owner != null && u.getGuild() != owner) {
                Variables var = Necessities.getVar();
                u.getPlayer().sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "You are not a part of that guild, and are not allowed to build there.");
                ItemFrame frame = (ItemFrame) entity;
                frame.setRotation(frame.getRotation().rotateCounterClockwise());
                e.setCancelled(true);
            }
        }
        if (!e.isCancelled()) {
            ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
            if (e.getHand().equals(EquipmentSlot.HAND) && item != null && item.hasItemMeta() && item.getItemMeta()
                  .hasLore() && item.getItemMeta().getLore().contains("Wrench")) {
                if (entity.getType().equals(EntityType.PRIMED_TNT)) {
                    entity.getLocation().getBlock().setType(Material.TNT);
                    entity.remove();
                } else if (entity.getType().equals(EntityType.CREEPER)) {
                    Creeper c = (Creeper) entity;
                    c.setIgnited(!c.isIgnited());
                }
            }
        }
    }

    @EventHandler
    public void onHangingBreak(HangingBreakByEntityEvent e) {
        if (e.getRemover() != null && e.getRemover() instanceof Player) {
            Player p = (Player) e.getRemover();
            User u = Necessities.getUM().getUser(p.getUniqueId());
            YamlConfiguration config = Necessities.getInstance().getConfig();
            if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") && !p
                  .hasPermission("Necessities.guilds.admin")) {
                Guild owner = Necessities.getGM().chunkOwner(e.getEntity().getLocation().getChunk());
                if (owner != null && u.getGuild() != owner) {
                    Variables var = Necessities.getVar();
                    u.getPlayer().sendMessage(var.getEr() + "Error: " + var.getErMsg()
                          + "You are not a part of that guild, and are not allowed to build there.");
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && Necessities.getUM().getUser(e.getEntity().getUniqueId()).godMode()) {
            e.setCancelled(true);
        }
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
            if (!e.getBow().getItemMeta().hasLore()) {
                return;
            }
            if (e.getBow().getItemMeta().getLore().contains("Machine gun")) {
                for (int i = 0; i < 63; i++) {
                    try {
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), () -> {
                            Arrow temp = (Arrow) e.getEntity().getWorld().spawnEntity(l, EntityType.ARROW);
                            temp.setCritical(isCrit);
                            temp.setFireTicks(fire);
                            temp.setKnockbackStrength(knockback);
                            temp.setTicksLived(100);
                            temp.setVelocity(v);
                            temp.setShooter(shooter);
                        }, i);
                    } catch (Exception ignored) {
                    }
                }
            } else if (e.getBow().getItemMeta().getLore().contains("Bazooka")) {
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
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        UserManager um = Necessities.getUM();
        User u = um.getUser(e.getPlayer().getUniqueId());
        if (u.isAfk()) {
            u.setAfk(false);
        }
        u.setLastAction(System.currentTimeMillis());
        final Player player = e.getPlayer();
        final UUID uuid = player.getUniqueId();
        String m = e.getMessage();
        m = Matcher.quoteReplacement(m);
        e.setMessage(m);
        if (m.endsWith(">") && !m.equals(">")) {
            String appended = u.getAppended() + ' ' + m.substring(0, m.length() - 1);
            u.setAppended(appended.trim());
            player.sendMessage(ChatColor.GREEN + "Message appended.");
            e.setCancelled(true);
            return;
        } else if (!u.getAppended().equals("")) {
            e.setMessage(u.getAppended() + ' ' + m);
            u.setAppended("");
        }
        YamlConfiguration configTitles = YamlConfiguration.loadConfiguration(configFileTitles);
        e.setFormat(ChatColor.translateAlternateColorCodes('&', config.getString("Necessities.ChatFormat")));
        boolean isOp = false;
        Variables var = Necessities.getVar();
        if (u.slackChat()) {
            e.setFormat(var.getMessages() + "To Slack - " + ChatColor.WHITE + e.getFormat());
            e.setFormat(e.getFormat().replaceAll("\\{WORLD} ", ""));
            e.setFormat(e.getFormat().replaceAll("\\{GUILD} ", ""));
            e.setFormat(e.getFormat().replaceAll("\\{TITLE} ", ""));
            e.setFormat(e.getFormat().replaceAll("\\{PREFIX} ", ""));
        } else if (u.opChat()) {
            isOp = true;
            e.setFormat(var.getMessages() + "To Ops - " + ChatColor.WHITE + e.getFormat());
            e.setFormat(e.getFormat().replaceAll("\\{WORLD} ", ""));
            e.setFormat(e.getFormat().replaceAll("\\{GUILD} ", ""));
            e.setFormat(e.getFormat().replaceAll("\\{TITLE} ", ""));
            e.setFormat(e.getFormat().replaceAll("\\{PREFIX} ", ""));
        } else if (player.hasPermission("Necessities.opBroadcast") && e.getMessage().startsWith("#")) {
            isOp = true;
            e.setFormat(var.getMessages() + "To Ops - " + ChatColor.WHITE + e.getFormat());
            e.setFormat(e.getFormat().replaceAll("\\{WORLD} ", ""));
            e.setFormat(e.getFormat().replaceAll("\\{GUILD} ", ""));
            e.setFormat(e.getFormat().replaceAll("\\{TITLE} ", ""));
            e.setFormat(e.getFormat().replaceAll("\\{PREFIX} ", ""));
            e.setMessage(e.getMessage().replaceFirst("#", ""));
        } else if (u.guildChat()) {
            e.setFormat(var.getMessages() + "To Guild - " + ChatColor.WHITE + e.getFormat());
            e.setFormat(e.getFormat().replaceAll("\\{WORLD} ", ""));
            e.setFormat(e.getFormat().replaceAll("\\{GUILD} ", ""));
            e.setFormat(e.getFormat().replaceAll("\\{TITLE} ", ""));
            e.setFormat(e.getFormat().replaceAll("\\{PREFIX} ", ""));
        }
        e.setFormat(e.getFormat().replaceAll("\\{GUILD} ",
              config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") && u.getGuild() != null
                    &&
                    u.getGuild().getRank(uuid) != null ? "{GCOLOR}" + Necessities.getGM()
                    .getPrefix(u.getGuild().getRank(uuid)) + u.getGuild().getName() + ' ' : ""));
        e.setFormat(e.getFormat().replaceFirst("\\{PREFIX}", u.getPrefix()));
        String fullTitle = "";
        if (configTitles.contains(player.getUniqueId() + ".title")) {
            ChatColor brackets = ChatColor.getByChar(configTitles.getString(player.getUniqueId() + ".color"));
            String title = configTitles.getString(player.getUniqueId() + ".title");
            title = ChatColor.translateAlternateColorCodes('&', title);
            fullTitle =
                  ChatColor.RESET.toString() + brackets + '[' + ChatColor.RESET + title + ChatColor.RESET.toString()
                        + brackets + "] " + ChatColor.RESET;
        }
        e.setFormat(e.getFormat().replaceAll("\\{TITLE} ", fullTitle));
        e.setFormat(e.getFormat().replaceAll("\\{NAME}", player.getDisplayName()));
        String world = player.getWorld().getName();
        if (world.contains("_")) {
            world = Utils.capFirst(world.replaceAll("world_", "").replaceAll("_", " ")).replaceAll(" ", "_");
        }
        e.setFormat(e.getFormat().replaceAll("\\{WORLD}", world));
        String rank = ChatColor.translateAlternateColorCodes('&', um.getUser(uuid).getRank().getTitle());
        e.setFormat(e.getFormat().replaceAll("\\{RANK}", rank));
        final String message = Necessities.getBot().logChat(uuid, e.getMessage());
        e.setMessage(message);//Why did it not previously setMessage?
        if (player.hasPermission("Necessities.colorchat")) {
            e.setMessage(ChatColor.translateAlternateColorCodes('&',
                  player.hasPermission("Necessities.magicchat") ? message : message.replaceAll("&k", "")));
        }
        if (u.isMuted()) {
            player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You are muted.");
        } else {
            if (!e.getRecipients().isEmpty()) {
                List<Player> toRem = new ArrayList<>();
                for (Player recip : e.getRecipients()) {
                    if (um.getUser(recip.getUniqueId()).isIgnoring(player.getUniqueId()) || isOp && !recip
                          .hasPermission("Necessities.opBroadcast") || u.slackChat() &&
                          !recip.hasPermission("Necessities.slack")
                          || u.guildChat() && u.getGuild() != null && u.getGuild() != um.getUser(recip.getUniqueId())
                          .getGuild()) {
                        toRem.add(recip);
                    }
                }
                toRem.forEach(recip -> e.getRecipients().remove(recip));
            }
            String msg = e.getFormat().replaceAll("\\{MESSAGE}", e.getMessage());
            if (!e.getRecipients().isEmpty()) {
                boolean noGuilds = u.getGuild() == null || u.getGuild().getRank(uuid) == null || !config
                      .contains("Necessities.Guilds") || !config.getBoolean("Necessities.Guilds");
                String ngm = "";
                if (noGuilds) {
                    ngm = msg.replaceFirst("\\{GCOLOR}", "");
                }
                for (Player recip : e.getRecipients()) {
                    User r = um.getUser(recip.getUniqueId());
                    if (noGuilds) {
                        recip.sendMessage(ngm);
                    } else {
                        recip.sendMessage(
                              msg.replaceFirst("\\{GCOLOR}", u.getGuild().relation(r.getGuild()).toString()));
                    }
                }
            }
            Bukkit.getConsoleSender().sendMessage(msg.replaceFirst("\\{GCOLOR}", var.getNeutral().toString()));
            if (u.slackChat()) {
                Necessities.getSlack().sendMessage(
                      msg.replaceFirst("\\{GCOLOR}", var.getNeutral().toString()).replaceFirst("To Slack - ", ""));
            } else if (!u.guildChat()) {
                Necessities.getSlack().handleInGameChat(msg.replaceFirst("\\{GCOLOR}", var.getNeutral().toString()));
            }
        }
        e.setCancelled(true);
        if (config.contains("Necessities.AI") && config.getBoolean("Necessities.AI") && (!isOp || message
              .startsWith("!")) && !u.guildChat()) {
            BukkitRunnable aiTask = new BukkitRunnable() {
                @Override
                public void run() {
                    Necessities.getAI().parseMessage(uuid, message, JanetAI.Source.Server, false, null);
                }
            };
            aiTask.runTaskLaterAsynchronously(Necessities.getInstance(), 1);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        Player player = e.getEntity().getPlayer();
        if (player != null) {
            Necessities.getBot().logDeath(player.getUniqueId(), e.getDeathMessage());
            e.setKeepLevel(player.hasPermission("Necessities.keepxp"));
            e.setKeepInventory(player.hasPermission("Necessities.keepitems"));
            if (player.hasPermission("Necessities.keepxp")) {
                e.setDroppedExp(0);
            }
            User u = Necessities.getUM().getUser(player.getUniqueId());
            u.setLastPos(player.getLocation());
            if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") && !player
                  .hasPermission("Necessities.guilds.admin")) {
                u.removePower();
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        User u = Necessities.getUM().getUser(e.getPlayer().getUniqueId());
        if (u.isJailed()) {
            e.setCancelled(true);
        }
        if (!e.isCancelled() && !e.getMessage().contains("login") && !e.getMessage().contains("register")) {
            Necessities.getSpy().broadcast(player.getName(), e.getMessage());
            String message = Necessities.getBot().logCom(player.getUniqueId(), e.getMessage());
            e.setMessage(message);
            u.setLastAction(System.currentTimeMillis());
            if (u.isAfk() && !message.startsWith("/afk") && !message.startsWith("/away")) {
                u.setAfk(false);
            }
            if (e.getMessage().startsWith("/tps")) {
                e.setMessage(e.getMessage().replaceFirst("tps", "necessities:tps"));
            }
            YamlConfiguration config = Necessities.getInstance().getConfig();
            if (config.contains("Necessities.customDeny") && config.getBoolean("Necessities.customDeny")) {
                PluginCommand pc = null;
                try {
                    pc = Bukkit.getPluginCommand(e.getMessage().split(" ")[0].replaceFirst("/", ""));
                } catch (Exception ignored) {
                }//Invalid command
                if (pc != null && !pc.testPermissionSilent(player)) {
                    Variables var = Necessities.getVar();
                    player.sendMessage(var.getEr() + "Error: " + var.getErMsg()
                          + "You do not have permission to perform this command.");
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onCommand(ServerCommandEvent e) {
        Console console = Necessities.getConsole();
        if (console.chatToggled() && !e.getCommand().equalsIgnoreCase("togglechat") && !e.getCommand()
              .equalsIgnoreCase("tc")) {
            e.setCommand("say " + e.getCommand());
        }
        e.setCommand(ChatColor.translateAlternateColorCodes('&', e.getCommand()));
        Necessities.getSpy().broadcast(console.getName().replaceAll(":", "") + ChatColor.AQUA, e.getCommand());
        Necessities.getBot().logConsole(e.getCommand());
        if (e.getCommand().startsWith("tps")) {
            e.setCommand("necessities:" + e.getCommand());
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        final User u = Necessities.getUM().getUser(e.getPlayer().getUniqueId());
        if (u.isFrozen()) {
            e.setCancelled(true);
            return;
        }
        if (!e.getFrom().getWorld().equals(e.getTo().getWorld())) {
            WorldManager wm = Necessities.getWM();
            if (wm.multiple()) {
                String s = wm.getSysPath(e.getTo().getWorld().getName()), from = wm
                      .getSysPath(e.getFrom().getWorld().getName());
                if (!from.equals(s)) {
                    u.saveInventory(s, from);
                    e.getPlayer().setGameMode(wm.getGameMode(
                          e.getTo().getWorld().getName()));//sets gamemode of player to world they teleported to
                }
            }
            if (!e.getPlayer().hasPermission("Necessities.ignoreGameMode")) {
                e.getPlayer().setGameMode(wm.getGameMode(
                      e.getTo().getWorld().getName()));//sets gamemode of player to world they teleported to
            }
            try {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(), u::respawnHat);
            } catch (Exception ignored) {
            }
        } else {
            Hat h = u.getHat();
            if (h != null) {
                Location from = e.getFrom();
                Location to = e.getTo();
                h.move(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - from.getZ(),
                      to.getYaw() - from.getYaw(), to.getPitch() - from.getPitch());
            }
        }
        if (!u.isJailed() && (e.getCause().equals(TeleportCause.COMMAND) || e.getCause()
              .equals(TeleportCause.PLUGIN))) {
            u.setLastPos(e.getFrom());
        }
        Variables var = Necessities.getVar();
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") && u.isClaiming()) {
            u.setClaiming(false);
            e.getPlayer().sendMessage(var.getMessages() + "No longer automatically claiming land.");
        }
        if (u.isJailed()) {
            e.setCancelled(true);
        }
        if (!e.isCancelled() && config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") && !e
              .getFrom().getChunk().equals(e.getTo().getChunk())) {
            GuildManager gm = Necessities.getGM();
            Guild owner = gm.chunkOwner(e.getTo().getChunk());
            if (owner != gm.chunkOwner(e.getFrom().getChunk())) {
                e.getPlayer().sendActionBar(owner == null ? Necessities.getVar().getWild() + "Wilderness"
                      : owner.relation(u.getGuild()) + owner.getName() + " ~ " + owner.getDescription());
            }
        }
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent e) {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds")) {
            List<Integer> indexes = new ArrayList<>();
            GuildManager gm = Necessities.getGM();
            for (int i = 0; i < e.blockList().size(); i++) {
                Guild g = gm.chunkOwner(e.blockList().get(i).getChunk());
                if (g != null && !g.canExplode()) {
                    indexes.add(0, i);
                }
            }
            for (int i : indexes) {
                e.blockList().remove(i);
            }
        }
    }

    @EventHandler
    public void onSignCreate(SignChangeEvent e) {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        String line0 = e.getLine(0);
        String line1 = e.getLine(1);
        String line2 = e.getLine(2);
        String line3 = e.getLine(3);
        e.setCancelled(true);
        Sign sign = (Sign) e.getBlock().getState();
        if (sign != null && sign.getLine(0).equals("") && sign.getLine(1).equals("") && sign.getLine(2).equals("")
              && sign.getLine(3).equals("")) {
            sign.setLine(0, line0);
            sign.setLine(1, line1);
            sign.setLine(2, line2);
            sign.setLine(3, line3);
            sign.update();
            Necessities.getSigns().censorSign(sign, e.getPlayer());
            Signs economySigns = Necessities.getEconomySigns();
            if (config.contains("Necessities.Economy") && config.getBoolean("Necessities.Economy") && economySigns
                  .checkFormat(sign)) {
                if (e.getPlayer().hasPermission("Necessities.economy.setSign")) {
                    economySigns.setSign(sign);
                } else {
                    sign.setLine(0, "");
                    sign.setLine(1, "");
                    sign.setLine(2, "");
                    sign.setLine(3, "");
                    sign.update();
                }
            }
        }
        User u = Necessities.getUM().getUser(e.getPlayer().getUniqueId());
        u.setLastAction(System.currentTimeMillis());
        if (u.isAfk()) {
            u.setAfk(false);
        }
    }

    @EventHandler
    public void onBookWrite(PlayerEditBookEvent e) {
        BookMeta book = e.getNewBookMeta();
        JanetBooks books = Necessities.getBooks();
        if (e.isSigning()) {
            book.setTitle(books.newTitle(book.getTitle(), e.getPlayer()));
        }
        e.setNewBookMeta(books.newMeta(book, e.getPlayer()));
        User u = Necessities.getUM().getUser(e.getPlayer().getUniqueId());
        u.setLastAction(System.currentTimeMillis());
        if (u.isAfk()) {
            u.setAfk(false);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        int rawSlot = e.getRawSlot();
        final Inventory inv = e.getInventory();
        Player p = (Player) e.getWhoClicked();
        if (inv instanceof PlayerInventory) {
            Player target = Necessities.getInvsee().getFromInv((PlayerInventory) inv);
            if (target != null && (target.hasPermission("Necessities.invseeonly")
                  || !target.getUniqueId().equals(p.getUniqueId()) && !p.hasPermission("Necessities.invseeCanEdit"))) {
                e.setCancelled(true);
            }
        } else if (inv instanceof AnvilInventory && rawSlot == e.getView().convertSlot(rawSlot) && rawSlot == 2) {
            ItemStack item = e.getCurrentItem();
            if (item != null) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null && meta.hasDisplayName()) {
                    meta.setDisplayName(Necessities.getRename().parseRename(meta.getDisplayName(), p));
                    item.setItemMeta(meta);
                    e.setCurrentItem(item);
                    try {
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Necessities.getInstance(),
                              () -> {//removes the client side bug of item showing as still there
                                  inv.setItem(0, new ItemStack(Material.STONE));
                                  inv.clear(0);
                              });
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        User u = Necessities.getUM().getUser(p.getUniqueId());
        u.setLastAction(System.currentTimeMillis());
        if (u.isAfk()) {
            u.setAfk(false);
        }
    }

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        User u = Necessities.getUM().getUser(e.getPlayer().getUniqueId());
        u.setLastAction(System.currentTimeMillis());
        if (u.isAfk()) {
            u.setAfk(false);
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        User u = Necessities.getUM().getUser(e.getPlayer().getUniqueId());
        u.setLastAction(System.currentTimeMillis());
        if (u.isAfk()) {
            u.setAfk(false);
        }
        Hat h = u.getHat();
        if (h != null) {
            if (e.isSneaking()) {
                h.move(0, -0.25, 0, 0, 0);
            } else {
                h.move(0, 0.25, 0, 0, 0);
            }
        }
    }

    @EventHandler
    public void onSprint(PlayerToggleSprintEvent e) {
        User u = Necessities.getUM().getUser(e.getPlayer().getUniqueId());
        u.setLastAction(System.currentTimeMillis());
        if (u.isAfk()) {
            u.setAfk(false);
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        User u = Necessities.getUM().getUser(e.getPlayer().getUniqueId());
        u.setLastAction(System.currentTimeMillis());
        if (u.isAfk()) {
            u.setAfk(false);
        }
    }

    @EventHandler
    public void onBreak(PlayerItemBreakEvent e) {
        if (e.getPlayer().hasPermission("Necessities.infiniteDurability")) {
            e.getBrokenItem().setAmount(1);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory() instanceof PlayerInventory) {
            Necessities.getInvsee().closeInv((PlayerInventory) e.getInventory());
        }
        User u = Necessities.getUM().getUser(e.getPlayer().getUniqueId());
        u.setLastAction(System.currentTimeMillis());
        if (u.isAfk()) {
            u.setAfk(false);
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        User u = Necessities.getUM().getUser(e.getPlayer().getUniqueId());
        u.setLastAction(System.currentTimeMillis());
        if (u.isAfk()) {
            u.setAfk(false);
        }
    }

    @EventHandler
    public void onPickupItem(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player && Necessities.getHide().isHidden((Player) e.getEntity())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) {
        if (e.getTarget() instanceof Player && e.getReason().equals(EntityTargetEvent.TargetReason.CLOSEST_PLAYER)
              && Necessities.getHide().isHidden((Player) e.getTarget())) {
            e.setCancelled(true);
            e.setTarget(null);
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds")) {
            Guild g = Necessities.getGM().chunkOwner(e.getLocation().getChunk());
            if (g != null && !g.canHostileSpawn() && e.getEntity() instanceof Monster) {
                e.setCancelled(true);
            }
        }
        if (!e.isCancelled() && config.contains("Necessities.MaxSingleTypeEntities") && !e.getEntityType()
              .equals(EntityType.ARMOR_STAND) &&
              (int) Arrays.stream(e.getLocation().getChunk().getEntities())
                    .filter(t -> t.getType().equals(e.getEntityType())).count() >= config
                    .getInt("Necessities.MaxSingleTypeEntities")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onVehicleDamage(VehicleDamageEvent e) {
        if (e.getAttacker() instanceof Player) {
            Player player = (Player) e.getAttacker();
            User u = Necessities.getUM().getUser(player.getUniqueId());
            YamlConfiguration config = Necessities.getInstance().getConfig();
            if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") && !player
                  .hasPermission("Necessities.guilds.admin")) {
                Guild g = Necessities.getGM().chunkOwner(e.getVehicle().getLocation().getChunk());
                if (g != null && u.getGuild() != g) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onArmorStandChange(PlayerArmorStandManipulateEvent e) {
        YamlConfiguration config = Necessities.getInstance().getConfig();
        if (config.contains("Necessities.Guilds") && config.getBoolean("Necessities.Guilds") && !e.getPlayer()
              .hasPermission("Necessities.guilds.admin")) {
            Guild owner = Necessities.getGM().chunkOwner(e.getRightClicked().getLocation().getChunk());
            User u = Necessities.getUM().getUser(e.getPlayer().getUniqueId());
            if (owner != null && u.getGuild() != owner) {
                Variables var = Necessities.getVar();
                e.getPlayer().sendMessage(var.getEr() + "Error: " + var.getErMsg()
                      + "You are not a part of that guild, and are not allowed to build there.");
                e.setCancelled(true);
            }
        }
    }
}