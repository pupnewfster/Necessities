package com.crossge.necessities.Commands.Creative;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CmdReviewList extends CreativeCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        int page = 0;
        if (args.length > 0) {
            if (!form.isLegal(args[0])) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid review request page.");
                return true;
            }
            page = Integer.parseInt(args[0]);
        }
        if (args.length == 0 || page == 0)
            page = 1;
        int time = 0;
        String review;
        int totalpages = rev.priceListPages();
        if (page > totalpages) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Input a number from 1 to " + Integer.toString(totalpages));
            return true;
        }
        sender.sendMessage(ChatColor.GOLD + "Review List Page [" + Integer.toString(page) + "/" + Integer.toString(totalpages) + "]");
        page = page - 1;
        review = rev.priceLists(page, time);
        while (review != null) {
            sender.sendMessage(ChatColor.GOLD + Integer.toString((page * 10) + time + 1) + ". " + var.getCatalog() + review.split(" ")[1] + " " + review.split(" ")[2]);
            time++;
            review = rev.priceLists(page, time);
        }
        return true;
    }
}