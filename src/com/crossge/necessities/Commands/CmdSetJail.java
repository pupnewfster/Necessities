package com.crossge.necessities.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class CmdSetJail extends Cmd {
    private File configFile = new File("plugins/Necessities", "config.yml");

    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            config.set("Jail.world", p.getWorld().getName());
            config.set("Jail.x", Double.toString(p.getLocation().getX()));
            config.set("Jail.y", Double.toString(p.getLocation().getY()));
            config.set("Jail.z", Double.toString(p.getLocation().getZ()));
            config.set("Jail.yaw", Float.toString(p.getLocation().getYaw()));
            config.set("Jail.pitch", Float.toString(p.getLocation().getPitch()));
            try {
                config.save(configFile);
            } catch (Exception e) {
            }
            p.sendMessage(var.getMessages() + "Jail set.");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot set the jail.");
        return true;
    }
}