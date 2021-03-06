package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdFly implements Cmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                if (p.getAllowFlight()) {
                    p.setAllowFlight(false);//are both needed?
                    p.setFlying(false);
                    p.sendMessage(var.getMessages() + "Fly " + var.getObj() + "disabled" + var.getMessages() + '.');
                } else {
                    p.setAllowFlight(true);
                    p.sendMessage(var.getMessages() + "Fly " + var.getObj() + "enabled" + var.getMessages() + '.');
                }
                return true;
            }
        } else if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot fly.");
            return true;
        }
        UUID uuid = Utils.getID(args[0]);
        if (uuid == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
            return true;
        }
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.flyOthers")) {
                uuid = p.getUniqueId();
            }
        }
        Player target = Bukkit.getPlayer(uuid);
        if (target.getAllowFlight()) {
            target.setAllowFlight(false);
            target.setFlying(false);
            target.sendMessage(var.getMessages() + "Fly " + var.getObj() + "disabled" + var.getMessages() + '.');
            sender.sendMessage(
                  var.getMessages() + "Fly " + var.getObj() + "disabled" + var.getMessages() + " for " + var.getObj()
                        + target.getDisplayName());
        } else {
            target.setAllowFlight(true);
            target.sendMessage(var.getMessages() + "Fly " + var.getObj() + "enabled" + var.getMessages() + '.');
            sender.sendMessage(
                  var.getMessages() + "Fly " + var.getObj() + "enabled" + var.getMessages() + " for " + var.getObj()
                        + target.getDisplayName());
        }
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length != 1 || (sender instanceof EntityPlayer && !sender.hasPermission("Necessities.flyOthers"))) {
            return Collections.emptyList();
        }
        return Utils.getPlayerComplete(sender, args[0]);
    }
}