package com.crossge.necessities.Commands;

import com.crossge.necessities.Economy.BalChecks;
import com.crossge.necessities.Economy.Formatter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public class CmdTitle extends Cmd {
    private File configFileTitles = new File("plugins/Necessities", "titles.yml");
    private File configFile = new File("plugins/Necessities", "config.yml");
    BalChecks balc = new BalChecks();
    Formatter form = new Formatter();

    public boolean commandUse(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a title.");
            return true;
        }
        UUID uuid = get.getID(args[0]);
        if (uuid == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
            return true;
        }
        Player target = sender.getServer().getPlayer(uuid);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            sender.sendMessage(var.getMessages() + "Title removed for player " + var.getObj() + target.getName());
            return true;
        }
        String title = "";
        for (String arg : args)
            title += arg + " ";
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        title = title.replaceFirst(args[0], "").trim();
        if (ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', title + "&r")).length() > 24) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Titles have a maximum of 24 characters.");
            return true;
        }
        if (config.contains("Necessities.Economy") && config.getBoolean("Necessities.Economy") && !free) {
            if (balc.balance(target.getUniqueId()) < 1000) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must have $1000 to change your title.");
                return true;
            }
            balc.removeMoney(target.getUniqueId(), 1000);
            target.sendMessage(var.getMoney() + "$1000.00" + var.getMessages() + " was removed from your acount.");
        }
        configTitles.set(target.getUniqueId() + ".title", title);
        if (configTitles.get(target.getUniqueId() + ".color") == null)
            configTitles.set(target.getUniqueId() + ".color", "r");
        try {
            configTitles.save(configFileTitles);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sender.sendMessage(var.getMessages() + "Title set to " + title + var.getMessages() + " for player " + var.getObj() + target.getName());
        return true;
    }
}