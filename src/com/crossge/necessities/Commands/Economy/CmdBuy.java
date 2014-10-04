package com.crossge.necessities.Commands.Economy;

import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CmdBuy extends EconomyCmd {
	public boolean commandUse(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length > 2 || args.length == 0) {
				sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a the amount you want to buy and of what.");
				return true;
			}
			PlayerInventory inventory = player.getInventory();
			String balance = balc.bal(player.getUniqueId());
			double intbal = Double.parseDouble(balance);
			int amount = 0;					
		    String itemName = "";
		    String temp = "";
		    short data = 0;
			if(args.length == 2) {
				temp = args[0].replaceAll(":", " ");
				itemName = temp.split(" ")[0];
				if(!form.isLegal(args[1])) {
					sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid amount to buy.");
					return true;
				}
				amount = Integer.parseInt(args[1]);
				if(form.isLegal(itemName)) {
					itemName = mat.idToName(Integer.parseInt(itemName));
					try {
						data = Short.parseShort(temp.split(" ")[1]);
					} catch(Exception e){}
				}
				else if(itemName.equalsIgnoreCase("hand")) {
					itemName = player.getItemInHand().getType().name();
					data = player.getItemInHand().getDurability();
				}
			} else {
				itemName = player.getItemInHand().getType().name();
				if(!form.isLegal(args[0])) {
					sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must enter a valid amount to buy.");
					return true;
				}
				data = player.getItemInHand().getDurability();
				amount = Integer.parseInt(args[0]);
			}
			itemName = mat.findItem(itemName);
			if(itemName == null) {
				player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "That item does not exist");
				return true;
			}
			itemName = itemName.toUpperCase();
			double cost = 0.00;
			cost = pr.getCost("buy", itemName, amount);
			if(cost == -1.00) {
				itemName = form.capFirst(mat.getName(itemName));
				player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + mat.pluralize(itemName, 2) + " cannot be bought from the server.");
			} else {
				if (intbal < cost) {
					player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You dont have enough money to buy that item.");
					return true;
				}
				if(Material.matchMaterial(itemName) == null) {
					player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + mat.pluralize(itemName, 2) + " is not a valid item as of yet.");
					return true;
				}
				ItemStack itemstack = new ItemStack(Material.matchMaterial(mat.findItem(itemName)), amount, data);
				HashMap<Integer, ItemStack> noFit = inventory.addItem(itemstack);
				if(!noFit.isEmpty()) {
					amount = amount - noFit.get(0).getAmount();
				    cost = pr.getCost("buy", itemName, amount);
				    player.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You do not have enough inventory space to buy that much of that item,"
				    										+ " buying the amount you have inventory space for.");
				}
				balc.removeMoney(player.getUniqueId(), cost);
				itemName = form.capFirst(mat.getName(itemName));
				player.sendMessage(var.getMessages() + "You bought " + var.getObj() + Integer.toString(amount) + " " + mat.pluralize(itemName, amount) + var.getMessages() +
						".");
				player.sendMessage(var.getMoney() + "$" + form.addCommas(form.roundTwoDecimals(cost)) + var.getMessages() + " was removed from your acount.");
			}
		} else
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "You must be logged in to use this command");
		return true;
	}
}