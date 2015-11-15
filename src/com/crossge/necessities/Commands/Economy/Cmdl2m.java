package com.crossge.necessities.Commands.Economy;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Cmdl2m extends EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (args.length == 0) {
                p.sendMessage(ChatColor.AQUA + "/l2m <level_amount> - Convert levels to $$");
                return true;
            }

            int level;
            try {
                level = Integer.parseInt(args[0]);
            } catch (Throwable t) {
                p.sendMessage(ChatColor.AQUA + "/l2m <level_amount> - Convert levels to $$");
                return true;
            }

            if (p.getLevel() < level) {
                p.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You don't have that many levels!");
                return true;
            }

            int exp = levelToExp(p.getLevel(), p.getLevel() - level);
            double money = 2 * exp;

            p.setLevel(p.getLevel() - level);
            balc.removeMoney(p.getUniqueId(), money);
            //PShop.econ.depositPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), money);

            p.sendMessage("" + ChatColor.BOLD + ChatColor.GREEN + "+" + ChatColor.RESET + " Converted " + ChatColor.BOLD + level + ChatColor.RESET + " levels to " + ChatColor.BOLD + "$" + money + ChatColor.RESET + "!");
        }

        return true;
    }

    public int levelToExp(int currentLevel, int destination) {
        int exp = 0;

        for (; currentLevel > destination; currentLevel--) {
            if (currentLevel <= 16) {
                exp += (2 * currentLevel) + 7;
            } else if (currentLevel >= 17 && currentLevel <= 31) {
                exp += (5 * currentLevel) - 38;
            } else {
                exp += (9 * currentLevel) - 158;
            }
        }

        return exp;
    }

    public double calculateMoney(int level) {
        if (level <= 0)
            return 0.0;

        if (level <= 16) {
            //At level 1, you get 100
            //At level 16, you get 1,600
            return 100.0 * level;
        }

        if (level > 16 && level < 32) {
            //At level 17, you get 4,335
            //At level 31, you get 14,415
            return 15.0 * (level * level);
        }

        if (level >= 32) {
            //At level 32, you get 15,768
            //At level 40, you get 47,000
            return (level * level * level) - 17000.0;
        }

        //Case would never happen
        return level;
    }
}
