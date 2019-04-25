package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.RankManager;
import gg.galaxygaming.necessities.RankManager.User;
import gg.galaxygaming.necessities.RankManager.UserManager;
import gg.galaxygaming.necessities.Variables;
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CmdHide implements Cmd {

    private final ArrayList<UUID> hidden = new ArrayList<>();
    private final File configFileHiding = new File("plugins/Necessities", "hiding.yml");
    private final File configFileLogOut = new File("plugins/Necessities", "logoutmessages.yml");
    private final File configFileLogIn = new File("plugins/Necessities", "loginmessages.yml");

    public boolean commandUse(CommandSender sender, String[] args) {
        UserManager um = Necessities.getUM();
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User u = um.getUser(p.getUniqueId());
            if (hidden.contains(p.getUniqueId())) {
                unhidePlayer(p);
                YamlConfiguration configLogIn = YamlConfiguration.loadConfiguration(configFileLogIn);
                Bukkit.broadcastMessage(
                      (ChatColor.GREEN + " + " + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&',
                            configLogIn.getString(p.getUniqueId().toString()).replaceAll("\\{NAME}", p.getDisplayName())
                                  .replaceAll("\\{RANK}",
                                        um.getUser(p.getUniqueId()).getRank().getTitle())))
                            .replaceAll(ChatColor.RESET.toString(), ChatColor.YELLOW.toString()));
                hidden.remove(p.getUniqueId());
                p.sendMessage(var.getMessages() + "You are now visible.");
                Bukkit.broadcast(var.getMessages() + "To Ops - " + var.getObj() + p.getDisplayName() + var.getMessages()
                            + " - is now " + ChatColor.DARK_GRAY + "visible" + var.getMessages() + '.',
                      "Necessities.opBroadcast");
                RankManager rm = Necessities.getRM();
                String rank = "";
                if (!rm.getOrder().isEmpty()) {
                    rank = ChatColor
                          .translateAlternateColorCodes('&', rm.getRank(rm.getOrder().size() - 1).getTitle() + ' ');
                }
                Bukkit.broadcastMessage(rank + "Janet" + ChatColor.DARK_RED + ": " + ChatColor.WHITE + "Welcome Back.");
                if (u.opChat()) {
                    u.toggleOpChat();
                    p.sendMessage(var.getMessages() + "You are no longer sending messages to ops.");
                }
            } else {
                hidePlayer(p);
                YamlConfiguration configLogOut = YamlConfiguration.loadConfiguration(configFileLogOut);
                Bukkit.broadcastMessage(
                      (ChatColor.RED + " - " + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&',
                            configLogOut.getString(p.getUniqueId().toString())
                                  .replaceAll("\\{NAME}", p.getDisplayName()).replaceAll("\\{RANK}",
                                  um.getUser(p.getUniqueId()).getRank().getTitle())))
                            .replaceAll(ChatColor.RESET.toString(), ChatColor.YELLOW.toString()));
                hidden.add(p.getUniqueId());
                p.sendMessage(var.getMessages() + "You are now hidden.");
                Bukkit.broadcast(var.getMessages() + "To Ops - " + var.getObj() + p.getDisplayName() + var.getMessages()
                            + " - is now " + ChatColor.WHITE + "invisible" + var.getMessages() + '.',
                      "Necessities.opBroadcast");
                if (!u.opChat()) {
                    u.toggleOpChat();
                    p.sendMessage(var.getMessages() + "You are now sending messages only to ops.");
                }
            }
        } else {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot hide.");
        }
        return true;
    }

    /**
     * Checks if the specified player is hidden.
     *
     * @param p The player to check.
     * @return True if the player is hidden, false otherwise.
     */
    public boolean isHidden(Player p) {
        return p != null && hidden.contains(p.getUniqueId());
    }

    /**
     * Hides all the hidden players if the specified player is unable to see them.
     *
     * @param p The player to check if they can see hidden players.
     */
    public void playerJoined(Player p) {
        if (!p.hasPermission("Necessities.seehidden")) {
            hidden.stream().filter(uuid -> Bukkit.getPlayer(uuid) != null)
                  .forEach(uuid -> p.hidePlayer(Necessities.getInstance(), Bukkit.getPlayer(uuid)));
        }
    }

    /**
     * Shows all the hidden players to the specified player, to prevent glitches when they log back in.
     *
     * @param p The player to show the hidden players to.
     */
    public void playerLeft(Player p) {
        hidden.stream().filter(uuid -> Bukkit.getPlayer(uuid) != null)
              .forEach(uuid -> p.showPlayer(Necessities.getInstance(), Bukkit.getPlayer(uuid)));
    }

    /**
     * Hides the specified player.
     *
     * @param p The player to hide.
     */
    public void hidePlayer(Player p) {
        Bukkit.getOnlinePlayers().stream()
              .filter(x -> !x.equals(p) && x.canSee(p) && !x.hasPermission("Necessities.seehidden"))
              .forEach(x -> x.hidePlayer(Necessities.getInstance(), p));
        Necessities.getInstance().removePlayer(p);
    }

    private void unhidePlayer(Player p) {
        Bukkit.getOnlinePlayers().stream().filter(x -> !x.equals(p) && !x.canSee(p))
              .forEach(x -> x.showPlayer(Necessities.getInstance(), p));
        Necessities.getInstance().addPlayer(p);
    }

    /**
     * Saves the hidden players to file.
     */
    public void unload() {
        YamlConfiguration configHiding = YamlConfiguration.loadConfiguration(configFileHiding);
        configHiding.getKeys(false).forEach(key -> configHiding.set(key, null));
        hidden.forEach(uuid -> configHiding.set(uuid.toString(), true));
        try {
            configHiding.save(configFileHiding);
        } catch (Exception ignored) {
        }
    }

    /**
     * Loads the hidden players from file.
     */
    public void init() {
        YamlConfiguration configSpying = YamlConfiguration.loadConfiguration(configFileHiding);
        hidden.addAll(configSpying.getKeys(false).stream().map(UUID::fromString).collect(Collectors.toList()));
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}