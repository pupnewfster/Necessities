package com.crossge.necessities.Commands.Guilds;

import com.crossge.necessities.Formatter;
import com.crossge.necessities.GetUUID;
import com.crossge.necessities.Guilds.GuildManager;
import com.crossge.necessities.RankManager.UserManager;
import com.crossge.necessities.SafeLocation;
import com.crossge.necessities.Variables;
import org.bukkit.command.CommandSender;

public class GuildCmd {
    protected SafeLocation safe = new SafeLocation();
    protected GuildManager gm = new GuildManager();
    protected Variables var = new Variables();
    protected GetUUID get = new GetUUID();
    protected UserManager um = new UserManager();
    protected Formatter form = new Formatter();

    public boolean commandUse(CommandSender sender, String[] args) {
        return false;
    }
}