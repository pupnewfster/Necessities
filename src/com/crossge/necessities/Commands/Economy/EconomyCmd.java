package com.crossge.necessities.Commands.Economy;

import org.bukkit.command.CommandSender;
import com.crossge.necessities.Commands.Cmd;
import com.crossge.necessities.Economy.*;
import com.crossge.necessities.RankManager.UserManager;

public class EconomyCmd extends Cmd {
	UserManager um = new UserManager();
	RankPrices rp = new RankPrices();
	CmdPrices cmdp = new CmdPrices();
	BalChecks balc = new BalChecks();
	Formatter form = new Formatter();
	Materials mat = new Materials();
	Prices pr = new Prices();
	Trade tr = new Trade();
	
	public boolean commandUse(CommandSender sender, String[] args) {
		return false;
	}
}