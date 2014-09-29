package com.crossge.necessities.Commands.WorldManager;

import org.bukkit.command.CommandSender;
import com.crossge.necessities.Commands.Cmd;
import com.crossge.necessities.WorldManager.*;

public class WorldCmd extends Cmd {
	WorldManager wm = new WorldManager();
	PortalManager pm = new PortalManager();
	WarpManager warps = new WarpManager();
	
	public boolean commandUse(CommandSender sender, String[] args) {
		return false;
	}
}