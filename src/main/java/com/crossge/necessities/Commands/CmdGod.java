package com.crossge.necessities.Commands;

import com.crossge.necessities.Necessities;
import com.crossge.necessities.RankManager.User;
import com.crossge.necessities.Utils;
import com.crossge.necessities.Variables;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdGod implements Cmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                User u = Necessities.getInstance().getUM().getUser(p.getUniqueId());
                if (u.godMode())
                    p.sendMessage(var.getMessages() + "God mode " + var.getObj() + "disabled" + var.getMessages() + ".");
                else {
                    p.setHealth(20);
                    p.setFoodLevel(20);
                    p.getActivePotionEffects().forEach(potion -> p.removePotionEffect(potion.getType()));
                    p.sendMessage(var.getMessages() + "God mode " + var.getObj() + "enabled" + var.getMessages() + ".");
                }
                u.setGod(!u.godMode());
                return true;
            }
        } else if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot become a god.");
            return true;
        }
        UUID uuid = Utils.getID(args[0]);
        if (uuid == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
            return true;
        }
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.godOthers"))
                uuid = p.getUniqueId();
        }
        Player target = Bukkit.getPlayer(uuid);
        User u = Necessities.getInstance().getUM().getUser(uuid);
        if (u.godMode()) {
            target.sendMessage(var.getMessages() + "God mode " + var.getObj() + "disabled" + var.getMessages() + ".");
            sender.sendMessage(var.getMessages() + "God mode " + var.getObj() + "disabled" + var.getMessages() + " for " + var.getObj() + target.getDisplayName());
        } else {
            target.setHealth(20);
            target.setFoodLevel(20);
            target.getActivePotionEffects().forEach(potion -> target.removePotionEffect(potion.getType()));
            target.sendMessage(var.getMessages() + "God mode " + var.getObj() + "enabled" + var.getMessages() + ".");
            sender.sendMessage(var.getMessages() + "God mode " + var.getObj() + "enabled" + var.getMessages() + " for " + var.getObj() + target.getDisplayName());
        }
        u.setGod(!u.godMode());
        return true;
    }
}