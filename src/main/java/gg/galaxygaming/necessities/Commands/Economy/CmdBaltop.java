package gg.galaxygaming.necessities.Commands.Economy;

import gg.galaxygaming.necessities.Economy.Economy;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CmdBaltop implements EconomyCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        int page = 0;
        if (args.length > 0) {
            if (!Utils.legalInt(args[0])) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid baltop page.");
                return true;
            }
            page = Integer.parseInt(args[0]);
        }
        if (args.length == 0 || page == 0) {
            page = 1;
        }
        int time = 0;
        Economy eco = Necessities.getEconomy();
        int totalPages = eco.baltopPages();
        if (page > totalPages) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Input a number from 1 to " + totalPages);
            return true;
        }
        sender.sendMessage(
              ChatColor.GOLD + "Balance Top Page [" + page + '/' + totalPages
                    + ']');
        List<String> balTop = eco.getBalTop(page);
        page -= 1;
        for (String bal : balTop)//TODO: Should users have their rank color for baltop. Is it even worth the extra calculations and loading of users?
        {
            sender.sendMessage(
                  ChatColor.GOLD + Integer.toString(page * 10 + time++ + 1) + ". " + var.getCatalog() + Utils
                        .nameFromString(bal.split(" ")[0]) + " has: " + var.getMoney() +
                        Economy.format(Double.parseDouble(bal.split(" ")[1])));
        }
        return true;
    }

    public boolean isPaintballEnabled() {
        return true;
    }
}