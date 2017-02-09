package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Economy.Economy;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public class CmdTitle implements Cmd {
    private final File configFileTitles = new File("plugins/Necessities", "titles.yml");

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a title.");
            return true;
        }
        UUID uuid = Utils.getID(args[0]);
        if (uuid == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
            return true;
        }
        Player target = Bukkit.getPlayer(uuid);
        boolean free = !(sender instanceof Player);
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (target != p && !p.hasPermission("Necessities.bracketOthers"))
                target = p;
            free = p.hasPermission("Necessities.freeCommand");
        }
        YamlConfiguration configTitles = YamlConfiguration.loadConfiguration(configFileTitles);
        if (args.length == 1) {
            configTitles.set(target.getUniqueId() + ".title", null);
            try {
                configTitles.save(configFileTitles);
            } catch (Exception ignored) {
            }
            sender.sendMessage(var.getMessages() + "Title removed for player " + var.getObj() + target.getName());
            return true;
        }
        String title = "";
        for (String arg : args)
            title += arg + " ";
        YamlConfiguration config = Necessities.getInstance().getConfig();
        title = title.replaceFirst(args[0], "").trim();
        if (ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', title + "&r")).length() > 24) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Titles have a maximum of 24 characters.");
            return true;
        }
        if (config.contains("Necessities.Economy") && config.getBoolean("Necessities.Economy") && !free && config.contains("Necessities.Creative") && !config.getBoolean("Necessities.Creative")) {
            Economy eco = Necessities.getInstance().getEconomy();
            if (eco.getBalance(target.getUniqueId()) < 1000) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must have " + Economy.format(1000) + " to change your title.");
                return true;
            }
            eco.removeMoney(target.getUniqueId(), 1000);
            target.sendMessage(var.getMoney() + Economy.format(1000) + var.getMessages() + " was removed from your account.");
        }
        configTitles.set(target.getUniqueId() + ".title", title);
        if (configTitles.get(target.getUniqueId() + ".color") == null)
            configTitles.set(target.getUniqueId() + ".color", "r");
        try {
            configTitles.save(configFileTitles);
        } catch (Exception ignored) {
        }
        sender.sendMessage(var.getMessages() + "Title set to " + title + var.getMessages() + " for player " + var.getObj() + target.getName());
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}