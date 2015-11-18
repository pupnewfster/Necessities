package com.crossge.necessities.Commands.Economy;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CmdBaltop extends EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        int page = 0;
        if (args.length >= 1) {
            if (!form.isLegal(args[0])) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid baltop page.");
                return true;
            }
            page = Integer.parseInt(args[0]);
        }
        if (args.length == 0 || page == 0)
            page = 1;
        int time = 0;
        String bal;
        int totalpages = balc.baltopPages();
        if (page > totalpages) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Input a number from 1 to " + Integer.toString(totalpages));
            return true;
        }
        sender.sendMessage(ChatColor.GOLD + "Balance Top Page [" + Integer.toString(page) + "/" + Integer.toString(totalpages) + "]");
        page = page - 1;
        bal = balc.balTop(page, time);
        while (bal != null) {
            bal = ChatColor.GOLD + Integer.toString((page * 10) + time + 1) + ". " + var.getCatalog() +
                    bal.split(" ")[0] + " has: " + var.getMoney() + "$" + form.addCommas(bal.split(" ")[1]);
            sender.sendMessage(bal);
            time++;
            bal = balc.balTop(page, time);
        }
        return true;
    }
}