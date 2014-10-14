package com.crossge.necessities.Commands.Guilds;

import com.crossge.necessities.Economy.Formatter;
import com.crossge.necessities.GetUUID;
import com.crossge.necessities.Guilds.GuildManager;
import com.crossge.necessities.RankManager.UserManager;
import com.crossge.necessities.SafeLocation;
import com.crossge.necessities.Variables;
import org.bukkit.command.CommandSender;

public class GuildCmd {
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