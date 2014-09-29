package com.crossge.necessities.Commands.Economy;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdSetPrice extends EconomyCmd {
	public boolean commandUse(CommandSender sender, String[] args) {
		if (args.length == 0 || args.length == 1 || args.length > 3) {
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you enter the item and price for it and wether buying or selling price.");
			return true;
		}
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if(args.length == 2) {
				String itemName = player.getItemInHand().getType().name();
				if(!form.isLegal(args[0]) && !args[0].equalsIgnoreCase("null")) {
					sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a the price you want to set the item at.");
					return true;
				}
				itemName = mat.findItem(itemName);
				if(itemName == null) {
					player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That item does not exist");
					return true;
				}
				String file;
				if(args[1].equalsIgnoreCase("buy"))
					file = "buy";
				else if(args[1].equalsIgnoreCase("sell"))
					file = "sell";
				else {
					player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Input either sell or buy");
					return true;
				}
				if(args[0].equalsIgnoreCase("null")) {
					pr.setCost(file, itemName, args[0]);
					itemName = mat.pluralize(mat.getName(itemName), 2);
					if(args[1].equalsIgnoreCase("buy"))
						player.sendMessage(var.getObj() + itemName + var.getMessages() + " can no longer be bought");
					else
						player.sendMessage(var.getObj() + itemName + var.getMessages() + " can no longer be sold");
				} else {
					pr.setCost(file, itemName, form.roundTwoDecimals(Double.parseDouble(args[0])));
					itemName = mat.pluralize(mat.getName(itemName), 2);
					if(args[1].equalsIgnoreCase("buy"))
						player.sendMessage(var.getObj() + ownerShip(itemName) + var.getMessages() + " buy price was set to " + var.getMoney() +
									"$" + form.addCommas(form.roundTwoDecimals(Double.parseDouble(args[0]))));
					else
						player.sendMessage(var.getObj() + ownerShip(itemName) + var.getMessages() + " sell price was set to " + var.getMoney() +
									"$" + form.addCommas(form.roundTwoDecimals(Double.parseDouble(args[0]))));
				}
				return true;
			}
		}
		if (args.length != 3) {
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you enter the item and price for it and wether buying or selling price.");
			return true;
		}
		String itemName;
		itemName = args[0];
		if(form.isLegal(itemName))
			itemName = mat.idToName(Integer.parseInt(itemName));
		if(!form.isLegal(args[1]) && !args[1].equalsIgnoreCase("null")) {
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a the price you want to set the item at.");
			return true;
		}
		itemName = mat.findItem(itemName);
		if(itemName == null) {
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That item does not exist");
			return true;
		}
		String file;
		if(args[2].equalsIgnoreCase("buy"))
			file = "buy";
		else if(args[2].equalsIgnoreCase("sell"))
			file = "sell";
		else {
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Input either sell or buy");
			return true;
		}
		if(args[1].equalsIgnoreCase("null")) {
			pr.setCost(file, itemName, args[1]);
			itemName = mat.pluralize(mat.getName(itemName), 2);
			if(args[2].equalsIgnoreCase("buy"))
				sender.sendMessage(var.getObj() + itemName + var.getMessages() + " can no longer be bought");
			else
				sender.sendMessage(var.getObj() + itemName + var.getMessages() + " can no longer be sold");
		} else {
			pr.setCost(file, itemName, form.roundTwoDecimals(Double.parseDouble(args[1])));
			itemName = mat.pluralize(mat.getName(itemName), 2);
			if(args[2].equalsIgnoreCase("buy"))
				sender.sendMessage(var.getObj() + ownerShip(itemName) + var.getMessages() + " buy price was set to " + var.getMoney() +
							"$" + form.addCommas(form.roundTwoDecimals(Double.parseDouble(args[1]))));
			else
				sender.sendMessage(var.getObj() + ownerShip(itemName) + var.getMessages() + " sell price was set to " + var.getMoney() +
							"$" + form.addCommas(form.roundTwoDecimals(Double.parseDouble(args[1]))));
		}
		return true;
	}
	
	private String ownerShip(String s) {
		if(s.endsWith("s") || s.endsWith("S"))
			return s + "'";
		return s + "'s";
	}
}