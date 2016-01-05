package com.crossge.necessities.Commands.Economy;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdCmdPrices extends EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        int page = 0;
        if (args.length >= 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "This command is currently disabled.");
            return true;
        }
        if (args.length > 1) {
            if (!form.isLegal(args[0])) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid command price page.");
                return true;
            }
            page = Integer.parseInt(args[0]);
        }
        if (args.length == 0 || page == 0)
            page = 1;
        int time = 0;
        String price;
        int totalpages = cmdp.priceListPages();
        if (page > totalpages) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Input a number from 1 to " + Integer.toString(totalpages));
            return true;
        }
        sender.sendMessage(ChatColor.GOLD + "Command Prices Page [" + Integer.toString(page) + "/" + Integer.toString(totalpages) + "]");
        page = page - 1;
        price = cmdp.priceLists(page, time);
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String rank = um.getUser(player.getUniqueId()).getRank().getName();
            Command com;
            boolean hasNode;
            while (price != null) {
                com = player.getServer().getPluginCommand(price.split(" ")[0]);
                if (com == null)
                    hasNode = false;
                else {
                    String tempn = com.getPermission();
                    if (tempn == null || tempn.contains("mcmmo"))
                        tempn = "necessities." + price.split(" ")[0].toLowerCase();
                    hasNode = player.hasPermission(tempn);
                }
                price = formL(form.capFirst(price.split(" ")[0]), price.split(" ")[1], price.split(" ")[2], Integer.toString((page * 10) + time + 1) + ".", rank, hasNode);
                player.sendMessage(price);
                time++;
                price = cmdp.priceLists(page, time);
            }
        } else
            while (price != null) {
                price = formL(form.capFirst(price.split(" ")[0]), price.split(" ")[1], price.split(" ")[2], Integer.toString((page * 10) + time + 1) + ".", "CONSOLE", true);
                sender.sendMessage(price);
                time++;
                price = cmdp.priceLists(page, time);
            }
        return true;
    }

    private String formL(String cmd, String rank, String cost, String numb, String curRank, boolean hasCmd) {
        String price;
        rank = rank.toLowerCase();
        if (!numb.equalsIgnoreCase("10."))
            numb += " ";
        numb += " ";
        if (curRank.equals("CONSOLE")) {
            cost = "$" + form.addCommas(form.roundTwoDecimals(Double.parseDouble(cost)));
            price = ChatColor.GOLD + numb + var.getCatalog() + cmd + " can be bought for " + var.getMoney() + cost + var.getMessages() + " by " + rank + "s";
        } else {
            cost = "$" + form.addCommas(form.roundTwoDecimals(Double.parseDouble(cost)));
            price = ChatColor.GOLD + numb + var.getCatalog() + cmd + " can be bought for " + var.getMoney() + cost;
            price +=  var.getCatalog() + (hasCmd ? "   Already Aquired." : " by " + form.ownerShip(rank));
        }
        return price;
    }
}