package com.crossge.necessities.Commands;

import com.crossge.necessities.Necessities;
import com.crossge.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CmdSpawn implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            YamlConfiguration config = Necessities.getInstance().getConfig();
            if (!config.contains("Spawn")) {
                p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Spawn not set.");
                return true;
            }
            World world = Bukkit.getWorld(config.getString("Spawn.world"));
            double x = Double.parseDouble(config.getString("Spawn.x"));
            double y = Double.parseDouble(config.getString("Spawn.y"));
            double z = Double.parseDouble(config.getString("Spawn.z"));
            float yaw = Float.parseFloat(config.getString("Spawn.yaw"));
            float pitch = Float.parseFloat(config.getString("Spawn.pitch"));
            p.sendMessage(var.getMessages() + "Teleporting to spawn.");
            Necessities.getInstance().getUM().getUser(p.getUniqueId()).teleport(Necessities.getInstance().getSafeLocations().getSafe(new Location(world, x, y, z, yaw, pitch)));
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot go to the spawn.");
        return true;
    }
}