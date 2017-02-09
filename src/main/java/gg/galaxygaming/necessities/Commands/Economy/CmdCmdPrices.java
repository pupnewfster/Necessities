package gg.galaxygaming.necessities.Commands.Economy;

import gg.galaxygaming.necessities.Economy.CmdPrices;
import gg.galaxygaming.necessities.Economy.Economy;
import gg.galaxygaming.necessities.Necessities;
import gg.galaxygaming.necessities.Utils;
import gg.galaxygaming.necessities.Variables;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdCmdPrices implements EconomyCmd {
    public boolean commandUse(CommandSender sender, String[] args) {
        Variables var = Necessities.getVar();
        int page = 0;
        if (args.length >= 0) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "This command is currently disabled.");
            return true;
        }
        if (args.length > 1) {
            if (!Utils.legalInt(args[0])) {
                sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid command price page.");
                return true;
            }
            page = Integer.parseInt(args[0]);
        }
        if (args.length == 0 || page == 0)
            page = 1;
        int time = 0;
        String price;
        CmdPrices cmdp = Necessities.getCommandPrices();
        int totalPages = cmdp.priceListPages();
        if (page > totalPages) {
            sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Input a number from 1 to " + Integer.toString(totalPages));
            return true;
        }
        sender.sendMessage(ChatColor.GOLD + "Command Prices Page [" + Integer.toString(page) + "/" + Integer.toString(totalPages) + "]");
        page = page - 1;
        price = cmdp.priceLists(page, time);
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String rank = Necessities.getUM().getUser(player.getUniqueId()).getRank().getName();
            Command com;
            boolean hasNode;
            while (price != null) {
                com = player.getServer().getPluginCommand(price.split(" ")[0]);
                if (com == null)
                    hasNode = false;
                else {
                    String temp = com.getPermission();
                    if (temp == null || temp.contains("mcmmo"))
                        temp = "necessities." + price.split(" ")[0].toLowerCase();
                    hasNode = player.hasPermission(temp);
                }
                price = formL(Utils.capFirst(price.split(" ")[0]), price.split(" ")[1], price.split(" ")[2], Integer.toString((page * 10) + time + 1) + ".", rank, hasNode);
                player.sendMessage(price);
                time++;
                price = cmdp.priceLists(page, time);
            }
        } else
            while (price != null) {
                price = formL(Utils.capFirst(price.split(" ")[0]), price.split(" ")[1], price.split(" ")[2], Integer.toString((page * 10) + time + 1) + ".", "CONSOLE", true);
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
        Variables var = Necessities.getVar();
        if (curRank.equals("CONSOLE")) {
            cost = Economy.format(Double.parseDouble(cost));
            price = ChatColor.GOLD + numb + var.getCatalog() + cmd + " can be bought for " + var.getMoney() + cost + var.getMessages() + " by " + rank + "s";
        } else {
            cost = Economy.format(Double.parseDouble(cost));
            price = ChatColor.GOLD + numb + var.getCatalog() + cmd + " can be bought for " + var.getMoney() + cost;
            price += var.getCatalog() + (hasCmd ? "   Already Acquired." : " by " + Utils.ownerShip(rank));
        }
        return price;
    }
}