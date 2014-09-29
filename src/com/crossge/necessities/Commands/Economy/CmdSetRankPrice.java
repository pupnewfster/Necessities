package com.crossge.necessities.Commands.Economy;

import org.bukkit.command.CommandSender;

public class CmdSetRankPrice extends EconomyCmd {
	public boolean commandUse(CommandSender sender, String[] args) {
		if(args.length != 2) {
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Format requires you enter a rank and a price to sell that rank for.");
			return true;
		}
		String rankName = form.capFirst(args[0]);
		if(!form.isLegal(args[1])) {
			if(args[1].equalsIgnoreCase("null")) {
				rp.rCost(rankName);
				sender.sendMessage(var.getObj() + rankName + var.getMessages() + " can no longer be bought.");
				return true;
			}
			sender.sendMessage(var.getEr() + "Error: " + var.getErMsg() + "Must enter a price to sell rank for.");
			return true;
		}
		String cost = args[1];
		rp.setCost(rankName, cost);
		cost = form.roundTwoDecimals(Double.parseDouble(cost));
		sender.sendMessage(var.getMessages() + "Added " + var.getObj() + rankName + var.getMessages() + " at the price of " + var.getMoney() + "$" + cost);
		return true;
	}
}