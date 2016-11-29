package com.crossge.necessities.Commands;

import com.crossge.necessities.Necessities;
import com.crossge.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CmdSetspawn implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            YamlConfiguration config = Necessities.getInstance().getConfig();
            config.set("Spawn.world", p.getWorld().getName());
            config.set("Spawn.x", Double.toString(p.getLocation().getX()));
            config.set("Spawn.y", Double.toString(p.getLocation().getY()));
            config.set("Spawn.z", Double.toString(p.getLocation().getZ()));
            config.set("Spawn.yaw", Float.toString(p.getLocation().getYaw()));
            config.set("Spawn.pitch", Float.toString(p.getLocation().getPitch()));
            try {
                config.save(Necessities.getInstance().getConfigFile());
            } catch (Exception ignored) {
            }
            p.sendMessage(var.getMessages() + "Spawn set.");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot set the spawn.");
        return true;
    }
}