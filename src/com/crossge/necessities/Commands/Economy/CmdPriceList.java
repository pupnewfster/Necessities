package com.crossge.necessities.Commands.Economy;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CmdPriceList extends EconomyCmd {
	public boolean commandUse(CommandSender sender, String[] args) {
		int page = 0;
		if(args.length >= 1) {
       		if(!form.isLegal(args[0])) {
       			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid price page.");
       			return true;
       		}
    	   	page = Integer.parseInt(args[0]);
       	}
       	if(args.length == 0 || page == 0)
       		page = 1;
	   	int time = 0;
	   	String price;
	   	int totalpages = pr.priceListPages();
	   	if (page>totalpages) {
	   		sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Input a number from 1 to " + Integer.toString(totalpages));
	   		return true;
	   	}
	   	sender.sendMessage(ChatColor.GOLD + "Price List Page [" + Integer.toString(page) + "/" + Integer.toString(totalpages) + "]");
	   	page = page - 1;
	   	price = pr.priceLists(page, time);
	   	while(price != null) {
	   		price = formL(form.capFirst(mat.getName(mat.findItem(price.split(" ")[0]))),
	   										price.split(" ")[1],
	   										price.split(" ")[2],
	   										Integer.toString((page * 10) + time + 1) + ".");
	   		sender.sendMessage(price);
	   		time++;
	   		price = pr.priceLists(page, time);
	   	}
		return true;
	}
	
	private String formL(String item, String buy, String sell, String numb) {
		item = mat.pluralize(item, 2);
		String selling = "  sell price: ";
	   	String buying = "  buy price: ";
		if(!numb.equalsIgnoreCase("10."))
			numb += " ";
		numb += " ";
		if(buy.trim().equalsIgnoreCase("null")) {//shouldnt ever happen now anyways
			buying = "";
			buy = "";
   		} else
			buy = "$" + form.addCommas(buy);
   		if(sell.trim().equalsIgnoreCase("null")) {
   			selling = "";
   			sell = "";
   		} else
   			sell = "$" + form.addCommas(sell);
	   	return ChatColor.GOLD + numb + var.getCatalog() + item + buying + var.getMoney() + buy + var.getCatalog() + selling + var.getMoney() + sell;
	}
}