package com.crossge.necessities.Commands.RankManager;

import org.bukkit.command.CommandSender;
import com.crossge.necessities.Commands.Cmd;
import com.crossge.necessities.RankManager.*;

public class RankCmd extends Cmd {
	RankManager rm = new RankManager();
	UserManager um = new UserManager();
	
	public boolean commandUse(CommandSender sender, String[] args) {
		return false;
	}
}