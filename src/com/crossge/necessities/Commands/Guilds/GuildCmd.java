package com.crossge.necessities.Commands.Guilds;

import org.bukkit.command.CommandSender;

import com.crossge.necessities.*;
import com.crossge.necessities.Guilds.*;
import com.crossge.necessities.Economy.*;
import com.crossge.necessities.RankManager.*;

public class GuildCmd{
	SafeLocation safe = new SafeLocation();
	GuildManager gm = new GuildManager();
	Variables var = new Variables();
	GetUUID get = new GetUUID();
	UserManager um = new UserManager();
	Formatter form = new Formatter();
	
	public boolean commandUse(CommandSender sender, String[] args) {
		return false;
	}
}