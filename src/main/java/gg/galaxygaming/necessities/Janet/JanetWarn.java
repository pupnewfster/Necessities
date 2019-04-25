package gg.galaxygaming.necessities.Janet;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.RankManager.RankManager;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class JanetWarn {

    private final Map<UUID, Integer> warnCount = new HashMap<>();
    private String JanetName = "";
    private int warns;

    /**
     * Initiates JanetWarn.
     */
    public void initiate() {
        RankManager rm = Necessities.getRM();
        JanetName = (!rm.getOrder().isEmpty() ? ChatColor
              .translateAlternateColorCodes('&', rm.getRank(rm.getOrder().size() - 1).getTitle() + ' ') : "") + "Janet"
              + ChatColor.DARK_RED + ": " + ChatColor.WHITE;
        warns = Necessities.getInstance().getConfig().getInt("Necessities.warns");
    }

    void removePlayer(UUID uuid) {
        warnCount.remove(uuid);
    }

    /**
     * Warns a specified player for the given reason.
     *
     * @param uuid The uuid of the player to warn.
     * @param reason The reason the player is getting warned.
     * @param warner The name of the person who warned the player.
     */
    public void warn(UUID uuid, String reason, String warner) {
        if (!warnCount.containsKey(uuid)) {
            warnCount.put(uuid, 1);
        } else {
            warnCount.put(uuid, warnCount.get(uuid) + 1);
        }
        String warning;
        if (warnCount.get(uuid) == warns) {
            switch (reason) {
                case "Language":
                    warning = language(uuid);
                    break;
                case "ChatSpam":
                    warning = chatSpam(uuid);
                    break;
                case "CmdSpam":
                    warning = cmdSpam(uuid);
                    break;
                case "Adds":
                    warning = advertising(uuid);
                    break;
                default:
                    warning = other(uuid, reason);
                    break;
            }
        } else {
            switch (reason) {
                case "Language":
                    warning = langMsg(uuid);
                    break;
                case "ChatSpam":
                    warning = chatMsg(uuid);
                    break;
                case "CmdSpam":
                    warning = cmdMsg(uuid);
                    break;
                case "Adds":
                    warning = addsMsg(uuid);
                    break;
                default:
                    warning = warnMessage(uuid, reason, warner);
                    break;
            }
            timesLeft(uuid);
        }
        Necessities.getLog().log("Janet: " + warning);
    }

    private void timesLeft(UUID uuid) {
        String left = Integer.toString(warns - warnCount.get(uuid));
        String plural = "times";
        if (this.warns - warnCount.get(uuid) == 1) {
            plural = "time";
        }
        Bukkit.getPlayer(uuid)
              .sendMessage(JanetName + "Do it " + left + " more " + plural + " and you will be kicked.");
    }

    private String langMsg(UUID uuid) {
        Bukkit.getPlayer(uuid)
              .sendMessage(JanetName + ChatColor.DARK_RED + "Warning, " + ChatColor.WHITE + "Do not swear please.");
        broadcast(JanetName + getName(uuid) + " was warned for using bad language.", uuid);
        return getName(uuid) + " was warned for using bad language.";
    }

    private String chatMsg(UUID uuid) {
        Bukkit.getPlayer(uuid).sendMessage(
              JanetName + ChatColor.DARK_RED + "Warning, " + ChatColor.WHITE + "Do not spam the chat please.");
        broadcast(JanetName + getName(uuid) + " was warned for spamming the chat.", uuid);
        return getName(uuid) + " was warned for spamming the chat.";
    }

    private String cmdMsg(UUID uuid) {
        Bukkit.getPlayer(uuid).sendMessage(
              JanetName + ChatColor.DARK_RED + "Warning, " + ChatColor.WHITE + "Do not spam commands please.");
        broadcast(JanetName + getName(uuid) + " was warned for spamming commands.", uuid);
        return getName(uuid) + " was warned for spamming commands.";
    }

    private String addsMsg(UUID uuid) {
        Bukkit.getPlayer(uuid).sendMessage(JanetName + ChatColor.DARK_RED + "Warning, " + ChatColor.WHITE
              + "Do not advertise other servers please.");
        broadcast(JanetName + getName(uuid) + " was warned for advertising other servers.", uuid);
        return getName(uuid) + " was warned for advertising other servers.";
    }

    private String warnMessage(UUID uuid, String reason, String warner) {
        reason = ChatColor.translateAlternateColorCodes('&', reason);
        Bukkit.getPlayer(uuid).sendMessage(
              JanetName + ChatColor.DARK_RED + "Warning, " + ChatColor.WHITE + "You were warned for " + reason + '.');
        broadcast(JanetName + getName(uuid) + " was warned by " + warner + " for " + reason + '.', uuid);
        return getName(uuid) + " was warned by " + warner + " for " + reason + '.';
    }

    private String other(UUID uuid, String reason) {
        String pname = getName(uuid);
        Bukkit.broadcastMessage(JanetName + pname + " was kicked for " + reason + '.');
        Bukkit.getPlayer(uuid).kickPlayer(ChatColor.WHITE + "You were kicked for " + reason);
        return pname + " was kicked for " + reason + '.';
    }

    private String chatSpam(UUID uuid) {
        String pname = getName(uuid);
        Bukkit.broadcastMessage(JanetName + pname + " was kicked for spamming the chat.");
        Bukkit.getPlayer(uuid).kickPlayer(ChatColor.WHITE + "Don't spam the chat!");
        return pname + " was kicked for spamming the chat.";
    }

    private String cmdSpam(UUID uuid) {
        String pname = getName(uuid);
        Bukkit.broadcastMessage(JanetName + pname + " was kicked for spamming commands.");
        Bukkit.getPlayer(uuid).kickPlayer(ChatColor.WHITE + "Don't spam commands!");
        return pname + " was kicked for spamming commands.";
    }

    private String language(UUID uuid) {
        String pname = getName(uuid);
        Bukkit.broadcastMessage(JanetName + pname + " was kicked for using bad language.");
        Bukkit.getPlayer(uuid).kickPlayer(ChatColor.WHITE + "Watch your language!");
        return pname + " was kicked for using bad language.";
    }

    private String advertising(UUID uuid) {
        String pname = getName(uuid);
        Bukkit.broadcastMessage(JanetName + pname + " was kicked for advertising.");
        Bukkit.getPlayer(uuid).kickPlayer(ChatColor.WHITE + "Do not advertise other servers!");
        return pname + " was kicked for advertising.";
    }

    private String getName(UUID uuid) {
        return Bukkit.getPlayer(uuid).getName();
    }

    private void broadcast(String message, UUID uuid) {
        Bukkit.getConsoleSender().sendMessage(message);
        Bukkit.getOnlinePlayers().stream().filter(p -> p.getUniqueId() != uuid).forEach(p -> p.sendMessage(message));
    }
}