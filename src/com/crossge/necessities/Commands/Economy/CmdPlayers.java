package com.crossge.necessities.Commands.Economy;

import org.bukkit.command.CommandSender;

public class CmdPlayers  extends EconomyCmd {
	public boolean commandUse(CommandSender sender, String[] args) {
	   	sender.sendMessage(var.getMessages() + balc.players() + " players have joined the server.");
	   	return true;
	}
}