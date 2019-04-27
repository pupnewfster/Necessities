package gg.galaxygaming.necessities.Commands.Creative;

import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Reviews;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CmdReviewList implements CreativeCmd {

    public boolean commandUse(CommandSender sender, String[] args) {
        int page = 0;
        Variables var = Necessities.getVar();
        if (args.length > 0) {
            if (!Utils.legalInt(args[0])) {
                sender.sendMessage(
                      var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid review request page.");
                return true;
            }
            page = Integer.parseInt(args[0]);
        }
        if (args.length == 0 || page == 0) {
            page = 1;
        }
        int time = 0;
        String review;
        Reviews rev = Necessities.getRev();
        int totalPages = rev.reviewPages();
        if (page > totalPages) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Input a number from 1 to " + totalPages);
            return true;
        }
        sender.sendMessage(ChatColor.GOLD + "Review List Page [" + page + '/' + totalPages + ']');
        page = page - 1;
        review = rev.reviewLists(page, time);
        while (review != null) {
            sender.sendMessage(
                  ChatColor.GOLD + Integer.toString(page * 10 + time + 1) + ". " + var.getCatalog() + review
                        .split(" ")[1] + ' ' + review.split(" ")[2]);
            time++;
            review = rev.reviewLists(page, time);
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length != 1) {
            return Collections.emptyList();
        }
        Reviews rev = Necessities.getRev();
        int totalPages = rev.reviewPages();
        if (totalPages == 0) {
            return Collections.emptyList();
        }
        List<String> complete = new ArrayList<>();
        //TODO: TabComplete of the possible page numbers
        return complete;
    }
}