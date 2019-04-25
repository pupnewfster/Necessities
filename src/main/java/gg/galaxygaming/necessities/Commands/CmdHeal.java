package gg.galaxygaming.necessities.Commands;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdHeal implements Cmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                p.setHealth(20);
                p.setFoodLevel(20);
                p.getActivePotionEffects().forEach(potion -> p.removePotionEffect(potion.getType()));
                p.setSaturation(5);
                p.sendMessage(var.getMessages() + "You have been healed.");
                return true;
            }
        } else if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot be healed.");
            return true;
        }
        UUID uuid = Utils.getID(args[0]);
        if (uuid == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
            return true;
        }
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.healOthers")) {
                uuid = p.getUniqueId();
            }
        }
        Player target = Bukkit.getPlayer(uuid);
        target.setHealth(20);
        target.setFoodLevel(20);
        target.getActivePotionEffects().forEach(potion -> target.removePotionEffect(potion.getType()));
        target.setSaturation(5);
        target.sendMessage(var.getMessages() + "You have been healed.");
        sender.sendMessage(var.getMessages() + "Healed player: " + var.getObj() + target.getDisplayName());
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        //TODO: TabComplete
        return Collections.emptyList();
    }
}