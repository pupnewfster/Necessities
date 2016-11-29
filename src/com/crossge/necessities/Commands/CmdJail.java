package com.crossge.necessities.Commands;

import com.crossge.necessities.Necessities;
import com.crossge.necessities.RankManager.User;
import com.crossge.necessities.SafeLocation;
import com.crossge.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.UUID;

public class CmdJail implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a player to jail.");
            return true;
        }
        UUID uuid = Necessities.getInstance().getUUID().getID(args[0]);
        if (uuid == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
            return true;
        }
        YamlConfiguration config = Necessities.getInstance().getConfig();
        if (!config.contains("Jail")) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Jail not set.");
            return true;
        }
        User u = Necessities.getInstance().getUM().getUser(uuid);
        if (!config.contains("Spawn") && u.getLastPos() == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Spawn not set.");
            return true;
        }

        if (u.getPlayer().hasPermission("Necessities.unjailable")) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That player cannot be jailed.");
            return true;
        }
        SafeLocation safe = Necessities.getInstance().getSafeLocations();
        if (!u.isJailed()) {
            sender.sendMessage(var.getMessages() + "You jailed " + var.getObj() + u.getPlayer().getDisplayName() + var.getMessages() + ".");
            u.getPlayer().sendMessage(var.getDemote() + "You have been jailed.");
            World world = Bukkit.getWorld(config.getString("Jail.world"));
            double x = Double.parseDouble(config.getString("Jail.x"));
            double y = Double.parseDouble(config.getString("Jail.y"));
            double z = Double.parseDouble(config.getString("Jail.z"));
            float yaw = Float.parseFloat(config.getString("Jail.yaw"));
            float pitch = Float.parseFloat(config.getString("Jail.pitch"));
            u.getPlayer().teleport(safe.getSafe(new Location(world, x, y, z, yaw, pitch)));
            u.setJailed(true);
        } else {
            sender.sendMessage(var.getMessages() + "You unjailed " + var.getObj() + u.getPlayer().getDisplayName() + var.getMessages() + ".");
            u.getPlayer().sendMessage(var.getPromote() + "You have been unjailed.");
            u.setJailed(false);
            if (u.getLastPos() == null) {
                World world = Bukkit.getWorld(config.getString("Spawn.world"));
                double x = Double.parseDouble(config.getString("Spawn.x"));
                double y = Double.parseDouble(config.getString("Spawn.y"));
                double z = Double.parseDouble(config.getString("Spawn.z"));
                float yaw = Float.parseFloat(config.getString("Spawn.yaw"));
                float pitch = Float.parseFloat(config.getString("Spawn.pitch"));
                u.getPlayer().teleport(safe.getSafe(new Location(world, x, y, z, yaw, pitch)));
            } else
                u.getPlayer().teleport(safe.getSafe(u.getLastPos()));
        }
        return true;
    }
}