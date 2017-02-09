package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CmdSetJail implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            YamlConfiguration config = Necessities.getInstance().getConfig();
            config.set("Jail.world", p.getWorld().getName());
            config.set("Jail.x", Double.toString(p.getLocation().getX()));
            config.set("Jail.y", Double.toString(p.getLocation().getY()));
            config.set("Jail.z", Double.toString(p.getLocation().getZ()));
            config.set("Jail.yaw", Float.toString(p.getLocation().getYaw()));
            config.set("Jail.pitch", Float.toString(p.getLocation().getPitch()));
            try {
                config.save(Necessities.getInstance().getConfigFile());
            } catch (Exception ignored) {
            }
            p.sendMessage(var.getMessages() + "Jail set.");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot set the jail.");
        return true;
    }
}