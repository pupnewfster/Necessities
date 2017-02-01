package com.crossge.necessities.Commands.Economy;

import com.crossge.necessities.Necessities;
import com.crossge.necessities.Variables;
import net.nyvaria.googleanalytics.hit.EventHit;
import net.nyvaria.openanalytics.bukkit.client.Client;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdL2M implements EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getInstance().getVar();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must input a valid number of levels to convert.");
                return true;
            }
            int level;
            try {
                level = Integer.parseInt(args[0]);
            } catch (Throwable t) {
                p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must input a valid number of levels to convert.");
                return true;
            }
            if (level < 1) {
                p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Number of levels must be positive.");
                return true;
            }
            if (p.getLevel() < level) {
                p.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have that many levels.");
                return true;
            }
            double money = 2 * levelToExp(p.getLevel(), p.getLevel() - level);
            p.setLevel(p.getLevel() - level);
            Necessities.getInstance().getBalChecks().addMoney(p.getUniqueId(), money);
            if (Necessities.isTracking()) {
                EventHit hit = new EventHit(new Client(p), "ConvertLevel", "ConvertLevel");
                hit.event_value = level;
                Necessities.trackAction(hit);
            }
            p.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "+" + ChatColor.RESET + " Converted " + ChatColor.BOLD + level + ChatColor.RESET + " levels to " + var.getMoney() + "$" + money + ChatColor.RESET + "!");
        } else
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be logged in to use this command");
        return true;
    }

    private int levelToExp(int currentLevel, int destination) {
        int exp = 0;
        for (; currentLevel > destination; currentLevel--) {
            if (currentLevel <= 16)
                exp += (2 * currentLevel) + 7;
            else if (currentLevel >= 17 && currentLevel <= 31)
                exp += (5 * currentLevel) - 38;
            else
                exp += (9 * currentLevel) - 158;
        }
        return exp;
    }
}