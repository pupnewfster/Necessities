package com.crossge.necessities.Commands.Guilds;

import org.bukkit.command.CommandSender;

interface GuildCmd {
    @SuppressWarnings("SameReturnValue")
    boolean commandUse(CommandSender sender, String[] args);
}