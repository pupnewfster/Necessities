package com.crossge.necessities.Commands.Economy;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.crossge.necessities.RankManager.RankManager;

public class CmdRankPrices extends EconomyCmd {
	RankManager rm = new RankManager();
	
	public boolean commandUse(CommandSender sender, String[] args) {
		int page = 0;
		if(args.length > 1) {
       		if(!form.isLegal(args[0])) {
       			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid command price page.");
       			return true;
       		}
    	   	page = Integer.parseInt(args[0]);
       	}
       	if(args.length == 0 || page == 0)
       		page = 1;
	   	int time = 0;
	   	String price;
	   	int totalpages = rp.priceListPages();
	   	if (page>totalpages) {
	   		sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Input a number from 1 to " + Integer.toString(totalpages));
	   		return true;
	   	}
	   	sender.sendMessage(ChatColor.GOLD + "Rank Prices Page [" + Integer.toString(page) + "/" + Integer.toString(totalpages) + "]");
	   	page = page - 1;
	   	price = rp.priceLists(page, time);
	   	String rank = "CONSOLE";
	   	
		if (sender instanceof Player) {
			Player player = (Player) sender;
    	   	rank = um.getUser(player.getUniqueId()).getRank().getName();
		}
	   	while(price != null) {
	   		price = formL(price.split(" ")[0], price.split(" ")[1], Integer.toString((page*10) + time + 1) + ".", rank);
	   		sender.sendMessage(price);
	   		time++;
	   		price = rp.priceLists(page, time);
	   	}
		return true;
	}
	
	private String formL(String rank, String cost, String numb, String curRank) {
		String price = "";
		if(!numb.equalsIgnoreCase("10."))
			numb += " ";
		numb += " ";
		if(curRank.equals("CONSOLE")) {
			cost = "$" + form.addCommas(form.roundTwoDecimals(Double.parseDouble(cost)));
			price = ChatColor.GOLD + numb + var.getCatalog() + rank + " can be bought for " + var.getMoney() + cost;
		} else {
			cost = "$" + form.addCommas(form.roundTwoDecimals(Double.parseDouble(cost)));
			price = ChatColor.GOLD + numb + var.getCatalog() + rank + " can be bought for " + var.getMoney() + cost;
			if(rm.hasRank(rm.getRank(curRank), rm.getRank(rank)))
				price += var.getCatalog() + "   Already Aquired.";
		}
   		return price;
	}
}