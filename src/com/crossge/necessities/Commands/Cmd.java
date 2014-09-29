package com.crossge.necessities.Commands;

import java.util.List;
import org.bukkit.command.CommandSender;
import com.crossge.necessities.*;
import com.crossge.necessities.RankManager.*;

public class Cmd {
	protected SafeLocation safe = new SafeLocation();
	protected Variables var = new Variables();
	protected Console console = new Console();
	protected GetUUID get = new GetUUID();
	protected UserManager um = new UserManager();
	RankManager rm = new RankManager();
	
	public boolean commandUse(CommandSender sender, String[] args) {
		return false;
	}
	
	public List<String> tabComplete(CommandSender sender, String[] args) {
    	return null;
    }
}