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

public class CmdFeed implements Cmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                p.setFoodLevel(20);
                p.setSaturation(5);
                p.sendMessage(var.getMessages() + "You have been satisfied.");
                return true;
            }
        } else if (args.length == 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "The console cannot be fed.");
            return true;
        }
        UUID uuid = Utils.getID(args[0]);
        if (uuid == null) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Invalid player.");
            return true;
        }
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("Necessities.feedOthers")) {
                uuid = p.getUniqueId();
            }
        }
        Player target = Bukkit.getPlayer(uuid);
        target.setFoodLevel(20);
        target.setSaturation(5);
        target.sendMessage(var.getMessages() + "You have been satisfied.");
        sender.sendMessage(var.getMessages() + "Satisfied player: " + var.getObj() + target.getDisplayName());
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length != 1 || (sender instanceof EntityPlayer && !sender.hasPermission("Necessities.feedOthers"))) {
            return Collections.emptyList();
        }
        return Utils.getPlayerComplete(sender, args[0]);
    }
}