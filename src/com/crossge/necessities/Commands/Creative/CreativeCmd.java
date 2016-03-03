package com.crossge.necessities.Commands.Creative;

import com.crossge.necessities.Commands.Cmd;
import com.crossge.necessities.Reviews;
import org.bukkit.command.CommandSender;

public class CreativeCmd extends Cmd {
    protected Reviews rev = new Reviews();

    public boolean commandUse(CommandSender sender, String[] args) {
        return false;
    }
}