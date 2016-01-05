package com.crossge.necessities.Commands;

import com.crossge.necessities.*;
import com.crossge.necessities.RankManager.RankManager;
import com.crossge.necessities.RankManager.UserManager;
import org.bukkit.command.CommandSender;

import java.util.List;

public class Cmd {
    protected SafeLocation safe = new SafeLocation();
    protected UserManager um = new UserManager();
    protected RankManager rm = new RankManager();
    protected Formatter form = new Formatter();
    protected Variables var = new Variables();
    protected Console console = new Console();
    protected GetUUID get = new GetUUID();

    public boolean commandUse(CommandSender sender, String[] args) {
        return false;
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}